/**
 * Custom Exception for invalid passwords.
 * @author Thomas Hague, created 31/3/2025
 */

package ebook6.user;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(String errorMessage) {
        super(errorMessage);
    }
}
