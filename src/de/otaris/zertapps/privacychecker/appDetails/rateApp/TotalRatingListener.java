package de.otaris.zertapps.privacychecker.appDetails.rateApp;

import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;
import de.otaris.zertapps.privacychecker.R;

/**
 * handles clicks on locks for total rating by updating the images
 */
public class TotalRatingListener implements View.OnClickListener {

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
			String ratingIdentifierName = "app_detail_rate_app_overlay_rating_"
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
				button.setChecked(true);
				valueText.setText(ratingStringID);
				// all other buttons are unchecked
			} else {
				button.setChecked(false);
			}
		}
	}
}
