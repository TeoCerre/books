package it.uniroma3.siw.SiwBooks.controller;

import org.springframework.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import it.uniroma3.siw.SiwBooks.model.Author;
import it.uniroma3.siw.SiwBooks.repository.AuthorRepository;
import it.uniroma3.siw.SiwBooks.service.AuthorService;

@Controller
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorService authorService;

    @GetMapping("/authors")
    public String listAuthors(Model model) {
        model.addAttribute("autori", authorRepository.findAll());
        return "authors";
    }

    @GetMapping("/authors/{id}/photo")
    @ResponseBody
    public ResponseEntity<byte[]> getAuthorPhoto(@PathVariable Long id) {
        Author author = authorRepository.findById(id).orElse(null);
        if (author != null && author.getPhoto() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "image/jpeg");
            return new ResponseEntity<>(author.getPhoto(), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/authors/{id}")
    public String authorDetails(@PathVariable Long id, Model model) {
        Author author;
        try {
            author = authorService.findByIdWithBooks(id);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        model.addAttribute("author", author);
        model.addAttribute("books", author.getBooks() != null ? author.getBooks() : java.util.Collections.emptySet());
        return "authorDetails";
    }

}