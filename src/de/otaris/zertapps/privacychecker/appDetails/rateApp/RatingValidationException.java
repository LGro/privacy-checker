package de.otaris.zertapps.privacychecker.appDetails.rateApp;

/**
 * Exception Class to handle exceptions concerning RatingElements should be used
 * if Validation Error occurs
 */
public class RatingValidationException extends Exception {

	private static final long serialVersionUID = -5608809854313203918L;
	private int validationError = -1;

	public RatingValidationException(int validationError) {
		this.validationError = validationError;
	}

	public int getValidationErrorId() {
		return validationError;
	}

}
