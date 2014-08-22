package de.otaris.zertapps.privacychecker.appDetails.rateApp.comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElement;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElementViewHelper;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.totalPrivacyRating.TotalPrivacyRating;

public class CommentViewHelper extends RatingElementViewHelper {

	@Override
	public View getView(Context context, ViewGroup parent,
			RatingElement ratingElement) {
		
		if (!(ratingElement instanceof Comment))
			throw new IllegalArgumentException(
					"Illegal Detail Object. Expected Comment.");
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(
				R.layout.app_detail_rate_app_comment, parent, false);		
	
		return rowView;
	}

}
