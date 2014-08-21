package de.otaris.zertapps.privacychecker.appDetails;

import java.util.ArrayList;

import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElement;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingValidationException;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.Registry;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Processes all the validation and saving of each rating element. This is where
 * the complete Rating is created.
 */
public class RateAppOnClickListener implements OnClickListener {

	/**
	 * Custom Alert dialog in privacy-checker style.
	 * 
	 * TODO: externalize in a static way?
	 * 
	 * @param message
	 * @param context
	 */
	protected void callAlert(String message, final Context context) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.app_detail_alert_dialog);
		dialog.getWindow()
				.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

		TextView tvTitle = (TextView) dialog
				.findViewById(R.id.app_detail_alert_dialog_textview_title);
		// TODO: externalize to string
		tvTitle.setText("Privacy Checker Hinweis");

		TextView tvText = (TextView) dialog
				.findViewById(R.id.app_detail_alert_dialog_textview_description);
		tvText.setText(message);

		Button buttonOk = (Button) dialog
				.findViewById(R.id.app_detail_alert_dialog_button_ok);
		buttonOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

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
			// get intro text
			String title = v.getResources().getText(
					R.string.validation_error_intro)
					+ "\n";

			// add error messages
			for (String error : errors)
				title += "- " + error + "\n";

			// display custom alert window
			callAlert(title, v.getContext());
		} else {
			// only if there have been no errors: iterate over all rating
			// elements again and save the data
			for (RatingElement element : ratingElements)
				element.save(v.getContext());

			// close overlay
			((AppDetailsActivity) v.getContext()).hideOverlay(v);

			// display custom success alert windows
			callAlert(
					(String) v.getResources().getText(
							R.string.app_detail_rate_app_success),
					v.getContext());
		}
	}
}
