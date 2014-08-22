package de.otaris.zertapps.privacychecker.appDetails.rateApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.Detail;
import de.otaris.zertapps.privacychecker.appDetails.DetailViewHelper;
import de.otaris.zertapps.privacychecker.appDetails.RateAppOverlayOnClickListener;

/*
 * Inflates the layout containing the elements to rate a app.
 * Button "Jetzt Bewerten"
 * (click) -> open overlay "Rate App":
 * 				- Rate permissions
 * 				- Total Rating > locks
 * 				- Button "Senden"
 * 
 * (click) -> close overlay "Rate App"
 * 		   -> open overlay "Rating saved"
 * 				- Permission rating saved in database
 * 				- Lock rating saved in  database
 */
public class RateAppViewHelper extends DetailViewHelper {

	protected Button rateAppButton;

	protected void initializeViews(View contextView) {
		rateAppButton = (Button) contextView
				.findViewById(R.id.app_detail_rate_app_button);
	}

	@Override
	public View getView(Context context, ViewGroup parent, Detail detail) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.app_detail_rate_app, parent,
				false);

		initializeViews(rowView);

		rateAppButton.setTag(detail.getApp());
		rateAppButton.setOnClickListener(new RateAppOverlayOnClickListener());

		return rowView;
	}
}
