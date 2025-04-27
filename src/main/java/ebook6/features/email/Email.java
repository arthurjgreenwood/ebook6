/**
 * Class representing an Email.
 * @author Thomas Hague
 * Created by Thomas Hague, 1/4/2025 with package, annotations, fields, constructors, getters and setters.
 */

package ebook6.features.email;
import ebook6.loan.Loan;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Email")
public class Email {
    @Id
    private UUID emailId;
    @ManyToOne
    @JoinColumn(name = "Loan_id", nullable = false)
    private Loan loan;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String body;

    @CreationTimestamp
    private LocalDateTime sentAt;

    /**
     * Constructors for creating emails. Includes a no parameter constructor for the JPA and normal parameterised constructor.
     */
    public Email(Loan loan, String subject, String body) {
        this.emailId = UUID.randomUUID();
        this.loan = loan;
        this.subject = subject;
        this.body = body;
        this.sentAt = LocalDateTime.now();
    }

    public Email() {
        // no parameter constructor for the JPA
        this.emailId = UUID.randomUUID();
        this.sentAt = LocalDateTime.now();
    }

    // getters and setters

    public UUID getEmailId() { return emailId; }

    public Loan getLoan() { return loan; }

    public void setLoan(Loan loan) { this.loan = loan; }

    public String getSubject() { return subject; }

    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }

    public void setBody(String body) { this.body = body; }

    public LocalDateTime getSentAt() { return sentAt; }

}
