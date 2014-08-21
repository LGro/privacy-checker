package de.otaris.zertapps.privacychecker.appDetails.rateApp.totalPrivacyRating;

import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.Registry;

/**
 * handles clicks on locks for total rating by updating the images
 */
public class TotalPrivacyRatingListener implements View.OnClickListener {

	@Override
	public void onClick(View v) {

		// retrieve the tag of the selected button
		int tag = Integer.parseInt((String) v.getTag());
		// get the text element from the view
		TextView valueText = (TextView) v.getRootView().findViewById(
				R.id.app_detail_rate_app_value_text);
		valueText.setVisibility(View.VISIBLE);

		// sets the chosen locks green/white depending on the tag
		for (int i = 1; i <= 5; i++) {
			// programmatically get the id of the button for the current
			// iteration
			String packageName = v.getContext().getPackageName();
			String ratingIdentifierName = "app_detail_rate_app_total_rating_"
					+ i;
			int ratingIdentifierId = v.getResources().getIdentifier(
					ratingIdentifierName, "id", packageName);
			ToggleButton button = (ToggleButton) v.getRootView().findViewById(
					ratingIdentifierId);

			// programmatically get the id of the string for the current
			// iteration
			String ratingStringName = "app_detail_rate_app_value_" + i;
			int ratingStringID = v.getResources().getIdentifier(
					ratingStringName, "string", packageName);

			// set buttons tag-1 checked
			if (i < tag) {
				button.setChecked(true);
				// set tag button checked + modify text
			} else if (i == tag) {
				// save rating in ratingElement from registry
				Registry reg = Registry.getInstance();
				TotalPrivacyRating ratingElement = (TotalPrivacyRating) reg
						.getRatingElement(TotalPrivacyRating.class);
				ratingElement.setRating(i);
				reg.updateRatingElement(TotalPrivacyRating.class, ratingElement);

				button.setChecked(true);
				valueText.setText(ratingStringID);
			} else {
				// all other buttons are unchecked
				button.setChecked(false);
			}
		}
	}
}
