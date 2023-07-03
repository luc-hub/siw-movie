package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;

@Service
public class ArtistService {
	
	@Autowired 
	private ArtistRepository artistRepository;
	
	@Transactional
	public Iterable<Artist> findAllArtist(){
		return artistRepository.findAll();
	}
	
	public Artist findArtistById(Long idArtist) {
		return this.artistRepository.findById(idArtist).orElse(null);
	}
	
	public List<Artist> findAllArtistByStarredMoviesIsContaining(Movie movie) {
		return this.artistRepository.findAllByStarredMoviesIsContaining(movie);
	}
	
	public List<Artist> findAllArtistByStarredMoviesIsNotContaining(Movie movie){
		return this.artistRepository.findAllByStarredMoviesIsNotContaining(movie);
	}
	
	public boolean alreadyExists(Artist artist) {
		return artist.getName()!=null && artist.getSurname()!=null && artist.getDateOfBirth()!=null
				&& artistRepository.existsByNameAndSurname(artist.getName(), artist.getSurname());
	}
	
	@Transactional
	public void removeMovieAssociationFromAllActor(Movie movie) {
		List<Artist> actors=this.artistRepository.findAllByStarredMoviesIsContaining(movie);
		for(Artist actor:actors) {
			actor.getActorOf().remove(movie);
			this.artistRepository.save(actor);
		}
	}
	
	@Transactional
	public void removeMovieAssociationFromActor(Long idActor) {
		Artist artist= this.artistRepository.findById(idActor).get();
		artist.setActorOf(null);;
		artist.setDirectorOf(null);;
		this.artistRepository.save(artist);
	}
	
	public void delete(Long idArtist) {
		Artist artist= this.artistRepository.findById(idArtist).get();
		this.artistRepository.delete(artist);
	}
	
	public void saveArtist(@Valid Artist artist) {
		this.artistRepository.save(artist);		
	}
	
	@Transactional
	public void createArtist(Artist artist, MultipartFile image) {
		try {
			String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
			artist.setImageString(base64Image);
			this.saveArtist(artist);
			} catch(IOException e) {}
	}
	
	public Artist update(Long idArtist, Artist newArtist, MultipartFile image) {
		Artist artist= this.artistRepository.findById(idArtist).get();
		artist.setName(newArtist.getName());
		artist.setSurname(newArtist.getSurname());
		artist.setDateOfBirth(newArtist.getDateOfBirth());
		if(!image.isEmpty()) {
			try {
				String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
				artist.setImageString(base64Image);
				} catch(IOException e) {}
		}
		this.artistRepository.save(artist);
		return artist;
	}
	
}
