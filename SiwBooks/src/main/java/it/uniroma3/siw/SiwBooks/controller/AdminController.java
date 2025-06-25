package it.uniroma3.siw.SiwBooks.controller;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import it.uniroma3.siw.SiwBooks.model.Author;
import it.uniroma3.siw.SiwBooks.model.Book;
import it.uniroma3.siw.SiwBooks.model.BookImage;
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
        Book book = new Book();
        book.setAuthors(new HashSet<>());
        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.findAll());
        return "admin/newBook";
    }

    @GetMapping("/books")
    public String adminBooks(Model model) {
        model.addAttribute("libri", bookService.findAll());
        return "admin/bookList";
    }

    @PostMapping("/books")
    public String saveBook(@ModelAttribute("book") Book book,
            @RequestParam("authors") List<Long> authorIds,
            @RequestParam("coverFile") MultipartFile coverFile,
            Model model) {

        if (!coverFile.isEmpty()) {
            try {
                book.setCoverImage(coverFile.getBytes());
            } catch (IOException e) {
                model.addAttribute("errore", "Errore nel caricamento della copertina.");
                model.addAttribute("authors", authorService.findAll());
                return "admin/newBook";
            }
        }

        Set<Author> fullAuthors = authorIds.stream()
                .map(authorService::findById)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        book.setAuthors(fullAuthors);

        bookService.save(book);
        return "redirect:/admin/books";
    }

    @GetMapping("/books/{id}/cover")
    @ResponseBody
    public byte[] getBookCover(@PathVariable Long id) {
        Book book = bookService.findById(id);
        return book != null ? book.getCoverImage() : new byte[0];
    }

    @GetMapping("/books/edit/{id}")
    public String editBook(@PathVariable Long id, Model model) {
        Book book = bookService.findByIdWithReviews(id); 
        if (book == null) {
            return "redirect:/admin/books";
        }

        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("images", book.getImages()); 
        return "admin/editBook";
    }

    @PostMapping("/books/edit/{id}")
    public String updateBook(@PathVariable Long id,
            @ModelAttribute("book") Book updatedBook,
            @RequestParam("authors") List<Long> authorIds,
            @RequestParam("coverFile") MultipartFile coverFile,
            @RequestParam(value = "newImages", required = false) MultipartFile[] newImages,
            Model model) {

        Book existing = bookService.findById(id);
        if (existing == null)
            return "redirect:/admin/books";

        existing.setTitle(updatedBook.getTitle());
        existing.setYear(updatedBook.getYear());

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
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        existing.setAuthors(selectedAuthors);

        if (newImages != null) {
            for (MultipartFile file : newImages) {
                if (!file.isEmpty()) {
                    try {
                        BookImage img = new BookImage();
                        img.setBook(existing);
                        img.setImageData(file.getBytes());
                        existing.getImages().add(img);
                    } catch (IOException e) {
                        model.addAttribute("errore", "Errore nel caricamento immagini aggiuntive.");
                        model.addAttribute("authors", authorService.findAll());
                        return "admin/editBook";
                    }
                }
            }
        }

        bookService.save(existing);
        return "redirect:/admin/books";
    }

    @PostMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return "redirect:/admin/books";
    }

    @PostMapping("/books/image/delete/{id}")
    public String deleteBookImage(@PathVariable Long id) {
        BookImage image = bookService.findImageById(id);
        if (image != null) {
            Book book = image.getBook();
            book.getImages().remove(image);
            bookService.deleteImage(image); 
            bookService.save(book);
            return "redirect:/admin/books/edit/" + book.getId();
        }
        return "redirect:/admin/books";
    }

   

    @GetMapping("/authors/new")
    public String newAuthor(Model model) {
        Author author = new Author();
        author.setBooks(new HashSet<>());
        model.addAttribute("author", author);
        model.addAttribute("books", bookService.findAll());
        return "admin/newAuthor";
    }

    @PostMapping("/autori")
    public String saveAuthor(@ModelAttribute("author") Author author,
            @RequestParam(value = "bookIds", required = false) List<Long> bookIds,
            @RequestParam("photoFile") MultipartFile photoFile,
            Model model) {

        if (!photoFile.isEmpty()) {
            try {
                author.setPhoto(photoFile.getBytes());
            } catch (IOException e) {
                model.addAttribute("errore", "Errore nel caricamento della foto.");
                model.addAttribute("books", bookService.findAll());
                return "admin/newAuthor";
            }
        }

        authorService.save(author);

        if (bookIds != null && !bookIds.isEmpty()) {
            Set<Book> books = bookIds.stream()
                    .map(bookService::findById)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            for (Book book : books) {
                book.getAuthors().add(author);
            }

            author.setBooks(books);
            authorService.save(author);
        }

        return "redirect:/admin/autori";
    }

    @GetMapping("/autori")
    public String adminAuthors(Model model) {
        model.addAttribute("autori", authorService.findAll());
        return "admin/authorList";
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
        if (existingAuthor == null)
            return "redirect:/admin/autori";

        existingAuthor.setName(updatedAuthor.getName());
        existingAuthor.setSurname(updatedAuthor.getSurname());
        existingAuthor.setBirthDate(updatedAuthor.getBirthDate());
        existingAuthor.setDeathDate(updatedAuthor.getDeathDate());
        existingAuthor.setNationality(updatedAuthor.getNationality());

        if (!photoFile.isEmpty()) {
            try {
                existingAuthor.setPhoto(photoFile.getBytes());
            } catch (IOException e) {
                model.addAttribute("errore", "Errore nella modifica della foto.");
                model.addAttribute("books", bookService.findAll());
                return "admin/editAuthor";
            }
        }

        Set<Book> books = new HashSet<>();
        if (bookIds != null) {
            for (Long bookId : bookIds) {
                Book book = bookService.findById(bookId);
                if (book != null) {
                    books.add(book);
                    book.getAuthors().add(existingAuthor);
                }
            }
        }
        existingAuthor.setBooks(books);
        authorService.save(existingAuthor);

        return "redirect:/admin/autori";
    }

    @PostMapping("/autori/delete/{id}")
    public String deleteAuthor(@PathVariable Long id) {
        Author author = authorService.findByIdWithBooks(id);
        if (author.getBooks() != null) {
            for (Book book : author.getBooks()) {
                book.getAuthors().remove(author);
            }
        }
        authorService.deleteById(id);
        return "redirect:/admin/autori";
    }


    @GetMapping("/reviews/delete")
    public String deleteReviews(Model model) {
        model.addAttribute("reviews", reviewService.findAll());
        return "admin/reviewDelete";
    }

    @PostMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable Long id) {
        reviewService.deleteById(id);
        return "redirect:/admin/reviews/delete";
    }


    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null);
                } else {
                    setValue(LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE));
                }
            }
        });
    }
}
