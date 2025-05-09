/**
 * Controller Class for loan-related REST API endpoints.
 *
 * @authors Thomas Hague and Arthur Greenwood
 * Created by Thomas Hague, 2/4/2025 with package, annotations, LoanController, createLoan and loanEnding methods
 * Modified by Arthur Greenwood 8/5/2025. Refactored createLoan
 * Modified by Thomas Hague 8/5/2025. added listLoans, refactored loanEnding.
 */

package ebook6.loan;

import ebook6.ApiResponse;
import ebook6.user.User;
import ebook6.user.UserNotLoggedInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/loan")
public class LoanController {
    private final LoanService loanService;

    /**
     * Creates an LoanController using our loanService
     *
     * @param loanService
     */
    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    /**
     * Creates a new loan by calling the createLoan method from our loan class.
     *
     * @param userId  the user who is loaning
     * @param ebookId the ebook to be loaned
     * @return a ApiResponse with the created loan or an error message
     */
    @PostMapping("/rent")
    @Transactional
    public ApiResponse<?> createLoan(@RequestParam UUID userId, @RequestParam UUID ebookId) {
        try {
            Loan createdLoan = loanService.createLoan(userId, ebookId);
            return ApiResponse.success(createdLoan);
        } catch (EntityNotFoundException e) {
            return ApiResponse.error(404,"Loan not found." );
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(405, "Bad Request.");
        }
        catch (UserNotLoggedInException e) {
            return ApiResponse.fail("User not logged in.");
        }
    }

    /**
     * Terminates a loan. Once a loan ends, it is still stored in our database but with isLiveStatus set to false.
     * @param loanId of the loan that has ended
     * @return ApiResponse with the terminated loan or an error message.
     */
    @PatchMapping("/{loanId}")
    public ApiResponse<?> loanEnding(@PathVariable UUID loanId) {
        Optional<Loan> optionalLoan = loanService.findByLoanId(loanId);
        if (optionalLoan.isEmpty()) {
            return ApiResponse.error(404, "Loan hasn't been found. Please have another go");
        }
        try {
            Loan loanEnding = loanService.terminateLoan(optionalLoan.get());
            return ApiResponse.success(loanEnding);
        } catch (Exception e) {
            return ApiResponse.error(500, e.getMessage());
        }
    }
    
    /**
     * Returns a list of loans by a specified user
     * @param userId to search for
     * @return an ApiResponse with a list of loans by the user, or error message.
     */
    @GetMapping("/list")
    public ApiResponse<List<Loan>> listLoans(@RequestParam UUID userId) {
        try {
            List<Loan> userLoans = loanService.findLoansByUserId(userId);
            return ApiResponse.success(userLoans);
        }
        catch (EntityNotFoundException e) {
            return ApiResponse.error(404, e.getMessage());
        }
        catch (Exception e) {
            return ApiResponse.error(500, "Error retrieving loans. PLease try again.");
        }
    }
    

}

