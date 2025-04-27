/**
 * Main application connecting to SpringBoot.
 * @authors Fedrico Leal Quintero and Thomas Hague
 * Created by Fedrico Leal Quintero, 27/3/2025 with UserRepository and createUser methods
 * Modified by Thomas Hague 31/3/2025. Added package.
 * Modified by Thomas Hague 1/4/2025. Added scheduling.
 */

package ebook6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"ebook6.user", "ebook6.SpringBoot"})
@EnableScheduling
public class EbookApplication {
    public static void main(String[] args) {
        SpringApplication.run(EbookApplication.class, args);
    }
}

