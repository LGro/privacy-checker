package de.otaris.zertapps.privacychecker.appDetails.rateApp.totalPrivacyRating;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElement;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElementViewHelper;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.Registry;

/**
 * handles display of total rating elements including 5 locks and display of
 * rating-description if locks are set
 * 
 */

public class TotalPrivacyRatingViewHelper extends RatingElementViewHelper {

	@Override
	public View getView(Context context, ViewGroup parent,
			RatingElement ratingElement) throws IllegalArgumentException {

		if (!(ratingElement instanceof TotalPrivacyRating))
			throw new IllegalArgumentException(
					"Illegal Detail Object. Expected Total Privacy Rating.");
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(
				R.layout.app_detail_rate_app_total_rating, parent, false);

		// loop over locks to set TotalRatingListener
		for (int i = 1; i <= 5; i++) {

			String packageName = context.getPackageName();
			String ratingIdentifierName = "app_detail_rate_app_total_rating_"
					+ i;
			// retrieve id of the lock
			int ratingIdentifierId = context.getResources().getIdentifier(
					ratingIdentifierName, "id", packageName);

			// rowView.findViewById(ratingIdentifierId).setOnClickListener(
			// new TotalPrivacyRatingListener());

			ToggleButton lock = (ToggleButton) rowView
					.findViewById(ratingIdentifierId);
			lock.setOnClickListener(new TotalPrivacyRatingListener());

			// get value for locks from Registry
			Registry reg = Registry.getInstance();
			TotalPrivacyRating totalRating = (TotalPrivacyRating) reg
					.getRatingElement(TotalPrivacyRating.class);

			// set locks green if they have already been chosen
			if (i <= totalRating.getRating()) {
				lock.setChecked(true);
				// set text for current rating
				if (i == totalRating.getRating()) {
					String ratingStringName = "app_detail_rate_app_value_" + i;
					int ratingStringID = context.getResources().getIdentifier(
							ratingStringName, "string", packageName);
					TextView valueText = (TextView) rowView.getRootView()
							.findViewById(R.id.app_detail_rate_app_value_text);
					valueText.setText(ratingStringID);
					valueText.setVisibility(ViewGroup.VISIBLE);
				}
			}
		}

		return rowView;
	}
}
