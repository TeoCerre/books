package it.uniroma3.siw.SiwBooks.config;

import it.uniroma3.siw.SiwBooks.model.Author;
import it.uniroma3.siw.SiwBooks.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.Map;

@Configuration
public class AuthorPhotoInitializer {

    @Autowired
    private AuthorService authorService;

    @Bean
    CommandLineRunner loadAuthorPhotos() {
        return args -> {
            Map<String, String> authorPhotos = Map.of(
                "Elena Ferrante", "Ferrante.jpeg",
                "Don DeLillo", "DeLillo.jpeg",
                "Ian McEwan", "McEwan.jpeg",
                "Niccolò Ammaniti", "Ammaniti.jpeg",
                "Jonathan Franzen", "Franzen.jpeg"
            );

            for (Map.Entry<String, String> entry : authorPhotos.entrySet()) {
                String fullName = entry.getKey();
                String fileName = entry.getValue();

                String[] nameParts = fullName.split(" ");
                String name = nameParts[0];
                String surname = nameParts[1];

                Author author = authorService.findByNameAndSurname(name, surname);
                if (author != null) {
                    try (InputStream photoStream = getClass().getClassLoader().getResourceAsStream("static/images/" + fileName)) {
                        if (photoStream != null) {
                            byte[] photoBytes = photoStream.readAllBytes();
                            author.setPhoto(photoBytes);
                            authorService.save(author);
                            System.out.println("Foto caricata per: " + fullName);
                        }
                    }
                } else {
                    System.out.println("Autore non trovato: " + fullName);
                }
            }
        };
    }
}
