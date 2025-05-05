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

    @Autowired
    public WishListController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

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
