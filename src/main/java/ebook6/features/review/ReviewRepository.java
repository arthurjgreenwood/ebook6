/**
 * Repository interface for review database operations.
 * We're using Optional to handle the case where no review is found with the given title.
 * We're using List in cases where multiple reviews may be returned.
 * @authors Thomas Hague
 * Created by Thomas Hague, 1/4/2025 with package, annotations and findById, findByLoan_LoanId, findByLoan, findByTitleContainingIgnoreCase
 * and findByUser methods.
 */
package ebook6.features.review;

import ebook6.loan.Loan;
import ebook6.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {

    /**
     * Finds a review by reviewId
     * @param reviewId of the review to search for
     * return an Optional containing the review if found, or an empty Optional if not found
     */
    Optional<Review> findByReviewId(UUID reviewId);

    /**
     * Finds a review by loanId
     * @param loanId of the review to search for
     * return an Optional containing the review if found, or an empty Optional if not found
     */
    Optional<Review> findByLoan_LoanId(UUID loanId);

    /**
     * Finds a review by loan
     * @param loan of the review to search for
     * return an Optional containing the review if found, or an empty Optional if not found
     */
    Optional<Review> findByLoan(Loan loan);

    /**
     * Finds reviews by a given ebook Title
     * @param title of the reviews to search for
     * return a List containing any reviews of the book by title or an empty List if not found
     */
    List<Review> findByTitleContainingIgnoreCase(String title);

    /**
     * Finds reviews of all ebooks by a given user
     * @param user of the reviews to search for
     * return a List containing any reviews of the book by the user or an empty List if not found
     */
    List<Review> findByUser(User user);


}
