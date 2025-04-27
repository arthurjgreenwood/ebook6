/**
 * Service Class for loan-related operations.
 *
 * @authors Thomas Hague
 * Created by Thomas Hague, 31/3/2025 with LoanService, createLoan, validateLoan.
 * Modified by Thomas Hague, 3/4/2025 with  calculateEndDate, terminateLoan, terminateLoanAtEndDate and emailTerminatingLoans methods added.
 */

package ebook6.loan;

import ebook6.ebook.EBook;
import ebook6.ebook.EBookRepository;
import ebook6.features.email.EmailService;
import ebook6.user.User;
import ebook6.user.UserNotLoggedInException;
import ebook6.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.*;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final EBookRepository ebookRepository;
    private final EmailService emailService;

    /**
     * Creates LoanService using our LoanRepository
     *
     * @param loanRepository
     */
    @Autowired
    public LoanService(LoanRepository loanRepository, UserRepository userRepository, EBookRepository ebookRepository, EmailService emailService) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.ebookRepository = ebookRepository;
        this.emailService = emailService;
    }

    /**
     * Finds loan with a target loanId from our database.
     * @param loanId
     * @return an Optional containing the target Loan or empty.
     */
    public Optional<Loan> findByLoanId(UUID loanId) {
        return loanRepository.findByLoanId(loanId);
    }


    /**
     * Creates a new loan and saves it to our database
     * Checks the user and loan both exist in our database, throws an exception if not.
     * Further exceptions thrown if user is already loaning the maximum of 10 ebooks, there are no available of this ebook
     * for loan, or the user doesn't have enough money for the loan.
     * Confirmation email is sent to user using our emailService createConfirmationEmail method.
     * @param userId  that is loaning an ebook
     * @param ebookID the ebook to be loaned
     * @return the loan
     */
    public Loan createLoan(UUID userId, UUID ebookID) {

        Optional<User> optionalUser = userRepository.findByUserId(userId);
        Optional<EBook> optionalEBook = ebookRepository.findByEbookId(ebookID);

        User userLoaning;
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        } else {
            userLoaning = optionalUser.get();
        }
        EBook ebookForLoan;
        if (optionalEBook.isEmpty()) {
            throw new EntityNotFoundException("EBook not found");
        } else {
            ebookForLoan = optionalEBook.get();
        }
        validateLoan(userLoaning, ebookForLoan);
        // sets the start to today's data and calls calculateEndDate method to calculate termination date
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = calculateEndDate(startDate, ebookForLoan);
        // creates the loan, reduces the ebook available quantity, increases the users loaned quantity and deducts the price
        //of the loan of the users balance
        Loan newLoan = new Loan(userLoaning, ebookForLoan, startDate, endDate);
        ebookForLoan.setQuantityAvailable(ebookForLoan.getQuantityAvailable() - 1);
        userLoaning.setTotalLoaned(userLoaning.getTotalLoaned() + 1);
        userLoaning.setBalance(userLoaning.getBalance() - ebookForLoan.getPrice());
        ebookRepository.save(ebookForLoan);
        userRepository.save(userLoaning);
        System.out.println(userLoaning + " has successfully loaned eBook: " + ebookForLoan);
        emailService.createConfirmationEmail(newLoan);
        return loanRepository.save(newLoan);
    }

    /**
     * Validates if a user can load a given eBook by checking users current loan quantity, ebook availability and customer's balance.
     * Exceptions thrown if errors.
     *
     * @param userLoaning  the ebook
     * @param ebookForLoan the user wants
     * @return true if user can loan ebook, false if not
     */
    private void validateLoan(User userLoaning, EBook ebookForLoan) {
        // ensures a user can't loan more than 10 books
        if (!userLoaning.getLoggedIn()) {
            throw new UserNotLoggedInException("User needs to be logged in to loan a book.");
        }
        if (userLoaning.getTotalLoaned() >= 10) {
            throw new IllegalArgumentException("User " + userLoaning + " is already loaning the maximum 10 eBooks at a time.");
        }
        // ensures the ebook is available to loan
        if (ebookForLoan.getQuantityAvailable() == 0) {
            throw new IllegalArgumentException("All versions of " + ebookForLoan + " are currently on loan");
        }
        // ensures the user has enough money in their account for the loan
        if (userLoaning.getBalance() < ebookForLoan.getPrice()) {
            throw new IllegalArgumentException("User doesn't have enough funds to loan " + ebookForLoan + ". Please add: £"
                    + (ebookForLoan.getPrice() - userLoaning.getBalance()) + " to your account.");
        }
    }

    /**
     * Calculates the end date of the loan
     *
     * @param startDate of the loan
     * @param eBook     to be loaned
     * @return the termination date
     */
    private LocalDate calculateEndDate(LocalDate startDate, EBook eBook) {
        return startDate.plusDays(eBook.getMaxLoanDuration());
    }

    /**
     * Terminates a loan of an EBook, setting the isLiveStatus to false.
     * Sets the end of data of the loan to today' date, increases the ebook's quantity available by 1 and reduces the users'
     * total ebook's loaned by 1.
     * Cancellation email is automatically sent to the user using our emailService createCancellationEmail method.
     * @param loan to be terminated
     * @return the termindatedLoan
     */
    public Loan terminateLoan(Loan loan) {
        if (loan == null) {
            throw new EntityNotFoundException("Loan hasn't been found.");
        }
        User user = loan.getUser();
        EBook eBook = loan.getEbook();
        LocalDate todaysDate = LocalDate.now();

        loan.setStatus(false);
        loan.setEndDate(todaysDate);
        eBook.setQuantityAvailable(eBook.getQuantityAvailable() + 1);
        user.setTotalLoaned(user.getTotalLoaned() - 1);
        emailService.createCancellationEmail(loan);
        ebookRepository.save(eBook);
        userRepository.save(user);
        return loanRepository.save(loan);
    }

    /**
     * Automatically terminates loans that reach their end date by calling the terminateLoan method when a loans end date is reached.
     * The method refreshes at midnight everyday to look for loans that have now reached their end date.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void terminateLoansAtEndDate() {
        LocalDate todaysDate = LocalDate.now();
        List<Loan> listAllLoans = loanRepository.findAll();
        List<Loan> expiredLoans = new ArrayList<>();
        for (Loan loans : listAllLoans) {
            if (loans.getEndDate().isEqual(todaysDate) && loans.getStatus()) {
                expiredLoans.add(loans); }
        }
        for (Loan loan : expiredLoans) {
            terminateLoan(loan);
            System.out.println("Loan by" + loan.getUser() + " of " + loan.getEbook() + " has ended. Hope you enjoyed the book!");
        }
    }

    /**
     * Identifies loans that are expiring in 1 days time and sends reminder email to the user using our emailService createReminderEmail method.
     * Method refreshes daily at midnight, looking for new loans that are expiring soon.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void emailTerminatingLoans() {
        LocalDate notificationDate = LocalDate.now().plusDays(1);
        List<Loan> listAllLoans = loanRepository.findAll();
        List<Loan> expiringLoans = new ArrayList<>();
        for (Loan loans : listAllLoans) {
            if (loans.getEndDate().isEqual(notificationDate) && loans.getStatus()) {
                expiringLoans.add(loans); }
        }
        for (Loan loans : expiringLoans) {
            emailService.createReminderEmail(loans);
        }
    }

}
