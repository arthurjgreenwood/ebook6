/**
 * Class representing an EBook.
 *
 * @author Thomas Hague
 * Created by Thomas Hague, 31/3/2025 with package, fields, constructors, getters, setters, toString, equals and hashCode methods.
 */

package ebook6.ebook;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "EBook")
public class EBook {

    @Id
    private UUID ebookId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private int quantityAvailable;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private int maxLoanDuration;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private double avgRating;
    private String coverURL;

    /**
     * Constructors for creating EBooks. Includes a no parameter constructor for the JPA and normal parameterised constructor.
     */
    public EBook() {
        this.ebookId = UUID.randomUUID();
        this.avgRating = 0.0;
    }

    public EBook(String title, String author, int quantityAvailable, String category, double price, int maxLoanDuration, String description) {
        this.ebookId = UUID.randomUUID();
        this.title = title;
        this.author = author;
        this.quantityAvailable = quantityAvailable;
        this.category = category;
        this.price = price;
        this.description = description;
        this.maxLoanDuration = maxLoanDuration;
        this.avgRating = 0.0;
    }

    // getters and setters

    public UUID getEBookId() {
        return ebookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMaxLoanDuration() {
        return maxLoanDuration;
    }

    public void setMaxLoanDuration(int maxLoanDuration) {
        this.maxLoanDuration = maxLoanDuration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public String getCoverURL() {
        return coverURL;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    /**
     * Overrides toString to display books by Title and Author.
     * @return string representation of EBooks
     */
    @Override
    public String toString() {
        return getTitle() + " by " + getAuthor();
    }

    /**
     * Overrides equals to determine two books as equal if they have the same title and author.
     * @param obj , the EBook to be compared
     * @return true if equal, false if not
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EBook)) {
            return false;
        }
        final EBook eB = (EBook) obj;
        return this.getTitle().equals(eB.getTitle()) && this.getAuthor().equals(eB.getAuthor());
    }

    /**
     * Overrides hashCode so two EBookw will have the same hashCode if their title and author match.
     * @return an int, the hashCode
     */
    @Override
    public int hashCode() {
        int hc = 19;
        hc = hc * 37 + this.getTitle().hashCode();
        return hc * 19 + getAuthor().hashCode();
    }


}

