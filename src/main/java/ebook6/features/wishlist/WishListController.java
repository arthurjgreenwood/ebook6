/**
 * Controller Class for wishlist-related REST API endpoints.
 *
 * @authors Thomas Hague
 * Created by Thomas Hague, 5/5/2025 with package, annotations and WishlistController, addToWishList, removeFromWishlist
 * and getWishList methods.
 */

package ebook6.features.wishlist;

import ebook6.user.User;
import ebook6.user.UserNotLoggedInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {
    private final WishlistService wishlistService;

    /**
     * Creates a WishListController using our wishlistService
     * @param wishlistService
     */
    @Autowired
    public WishListController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    /**
     * Adds an eBook to the user's wishlist by calling the addtoWishList method from our wishlistService.
     * @param userId
     * @param ebookId
     * @return a ResponseEntity with the created wishlist entry or an error message
     */
    @PostMapping
    @Transactional
    public ResponseEntity<?> addToWishlist(@RequestParam UUID userId, @RequestParam UUID ebookId) {
        try {
            Wishlist addedWishlist = wishlistService.addToWishlist(userId, ebookId);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedWishlist);
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (UserNotLoggedInException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /**
     * Removes an eBook from the user's wishlist by calling the removeFromWishList method from our wishlistService
     * @param userId who is removing the eBook
     * @param ebookId to be removed
     * @return a ResponseEntity with a confirmation message of the removal, or error message.
     */
    @DeleteMapping
    public ResponseEntity<?> removeFromWishlist(@RequestParam UUID userId, @RequestParam UUID ebookId) {
        try {
            wishlistService.removeFromWishlist(userId, ebookId);
            return ResponseEntity.status(HttpStatus.OK).body("Removed " + ebookId + " from wishlist");
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (UserNotLoggedInException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /**
     * Retrieves a user's wishlist
     * @param userId whose wishlist to search for
     * @return a ResponseEntity with the wishlist or an error message
     */
    @GetMapping
    public ResponseEntity<?> getWishlist(@RequestParam UUID userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(wishlistService.getUsersWishlist(userId));
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
