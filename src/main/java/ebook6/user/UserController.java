/**
 * Controller Class for user-related REST API endpoints.
 *
 * @authors Fedrico Leal Quintero, Thomas Hague and Arthur Greenwood
 * Created by Fedrico Leal Quintero, 27/3/2025 with UserController and createUser methods
 * Modified by Thomas Hague 31/3/2025. Package and comments added. Methods updateUser, promoteUserToAdmin, deleteUserByEmail,
 * deleteUserByID, getUserByEmail, getUserByID and getAllUsers developed. Method createUser edited now with custom exceptions.
 * Modified by Thomas Hague, 4/4/2025. loginUser added.
 * Modified by Arthur Greenwood, 6/5/2025. Created topUpBalance, refactored all methods to return ApiResponses
 * Modified by Thomas Hague, 6/5/2025. createUser method edited.
 * Modified by Arthur Greenwood, 7/5/2025. Refactored loginUser
 */

// getUserMethods??!!
//todo
package ebook6.user;

import ebook6.ApiResponse;
import org.hibernate.id.insert.IdentifierGeneratingInsert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.*;


@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    
    /**
     * Creates an UserController using our ebookService
     * @param userService
     */
    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user by calling the createUser method from our service class.
     * @param reg
     * @return a ResponseEntity with the created user or an error message
     */
    @PostMapping("/register")
    @Transactional
    public ApiResponse<?> createUser(@RequestBody RegisterRequest reg) {
        try {
            User createdUser = userService.createUser(reg.getEmail(), reg.getPassword());
            return ApiResponse.success(createdUser);
        } catch (UserAlreadyInDatabaseException e) {
            return ApiResponse.fail(e.getMessage());
        } catch (InvalidPasswordException e) {
            return ApiResponse.fail("Invalid password");
        }
    }
    
    
    @PostMapping("/topup")
    @Transactional
    public ApiResponse<?> topUpBalance(@RequestParam UUID userID, @RequestParam double amount) {
        try {
            Optional<User> user = userService.findUserByUserId(userID);
            if (user.isEmpty()) {
                return ApiResponse.error(404, "User not found");
            }
            userService.topUpBalance(userID, amount);
            return ApiResponse.success(String.format("Successfully added %.2f GBP", amount));
        } catch (UserNotLoggedInException e) {
            return ApiResponse.fail("User not logged in");
        }
    }
    

    /**
     * Updates a user by calling the updateUser method from our service class.
     * @param userId to be updated
     * @param updatedUser what the user will be updated to.
     * @return a ResponseEntity with the updated User or an error message
     */
    @PatchMapping("/{userId}")
    @Transactional
    public ApiResponse<?> updateUser(@PathVariable UUID userId, @RequestBody User updatedUser) {
        try {
            User finalUser = userService.updateUser(userId, updatedUser);
            return ApiResponse.success(finalUser);
        }
        catch (EntityNotFoundException e) {
            return ApiResponse.error(404, "User not found");
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
    @Transactional
    public ApiResponse<String> promoteUserToAdmin (@RequestParam UUID currentAdminId, @RequestParam UUID newAdminId) {
        Optional<User> optionalCurrentAdmin = userService.findUserByUserId(currentAdminId);
        Optional<User> optionalNewAdmin = userService.findUserByUserId(newAdminId);
        if (optionalCurrentAdmin.isPresent() && optionalNewAdmin.isPresent()) {
            User currentAdmin = optionalCurrentAdmin.get();
            User newAdmin = optionalNewAdmin.get();
            try {
                userService.makeAdmin(currentAdmin, newAdmin);
                return ApiResponse.success("User successfully promoted");
            } catch (InvalidAccessException e) {
                return ApiResponse.fail("Invalid access");
            }
        }
        else {
            return ApiResponse.error(404, "User not found");
        }
    }

    /**
     * Deletes a user from our database by calling the delete user message from our service class.
     * Checks if the user's email address matches one in our database. Error message if not.
     * @param email of the user to be deleted
     * @return a ResponseEntity confirming the user is deleted or error message.
     */
    @DeleteMapping("/{email}")
    @Transactional
    public ApiResponse<?> deleteUserByEmail(@PathVariable String email) {
        Optional<User> optionalUser = userService.findUserByEmail(email);
        if (optionalUser.isPresent()) {
            User userToDelete = optionalUser.get();
            userService.deleteUser(userToDelete);
            return ApiResponse.success("User deleted successfully");
        } else {
            return ApiResponse.error(404, "User not found");
        }
    }

    /**
     * Deletes a user from our database by calling the delete user message from our service class.
     * Checks if the user's Id matches one in our database. Error message if not.
     * @param userId of the user to be deleted
     * @return a ResponseEntity confirming the user is deleted or error message.
     */
    @DeleteMapping("/{userId}")
    @Transactional
    public ApiResponse<?> deleteUserById(@PathVariable UUID userId) {
        Optional<User> optionalUser = userService.findUserByUserId(userId);
        if (optionalUser.isPresent()) {
            User userToDelete = optionalUser.get();
            userService.deleteUser(userToDelete);
            return ApiResponse.success("User deleted successfully");
        } else {
            return ApiResponse.error(404, "User not found");
        }
    }

    /**
     * Identifies a user by matching their email address to one in our database. Error message printed if not.
     * @param email of the user we are looking for
     * @return a ResponseEntity with the user or error message.
     */
    @GetMapping("/email/{email}")
    public ApiResponse<?> getUserByEmail(@PathVariable String email) {
        Optional<User> optionalUser = userService.findUserByEmail(email);
        if (optionalUser.isPresent()) {
            return ApiResponse.success(optionalUser.get());
        } else {
            return ApiResponse.error(404, "User not found");
        }
    }

    /**
     * Identifies a user by matching their userId to one in our database. Error message printed if not.
     * @param userId of the user we are looking for
     * @return a ResponseEntity with the user or error message.
     */
    @GetMapping("/id/{userId}")
    public ApiResponse<?> getUserById(@PathVariable UUID userId) {
        Optional<User> optionalUser = userService.findUserByUserId(userId);
        if (optionalUser.isPresent()) {
            return ApiResponse.success(optionalUser.get());
        } else {
            return ApiResponse.error(404, "User not found");
        }
    }

    /**
     * Identifies a user by matching their name to one in our database. Error message printed if not.
     * @param name of the user we are looking for
     * @return a ResponseEntity with matching users or error message.
     */
    @GetMapping("/name/{name}")
    public ApiResponse<List<User>> getUsersByName(@PathVariable String name) {
        List<User> users = userService.findByNameIgnoreCase(name);
        if (!users.isEmpty()) {
            return ApiResponse.success(users);
        } else {
            return ApiResponse.error(404, "User(s) not found");
        }
    }

    /**
     * Identifies all users in our database. Error message printed if none
     * @return a ResponseEntity with all users or error message.
     */
    @GetMapping
    public ApiResponse<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        if (!users.isEmpty()) {
            return ApiResponse.success(users);
        } else {
            return ApiResponse.error(404, "No users found");
        }
    }

    /**
     * Logs a user into our EBookStore. Error message printed if unsuccessful.
     * @param payload a Map containing the users email and password
     * @return ApiResponse containing user ID or an error.
     */
    @PostMapping("/login")
    @Transactional
    public Object loginUser(@RequestBody Map<String, String> payload) {
        try {
            
            return userService.loginUser(payload.get("email"), payload.get("password"))
                    .map(user -> Map.of(
                            "message", "Login successful",
                            "userId", user.getUserId()
                    ))
                    .orElseThrow(() -> new RuntimeException("Invalid credentials"));
            
         } //userService.findUserByEmail(payload.get("email"))
        catch (EntityNotFoundException e) {
            return ApiResponse.error(404, "User not found");
        }
        catch (IllegalArgumentException e) {
            return ApiResponse.fail("Invalid email or password");
        }
    }

}