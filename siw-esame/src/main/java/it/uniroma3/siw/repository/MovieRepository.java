package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;

public interface MovieRepository extends CrudRepository<Movie, Long> {

	public List<Movie> findByYear(int year);
	public boolean existsByTitleAndYear(String title, int year);
	
	public List<Movie> findAllByActorsIsContaining(Artist actor);
	public List<Movie> findAllByDirector(Artist director);
	public List<Movie> findAllByReviewsIsContaining(Review review);
	
}