/**
 * Controller Class for loan-related REST API endpoints.
 *
 * @authors Thomas Hague
 * Created by Thomas Hague, 2/4/2025 with package, annotations, LoanController, createLoan and loanEnding methods
 */

package ebook6.loan;

import ebook6.user.User;
import ebook6.user.UserNotLoggedInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/loans")
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
     * @return a ResponseEntity with the created loan or an error message
     */
    @PostMapping
    @Transactional
    public ResponseEntity<?> createLoan(@RequestParam UUID userId, @RequestParam UUID ebookId) {
        try {
            Loan createdLoan = loanService.createLoan(userId, ebookId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLoan);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (UserNotLoggedInException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /**
     * Terminates a loan. Once a loan ends, it is still stored in our database but with isLiveStatus set to false.
     * @param loanId of the loan that has ended
     * @return ResponseEntity with the terminated loan or an error message.
     */
    @PatchMapping("/{loanId}")
    public ResponseEntity<?> loanEnding(@PathVariable UUID loanId) {
        Optional<Loan> optionalLoan = loanService.findByLoanId(loanId);
        if (optionalLoan.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loan hasn't been found");
        }
        try {
            Loan loanEnding = loanService.terminateLoan(optionalLoan.get());
            return ResponseEntity.status(HttpStatus.OK).body(loanEnding);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}

