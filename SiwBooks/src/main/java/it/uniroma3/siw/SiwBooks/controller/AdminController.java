package it.uniroma3.siw.SiwBooks.controller;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import it.uniroma3.siw.SiwBooks.model.Author;
import it.uniroma3.siw.SiwBooks.model.Book;
import it.uniroma3.siw.SiwBooks.service.AuthorService;
import it.uniroma3.siw.SiwBooks.service.BookService;
import it.uniroma3.siw.SiwBooks.service.ReviewService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/home")
    public String adminHome() {
        return "admin/adminHome";
    }

    @GetMapping("/books/new")
    public String newBook(Model model) {
        List<Author> authors = authorService.findAll();
        System.out.println("Autori trovati: " + authors.size()); // DEBUG
        Book book = new Book();
        book.setAuthors(new HashSet<>());
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        return "admin/newBook";
    }

    @GetMapping("/books")
    public String adminBooks(Model model) {
        model.addAttribute("libri", bookService.findAll());
        return "admin/bookList";
    }

    @GetMapping("/books/{id}/cover")
    @ResponseBody
    public byte[] getBookCover(@PathVariable Long id) {
        Book book = bookService.findById(id);
        return book.getCoverImage();
    }

    @GetMapping("/authors/new")
    public String newAuthor(Model model) {
        Author author = new Author();
        author.setBooks(new HashSet<>()); // ✅ Inizializzazione esplicita
        model.addAttribute("author", author);
        model.addAttribute("books", bookService.findAll());
        return "admin/newAuthor";
    }

    @GetMapping("/autori")
    public String adminAuthors(Model model) {
        model.addAttribute("autori", authorService.findAll());
        return "admin/authorList";
    }

    @PostMapping("/books")
    public String saveBook(@ModelAttribute("book") Book book,
            @RequestParam("authors") List<Long> authorIds,
            @RequestParam("coverFile") MultipartFile coverFile,
            Model model) {
        // Gestione immagine come già fatto
        if (!coverFile.isEmpty()) {
            try {
                byte[] imageBytes = coverFile.getBytes();
                book.setCoverImage(imageBytes);
            } catch (IOException e) {
                model.addAttribute("errore", "Errore nel caricamento della copertina.");
                model.addAttribute("authors", authorService.findAll());
                return "admin/newBook";
            }
        }

        // Recupera gli autori completi dal DB
        Set<Author> fullAuthors = authorIds.stream()
                .map(id -> authorService.findById(id))
                .filter(a -> a != null)
                .collect(Collectors.toSet());

        book.setAuthors(fullAuthors);

        // Salva il libro
        bookService.save(book);

        return "redirect:/admin/books";
    }

    @PostMapping("/autori/delete/{id}")
    public String deleteAuthor(@PathVariable Long id) {
        Author author = authorService.findByIdWithBooks(id);

        // Scollega l'autore da tutti i libri
        if (author.getBooks() != null) {
            for (Book book : author.getBooks()) {
                book.getAuthors().remove(author);
            }
        }

        authorService.deleteById(id); // Ora si può eliminare senza problemi
        return "redirect:/admin/autori";
    }

    @PostMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return "redirect:/admin/books";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null); // stringa vuota diventa null
                } else {
                    setValue(LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE));
                }
            }
        });
    }

    @PostMapping("/autori")
    public String saveAuthor(@ModelAttribute("author") Author author,
            @RequestParam(value = "bookIds", required = false) List<Long> bookIds,
            @RequestParam("photoFile") MultipartFile photoFile,
            Model model) {

        // Caricamento foto
        if (!photoFile.isEmpty()) {
            try {
                author.setPhoto(photoFile.getBytes());
            } catch (IOException e) {
                model.addAttribute("errore", "Errore nel caricamento della foto.");
                model.addAttribute("books", bookService.findAll());
                return "admin/newAuthor";
            }
        }

        // Primo salvataggio per ottenere ID
        authorService.save(author);

        // Se ci sono libri selezionati, crea la relazione
        if (bookIds != null && !bookIds.isEmpty()) {
            Set<Book> books = bookIds.stream()
                    .map(bookService::findById)
                    .filter(b -> b != null)
                    .collect(Collectors.toSet());

            for (Book book : books) {
                book.getAuthors().add(author); // bidirezionale
            }

            author.setBooks(books);
            authorService.save(author); // risalva con i libri
        }

        return "redirect:/admin/autori";
    }

    @GetMapping("/books/edit/{id}")
    public String editBook(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id);
        if (book == null) {
            return "redirect:/admin/books";
        }

        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.findAll());
        return "admin/editBook";
    }

    @PostMapping("/books/edit/{id}")
    public String updateBook(@PathVariable Long id,
            @ModelAttribute("book") Book book,
            @RequestParam("authors") List<Long> authorIds,
            @RequestParam("coverFile") MultipartFile coverFile,
            Model model) {

        Book existing = bookService.findById(id);
        if (existing == null) {
            return "redirect:/admin/books";
        }

        existing.setTitle(book.getTitle());
        existing.setYear(book.getYear());

        if (!coverFile.isEmpty()) {
            try {
                existing.setCoverImage(coverFile.getBytes());
            } catch (IOException e) {
                model.addAttribute("errore", "Errore nella modifica della copertina.");
                model.addAttribute("authors", authorService.findAll());
                return "admin/editBook";
            }
        }

        Set<Author> selectedAuthors = authorIds.stream()
                .map(authorService::findById)
                .filter(a -> a != null)
                .collect(Collectors.toSet());
        existing.setAuthors(selectedAuthors);

        bookService.save(existing);

        return "redirect:/admin/books";
    }

    @GetMapping("/autori/edit/{id}")
    public String editAuthor(@PathVariable Long id, Model model) {
        Author author = authorService.findByIdWithBooks(id);
        model.addAttribute("author", author);
        model.addAttribute("books", bookService.findAll());
        return "admin/editAuthor";
    }

    @PostMapping("/autori/edit/{id}")
    public String updateAuthor(@PathVariable Long id,
            @ModelAttribute("author") Author updatedAuthor,
            @RequestParam(value = "bookIds", required = false) List<Long> bookIds,
            @RequestParam("photoFile") MultipartFile photoFile,
            Model model) {
        Author existingAuthor = authorService.findById(id);

        // Aggiorna i campi base
        existingAuthor.setName(updatedAuthor.getName());
        existingAuthor.setSurname(updatedAuthor.getSurname());
        existingAuthor.setBirthDate(updatedAuthor.getBirthDate());
        existingAuthor.setDeathDate(updatedAuthor.getDeathDate());
        existingAuthor.setNationality(updatedAuthor.getNationality());

        // Se è stata caricata una nuova foto
        if (!photoFile.isEmpty()) {
            try {
                existingAuthor.setPhoto(photoFile.getBytes());
            } catch (IOException e) {
                model.addAttribute("errore", "Errore nella modifica della foto.");
                model.addAttribute("books", bookService.findAll());
                return "admin/editAuthor";
            }
        }

        // Aggiorna libri associati
        Set<Book> books = new HashSet<>();
        if (bookIds != null) {
            for (Long bookId : bookIds) {
                Book book = bookService.findById(bookId);
                books.add(book);
                book.getAuthors().add(existingAuthor); // bidirezionale
            }
        }
        existingAuthor.setBooks(books);

        authorService.save(existingAuthor);

        return "redirect:/admin/autori";
    }

    @GetMapping("/reviews/delete")
    public String deleteReviews(Model model) {
        // Assumi che ReviewService sia autowired
        model.addAttribute("reviews", reviewService.findAll());
        return "admin/reviewDelete";
    }

    @PostMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable Long id) {
        reviewService.deleteById(id);
        return "redirect:/admin/reviews/delete";
    }

}
