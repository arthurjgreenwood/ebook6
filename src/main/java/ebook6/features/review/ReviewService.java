/**
 * Service Class for review-related operations.
 *
 * @authors Thomas Hague
 * Created by Thomas Hague, 1/4/2025 with package, annotations, ReviewService, createReview, reviewValidation methods.
 * Modified by Thomas Hague 4/4/2025 with findReviewsByUser, findReviewsByTitle, findReviewsById, findReviewsByLoan,
 * findReviewsByLoanId, updateReview and deleteReview methods.
 */

package ebook6.features.review;

import ebook6.loan.Loan;
import ebook6.user.User;
import ebook6.user.UserNotLoggedInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * Creates a new review by calling the createReview method in the ReviewService class.
     * Also calls the reviewValidation method, exceptions thrown if review is invalid or loan already has a review.
     *
     * @param loan       to create the review for
     * @param reviewText body of the review
     * @param rating     of the review (out of 5)
     * @return a ResponseEntity with the created review or an error message
     */
    @Transactional
    public Review createReview(Loan loan, String reviewText, int rating, User user, String title) {
        if (reviewRepository.findByLoan(loan).isPresent()) {
            throw new IllegalStateException("Review already exists");
        }
        if (!loan.getUser().getLoggedIn()) {
            throw new UserNotLoggedInException("User needs to be logged in to leave a review.");
        }
        reviewValidation(reviewText, rating);
        Review review = new Review(loan, reviewText, rating, user, title);
        return reviewRepository.save(review);
    }

    /**
     * Validates a review by ensuring the body isn't empty and the rating is between 1-5.
     * Exceptions throw if errors.
     *
     * @param reviewText body of the review
     * @param rating     of the review (out of 5)
     */
    private void reviewValidation(String reviewText, int rating) {
        if (reviewText == null || reviewText.isEmpty()) {
            throw new IllegalArgumentException("Review text can't be empty");
        }
        if (rating <= 0 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
    }

    /**
     * Finds reviews by a given user from our database.
     * @param user
     * @return a List containing the target review(s) or empty.
     */
    public List<Review> findReviewsByUser(User user) {
        return reviewRepository.findByUser(user);
    }

    /**
     * Finds reviews by a given title from our database.
     * @param title of the ebook
     * @return a List containing the target review(s) or empty.
     */
    public List<Review> findReviewsByTitle(String title) {
        return reviewRepository.findByTitleContainingIgnoreCase(title);
    }

    /**
     * Finds reviews matching an reviewId from our database.
     * @param reviewId
     * @return an Optional containing the target review or empty.
     */
    public Optional<Review> findReviewById(UUID reviewId) {
        return reviewRepository.findByReviewId(reviewId);
    }

    /**
     * Finds reviews of a given loan from our database.
     * @param loan
     * @return a List containing the target review(s) or empty.
     */
    public Optional<Review> findReviewByLoan(Loan loan) {
        return reviewRepository.findByLoan(loan);
    }

    /**
     * Finds reviews of a given loan by Id from our database.
     * @param loanId
     * @return a List containing the target review(s) or empty.
     */
    public Optional<Review> findLoanByLoanId(UUID loanId) {
        return reviewRepository.findByLoan_LoanId(loanId);
    }

    /**
     * Updates a review in our database, by changing review text and/or rating as specified.
     * Checks the review exists by searching for reviewId, Exception thrown if it doesn't.
     * @param reviewId that will be updated
     * @param updatedReview what the review will be updated to
     * @return the updated review.
     */
    @Transactional
    public Review updateReview(UUID reviewId, Review updatedReview) {
        Optional<Review> optionalReview = findReviewById(reviewId);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            review.setReviewText(updatedReview.getReviewText());
            review.setRating(updatedReview.getRating());
            return reviewRepository.save(review);
        } else {
            throw new EntityNotFoundException("Review not found");
        }
    }

    /**
     * Deletes a review from our databases
     * Checks the review does exist first by searching for reviewId, Exception thrown if it doesn't.
     * @param reviewId to be deleted
     */
    public void deleteReview(UUID reviewId) {
        Optional<Review> optionalReview = findReviewById(reviewId);
        if (optionalReview.isPresent()) {
            reviewRepository.delete(optionalReview.get());
        }
        else {
            throw new EntityNotFoundException("Review not found");
        }
    }

}
