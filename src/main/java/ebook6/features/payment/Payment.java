/**
 * Class representing a Payment.
 * @authors Thomas Hague and Arthur Greenwood
 * Created by Thomas Hague, 31/3/2025 with package, annotations, fields, constructors, getters and setters.
 * Modified by Arthur Greenwood 05/5/2025. Made paymentDate nullable
 */

package ebook6.features.payment;

import ebook6.user.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "Payment")
public class Payment {
    @Id
    private UUID paymentId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Range(min = 0)
    @Column(nullable = false)
    private double amount;
    @CreationTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column //This needs to be nullable because hibernate is automatically populating the dates as 0000-00-00
    private LocalDate paymentDate;
    @Column
    private LocalTime paymentTime;

    /**
     * Constructors for creating payments. Includes a no parameter constructor for the JPA and normal parameterised constructor.
     */
    public Payment() {
        this.paymentId = UUID.randomUUID();
        this.paymentDate = LocalDate.now();
        this.paymentTime = LocalTime.now();
    }

    public Payment(User user, double amount) {
        this.paymentId = UUID.randomUUID();
        this.user = user;
        this.amount = amount;
        this.paymentDate = LocalDate.now();
        this.paymentTime = LocalTime.now();
    }

    // getters and setters
    public UUID getPaymentId() { return paymentId; }

    public User getUser() { return user; }

    public double getAmount() { return amount; }

    public LocalDate getPaymentDate() { return paymentDate; }

    public LocalTime getPaymentTime() { return paymentTime; }
}
