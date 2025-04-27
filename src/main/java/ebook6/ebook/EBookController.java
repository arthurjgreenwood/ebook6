/**
 * Controller Class for eBook-related REST API endpoints.
 * @authors Thomas Hague
 * Created by Thomas Hague, 31/3/2025 with Package, comments and EbookController and createEbook methods
 * Modified by Thomas Hague 2/4/2025. Added updateEBook, deleteEBookById, getEBooksByTitle, getEbooksByAuthor, getEbooksByCategory,
 * getEbooksByPriceInBetween methods.
 */

package ebook6.ebook;

import ebook6.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/ebooks")
public class EBookController {

    private final EBookService ebookService;

    /**
     * Creates an EBookController using our ebookService
     * @param ebookService
     */
    @Autowired
    public EBookController(EBookService ebookService) {
        this.ebookService = ebookService;
    }

    /**
     * Creates a new eBook by calling the createEBook method in the EBookService class and returns a ResponseEntity with the created EBook or an error message.
     * @param ebook the EBook to create
     * @return a ResponseEntity with the created EBook or an error message
     */
    @PostMapping
    public ResponseEntity<?> createEBook(@RequestBody EBook ebook) {
        try {
            EBook createdEBook = ebookService.createEBook(ebook);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEBook);
        }
        catch (EbookAlreadyInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * Updates an Ebook by calling the updateEBook method from our service class.
     * @param ebookId to be updated
     * @param updatedEbook what the eBook will be updated to.
     * @return a ResponseEntity with the updated EBook or an error message if failure.
     */
    @PatchMapping("/{ebookId}")
    public ResponseEntity<?> updateEBook(@PathVariable UUID ebookId, @RequestBody EBook updatedEbook) {
        try {
            EBook finalEBook = ebookService.updateEBook(ebookId, updatedEbook);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(finalEBook);
        }
        catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Deletes an EBook from our database. Error message printed if EBook doesn't exist.
     * @param ebookId of the EBook to be deleted
     * @return a ResponseEntity confirming the EBook is deleted or error message.
     */
    @DeleteMapping("/{ebookId}")
    public ResponseEntity<?> deleteEBookById(@PathVariable UUID ebookId) {
        Optional<EBook> optionalEBook = ebookService.findEBookById(ebookId);
        if (optionalEBook.isPresent()) {
            EBook ebookToDelete = optionalEBook.get();
            ebookService.deleteEBookByTitleAndAuthor(ebookToDelete);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Ebook successfully deleted from our EBookStore");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("EBook with ID " + ebookId + " doesn't exist in our EBookStore.");
        }
    }

    /**
     * Identifies an Ebook by matching their title to ones in our database. Error message printed if not.
     * @param title of the eBook we are looking for
     * @return a ResponseEntity confirming the Ebook exists or error message.
     */
    @GetMapping("/title/{title}")
    public ResponseEntity<?> getEBooksByTitle(@PathVariable String title) {
        List<EBook> ebooks = ebookService.findEBookByTitle(title);
        if (!ebooks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(ebooks);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No EBooks matching title exist.");
        }
    }

    /**
     * Identifies an Ebook by matching their author to ones in our database. Error message printed if not.
     * @param author of the eBook we are looking for
     * @return a ResponseEntity confirming the Ebook exists or error message.
     */
    @GetMapping("/author/{author}")
    public ResponseEntity<?> getEBooksByAuthor(@PathVariable String author) {
        List<EBook> ebooks = ebookService.findEBookByAuthor(author);
        if (!ebooks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(ebooks);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No EBooks by this Author exist.");
        }
    }

    /**
     * Identifies Ebook by matching a specified category in our database. Error message printed if not.
     * @param category of the eBooks we are looking for
     * @return a ResponseEntity confirming the Ebooks exist or error message.
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getEBooksByCategory(@PathVariable String category) {
        List<EBook> ebooks = ebookService.findEBookByCategory(category);
        if (!ebooks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(ebooks);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No EBooks in this category exist.");
        }
    }

    /**
     * Identifies Ebooks in a specified price range in our database. Error message printed if not.
     * @param minPrice the eBooks we are looking for
     * @param maxPrice of the eBooks we are looking for
     * @return a ResponseEntity confirming the Ebooks exist or error message.
     */
    @GetMapping("/price/{minPrice}/{maxPrice}")
    public ResponseEntity<?> getEBooksByPriceInbetween(@PathVariable double minPrice, @PathVariable double maxPrice) {
        List<EBook> ebooks = ebookService.findEBookByPriceBetween(minPrice, maxPrice);
        if (!ebooks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(ebooks);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No EBooks in this price range exist.");
        }
    }





}
