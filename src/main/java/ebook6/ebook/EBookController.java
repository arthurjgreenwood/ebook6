/**
 * Controller Class for eBook-related REST API endpoints.
 *
 * @authors Thomas Hague
 * Created by Thomas Hague, 31/3/2025 with Package, comments and EbookController and createEbook methods
 * Modified by Thomas Hague 2/4/2025. Added updateEBook, deleteEBookById, getEBooksByTitle, getEbooksByAuthor, getEbooksByCategory,
 * getEbooksByPriceInBetween methods.
 * Modified by Federico Leal 6/5/2025. searchEbooks, getEBooksByTitle, getEBooksByAuthor methods edited
 * Modified by Thomas Hague, 6/5/2025. createEbook method edited.
 * Modified by Thomas Hague, 6/5/2025. deleteEbook, updateEbook, getEbooksByCategory, getEBooksByPriceInbetween methods edited.
 * Modified by Arthur Greenwood, 8/5/2025. Refactored getRecommended
 */

package ebook6.ebook;

import ebook6.ApiResponse;
import ebook6.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class EBookController {
    
    private final EBookService ebookService;
    
    /**
     * Creates an EBookController using ebookService
     * @param ebookService instance of service class
     */
    @Autowired
    public EBookController(EBookService ebookService) {
        this.ebookService = ebookService;
    }
    
    /**
     * Creates a new eBook by calling the createEBook method in the EBookService class and returns a ResponseEntity with the created EBook or an error message.
     * @param request ebook request object passed from frontend
     * @return a ResponseEntity with the created EBook or an error message
     */
    @PostMapping("/add")
    public ApiResponse<?> createEBook(@RequestBody EbookRequest request) {
        try {
            EBook createdEBook = ebookService.createEBook(
                    request.getTitle(),
                    request.getAuthor(),
                    request.getQuantityAvailable(),
                    request.getCategory(),
                    request.getPrice(),
                    request.getMaxLoanDuration(),
                    request.getDescription()
            );
            return ApiResponse.success(createdEBook);
        } catch (EbookAlreadyInDatabaseException e) {
            return ApiResponse.fail("This eBook already exists");
        }
    }
    
    /**
     * Updates an Ebook by calling the updateEBook method from our service class.
     * @param ebookId to be updated
     * @param updatedEbook what the eBook will be updated to.
     * @return a ApiResponse with the updated EBook or an error message if failure.
     */
    @PatchMapping("/{ebookId}")
    public ApiResponse<?> updateEBook(@PathVariable UUID ebookId, @RequestBody EBook updatedEbook) {
        try {
            EBook finalEBook = ebookService.updateEBook(ebookId, updatedEbook);
            return ApiResponse.success(finalEBook);
        } catch (EntityNotFoundException e) {
            return ApiResponse.error(404, "This ebook does not exist");
        }
    }
    
    /**
     * Deletes an EBook from our database. Error message printed if EBook doesn't exist.
     * @param ebookId of the EBook to be deleted
     * @return a APIResponse confirming the EBook is deleted or error message.
     */
    @DeleteMapping("/ebook/{ebookId}")
    public ApiResponse<?> deleteEBookById(@PathVariable UUID ebookId) {
        Optional<EBook> optionalEBook = ebookService.findEBookById(ebookId);
        if (optionalEBook.isPresent()) {
            EBook ebookToDelete = optionalEBook.get();
            ebookService.deleteEBookByTitleAndAuthor(ebookToDelete);
            return ApiResponse.success("Ebook deleted sucessfully");
        } else {
            return ApiResponse.error(404, "This ebook does not exist");
        }
    }
    
    /**
     * Searches for EBooks by title and then by author.
     * @param searchText The search query.
     * @return An ApiResponse containing the search results or an error message.
     */
    @GetMapping("/search/{searchText}")
    public ApiResponse<List<EBook>> searchEbooks(@PathVariable String searchText) {
        ApiResponse<List<EBook>> response = getEBooksByTitle(searchText);
        if (response.getCode() == 200) {
            return response;
        }
        
        return getEBooksByAuthor(searchText);
    }
    
    /**
     * Identifies an Ebook by matching their title to ones in our database. Error message printed if not.
     *
     * @param title of the eBook we are looking for
     * @return An ApiResponse confirming the Ebook exists or error message.
     */
    @GetMapping("/title/{title}")
    public ApiResponse<List<EBook>> getEBooksByTitle(@PathVariable String title) {
        List<EBook> ebooks = ebookService.findEBookByTitle(title);
        if (!ebooks.isEmpty()) {
            return ApiResponse.success(ebooks);
        } else {
            return ApiResponse.error(404, "No EBooks matching title exist.");
        }
    }
    
    /**
     * Identifies an Ebook by matching their author to ones in our database. Error message printed if not.
     *
     * @param author of the eBook we are looking for
     * @return An ApiResponse confirming the Ebook exists or error message.
     */
    @GetMapping("/author/{author}")
    public ApiResponse<List<EBook>> getEBooksByAuthor(@PathVariable String author) {
        List<EBook> ebooks = ebookService.findEBookByAuthor(author);
        if (!ebooks.isEmpty()) {
            return ApiResponse.success(ebooks);
        } else {
            return ApiResponse.error(404, "No EBooks by this Author exist.");
        }
    }
    
    
    /**
     * Identifies Ebook by matching a specified category in our database. Error message printed if not.
     * @param category of the eBooks we are looking for
     * @return a ApiResponse confirming the Ebooks exist or error message.
     */
    @GetMapping("/category/{category}")
    public ApiResponse<List<EBook>> getEBooksByCategory(@PathVariable String category) {
        List<EBook> ebooks = ebookService.findEBookByCategory(category);
        if (!ebooks.isEmpty()) {
            return ApiResponse.success(ebooks);
        } else {
            return ApiResponse.error(404, "No EBooks by this Author exist.");
        }
    }
    
    /**
     * Identifies Ebooks in a specified price range in our database. Error message printed if not.
     * @param minPrice the eBooks we are looking for
     * @param maxPrice of the eBooks we are looking for
     * @return a ResponseEntity confirming the Ebooks exist or error message.
     */
    @GetMapping("/price/{minPrice}/{maxPrice}")
    public ApiResponse<List<EBook>> getEBooksByPriceInbetween(@PathVariable double minPrice, @PathVariable double maxPrice) {
        List<EBook> ebooks = ebookService.findEBookByPriceBetween(minPrice, maxPrice);
        if (!ebooks.isEmpty()) {
            return ApiResponse.success(ebooks);
        } else {
            return ApiResponse.error(404, "No EBooks by this Author exist.");
        }
    }
    
    /**
     * Gets the top 4 recommended books for a user.
     * @return An ApiResponse containing the list of recommended books.
     */
    @GetMapping("/books")
    public ApiResponse<?> getRecommendations() {
        return ApiResponse.success(ebookService.getRecommendedEbooks());
    }
    
    
    /**
     * Finds an eBook matching specified ID
     * @return ApiResponse with http code and ebook UUID
     */
    @GetMapping("/ebook/{bookId}")
    public ApiResponse<Map<String, Object>> getEBookById(@PathVariable UUID bookId) {
        
        Map<String, Object> map = new HashMap<>();
        Optional<EBook> ebook = ebookService.findEBookById(bookId);
        if (ebook.isPresent()) {
            EBookResponse response = new EBookResponse(ebook.get());
            map.put("code", 200);
            map.put("message", "Success");
            map.put("data", response);
            return ApiResponse.success(map);
        } else {
            map.put("code", 404);
            map.put("message", "EBook not found");
            map.put("data", null);
            return ApiResponse.fail("Could not find EBook");
        }
    }
    
}
