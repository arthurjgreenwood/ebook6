/**
 * Class representing a User.
 * @authors Fedrico Leal Quintero and Thomas Hague
 * Created by Fedrico Leal Quintero, 27/3/2025 with fields, no parameter constructor, getters and setters
 * Modified by Thomas Hague, 31/3/2025. Package, annotations, fields, getters, setters, methods (constructor with parameters,
 * toString, equals, hashCode) and comments added.
 * Modified by Arthur Greenwood, 6/5/2025. Replaced @Size with @Max for maxLoaned, added @GeneratedValue for ID, narrowed down constructor
 *  parameters to email and username to match the front end request parameters
 */

package ebook6.user;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    @Email
    private String email;
    @NotNull
    @Size(min = 8)
    private String password;
    @Column(nullable = false)
    private Double balance;
    @Column(nullable = false)
    private String address;
    private boolean loggedIn;
    private String jwtToken;
    private boolean admin;
    @Column(nullable = false)
    @Max(10)
    private int totalLoaned;
    private static final int maxLoans = 10;

    /**
     * Constructors for creating Users. Includes a no parameter constructor for the JPA and normal parameterised constructor.
     */
    public User() {
        //this.userId = UUID.randomUUID();
        this.balance = 0.0;
    }

    public User(String email, String password) {
        //this.userId = UUID.randomUUID();
        this.name = "Anonymous";
        this.email = email;
        this.password = password;
        this.address = "No Address";
        this.balance = 0.0;
        this.admin = false;
        this.loggedIn = false;
        this.totalLoaned = 0;
        //Most are assigned default values to allow easy registration, can be changed later in the users profile page
    }

    // Getters and setters
    public UUID getId() {
        return userId;
    }

    public void setId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public boolean getAdmin() { return admin; }

    public void setAdmin(boolean admin) { this.admin = admin; }

    public boolean getLoggedIn() { return loggedIn; }

    public void setLoggedIn(boolean loggedIn) { this.loggedIn = loggedIn; }

    public int getTotalLoaned() { return totalLoaned; }

    public void setTotalLoaned(int totalLoaned) { this.totalLoaned = totalLoaned; }

    public int getMaxLoans() { return maxLoans; }

    /**
     * Overrides toString to display user's name.
     * @return string representation of a user.
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Overrides equals to determine two users as equal if their email address matches.
     * @param o, to be compared
     * @return true equal, false if not.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        final User uA = (User) o;
        return getEmail().equals(uA.getEmail());
    }

    /**
     * Overrides hashCode so two users will have the same hashCode if their email address matches.
     * @return an int, the hashCode
     */
    @Override
    public int hashCode() {
        int hc = 19;
        return 31 * hc + getEmail().hashCode();
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}