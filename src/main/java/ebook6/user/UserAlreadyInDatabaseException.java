/**
 * Custom Exception for when someone tries to add a user that already exists in our database.
 * @author Thomas Hague, created 31/3/2025
 */

package ebook6.user;

public class UserAlreadyInDatabaseException extends RuntimeException {

    public UserAlreadyInDatabaseException(String errorMessage) {
        super(errorMessage);
    }
}
