/**
 * Service Class for payment-related operations.
 *
 * @authors Thomas Hague
 * Created by Thomas Hague, 2/4/2025 with package, annotations, PaymentService, create payment and findAllPayments methods.
 */

package ebook6.features.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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
     * If horsePay validates the payment, the payment is saved. Error is thrown if not.
     * @param payment to be created
     * @return the created payment
     */
    public Payment createPayment(Payment payment) {
        if (checkHorsePayPayment(payment)) {
            return paymentRepository.save(payment);
        }
        else {
            throw new HorsePayFailedException("HorsePay API failed. Please try again");
        }

    }

    /**
     * Method for checking if a payment is successful, by sending the payment to Dan's HorsePay API.
     * The payment details are stored in a HashMap and posted to the HorsePay API endpoint.
     * @param payment to check
     * @return true if the payment is sucessful, false otherwise.
     */
    private boolean checkHorsePayPayment(Payment payment) {
        String horsePayURL = "http://homepages.cs.ncl.ac.uk/daniel.nesbitt/CSC8019/HorsePay/HorsePay.php";

        Map<String, Object> payments = createMap(payment);

        try {
            ResponseEntity<Map> response = new RestTemplate().postForEntity(horsePayURL, payments, Map.class);
            Map body = response.getBody();
            if (body == null) {
                throw new HorsePayFailedException("HorsePay API failed and returned null. Please try again");
            }
            if (body.containsKey("paymentSuccess")) {
                Map paymentSuccess = (Map) body.get("paymetSuccess");
                Object status = paymentSuccess.get("Status");
                if (status instanceof Boolean) {
                    return (Boolean) status;
                }
            }
        }
        catch (HorsePayFailedException e) {
            System.out.println("HorsePay API error. Please try again");
        }
        return false;
    }

    /**
     * Creates a Map for storing payment details to send to the HorsePay API, including the fields mentioned in the HorsePay
     * documentation.
     * The label for each data will be the key in the map, and the corresponding value will be the value in the map.
     * @param payment , including the relevant information to send
     * @return a map to be sent with the payment details.
     */
    private Map<String, Object> createMap(Payment payment) {
        Map<String, Object> payments = new HashMap<>();
        payments.put("storeID", "Team13");
        payments.put("customerID", payment.getUser().getId().toString());
        payments.put("date", payment.getPaymentDate().toString());
        payments.put("time",  payment.getPaymentTime().toString());
        payments.put("timeZone", "BST");
        payments.put("transactionAmount", payment.getAmount());
        payments.put("currencyCode", "GBP");
        payments.put("forcePaymentSatusReturnType", true);
        return payments;
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
