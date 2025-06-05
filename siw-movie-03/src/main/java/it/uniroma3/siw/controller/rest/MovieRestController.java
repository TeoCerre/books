package it.uniroma3.siw.controller.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.MovieService;
import jakarta.validation.Valid;

@RestController
public class MovieRestController {

	@Autowired 
	private MovieService movieService;
	
	@Autowired 
	private ArtistService artistService;
	
	@PutMapping(value="/admin/rest/movies/{movieId}/directors/{directorId}")
	public Movie setDirectorToMovie(@PathVariable("movieId") Long movieId,
										@PathVariable("directorId") Long directorId) {
		
		Artist director = this.artistService.findById(directorId);
		Movie movie = this.movieService.findById(movieId);
		movie.setDirector(director);
		this.movieService.save(movie);
		return movie;
	}

	@PostMapping("/admin/rest/movies")
	public Movie newMovie(@Valid @RequestBody Movie movie) {
		this.movieService.save(movie); 
		return movie;
	}

	@GetMapping("/rest/movies/{id}")
	public Movie getMovie(@PathVariable("id") Long id) {
		return this.movieService.findById(id);
	}

	@GetMapping("/rest/movies")
	public List<Movie> getMovies() {	
		List<Movie> movies = new ArrayList<>();
		for(Movie m : this.movieService.findAll())
			movies.add(m);
		return movies;
	}
	
	// add an actor to a movie
	@PutMapping(value="/admin/rest/movies/{movieId}/actors/{actorId}/")
	public Movie addActorToMovie(@PathVariable("movieId") Long movieId, 
									@PathVariable("actorId") Long actorId) {
		Movie movie = this.movieService.findById(movieId);
		Artist actor = this.artistService.findById(actorId);
		Set<Artist> actors = movie.getActors();
		actors.add(actor);
		this.movieService.save(movie);
		return movie;
	}
	
	@DeleteMapping(value="/admin/rest/movies/{movieId}/actors/{actorId}")
	public Movie removeActorFromMovie(@PathVariable("movieId") Long movieId, @PathVariable("actorId") Long actorId) {
		Movie movie = this.movieService.findById(movieId);
		Artist actor = this.artistService.findById(actorId);
		Set<Artist> actors = movie.getActors();
		actors.remove(actor);
		this.movieService.save(movie);
		return movie;
	}
}
