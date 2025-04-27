/**
 * Repository interface for ebook database operations.
 * We're using Optional to handle the case where no ebook is found with the given title.
 * We're using List in cases where multiple eBooks may be returned.
 * @authors Thomas Hague
 * Created by Thomas Hague, 31/3/2025 with package, comments and findByTitleAndAuthor, findById, findByTitle, findByAuthor,
 * findByCategory, findByPriceLessThanEqual, findByPriceBetween and findByAvgRatingGreaterThanEqual methods.
 */
package ebook6.ebook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EBookRepository extends JpaRepository<EBook, UUID> {

    /**
     * Finds an eBook by title
     * @param title of the book
     * @param author of the book
     * return an Optional containing the EBook if found, or an empty Optional if not found
     */
    Optional<EBook> findByTitleAndAuthor(String title, String author);

    /**
     * Finds an eBook by ebookID
     * @param ebookId of the book to search for
     * return an Optional containing the EBook if found, or an empty Optional if not found
     */
    Optional<EBook> findByEbookId(UUID ebookId);

    /**
     * Finds eBooks by title
     * @param title of the book to search for
     * return a List containing any EBooks matching title or an empty List if not found
     */
    List<EBook> findByTitle(String title);

    /**
     * Finds eBooks by author
     * @param author of the book to search for
     * return a List containing any EBooks matching author or an empty List if not found
     */
    List<EBook> findByAuthor(String author);

    /**
     * Finds eBooks by category
     * @param category of the book to search for
     * return a List containing any EBooks matching category or an empty List if not found
     */
    List<EBook> findByCategory(String category);

    /**
     * Finds eBooks in between a min and max price
     * @param minPrice to search for
     * @param maxPrice to search for
     * return a List containing any EBooks in the price range or an empty List if not found
     */
    List<EBook> findByPriceBetween(double minPrice, double maxPrice);

    /**
     * Finds eBooks below a max price
     * @param maxPrice to search for
     * @return a List containing any EBooks below the price or an empty List if not found
     */
    List<EBook> findByPriceLessThanEqual(double maxPrice);

    /**
     * Finds eBooks above a min rating
     * @param minRating to search for
     * @return a List containing any EBooks above the rating or an empty List if not found
     */
    List<EBook> findByAvgRatingGreaterThanEqual(double minRating);

}
