package de.otaris.zertapps.privacychecker.appDetails.rateApp.totalPrivacyRating;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElement;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElementViewHelper;

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

		// initializeViews(rowView);

		// loop over locks to set TotalRatingListener
		for (int i = 1; i <= 5; i++) {

			String packageName = context.getPackageName();
			String ratingIdentifierName = "app_detail_rate_app_total_rating_"
					+ i;
			// retrieve id of the lock
			int ratingIdentifierId = context.getResources().getIdentifier(
					ratingIdentifierName, "id", packageName);

			rowView.findViewById(ratingIdentifierId).setOnClickListener(
					new TotalRatingListener());
		}

		return rowView;
	}
}
