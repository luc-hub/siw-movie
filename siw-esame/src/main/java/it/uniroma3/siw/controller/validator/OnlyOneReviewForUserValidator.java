package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.service.UserService;

@Component
public class OnlyOneReviewForUserValidator implements Validator{

	@Autowired UserService userService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Movie.class.equals(clazz);
	}

	@Override
	public void validate(Object o, Errors errors) {
		Movie movie=(Movie)o;
		if(this.userService.multipleReviewOfMovieBySameUser(movie))
			errors.reject("review.duplicate");		
	}

}
