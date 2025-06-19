package it.uniroma3.siw.SiwBooks.controller;

import it.uniroma3.siw.SiwBooks.service.BookImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookImageController {

    @Autowired
    private BookImageService bookImageService;

    @GetMapping("/bookImages/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        return bookImageService.findById(id)
                .map(image -> ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image.getImageData()))
                .orElse(ResponseEntity.notFound().build());
    }
}