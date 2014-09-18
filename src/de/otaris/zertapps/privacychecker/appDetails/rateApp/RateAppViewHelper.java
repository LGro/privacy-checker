package de.otaris.zertapps.privacychecker.appDetails.rateApp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.Detail;
import de.otaris.zertapps.privacychecker.appDetails.DetailViewHelper;
import de.otaris.zertapps.privacychecker.appDetails.RateAppOverlayOnClickListener;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;

/**
 * Displays a button to rate the privacy friendliness of a given app.
 * 
 * Button click opens a new overlay by using the RateAppOverlayOnClickListener.
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
//		rateAppButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// call rate app activity
//				Intent intent = new Intent(v.getContext(),
//						RateAppActivity.class);
//				intent.putExtra("AppExtended", (AppExtended) v.getTag());
//				v.getContext().startActivity(intent);
//			}
//		});
		return rowView;
	}
}
