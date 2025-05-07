/**
 * Controller Class for payment-related REST API endpoints.
 * @authors Thomas Hague
 * Created by Thomas Hague, 2/4/2025 with paymentController, create payment and getAllPayment methods.
 * Modified by Thomas Hague 6/5/2025.
 * Modified by Thomas Hague 6/5/2025. createPayment and getAllPayments method edited.
 */

package ebook6.features.payment;

import ebook6.ApiResponse;
import ebook6.user.User;
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
     * @param user making the payment
     * @param amount amount of the payment
     * @return a ResponseEntity with the created payment or an error message
     */
    @PostMapping
    public ApiResponse<?> createPayment(@RequestBody User user, double amount) {
        try {
            Payment createdPayment = paymentService.createPayment(user, amount);
            return ApiResponse.success(createdPayment);
        }
        catch (HorsePayFailedException e) {
            return ApiResponse.fail(e.getMessage());
        }

    }

    /**
     * Identifies all payments in our database. Error message printed if none
     * @return a APIResponse with all payments or error message.
     */
    @GetMapping
    public ApiResponse<List<Payment>> getAllPayments() {
        try {
        List<Payment> allPayments = paymentService.findAllPayments();
        if (!allPayments.isEmpty()) {
            return ApiResponse.success(allPayments);
        }
        return ApiResponse.success(allPayments); }
        catch (Exception e) {
            return ApiResponse.error(404, "Payment(s) not found");
        }
    }

}
