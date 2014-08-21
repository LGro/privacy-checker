package de.otaris.zertapps.privacychecker.appDetails.rateApp.totalPrivacyRating;

import android.content.Context;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElement;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingValidationException;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.Registry;
import de.otaris.zertapps.privacychecker.database.dataSource.AppPermissionDataSource;
import de.otaris.zertapps.privacychecker.database.dataSource.RatingAppDataSource;
import de.otaris.zertapps.privacychecker.database.dataSource.RatingPermissionDataSource;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;

public class TotalPrivacyRating extends RatingElement {

	protected int rating = -1;

	public TotalPrivacyRating(AppExtended app, boolean mandatory) {
		super(app, mandatory);
	}

	@Override
	public boolean validate() throws RatingValidationException {
		if (!mandatory)
			return true;

		if (rating == -1)
			throw new RatingValidationException(
					R.string.validation_error_no_total_rating);

		return true;
	}

	@Override
	public void save(Context context) {
		RatingAppDataSource ratingAppData = new RatingAppDataSource(context);
		RatingPermissionDataSource ratingPermissionData = new RatingPermissionDataSource(
				context);
		AppPermissionDataSource appPermissionData = new AppPermissionDataSource(
				context);

		ratingAppData.open();
		ratingPermissionData.open();
		appPermissionData.open();

		Registry reg = Registry.getInstance();
		int isExpert = 1;
		// Integer.getInteger(reg.get("de.otaris.zertapps.privacychecker.ExpertRating",
		// "isExpert"));

		// create new RatingApp with default isExpert = false
		ratingAppData.createRatingApp(rating, app.getId(), (isExpert == 1));

		ratingAppData.close();
		ratingPermissionData.close();
		appPermissionData.close();
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

}
