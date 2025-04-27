/**
 * Repository interface for Loan database operations.
 * We're using Optional to handle the case where no loan is found with the given title.
 * We're using List in cases where multiple loans may be returned.
 * @authors Thomas Hague
 * Created by Thomas Hague, 31/3/2025 with package, annotations, findByIdIgnoreCase, findByEBook, findByUser, findByUserAndEBook
 * findByStatus and findAllLoans methods.
 */
package ebook6.loan;

import ebook6.ebook.EBook;
import ebook6.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoanRepository extends JpaRepository<Loan, UUID> {

    /**
     * Finds a loan by loanID
     * @param loanId of the relevant loan.
     * return an Optional containing the EBook if found, or an empty Optional if not found
     */
    Optional<Loan> findByLoanId(UUID loanId);

    /**
     * Finds Loans by eBook
     * @param eBook to search for
     * @return a List of the ebook's Loans. Returns an empty List if none found.
     */
    List<Loan> findByEbook(EBook eBook);

    /**
     * Finds Loans by user
     * @param user to search for
     * @return a List of Loans by the user. Returns an empty List if none found.
     */
    List<Loan> findByUser(User user);

    /**
     * Finds Loans by ebook and user
     * @param ebook to search for
     * @param user to search for
     * @return a List of Loans of an ebook by the user. Returns an empty List if none found.
     */
    List<Loan> findByEbookAndUser(EBook ebook, User user);

    /**
     * Finds Loans by status (loan still live or loan ended)
     * @param isLiveStatus to search for
     * @return a List of Loans matching the status. Returns an empty List if none found.
     */
    List<Loan> findByIsLiveStatus(Boolean isLiveStatus);
}