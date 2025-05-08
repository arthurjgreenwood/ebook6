/**
 * Service Class for review-related operations.
 *
 * @authors Thomas Hague and Arthur Greenwood
 * Created by Thomas Hague, 1/4/2025 with package, annotations, ReviewService, createReview, reviewValidation methods.
 * Modified by Thomas Hague 4/4/2025 with findReviewsByUser, findReviewsByTitle, findReviewsById, findReviewsByLoan,
 * findReviewsByLoanId, updateReview and deleteReview methods.
 * Modified by Arthur Greenwood 05/5/2025. Commented out findByUser method as this is tied to the Loan object
 * Modified by Thomas Hague, 8/5/2025. Modified createReview, added findReviewByEbook.
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

