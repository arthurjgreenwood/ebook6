/**
 * Repository interface for payment database operations.
 * We're using Optional to handle the case where no payment is found with the given title.
 * We're using List in cases where multiple payment may be returned.
 * @authors Thomas Hague
 * Created by Thomas Hague, 2/4/2025 with findById and findByUser methods.
 */

package ebook6.features.payment;

import ebook6.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

        /**
         * Finds a payment by paymentId
         * @param paymentId of the payment to search for
         * return an Optional containing the payment if found, or an empty Optional if not found
         */
        Optional<Payment> findById(UUID paymentId);

        /**
         * Finds payments by a given user
         * @param user of the payment to search for
         * return a List containing any payments by a user or an empty List if not found
         */
        List<Payment> findByUser(User user);

}
