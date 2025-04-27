/**
 * Repository interface for user-related database operations.
 * We're using Optional to handle the case where no user is found with the given email.
 * We're using List in cases where multiple users may be returned.
 * @authors Fedrico Leal Quintero and Thomas Hague
 * Created by Fedrico Leal Quintero, 27/3/2025 with findByEmailIgnoreCase method.
 * Modified by Thomas Hague, 31/3/2025. Package, annotations and methods findByName and findByNameIgnoreCase added.
 */

package ebook6.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Finds a user by email (case-insensitive).
     * @param email the email to search for
     * @return an Optional containing the user if found, or an empty Optional if not found
     */
    Optional<User> findByEmailIgnoreCase(String email);

    /**
     * Finds a user by userId (case-insensitive).
     * @param userId the userId to search for
     * @return a mOptional containing the user if found, or an empty Optional if not found
     */
    Optional<User> findByUserId(UUID userId);

    /**
     * Finds a user by name (case-insensitive).
     * @param name the name to search for
     * @return a List containing the users matching name, or an empty List if not found
     */
    List<User> findByNameIgnoreCase(String name);

}