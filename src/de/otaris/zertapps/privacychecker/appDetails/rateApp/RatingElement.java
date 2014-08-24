package de.otaris.zertapps.privacychecker.appDetails.rateApp;

import android.content.Context;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;

/**
 * blueprint for multiple aspects to be combined to a (privacy) rating that are
 * displayed, validated and thats data is autonomously stored
 */
public abstract class RatingElement {

	protected AppExtended app;
	protected boolean mandatory = false;

	public RatingElement(AppExtended app, boolean mandatory) {
		this.app = app;
		this.mandatory = mandatory;
	}

	public AppExtended getApp() {
		return app;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	/**
	 * validate by mandatory
	 * 
	 * @return true if input is validate false if it is not
	 * @throws RatingValidationException
	 */

	public abstract boolean validate() throws RatingValidationException;

	/**
	 * save data that has been acquired
	 * 
	 * @param context
	 *            can be used to establish database connections
	 */
	public abstract void save(Context context);
}
