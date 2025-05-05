/**
 * Custom Exception for when horsePay doesn't run as expected.
 * @author Thomas Hague, created 31/3/2025
 */


package ebook6.features.payment;

public class HorsePayFailedException extends RuntimeException {
    public HorsePayFailedException(String message) {
        super(message);
    }
}
