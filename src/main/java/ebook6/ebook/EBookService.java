/**
 * Service Class for eBook-related operations.
 * @authors Thomas Hague
 * Created by Thomas Hague, 31/3/2025 with package, comments, annotations and EbookService, findEBookById, findEBookByTitle,
 * findEBookByAuthor,findEBookByPriceInbetween, findEBookByRating, findEBookByMaxPrice, findAll, createEBook, deleteEBook and UpdateEBook methods.
 */

package ebook6.ebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EBookService {

    private final EBookRepository eBookRepository;

    /**
     * Creates EBookService using our EBookRepository
     * @param eBookRepository
     */
    @Autowired
    public EBookService(EBookRepository eBookRepository) {
        this.eBookRepository = eBookRepository;
    }

    /**
     * Finds EBooks matching an eBookId from our database.
     * @param ebookId
     * @return an Optional containing the target EBook or empty.
     */
    public Optional<EBook> findEBookById(UUID ebookId) {
        return eBookRepository.findById(ebookId);
    }

    /**
     * Finds EBooks matching a target title from our database.
     * @param title
     * @return a List containing the target EBook(s) or empty.
     */
    public List<EBook> findEBookByTitle(String title) {
        return eBookRepository.findByTitle(title);
    }

    /**
     * Finds EBooks matching a target author from our database.
     * @param author
     * @return a List containing the target EBook(s) or empty.
     */
    public List<EBook> findEBookByAuthor(String author) {
        return eBookRepository.findByAuthor(author);
    }

    /**
     * Finds EBooks matching a target category from our database.
     * @param category
     * @return a List containing the target EBook(s) or empty.
     */
    public List<EBook> findEBookByCategory(String category) {
        return eBookRepository.findByCategory(category);
    }

    /**
     * Finds EBooks within a given price range from our database.
     * @param minPrice to search for
     * @param maxPrice to search for
     * @return a List containing the target EBook(s) or empty.
     */
    public List<EBook> findEBookByPriceBetween(double minPrice, double maxPrice) {
        return eBookRepository.findByPriceBetween(minPrice, maxPrice);
    }

    /**
     * Finds EBooks above a target rating from our database.
     * @param rating
     * @return a List containing the target EBook(s) or empty.
     */
    public List<EBook> findEBookByRating(double rating) {
        return eBookRepository.findByAvgRatingGreaterThanEqual(rating);
    }

    /**
     * Finds EBooks below a target price from our database.
     * @param maxPrice
     * @return a List containing the target EBook(s) or empty.
     */
    public List<EBook> findEBookByMaxPrice(double maxPrice) {
        return eBookRepository.findByPriceLessThanEqual(maxPrice);
    }

    /**
     * Finds all ebooks in our database.
     * @return a List containing all ebook(s) or empty.
     */
    public List<EBook> findAll() {
        return eBookRepository.findAll();
    }

    /**
     * Creates a new ebook and saves it to the database.
     * Checks if the ebook already exists in the database by searching for title and author, Exception thrown if it does.
     * @param ebook the ebook to create
     * @return the created ebook
     */
    public EBook createEBook(EBook ebook) throws EbookAlreadyInDatabaseException {
        // check if eBook exists
        if (eBookRepository.findByTitleAndAuthor(ebook.getTitle(), ebook.getAuthor()).isPresent()) {
            throw new EbookAlreadyInDatabaseException("EBook with title: " + ebook.getTitle() + ", and Author: " + ebook.getAuthor() + " already exists");
        }
        return eBookRepository.save(ebook);
    }

    /**
     * Deletes an ebook from our databases
     * Checks the ebook does exist first by searching for title and author, Exception thrown if it doesn't.
     * @param ebook to be deleted
     */
    public void deleteEBookByTitleAndAuthor(EBook ebook) {
        if (eBookRepository.findByTitleAndAuthor(ebook.getTitle(), ebook.getAuthor()).isEmpty()) {
            throw new EntityNotFoundException("Ebook with title: " + ebook.getTitle() + ", and Author: " + ebook.getAuthor() + " not found");
        }
        eBookRepository.delete(ebook);
        System.out.println("Ebook with title: " + ebook.getTitle() + ", and Author: " + ebook.getAuthor() + " has been deleted");
    }

    /**
     * Updates a user in our database, by changing title, author, category, description, price, max loan duration and cover photo.
     * Checks the ebook does exist first by searching for title and author, Exception thrown if it doesn't.
     * @param existingEBookId that will be updated
     * @param updatedEbook what the ebook will be updated to
     * @return the updated EBook.
     */
    public EBook updateEBook(UUID existingEBookId, EBook updatedEbook) {
        Optional<EBook> optionalEBook = eBookRepository.findById(existingEBookId);
        EBook existingEBook;
        if (optionalEBook.isPresent()) {
            existingEBook = optionalEBook.get();
        }
        else {
            throw new EntityNotFoundException("Ebook with id: " + existingEBookId + " not found");
        }
        existingEBook.setTitle(updatedEbook.getTitle());
        existingEBook.setAuthor(updatedEbook.getAuthor());
        existingEBook.setCategory(updatedEbook.getCategory());
        existingEBook.setDescription(updatedEbook.getDescription());
        existingEBook.setPrice(updatedEbook.getPrice());
        existingEBook.setMaxLoanDuration(updatedEbook.getMaxLoanDuration());
        existingEBook.setCoverURL(updatedEbook.getCoverURL());
        return eBookRepository.save(existingEBook);
    }
}
