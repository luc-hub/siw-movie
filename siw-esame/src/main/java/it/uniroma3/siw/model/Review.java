package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Review {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@NotBlank
	private String title;
	private static int maxLengthTitle=20;
	@NotBlank
	private String text;
	private static int maxLengthText=150;
	@NotNull
	@Min(1)
	@Max(5)
	private int rating;
	private LocalDate date;
	@ManyToOne
	private Movie movie;
	@ManyToOne
	private User user;
	
	public Review() {	}
	
	public Review(String title, String text, int rating, LocalDate date) {
		this.title = title;
		this.text = text;
		this.date = date;
		this.rating = rating;
	}


	@Override
	public int hashCode() {
		return Objects.hash(date, text, title);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Review other = (Review) obj;
		return Objects.equals(date, other.date) && Objects.equals(text, other.text)
				&& Objects.equals(title, other.title);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public static int getMaxLengthTitle() {
		return maxLengthTitle;
	}

	public static void setMaxLengthTitle(int maxLengthTitle) {
		Review.maxLengthTitle = maxLengthTitle;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public static int getMaxLengthText() {
		return maxLengthText;
	}

	public static void setMaxLengthText(int maxLengthText) {
		Review.maxLengthText = maxLengthText;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
