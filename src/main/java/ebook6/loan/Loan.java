/**
 * Class representing a Loan.
 * @author Thomas Hague
 * Created by Thomas Hague, 31/3/2025 with package, fields, annotations, constructors, getters and setters.
 */

package ebook6.loan;

import ebook6.ebook.EBook;
import ebook6.user.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "Loan")
public class Loan {
    @Id
    private UUID loanId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "ebook_id", nullable = false)
    private EBook ebook;
    @Column(nullable = false)
    private boolean isLiveStatus;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;

    /**
     * Constructors for creating Loans. Includes a no parameter constructor for the JPA and normal parameterised constructor.
     */
    public Loan(User user, EBook ebook, LocalDate startDate, LocalDate terminationDate) {
        this.loanId = UUID.randomUUID();
        this.user = user;
        this.ebook = ebook;
        this.isLiveStatus = true;
        this.startDate = startDate;
        this.endDate = terminationDate;
    }

    public Loan() {
        this.loanId = UUID.randomUUID();
    }

    // getters and setters

    public UUID getLoanId() { return loanId; }

    public User getUser() { return user; }

    public EBook getEbook() { return ebook; }

    public boolean getStatus() {
        return isLiveStatus;
    }

    public void setStatus(boolean isLiveStatus) {
        this.isLiveStatus = isLiveStatus;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}

