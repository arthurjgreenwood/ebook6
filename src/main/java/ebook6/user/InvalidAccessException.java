/**
 * Custom Exception for a user using trying to make administrative changes with admin privileges.
 * @author Thomas Hague, created 31/3/2025
 */

package ebook6.user;

public class InvalidAccessException extends RuntimeException {

    public InvalidAccessException(String message) {
        super(message);
    }
}
