/**
 * Service Class for user-related operations.
 *
 * @authors Fedrico Leal Quintero and Thomas Hague
 * Created by Fedrico Leal Quintero, 27/3/2025 with UserService, createUser and invalidPassword methods
 * Modified by Thomas Hague, 31/3/2025. New package, annotations, methods (findUserByEmail, findUserById, findByNAmeIgnoreCase
 * findAllUsers, updateUser, deleteUser, makeAdmin) and comments added. InvalidPassword and createUser methods edited.
 * Modified by Thomas Hague, 4/4/2025. loginUser added.
 */
package ebook6.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Creates userService using our userRepository
     * @param userRepository
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Finds user with a target email address from our database.
     * @param email
     * @return an Optional containing the target user or empty.
     */
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    /**
     * Finds user with a target userId from our database.
     * @param userId
     * @return an Optional containing the target user or empty.
    */
    public Optional<User> findUserByUserId(UUID userId) {
        return userRepository.findByUserId(userId);
    }

    /**
     * Finds users with a target name from our database.
     * @param name
     * @return a List containing the target user(s) or empty.
     */
    public List<User> findByNameIgnoreCase(String name) {
        return userRepository.findByNameIgnoreCase(name);
    }

    /**
     * Finds all users in our database.
     * @return a List containing all user(s) or empty.
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Creates a new user and saves it to the database.
     * Checks if the email already exists in the database (case insensitive) and validates the password using a regular
     * expression, exceptions thrown if errors
     *
     * @param user the user to create
     * @return the created user
     */
    public User createUser(User user) throws RuntimeException {
        // Check if email already exists
        if (userRepository.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
            throw new UserAlreadyInDatabaseException("User with email: " + user.getEmail() + " already exists");
        }
        // Validate password
        if (!isValidPassword(user.getPassword())) {
            throw new InvalidPasswordException("Password must contain at least one uppercase letter, one lowercase letter, and one number" +
                    "and be a minimum of 8 characters");
        }
        System.out.println("User: " + user + " has been added to our website");
        return userRepository.save(user);
    }

    /**
     * Checks if a password is valid by ensuring it contains at least one uppercase letter, one lowercase letter, and one number
     * and is 8 or more characters.
     *
     * @param password the password to validate
     * @return true if the password is valid, false otherwise
     */
    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$";
        return Pattern.matches(regex, password) && password.length() >= 8;
    }

    /**
     * Updates a user in our database, by changing name, email, password or address as specified.
     * Checks the user exists by searching for userId, Exception thrown if it doesn't.
     * @param existingUserId that will be updated
     * @param updatedUser what the user will be updated to
     * @return the updated user.
     */
    public User updateUser(UUID existingUserId, User updatedUser) {
        Optional<User> optionalUser = userRepository.findByUserId(existingUserId);
        User existingUser;
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("User with id: " + existingUserId + " doesn't exist in our book store");
        } else {
            existingUser = optionalUser.get();
        }
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setAddress(updatedUser.getAddress());
        return userRepository.save(existingUser);
    }

    /**
     * Deletes a user from our databases
     * Checks the user does exist first by searching for user's email, Exception thrown if it doesn't.
     * @param user to be deleted
     */
    public void deleteUser(User user) {
        if (userRepository.findByEmailIgnoreCase(user.getEmail()).isEmpty()) {
            throw new EntityNotFoundException("User with email: " + user.getEmail() + " doesn't exist in our website");
        }
        userRepository.delete(user);
        System.out.println("User with email: " + user.getEmail() + " has been deleted");
    }

    /**
     * Changes a users' admin privileges to make them an admin and saves changes to our database. Requires a current admin to make the change.
     * Exceptions throw if either user doesn't exist, or the user making the change doesn't have admin privileges.
     * @param currentAdminUser making the change
     * @param newAdminUser to change into admin
     * @return
     */
    public User makeAdmin (User currentAdminUser, User newAdminUser) {
        if (!userRepository.findByEmailIgnoreCase(currentAdminUser.getEmail()).isPresent()) {
            throw new EntityNotFoundException("User with email: " + currentAdminUser.getEmail() + " doesn't exist in our website");
        }
        if (!userRepository.findByEmailIgnoreCase(newAdminUser.getEmail()).isPresent()) {
            throw new EntityNotFoundException("User with email: " + newAdminUser.getEmail() + " doesn't exist in our website");
        }
        if (currentAdminUser.getAdmin()) {
            throw new InvalidAccessException("You don't have permission to make admin changes");
        }
        newAdminUser.setAdmin(true);
        return userRepository.save(newAdminUser);
    }

    /**
     * Method for logging in a user and updating their logged in status in our database.
     * Exceptions thrown if password is incorrect or email doesn't match an address in our database.
     * @param email user's email
     * @param password user's password
     */
    public void loginUser(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getPassword().equals(password)) {
                user.setLoggedIn(true);
                userRepository.save(user);
            }
            else {
                throw new IllegalArgumentException("Incorrect password, please try again.");
            }
        }
        else {
            throw new EntityNotFoundException("User with email: " + email + " doesn't exist in our website");
        }
    }


}