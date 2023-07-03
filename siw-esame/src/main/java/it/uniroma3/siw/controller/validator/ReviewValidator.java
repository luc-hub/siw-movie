package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.ReviewService;

@Component
public class ReviewValidator implements Validator {

	@Autowired ReviewService reviewService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Review.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Review review=(Review)target;
		if(this.reviewService.isTextLengthOverMax(review))
			errors.reject("review.maxLengthText");
		if(this.reviewService.isTitleLengthOverMax(review))
			errors.reject("review.maxLengthTitle");
	}
	

}
