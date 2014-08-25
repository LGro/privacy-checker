package de.otaris.zertapps.privacychecker.appDetails.rateApp.expertMode;

import android.content.Context;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElement;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingValidationException;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;

public class ExpertMode extends RatingElement {

	public ExpertMode(AppExtended app, boolean mandatory) {
		super(app, mandatory);
	}

	@Override
	public void validate() throws RatingValidationException {

	}

	@Override
	public void save(Context context) {
		// do nothing
	}

}
