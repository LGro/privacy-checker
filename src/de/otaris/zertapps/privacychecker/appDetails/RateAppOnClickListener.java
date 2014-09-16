package de.otaris.zertapps.privacychecker.appDetails;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import de.otaris.zertapps.privacychecker.PrivacyCheckerAlert;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElement;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingValidationException;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.Registry;
import de.otaris.zertapps.privacychecker.database.dataSource.AppExtendedDataSource;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;
import de.otaris.zertapps.privacychecker.totalPrivacyRatingAlgorithm.TotalPrivacyRatingAlgorithm;
import de.otaris.zertapps.privacychecker.totalPrivacyRatingAlgorithm.TotalPrivacyRatingAlgorithmFactory;

/**
 * Processes all the validation and saving of each rating element. This is where
 * the complete Rating is created.
 */
public class RateAppOnClickListener implements OnClickListener {

	@Override
	public void onClick(View v) {
		// list to collect errors that occur through the validation process
		ArrayList<String> errors = new ArrayList<String>();

		// get all rating elements from registry
		Registry reg = Registry.getInstance();
		ArrayList<RatingElement> ratingElements = reg.getRatingElements();

		// iterate over all rating elements
		for (RatingElement element : ratingElements) {
			// validate and collect errors
			try {
				element.validate();
			} catch (RatingValidationException e) {
				errors.add((String) v.getResources().getText(
						e.getValidationErrorId()));
			}
		}

		// if errors occurred, display them
		if (errors.size() > 0) {
			String title = v.getResources()
					.getText(R.string.validation_error_title).toString();

			// get intro text
			String message = v.getResources().getText(
					R.string.validation_error_intro)
					+ "\n";

			// add error messages
			for (String error : errors)
				message += "- " + error + "\n";

			// display custom alert window
			PrivacyCheckerAlert.callInfoDialog(title, message, v.getContext(), false);
		} else {
			String title = v.getResources()
					.getText(R.string.rating_save_success_title).toString();

			// only if there have been no errors: iterate over all rating
			// elements again and save the data
			for (RatingElement element : ratingElements)
				element.save(v.getContext());

			// get current app from DB with newly saved ratings
			int appId = ratingElements.get(0).getApp().getId();
			AppExtendedDataSource appData = new AppExtendedDataSource(
					v.getContext());
			appData.open();
			AppExtended app = appData.getAppById(appId);
			// re-calculate weighted total privacy rating
			TotalPrivacyRatingAlgorithmFactory totalRatingFactory = new TotalPrivacyRatingAlgorithmFactory();
			TotalPrivacyRatingAlgorithm algo = totalRatingFactory
					.makeAlgorithm();

			// update app
			app.setPrivacyRating(algo.calculate(app));
			appData.update(app);
			appData.close();

//			// close overlay
//			((AppDetailsActivity) v.getContext()).hideOverlay(v);
//
//			// display custom success alert windows
			PrivacyCheckerAlert.callInfoDialog(
					title,
					v.getResources()
							.getText(R.string.app_detail_rate_app_success)
							.toString(), v.getContext(), true);
		}
	}
}
