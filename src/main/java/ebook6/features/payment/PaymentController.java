/**
 * Controller Class for payment-related REST API endpoints.
 * @authors Thomas Hague
 * Created by Thomas Hague, 2/4/2025 with paymentController, create payment and getAllPayment methods.
 */

package ebook6.features.payment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Creates PaymentController using our PaymentService
     * @param paymentService
     */
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Creates a new payment by calling the createPayment method in the PaymentService class.
     * @param payment to create
     * @return a ResponseEntity with the created payment or an error message
     */
    @PostMapping
    public ResponseEntity<?> createPayment(Payment payment) {
        try {
            Payment createdPayment = paymentService.createPayment(payment);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPayment);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment failed.");
        }
    }

    /**
     * Identifies all payments in our database. Error message printed if none
     * @return a ResponseEntity with all users or error message.
     */
    @GetMapping
    public ResponseEntity<?> getAllPayments() {
        try {
        List<Payment> allPayments = paymentService.findAllPayments();
        return ResponseEntity.status(HttpStatus.OK).body(allPayments); }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment's not found.");
        }
    }

}
