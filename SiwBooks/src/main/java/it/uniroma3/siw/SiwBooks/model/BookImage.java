package it.uniroma3.siw.SiwBooks.model;

import jakarta.persistence.*;

@Entity
public class BookImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] imageData;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public byte[] getImageData() { return imageData; }
    public void setImageData(byte[] imageData) { this.imageData = imageData; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
}
