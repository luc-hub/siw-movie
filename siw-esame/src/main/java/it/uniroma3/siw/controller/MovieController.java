package it.uniroma3.siw.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validator.MovieValidator;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.service.UserService;

@Controller
public class MovieController {

	@Autowired MovieService movieService;
	@Autowired ArtistService artistService;
	@Autowired ReviewService reviewService;
	@Autowired UserService userService;
	@Autowired CredentialsService credentialsService;
	@Autowired MovieValidator movieValidator;

	
	@GetMapping(value="/admin/formNewMovie")
	public String formNewMovie(Model model) {
		model.addAttribute("movie", new Movie());
		return "admin/formNewMovie.html";
	}
	
	@Transactional
	@PostMapping("/admin/movies")
	public String newMovie(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult, 
			MultipartFile image, Model model) {
		this.movieValidator.validate(movie, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.movieService.createNewMovie(movie, image);
			
			model.addAttribute("movie", movie);
			return "generic/movie.html";
		}
		else {
			return "admin/formNewMovie.html";
		}
	}
	
	@GetMapping("/admin/manageMovies")
	public String manageMovies(Model model) {
		model.addAttribute("movies", this.movieService.findAllMovie());
		return "admin/manageMovies.html";
	}
	
	@Transactional
	@GetMapping("/admin/formUpdateMovie/{id}")
	public String formUpdateMovie(@PathVariable("id") Long id, Model model) {
		Movie movie= this.movieService.findMovieById(id);
		if(movie!=null) {
			model.addAttribute("movie",movie);
		}
		else {
			return "movieError.html";
		}
		return "/admin/formUpdateMovie.html";
	}
	
	@Transactional
	@GetMapping("/admin/addDirectorToMovie/{id}")
	public String addDirector(@PathVariable("id") Long id, Model model) {
		model.addAttribute("directors",this.artistService.findAllArtist());
		Movie movie= this.movieService.findMovieById(id);
		if(movie == null)
			return "generic/movieError.html";
		model.addAttribute("movie",movie);
		return "/admin/directorToAdd.html";
	}
	
	@Transactional
	@GetMapping("/admin/setDirectorToMovie/{idArtist}/{idMovie}")
	public String setDirectorToMovie(@PathVariable("idArtist") Long idArtist, @PathVariable("idMovie") Long idMovie, Model model){
		Movie movie=this.movieService.saveDirectorToMovie(idMovie, idArtist);
		if(movie!=null) {
			model.addAttribute("movie", movie);
			model.addAttribute("director", movie.getDirector());
			return "/admin/formUpdateMovie.html";
		}
		else
			return "generic/movieError.html";
	}
	
	@Transactional
	@GetMapping("/admin/updateActorsOfMovie/{idMovie}")
	public String updateActorsOfMovie(@PathVariable("idMovie") Long idMovie, Model model) {
		Movie movie=this.movieService.findMovieById(idMovie);
		if(movie==null)
			return "generic/movieError.html";
		model.addAttribute("movie", movie);
		model.addAttribute("actorsOfMovie", this.artistService.findAllArtistByStarredMoviesIsContaining(movie));
		model.addAttribute("notActorsOfMovie", this.artistService.findAllArtistByStarredMoviesIsNotContaining(movie));
		return "/admin/actorsToAdd.html";
	}
	
	@Transactional
	@GetMapping("/admin/addActorToMovie/{idActor}/{idMovie}")
	public String addActorToMovie(@PathVariable("idActor") Long idActor, @PathVariable("idMovie") Long idMovie, Model model) {
		Movie movie=this.movieService.addActorToMovie(idMovie,idActor);
		if(movie==null)
			return "generic/movieError.html";
		model.addAttribute("movie", movie);
		model.addAttribute("actorsOfMovie", this.artistService.findAllArtistByStarredMoviesIsContaining(movie));
		model.addAttribute("notActorsOfMovie", this.artistService.findAllArtistByStarredMoviesIsNotContaining(movie));
		return "admin/actorsToAdd.html";
	}
	
	@Transactional
	@GetMapping("/admin/removeActorFromMovie/{idActor}/{idMovie}")
	public String removeActorFromMovie(@PathVariable("idActor") Long idActor, @PathVariable("idMovie") Long idMovie, Model model) {
		Movie movie=this.movieService.removeActorFromMovie(idMovie, idActor);
		if(movie==null) {
			return "generic/movieError.html";
		}
		model.addAttribute("movie", movie);
		model.addAttribute("actorsOfMovie", this.artistService.findAllArtistByStarredMoviesIsContaining(movie));
		model.addAttribute("notActorsOfMovie", this.artistService.findAllArtistByStarredMoviesIsNotContaining(movie));
		return "admin/actorsToAdd.html";
	}
	
	@GetMapping("/admin/formConfirmDeleteFilm/{idMovie}")
	public String formConfirmDeleteFilm(@PathVariable("idMovie") Long idMovie, Model model) {
		Movie movie=this.movieService.findMovieById(idMovie);
		if(movie==null)
			return "generic/movieError.html";
		else {
			model.addAttribute("movie", movie);
			return "admin/formConfirmDeleteFilm.html";
		}
	}
	@Transactional
	@GetMapping("/admin/deleteMovie/{idMovie}")
	public String deleteMovie(@PathVariable("idMovie") Long idMovie, Model model) {
		Movie movie=this.movieService.findMovieById(idMovie);
		if(movie==null)
			return "generic/movieError.html";
		this.artistService.removeMovieAssociationFromAllActor(movie);
		this.movieService.removeActorAssociationFromAllMovie(idMovie);
		this.reviewService.removeMovieAssociationFromReview(movie);
		this.movieService.delete(idMovie);
		model.addAttribute("movies", this.movieService.findAllMovie());
		return "admin/manageMovies.html";
	}	
	
	@GetMapping("/admin/formUpdateMovieData/{idMovie}")
	public String formUpdateMovieData(@PathVariable("idMovie") Long idMovie, Model model) {
		Movie movie=this.movieService.findMovieById(idMovie);
		if(movie==null)
			return "generic/movieError.html";
		model.addAttribute("movie",movie);
		return "admin/formUpdateMovieData.html";
	}
	@PostMapping("/admin/updateMovieData/{idMovie}")
		public String updateMovieData(@PathVariable("idMovie") Long idMovie, 
				@Valid @ModelAttribute("movie") Movie newMovie, BindingResult bindingResult,
				MultipartFile image, Model model) {
		this.movieValidator.validate(newMovie, bindingResult);
		if(!bindingResult.hasErrors()) {
			model.addAttribute("movie", this.movieService.update(idMovie, newMovie, image));
			return "/admin/formUpdateMovie.html";
		}
		else {
			model.addAttribute("movie", this.movieService.findMovieById(idMovie));
		}
			return "/admin/formUpdateMovieData.html";
	}
	
	
	@GetMapping("/generic/movies/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		Movie movie=this.movieService.findMovieById(id);
		if(movie==null)
			return "generic/movieError.html";
		else {
			model.addAttribute("movie", movie);
			return "generic/movie.html";
		}
	}
	
	@GetMapping("/generic/movies")
	public String showMovies(Model model) {
		model.addAttribute("movies", this.movieService.findAllMovie());
		return "generic/movies.html";
	}
	
	@PostMapping("/generic/searchMovies")
	public String searchMovies(Model model, @RequestParam Integer year) {
		model.addAttribute("movies", this.movieService.findMovieByYear(year));
		return "generic/movies.html";
	}
	

}