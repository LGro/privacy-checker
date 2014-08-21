package de.otaris.zertapps.privacychecker.appDetails.rateApp;

import android.content.Context;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;

public abstract class RatingElement {

	protected AppExtended app;
	protected boolean mandatory = false;
	
	public RatingElement(AppExtended app, boolean mandatory) {
		this.app = app;
		this.mandatory = mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public abstract boolean validate() throws RatingValidationException;

	public abstract void save(Context context);

}
