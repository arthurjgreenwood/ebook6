package ebook6.features.wishlist;

import ebook6.ebook.EBook;
import ebook6.features.review.Review;
import ebook6.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, UUID> {

    List<Wishlist> findByUser(User user);

    boolean existsByUserAndEbook(User user, EBook ebook);

    void deleteByUserAndEbook(User user, EBook ebook);



}
