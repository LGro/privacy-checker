package de.otaris.zertapps.privacychecker.appDetails;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElement;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingValidationErrorException;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingValidationWarningException;
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
		// list to collect errors and warnings that occur through the validation
		// process
		ArrayList<String> errors = new ArrayList<String>();
		ArrayList<String> warnings = new ArrayList<String>();

		// get all rating elements from registry
		Registry reg = Registry.getInstance();
		ArrayList<RatingElement> ratingElements = reg.getRatingElements();

		// iterate over all rating elements
		for (RatingElement element : ratingElements) {
			// validate and collect errors
			try {
				element.validate();
			} catch (RatingValidationErrorException e) {
				errors.add((String) v.getResources().getText(
						e.getValidationErrorId()));
			} catch (RatingValidationWarningException e) {
				warnings.add((String) v.getResources().getText(
						e.getValidationWarningId()));
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
			callInfoDialog(title, message, v.getContext(), false);

		} else if (warnings.size() > 0) {
			String title = v.getResources()
					.getText(R.string.validation_error_title).toString();

			// get intro text
			String message = v.getResources().getText(
					R.string.validation_warning_intro)
					+ "\n";

			// add warning messages
			for (String warning : warnings)
				message += "- " + warning + "\n";

			message += "\n"
					+ v.getResources().getText(
							R.string.validation_warning_continue);

			// display custom alert window
			callWarningDialog(title, message, v.getContext());
		} else {
			// only if there have been no errors...

			// display custom success alert windows
			callInfoDialog(
					v.getResources()
							.getText(R.string.rating_save_success_title)
							.toString(),
					v.getResources()
							.getText(R.string.app_detail_rate_app_success)
							.toString(), v.getContext(), true);
		}
	}

	/**
	 * displays info dialog with ok button and the ability to save
	 * ratingelements and close rating activity
	 * 
	 * @param title
	 * @param message
	 * @param context
	 * @param saveAndFinishActivity
	 */
	private void callInfoDialog(String title, String message,
			final Context context, final boolean saveAndFinishActivity) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.app_detail_alert_dialog);
		dialog.getWindow()
				.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

		TextView titleTextView = (TextView) dialog
				.findViewById(R.id.app_detail_alert_dialog_textview_title);
		titleTextView.setText(title);

		TextView messageTextview = (TextView) dialog
				.findViewById(R.id.app_detail_alert_dialog_textview_description);
		messageTextview.setText(message);

		Button okButton = (Button) dialog
				.findViewById(R.id.app_detail_alert_dialog_button_ok);
		okButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				if (context instanceof Activity && saveAndFinishActivity) {
					// get all rating elements from registry
					Registry reg = Registry.getInstance();
					ArrayList<RatingElement> ratingElements = reg
							.getRatingElements();

					for (RatingElement element : ratingElements)
						element.save(v.getContext());

					// get current app from DB with newly saved ratings
					int appId = ratingElements.get(0).getApp().getId();
					AppExtendedDataSource appData = new AppExtendedDataSource(v
							.getContext());
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

					((Activity) context).finish();
				}
			}
		});
		dialog.show();
	}

	/**
	 * displays warning with the possibility to return and continue rating or
	 * submit rating and close rating activity
	 * 
	 * @param title
	 * @param message
	 * @param context
	 */
	private void callWarningDialog(String title, String message,
			final Context context) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.app_detail_warning_dialog);
		dialog.getWindow()
				.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

		TextView titleTextView = (TextView) dialog
				.findViewById(R.id.app_detail_warning_alert_dialog_textview_title);
		titleTextView.setText(title);

		TextView messageTextview = (TextView) dialog
				.findViewById(R.id.app_detail_warning_alert_dialog_textview_description);
		messageTextview.setText(message);

		Button yesButton = (Button) dialog
				.findViewById(R.id.app_detail_warning_alert_dialog_button_yes);
		Button noButton = (Button) dialog
				.findViewById(R.id.app_detail_warning_alert_dialog_button_no);

		// close dialog and continue rating
		noButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}

		});

		// close dialog and submit rating
		yesButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();

				callInfoDialog(
						v.getResources()
								.getText(R.string.rating_save_success_title)
								.toString(),
						v.getResources()
								.getText(R.string.app_detail_rate_app_success)
								.toString(), context, true);
			}

		});
		dialog.show();
	}
}
