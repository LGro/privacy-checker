package de.otaris.zertapps.privacychecker.appDetails.rateApp;

import android.content.Context;
/**
 * abstract Class for all RatingElementView Helper
 */
import android.view.View;
import android.view.ViewGroup;

public abstract class RatingElementViewHelper {

	public abstract View getView(Context context, ViewGroup parent,
			RatingElement ratingElement);

}
