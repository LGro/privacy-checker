package de.otaris.zertapps.privacychecker.appDetails.rateApp.totalPrivacyRating;

import java.util.HashMap;

import android.content.Context;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElement;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingValidationException;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.Registry;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.permissionsExpected.PermissionsExpected;
import de.otaris.zertapps.privacychecker.database.dataSource.RatingAppDataSource;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;
import de.otaris.zertapps.privacychecker.database.model.Permission;

public class TotalPrivacyRating extends RatingElement {

	// default value -1 if no rating is set
	protected int rating = -1;

	public TotalPrivacyRating(AppExtended app, boolean mandatory) {
		super(app, mandatory);
	}

	@Override
	public void validate() throws RatingValidationException {
		if (rating == -1)
			throw new RatingValidationException(
					R.string.validation_error_no_total_rating);
	}

	@Override
	public void save(Context context) {
		
		Registry reg = Registry.getInstance();
		String isExpertString = reg
				.get("de.otaris.zertapps.privacychecker.appDetails.rateApp.expertMode",
						"isExpert");
		int isExpert = (isExpertString.equals("1")) ? 1 : 0;
		
		//TODO: get unecpected permissions for app rating and calculate:
		//totalrating*3/5 + ~criticality of unexpected permissions/48/*5*(2/5)
		//save this value as expert Rating
		//do same with non expert Rating
		//find good place for calculating average expert/non expert rating -> when opening detail view?
		
		
		RatingAppDataSource ratingAppData = new RatingAppDataSource(context);
		ratingAppData.open();

		
		// create new RatingApp
		ratingAppData.createRatingApp(rating, app.getId(), (isExpert == 1));

		ratingAppData.close();
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

}
