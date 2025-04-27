/**
 * Class representing a Review.
 * @author Thomas Hague
 * Created by Thomas Hague, 1/4/2025 with package, annotations, fields, constructors, getters and setters.
 */


package ebook6.features.review;

import ebook6.loan.Loan;
import ebook6.user.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.swing.border.TitledBorder;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Review")
public class Review {
    @Id
    private UUID reviewId;
    @ManyToOne
    @JoinColumn(name = "Loan_id", nullable = false)
    private Loan loan;
    @Column(nullable = false)
    private String reviewText;
    @Column(nullable = false)
    @Range(min = 1, max = 5)
    private int rating;
    @CreationTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateSent;
    @ManyToOne
    @JoinColumn(name = "Reviewer", nullable = false)
    private User user;
    @Column(nullable = false)
    private String title;


    /**
     * Constructors for creating reviews. Includes a no parameter constructor for the JPA and normal parameterised constructor.
     */
    public Review(Loan loan, String reviewText, int rating, User user, String title) {
        this.reviewId = UUID.randomUUID();
        this.loan = loan;
        this.reviewText = reviewText;
        this.rating = rating;
        this.dateSent = LocalDateTime.now();
        this.user = user;
        this.title = title;
    }

    public Review() {
        this.reviewId = UUID.randomUUID();
        this.dateSent = LocalDateTime.now();
    }

    // getters and setters

    public UUID getReviewId() { return reviewId; }

    public Loan getLoan() { return loan; }

    public String getReviewText() { return reviewText; }

    public void setReviewText(String reviewText) { this.reviewText = reviewText; }

    public int getRating() { return rating; }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDateTime getDateSent() { return dateSent; }


}
