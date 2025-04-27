/**
 * Controller Class for email-related REST API endpoints.
 * @authors Thomas Hague
 * Created by Thomas Hague, 1/4/2025 with EmailController and create confirmation, cancellation and reminder methods.
 */

package ebook6.features.email;

import ebook6.features.review.Review;
import ebook6.features.review.ReviewService;
import ebook6.loan.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/emails")
public class EmailController {

    private final EmailService emailService;

    /**
     * Creates EmailController using our EmailService
     * @param emailService
     */
    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Creates a new loan confirmation Email by calling the createConfirmation method in the EmailService class.
     * @param loan to create the email for
     * @return a ResponseEntity with the created loan or an error message
     */
    @PostMapping
    public ResponseEntity<?> createConfirmationEmail(@RequestBody Loan loan) {
        try {
            Email confirmationEmail = emailService.createConfirmationEmail(loan);
            return ResponseEntity.status(HttpStatus.CREATED).body(confirmationEmail);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Creates a new loan cancellation Email by calling the createConfirmation method in the EmailService class.
     * @param loan to create the email for
     * @return a ResponseEntity with the created loan or an error message
     */
    @PostMapping
    public ResponseEntity<?> createCancellationEmail(@RequestBody Loan loan) {
        try {
            Email cancellationEmail = emailService.createCancellationEmail(loan);
            return ResponseEntity.status(HttpStatus.CREATED).body(cancellationEmail);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Creates a new loan terminating soon reminder Email by calling the createConfirmation method in the EmailService class.
     * @param loan to create the email for
     * @return a ResponseEntity with the created loan or an error message
     */
    @PostMapping
    public ResponseEntity<?> createReminderEmail(@RequestBody Loan loan) {
        try {
            Email reminderEmail = emailService.createReminderEmail(loan);
            return ResponseEntity.status(HttpStatus.CREATED).body(reminderEmail);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

