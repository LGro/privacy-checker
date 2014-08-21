package de.otaris.zertapps.privacychecker.appDetails.rateApp;


public interface RatingElement {

	public boolean validate();

	public void isMandatory();

	public void save();

}
