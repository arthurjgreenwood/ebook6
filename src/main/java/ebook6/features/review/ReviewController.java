/**
 * Controller Class for review-related REST API endpoints.
 *
 * @authors Thomas Hague
 * Created by Thomas Hague, 1/4/2025 with package, annotations and ReviewController and createReview methods.
 * Modified by Thomas Hague 4/4/2025 with updateReview and deleteReview methods
 */

package ebook6.features.review;

import ebook6.ApiResponse;
import ebook6.loan.Loan;
import ebook6.loan.LoanService;
import ebook6.user.RegisterRequest;
import ebook6.user.User;
import ebook6.user.UserNotLoggedInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//TODO refactor review structures to have a user and an ebook associated with it. This can be pulled from the loan object

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Creates ReviewController using our ReviewService
     * @param reviewService
     */
    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @PostMapping("/add")
    public ApiResponse<?> createReview(@RequestBody ReviewRequest req) {
        try {
            //reviewService.createReview(getloan, reviewText, rating, title);
            return ApiResponse.success("Review created");
        } catch (IllegalStateException e) {
            return ApiResponse.fail("Review already exists");
        }
        catch (IllegalArgumentException e) {
            return ApiResponse.error(404, e.getMessage());
        }
        catch (UserNotLoggedInException e) {
            return ApiResponse.fail("User not logged in");
        }
    }

    /**
     * Updates a review by calling the updateReview method from our service class.
     * @param reviewId to be updated
     * @param updatedReview what the review will be updated to.
     * @return a ResponseEntity with the updated review or an error message
     */
    @PatchMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable UUID reviewId, @RequestBody Review updatedReview) {
        try {
            Review finalUpdatedReview = reviewService.updateReview(reviewId, updatedReview);
            return ResponseEntity.status(HttpStatus.OK).body(finalUpdatedReview);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Deletes a review from our database by calling the deleteReview message from our service class.
     * Checks if the reviewId matches one in our database. Error message if not.
     * @param reviewId of the review to be deleted
     * @return a ResponseEntity confirming the review is deleted or error message.
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable UUID reviewId) {
       try {
        Optional<Review> optionalReview = reviewService.findReviewById(reviewId);
            if (optionalReview.isPresent()) {
                reviewService.deleteReview(reviewId);
                return ResponseEntity.status(HttpStatus.OK).body("Review deleted successfully");
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found");
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
//    @GetMapping("/list")
//    public ResponseEntity<?> listReviews(@RequestParam UUID ebookId) {
//        //Requires extraction of eBook UUIDs from historical loans
//    }
    
}
