/**
 * Custom Exception for when an eBook already exists in our database.
 * @author Thomas Hague, created 31/3/2025
 */

package ebook6.ebook;

public class EbookAlreadyInDatabaseException extends RuntimeException {

    public EbookAlreadyInDatabaseException(String message) {
        super(message);
    }
}
