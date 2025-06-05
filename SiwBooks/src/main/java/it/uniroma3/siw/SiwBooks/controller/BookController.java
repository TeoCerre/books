package it.uniroma3.siw.SiwBooks.controller;

import org.springframework.http.MediaType;

import org.springframework.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.SiwBooks.model.Book;
import it.uniroma3.siw.SiwBooks.service.BookService;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books/{id}/cover")
    public ResponseEntity<byte[]> getBookCover(@PathVariable Long id) {
        try {
            System.out.println("👉 Richiesta cover per libro ID: " + id);
            Book book = bookService.findById(id);
            if (book == null) {
                System.out.println("❌ Libro non trovato");
                return ResponseEntity.notFound().build();
            }

            byte[] image = book.getCoverImage();
            if (image == null || image.length == 0) {
                System.out.println("⚠️ Nessuna immagine trovata");
                return ResponseEntity.notFound().build();
            }

            System.out.println("✅ Cover trovata: dimensione = " + image.length + " byte");

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(image);

        } catch (Exception e) {
            // Mostra errore completo nel terminale
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}
