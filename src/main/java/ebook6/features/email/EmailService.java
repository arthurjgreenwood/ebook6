/**
 * Service Class for Email-related operations.
 *
 * @authors Thomas Hague
 * Created by Thomas Hague, 1/4/2025 with package, annotations, Emailservice, create confirmation, cancellation and reminder email methods.
 */
package ebook6.features.email;

import ebook6.loan.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class EmailService {

    private final EmailRepository emailRepository;

    /**
     * Creates EmailService using our EmailRepository
     * @param emailRepository
     */
    @Autowired
    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    /**
     * Creates an email and saves it to our database
     * @param email to be created
     * @return the created Email
     */
    public Email createEmail(Email email) {
        return emailRepository.save(email);
    }

    /**
     * Creates an email for confirming a loan to a user, by setting the subject and body of the email and saving it to our database.
     * @param loan the email is being sent for
     * @return the email
     */
    public Email createConfirmationEmail(Loan loan) {
        String subject = "Confirmation of your eBook loan";
        String body = "Hi " + loan.getUser().getName() + ", " + '\n' + "You have successfully loaned " + loan.getEbook().getTitle()
                + " for " + loan.getEbook().getMaxLoanDuration() + " days. We hope you enjoy the book!" + '\n' + "Kind regards," +'\n' + "The eBookStore.";
        Email confirmationEmail = new Email(loan, subject, body);
        return emailRepository.save(confirmationEmail);
    }

    /**
     * Creates an email for confirming a loan cancellation to a user, by setting the subject and body of the email and saving it to our database.
     * @param loan the email is being sent for
     * @return the email
     */
    public Email createCancellationEmail(Loan loan) {
        String subject = "Cancellation of your eBook loan";
        String body = "Hi " + loan.getUser().getName() + ", " + '\n' + "You have successfully cancelled your loan of " + loan.getEbook().getTitle()
                + " with immediate effect. We hope you enjoyed the book!" + '\n' + "Kind regards," +'\n' + "The eBookStore.";
        Email cancellationEmail = new Email(loan, subject, body);
        return emailRepository.save(cancellationEmail);
    }

    /**
     * Creates an email for sending a reminder to a user their loan is terminating soon, by setting the subject and body of the email and saving it to our database.
     * @param loan the email is being sent for
     * @return the email
     */
    public Email createReminderEmail(Loan loan) {
        String subject = "Reminder of your eBook loan coming to an end soon";
        String body = "Hi " + loan.getUser().getName() + ", " + '\n' + "This is a reminder that your loan of " + loan.getEbook().getTitle()
                + " is due to end in 24 hours. Hope you have enjoyed reading it!" + '\n' + "Kind regards," +'\n' + "The eBookStore.";
        Email reminderEmail = new Email(loan, subject, body);
        return emailRepository.save(reminderEmail);
    }







}
