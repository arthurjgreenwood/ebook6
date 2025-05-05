package ebook6.features.wishlist;

import ebook6.ebook.EBook;
import ebook6.ebook.EBookRepository;
import ebook6.user.User;
import ebook6.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final EBookRepository ebookRepository;

    @Autowired
    public WishlistService(WishlistRepository wishlistRepository, UserRepository userRepository, EBookRepository ebookRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.ebookRepository = ebookRepository;
    }

    public Wishlist addToWishlist(UUID userId, UUID ebookId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        Optional<EBook> optionalEbook = ebookRepository.findByEbookId(ebookId);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("User not found.");
        }
        if (optionalEbook.isEmpty()) {
            throw new EntityNotFoundException("Ebook not found.");
        }
        User userAdding = optionalUser.get();
        EBook ebookToAdd = optionalEbook.get();

        List<Wishlist> usersWishList = wishlistRepository.findByUser(userAdding);
        if (usersWishList.size() >= 30) {
            throw new IllegalArgumentException("You cannot add more than 30 eBooks to your Wishlist.");
        }

        if (wishlistRepository.existsByUserAndEbook(userAdding, ebookToAdd)) {
            throw new IllegalArgumentException(ebookToAdd + " is already in " + userAdding +"'s wishlist.");
        }

        Wishlist wishlistEntry = new Wishlist(userAdding, ebookToAdd);
        System.out.println(userAdding + " has added " + ebookToAdd + " to their wishlist.");
        return wishlistRepository.save(wishlistEntry);
    }

    public void removeFromWishlist(UUID userId, UUID ebookId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        Optional<EBook> optionalEbook = ebookRepository.findByEbookId(ebookId);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("User not found.");
        }
        if (optionalEbook.isEmpty()) {
            throw new EntityNotFoundException("Ebook not found.");
        }
        User userRemoving = optionalUser.get();
        EBook ebookToRemove = optionalEbook.get();

        wishlistRepository.deleteByUserAndEbook(userRemoving, ebookToRemove);
        System.out.println(userRemoving + " has removed " + ebookToRemove + " from their wishlist.");
    }

    public List<Wishlist> getUsersWishlist(UUID userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);

        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("User not found.");
        }
        User userSearching = optionalUser.get();
        return wishlistRepository.findByUser(userSearching);
    }

}
