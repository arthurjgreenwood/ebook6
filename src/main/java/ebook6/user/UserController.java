/**
 * Controller Class for user-related REST API endpoints.
 *
 * @authors Fedrico Leal Quintero and Thomas Hague
 * Created by Fedrico Leal Quintero, 27/3/2025 with UserController and createUser methods
 * Modified by Thomas Hague 31/3/2025. Package and comments added. Methods updateUser, promoteUserToAdmin, deleteUserByEmail,
 * deleteUserByID, getUserByEmail, getUserByID and getAllUsers developed. Method createUser edited now with custom exceptions.
 * Modified by Thomas Hague, 4/4/2025. loginUser added.
 */

// getUserMethods??!!

package ebook6.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Creates an UserController using our ebookService
     * @param userService
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates a new user by calling the createUser method from our service class.
     * @param user the user to create
     * @return a ResponseEntity with the created user or an error message
     */
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (UserAlreadyInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Updates a user by calling the updateUser method from our service class.
     * @param userId to be updated
     * @param updatedUser what the user will be updated to.
     * @return a ResponseEntity with the updated User or an error message
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable UUID userId, @RequestBody User updatedUser) {
        try {
            User finalUser = userService.updateUser(userId, updatedUser);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(finalUser);
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Promotes a normal user to have admin privileges. Errors if admin or user to promote isn't found in our database,
     * or the user authorising the promotion doesn't have admin privileges.
     * Calls our service class to identify the relevant users by their userId's, and then if both users exist, calls the
     * makeAdmin method in our service class.
     * @param currentAdminId the admin authorising the promotion
     * @param newAdminId user to be promoted
     * @return a ResponseEntity with the promoted User or an error message
     */
    @PatchMapping("/adminPromotion")
    public ResponseEntity<String> promoteUserToAdmin (@RequestParam UUID currentAdminId, @RequestParam UUID newAdminId) {
        Optional<User> optionalCurrentAdmin = userService.findUserByUserId(currentAdminId);
        Optional<User> optionalNewAdmin = userService.findUserByUserId(newAdminId);
        if (optionalCurrentAdmin.isPresent() && optionalNewAdmin.isPresent()) {
            User currentAdmin = optionalCurrentAdmin.get();
            User newAdmin = optionalNewAdmin.get();
            try {
                userService.makeAdmin(currentAdmin, newAdmin);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User promoted to admin");
            } catch (InvalidAccessException e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User(s) don't exist in our website.");
        }
    }

    /**
     * Deletes a user from our database by calling the delete user message from our service class.
     * Checks if the user's email address matches one in our database. Error message if not.
     * @param email of the user to be deleted
     * @return a ResponseEntity confirming the user is deleted or error message.
     */
    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteUserByEmail(@PathVariable String email) {
        Optional<User> optionalUser = userService.findUserByEmail(email);
        if (optionalUser.isPresent()) {
            User userToDelete = optionalUser.get();
            userService.deleteUser(userToDelete);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        }
    }

    /**
     * Deletes a user from our database by calling the delete user message from our service class.
     * Checks if the user's Id matches one in our database. Error message if not.
     * @param userId of the user to be deleted
     * @return a ResponseEntity confirming the user is deleted or error message.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable UUID userId) {
        Optional<User> optionalUser = userService.findUserByUserId(userId);
        if (optionalUser.isPresent()) {
            User userToDelete = optionalUser.get();
            userService.deleteUser(userToDelete);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        }
    }

    /**
     * Identifies a user by matching their email address to one in our database. Error message printed if not.
     * @param email of the user we are looking for
     * @return a ResponseEntity with the user or error message.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        Optional<User> optionalUser = userService.findUserByEmail(email);
        if (optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(optionalUser.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        }
    }

    /**
     * Identifies a user by matching their userId to one in our database. Error message printed if not.
     * @param userId of the user we are looking for
     * @return a ResponseEntity with the user or error message.
     */
    @GetMapping("/id/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable UUID userId) {
        Optional<User> optionalUser = userService.findUserByUserId(userId);
        if (optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(optionalUser.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        }
    }

    /**
     * Identifies a user by matching their name to one in our database. Error message printed if not.
     * @param name of the user we are looking for
     * @return a ResponseEntity with matching users or error message.
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<List<User>> getUsersByName(@PathVariable String name) {
        List<User> users = userService.findByNameIgnoreCase(name);
        if (!users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(users);
        }
    }

    /**
     * Identifies all users in our database. Error message printed if none
     * @return a ResponseEntity with all users or error message.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        if (!users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(users);
        }
    }

    /**
     * Logs a user into our EBookStore. Error message printed if unsuccessful.
     * @param email users email
     * @param password users password
     * @return a ResponseEntity confirmed the user is logged in or error message.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody String email, @RequestParam String password) {
        try {
            userService.loginUser(email, password);
            return ResponseEntity.status(HttpStatus.CREATED).body("User logged in");
         }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}