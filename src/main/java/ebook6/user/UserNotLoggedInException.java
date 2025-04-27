/**
 * Custom Exception for when a user tries to access website features that require being logged in, without being logged in.
 * @author Thomas Hague, created 4/4/2025
 */


package ebook6.user;

public class UserNotLoggedInException extends RuntimeException {

    public UserNotLoggedInException(String message) {
        super(message);
    }
}
