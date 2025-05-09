package ebook6.ebook;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

/**
 * Class that automatically adds 10 eBooks to the database when the system is run
 *
 * @author Arthur Greenwood
 * Created by Arthur Greenwood, 7/05/2025 with seedEbooks
 */

@Configuration
    public class DefaultEBooks {
        
        @Bean
        CommandLineRunner seedEBooks(EBookRepository ebookRepo) {
            return args -> {
                if (ebookRepo.count() == 0) {
                    EBook b1 = new EBook("1984", "George Orwell", 10, "Dystopian", 12.99, 21, "A dystopian novel about totalitarianism.");
                    b1.setCoverURL("https://m.media-amazon.com/images/I/612ADI+BVlL._AC_UF894,1000_QL80_.jpg");
                    
                    EBook b2 = new EBook("To Kill a Mockingbird", "Harper Lee", 7, "Classic", 10.50, 14, "A novel about racial injustice.");
                    b2.setCoverURL("https://m.media-amazon.com/images/I/81gepf1eMqL._AC_UF894,1000_QL80_.jpg");
                    
                    EBook b3 = new EBook("The Great Gatsby", "F. Scott Fitzgerald", 5, "Classic", 11.00, 14, "A story of wealth and illusion.");
                    b3.setCoverURL("https://m.media-amazon.com/images/I/81TLiZrasVL._AC_UF894,1000_QL80_.jpg");
                    
                    EBook b4 = new EBook("Pride and Prejudice", "Jane Austen", 6, "Romance", 9.99, 14, "A romantic novel of manners.");
                    b4.setCoverURL("https://m.media-amazon.com/images/I/81a3sr-RgdL.jpg");
                    
                    EBook b5 = new EBook("The Hobbit", "J.R.R. Tolkien", 8, "Fantasy", 13.49, 21, "A fantasy adventure prelude to LOTR.");
                    b5.setCoverURL("https://m.media-amazon.com/images/I/81mCE+uclxL._UF1000,1000_QL80_.jpg");
                    
                    EBook b6 = new EBook("Sapiens", "Yuval Noah Harari", 10, "History", 14.99, 30, "A brief history of humankind.");
                    b6.setCoverURL("https://m.media-amazon.com/images/I/713jIoMO3UL.jpg");
                    
                    EBook b7 = new EBook("The Catcher in the Rye", "J.D. Salinger", 4, "Fiction", 10.75, 14, "A novel about teenage angst.");
                    b7.setCoverURL("https://m.media-amazon.com/images/I/8125BDk3l9L._AC_UF894,1000_QL80_.jpg");
                    
                    EBook b8 = new EBook("Brave New World", "Aldous Huxley", 6, "Dystopian", 11.50, 21, "A dystopia of pleasure and control.");
                    b8.setCoverURL("https://m.media-amazon.com/images/I/71GNqqXuN3L._AC_UF894,1000_QL80_.jpg");
                    
                    EBook b9 = new EBook("The Alchemist", "Paulo Coelho", 9, "Adventure", 9.25, 14, "A philosophical journey for treasure.");
                    b9.setCoverURL("https://m.media-amazon.com/images/I/71CaTj9MAFL.jpg");
                    
                    EBook b10 = new EBook("Thinking, Fast and Slow", "Daniel Kahneman", 5, "Psychology", 15.00, 30, "A deep dive into human thinking.");
                    b10.setCoverURL("https://m.media-amazon.com/images/I/61fdrEuPJwL._AC_UF894,1000_QL80_.jpg");
                    
                    ebookRepo.saveAll(List.of(b1, b2, b3, b4, b5, b6, b7, b8, b9, b10));
                }
            };
        }
    }
    