/**
 * Service Class for payment-related operations.
 *
 * @authors Thomas Hague
 * Created by Thomas Hague, 2/4/2025 with package, annotations, PaymentService, create payment and findAllPayments methods.
 */

package ebook6.features.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    /**
     * Creates PaymentService using our PaymentRepository
     * @param paymentRepository
     */
    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Creates a payment and saves it to our database
     * @param payment to be created
     * @return the created payment
     */
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    /**
     * Finds all payments in our database.
     * @return a List containing all payment(s) or empty.
     */
    public List<Payment> findAllPayments() {
        return paymentRepository.findAll();
    }

    /**
     * Finds payments matching an paymentId from our database.
     * @param paymentId
     * @return an Optional containing the target payment or empty.
     */
    public Optional<Payment> findPaymentById(UUID paymentId) {
        return paymentRepository.findById(paymentId);
    }

}
