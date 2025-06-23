package it.uniroma3.siw.SiwBooks.config;

import it.uniroma3.siw.SiwBooks.model.Book;
import it.uniroma3.siw.SiwBooks.model.BookImage;
import it.uniroma3.siw.SiwBooks.service.BookService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;

@Configuration
public class DataInitializer {

    @Autowired
    private BookService bookService;

    @Bean
    @Transactional
    CommandLineRunner loadImages() {
        return args -> {
            Map<String, String> booksImages = Map.of(
                    "L amica geniale", "L amica geniale.jpeg",
                    "White Noise", "White Noise.jpeg",
                    "Atonement", "Atonement.jpeg",
                    "Io non ho paura", "Io non ho paura.jpeg",
                    "The Corrections", "The Corrections.jpeg",
                    "Anna", "Anna.jpeg",
                    "Freedom", "Freedom.jpeg",
                    "Underworld", "Underworld.jpeg");

            for (Map.Entry<String, String> entry : booksImages.entrySet()) {
                String title = entry.getKey();
                String coverFileName = entry.getValue();

                Book book = bookService.findByTitle(title);
                if (book != null) {
                    try (InputStream coverStream = getClass().getClassLoader()
                            .getResourceAsStream("static/images/" + coverFileName)) {
                        if (coverStream != null) {
                            byte[] coverBytes = coverStream.readAllBytes();
                            book.setCoverImage(coverBytes);
                        }
                    }

                    if (book.getImages() == null) {
                        book.setImages(new HashSet<>());
                    }

                    int index = 1;
                    while (true) {
                        String extraFileName = String.format("%s%d.jpeg", title, index);
                        try (InputStream extraStream = getClass().getClassLoader()
                                .getResourceAsStream("static/images/" + extraFileName)) {
                            if (extraStream == null)
                                break;
                            byte[] extraBytes = extraStream.readAllBytes();

                            // Check se l'immagine è già presente
                            boolean alreadyExists = book.getImages().stream()
                                    .anyMatch(img -> java.util.Arrays.equals(img.getImageData(), extraBytes));

                            if (!alreadyExists) {
                                BookImage extraImage = new BookImage();
                                extraImage.setBook(book);
                                extraImage.setImageData(extraBytes);
                                book.getImages().add(extraImage);
                            }

                            index++;
                        }
                    }

                    bookService.save(book);
                    System.out.println("Immagini caricate per: " + title);
                } else {
                    System.out.println("Libro non trovato: " + title);
                }
            }
        };
    }
}
