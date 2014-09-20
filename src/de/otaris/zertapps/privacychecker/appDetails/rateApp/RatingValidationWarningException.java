package de.otaris.zertapps.privacychecker.appDetails.rateApp;

/**
 * Exception Class to handle exceptions concerning RatingElements should be used
 * if Validation warning occurs
 */
public class RatingValidationWarningException extends Exception {

	private static final long serialVersionUID = 3656802378502685781L;
	private int validationWarning = -1;

	public RatingValidationWarningException(int validationWarning) {
		this.validationWarning = validationWarning;
	}

	public int getValidationWarningId() {
		return validationWarning;
	}

}
