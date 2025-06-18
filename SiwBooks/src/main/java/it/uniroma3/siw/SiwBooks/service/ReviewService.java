package it.uniroma3.siw.SiwBooks.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.SiwBooks.model.Review;
import it.uniroma3.siw.SiwBooks.repository.ReviewRepository;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public boolean hasUserReviewedBook(Long userId, Long bookId) {
        return reviewRepository.existsByBookIdAndAuthorId(bookId, userId);
    }

    public void save(Review review) {
        reviewRepository.save(review);
    }

    public List<Review> findAll() {
        return StreamSupport.stream(reviewRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }
}
