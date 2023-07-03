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
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validator.ArtistValidator;
import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.MovieService;

@Controller
public class ArtistController {
	
	@Autowired 
	private ArtistService artistService;
	@Autowired 
	private ArtistValidator artistValidator;
	@Autowired 
	private MovieService movieService;

	@GetMapping("/generic/artists/{id}")
	public String getArtist(@PathVariable("id") Long id, Model model) {
		Artist artist=this.artistService.findArtistById(id);
		if(artist==null)
			return "artistError.html";
		model.addAttribute("artist" , artist);
		return "generic/artist.html";
	}
	@GetMapping("/admin/formNewArtist")
	public String formNewArtist(Model model) {
		model.addAttribute("artist", new Artist());
		return "/admin/formNewArtist.html";
	}
	@PostMapping("/admin/artist")
	public String newArtist(@Valid @ModelAttribute("artist") Artist artist, BindingResult bindingResult,
			MultipartFile image, Model model) {
		this.artistValidator.validate(artist, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.artistService.createArtist(artist, image);
			model.addAttribute("artist", artist);
			return "generic/artist.html";
		}
		else {
			return "admin/formNewArtist.html";
		}
	}
	@GetMapping("/admin/formUpdateArtist/{idArtist}")
	public String formUpdateArtist(@PathVariable("idArtist") Long idArtist, Model model) {
		Artist artist=this.artistService.findArtistById(idArtist);
		if(artist==null)
			return "generic/artistError.html";
		model.addAttribute("artist",artist);
		return "admin/formUpdateArtist.html";
	}
	@GetMapping("/admin/formUpdateArtistData/{idArtist}")
	public String formUpdateArtistData(@PathVariable("idArtist") Long idArtist, Model model) {
		Artist artist=this.artistService.findArtistById(idArtist);
		if(artist==null)
			return "generic/artistError.html";
		model.addAttribute("artist",artist);
		return "admin/formUpdateArtistData.html";
	}
	@PostMapping("/admin/updateArtistData/{idArtist}")
	public String updateArtistData(@PathVariable("idArtist") Long idArtist, 
			@Valid @ModelAttribute("artist") Artist newArtist, BindingResult bindingResult,
			MultipartFile image, Model model) {
		this.artistValidator.validate(newArtist, bindingResult);
		if(!bindingResult.hasErrors()) {
			model.addAttribute("artist", this.artistService.update(idArtist, newArtist, image));
			return "admin/formUpdateArtist.html";
		}
		else {
			return "admin/formUpdateArtistData.html";
		}
	}
	@GetMapping("/generic/artists")
	public String showArtists(Model model) {
		model.addAttribute("artists", this.artistService.findAllArtist());
		return "generic/artists.html";
	}
	@GetMapping("/admin/manageArtist")
	public String managaArtist(Model model) {
		model.addAttribute("artists", this.artistService.findAllArtist());
		return "admin/manageArtist.html";
	}
	
	@GetMapping("/admin/formConfirmDeleteArtist/{idArtist}")
	public String formConfirmDeleteArtist(@PathVariable ("idArtist") Long idArtist, Model model) {
		Artist artist=this.artistService.findArtistById(idArtist);
		if(artist==null)
			return "generic/artistError.html";
		model.addAttribute("artist", artist);
		return "/admin/formConfirmDeleteArtist.html";
	}
	@Transactional
	@GetMapping("/admin/deleteArtist/{idArtist}")
	public String deleteArtist(@PathVariable ("idArtist") Long idArtist, Model model) {
		Artist artist=this.artistService.findArtistById(idArtist);
		if(artist==null)
			return "generic/artistError.html";
		else {
			this.movieService.removeArtistAssociationFromAllMovie(idArtist); //problema
			this.artistService.removeMovieAssociationFromActor(idArtist);
			this.artistService.delete(idArtist);
			model.addAttribute("artists", this.artistService.findAllArtist());
			return "admin/manageArtist.html";
		}
	}
}
