package it.uniroma3.siw.SiwBooks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.SiwBooks.model.Review;
import it.uniroma3.siw.SiwBooks.repository.ReviewRepository;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public void save(Review review) {
        reviewRepository.save(review);
    }
}
