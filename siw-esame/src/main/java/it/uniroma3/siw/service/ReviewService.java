package it.uniroma3.siw.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ReviewRepository;

@Service
public class ReviewService {

	@Autowired private ReviewRepository reviewRepository;
	
	public Review findReviewById(Long id) {
		return this.reviewRepository.findById(id).orElse(null);
	}
	
	@Transactional
	public void removeMovieAssociationFromReview(Movie movie) {
		List<Review> reviewToDelete= this.reviewRepository.findAllByMovie(movie);
		for(Review review:reviewToDelete) {
			review.setMovie(null);
			this.reviewRepository.delete(review);
		}
	}
	
	@Transactional
	public void setMovieToReview(Review review, Movie movie) {
		review.setMovie(movie);
		this.reviewRepository.save(review);
	}
	public boolean isTitleLengthOverMax(Review review) {
		return review.getTitle()!=null && review.getTitle().length()>Review.getMaxLengthTitle();
	}
	public boolean isTextLengthOverMax(Review review) {
		return review.getText()!=null && review.getText().length()>Review.getMaxLengthText();
	}

	public void setUser(Review review, User user) {
		review.setUser(user);
		this.reviewRepository.save(review);		
	}

	public void delete(Long idReview) {
		Review review= this.findReviewById(idReview);
		this.reviewRepository.delete(review);
		
	}

}
