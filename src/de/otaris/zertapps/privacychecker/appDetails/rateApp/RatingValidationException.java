package de.otaris.zertapps.privacychecker.appDetails.rateApp;

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
