package de.otaris.zertapps.privacychecker.appDetails.rateApp.expertMode;

import android.content.Context;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElement;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingValidationErrorException;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;

/**
 * enables a user to give an expert-Rating
 *
 */
public class ExpertMode extends RatingElement {

	public ExpertMode(AppExtended app, boolean mandatory) {
		super(app, mandatory);
	}

	@Override
	public void validate() throws RatingValidationErrorException {

	}

	@Override
	public void save(Context context) {
		// do nothing
	}

}
