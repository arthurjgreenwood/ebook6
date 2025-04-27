/**
 * Repository interface for email database operations.
 * We're using Optional to handle the case where no email is found with the given title.
 * We're using List in cases where multiple emails may be returned.
 * @authors Thomas Hague
 * Created by Thomas Hague, 1/4/2025 with findById, and findByLoan methods.
 */

package ebook6.features.email;

import ebook6.loan.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailRepository extends JpaRepository<Email, String> {

    /**
     * Finds an email by emailId
     * @param emailId of the email to search for
     * return an Optional containing the email if found, or an empty Optional if not found
     */
    Optional<Email> findByEmailId(UUID emailId);

    /**
     * Finds emails by a given loan
     * @param loan of the emails to search for
     * return a List containing any emails for a loan or an empty List if not found
     */
    List<Email> findByLoan(Loan loan);
}

