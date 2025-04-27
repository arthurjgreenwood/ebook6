/**
 * Class representing a User's Wishlist.
 * @author Thomas Hague
 * Created by Thomas Hague, 31/3/2025 package, annotations, fields, constructors, getters and setters..
 */

package ebook6.features;

import ebook6.ebook.EBook;
import ebook6.user.User;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Wishlist")
public class Wishlist {
    @Id
    private UUID wishlistId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "ebook_id" , nullable = false)
    private EBook ebook;
    private LocalDateTime addedAt;

    /**
     * Constructors for creating reviews. Includes a no parameter constructor for the JPA and normal parameterised constructor.
     */
    public Wishlist() {
        this.wishlistId = UUID.randomUUID();
        this.addedAt = LocalDateTime.now();
    }

    public Wishlist(User user, EBook ebook) {
        this.wishlistId = UUID.randomUUID();
        this.user = user;
        this.ebook = ebook;
        this.addedAt = LocalDateTime.now();
    }

    // getters and setters

    public UUID getWishlistId() { return wishlistId; }

    public User getUserId() { return user; }

    public EBook getEbookId() { return ebook; }

    public LocalDateTime getAddedAt() { return addedAt; }
}
