package it.uniroma3.siw.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.OnlyOneReviewForUserValidator;
import it.uniroma3.siw.controller.validator.ReviewValidator;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.service.UserService;

@Controller
public class ReviewController {
	@Autowired ReviewService reviewService;
	@Autowired ReviewValidator reviewValidator;
	@Autowired MovieService movieService;
	@Autowired UserService userService;
	@Autowired OnlyOneReviewForUserValidator onlyOneReviewForUserValidator;
	
	
	
	@GetMapping("registered/addReviewToMovie/{idMovie}")
	public String createReviewToMovie(@PathVariable("idMovie") Long idMovie, Model model) {
		Movie movie= this.movieService.findMovieById(idMovie);
		model.addAttribute("movie", movie);
		model.addAttribute("review",new Review());
		return "registered/reviewToAddToMovie.html";
	}
	@PostMapping("registered/createReviewToFilm/{idMovie}")
	public String newReviewToMovie(@Valid @ModelAttribute("review") Review review, BindingResult bindingResult, @PathVariable("idMovie") Long idMovie , Model model) {
		this.reviewValidator.validate(review, bindingResult);
		this.onlyOneReviewForUserValidator.validate(this.movieService.findMovieById(idMovie), bindingResult);
		if(!bindingResult.hasErrors()) {
			Movie movie=this.movieService.findMovieById(idMovie);
			this.reviewService.setMovieToReview(review, movie);
			this.movieService.addReviewToMovie(movie, review);
			User user=userService.getCurrentUser();
			this.userService.addReview(user, review);
			this.reviewService.setUser(review, user);
			
			model.addAttribute("movie", movie);		
			model.addAttribute("reviews",movie.getReviews());
			return "generic/movie.html";
		}
		else {
			model.addAttribute("movie",this.movieService.findMovieById(idMovie));
			return "registered/reviewToAddToMovie.html";
		}
	}
	@GetMapping("registered/formConfirmDeleteReview/{idReview}")
	public String formConfirmDeleteReview(@PathVariable ("idReview") Long idReview, Model model) {
		Review review=this.reviewService.findReviewById(idReview);
		if(review==null)
			return "reviewError.html";
		model.addAttribute("review", review);
		return "registered/formConfirmDeleteReview.html";
	}
	@GetMapping("/admin/deleteReview/{idReview}")
	public String deleteReview(@PathVariable ("idReview") Long idReview, Model model) {
		Review review=this.reviewService.findReviewById(idReview);
		if(review==null)
			return "reviewError.html";
		Movie movie=review.getMovie();
		this.movieService.removeReviewAssociationFromMovie(review);
		this.userService.removeReviewAsscociationFromUser(review);
		this.reviewService.delete(idReview);
		model.addAttribute("movie",movie);
		return "generic/movie.html";
	}
	@GetMapping("/registered/deleteReviewRegistered/{idReview}")
	public String deleteReviewRegistered(@PathVariable ("idReview") Long idReview, Model model) {
		Review review=this.reviewService.findReviewById(idReview);
		if(review==null || !review.getUser().equals(userService.getCurrentUser()))
			return "reviewError.html";
		Movie movie=review.getMovie();
		this.movieService.removeReviewAssociationFromMovie(review);
		this.userService.removeReviewAsscociationFromUser(review);
		this.reviewService.delete(idReview);
		model.addAttribute("movie",movie);
		return "generic/movie.html";
	}
	
}
