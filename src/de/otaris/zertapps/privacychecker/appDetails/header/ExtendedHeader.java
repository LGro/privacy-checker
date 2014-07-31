package de.otaris.zertapps.privacychecker.appDetails.header;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.RatingController;
import de.otaris.zertapps.privacychecker.database.interfaces.App;

public class ExtendedHeader implements Header {

	@Override
	public View getView(Activity activity, App app)
			throws IllegalArgumentException {

		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.app_detail_header, null);

		rowView.setId(170892);

		// Get all the views ...
		TextView nameView = (TextView) rowView
				.findViewById(R.id.app_detail_header_name);
		TextView developerView = (TextView) rowView
				.findViewById(R.id.app_detail_header_developer);
		ImageView ratingView = (ImageView) rowView
				.findViewById(R.id.app_detail_header_rating_pic);
		ImageView ratingViewPS = (ImageView) rowView
				.findViewById(R.id.app_detail_header_ps_rating_image);
		ImageView iconView = (ImageView) rowView
				.findViewById(R.id.app_detail_header_icon);
		Button buttonInstall = (Button) rowView
				.findViewById(R.id.app_detail_header_button_install);
		Button buttonPs = (Button) rowView
				.findViewById(R.id.app_detail_header_button_ps);
		TextView psRatingAmount = (TextView) rowView
				.findViewById(R.id.app_detail_header_ps_rating_amount);
		TextView privacyRatingAmount = (TextView) rowView
				.findViewById(R.id.app_detail_header_privacy_rating_amount);

		// ... and fill them with the right information about the app.
		// Set icon, button and rating.
		if (app.isInstalled()) {
			buttonInstall.setText("Deinstallieren");
			try {
				iconView.setImageDrawable(activity.getPackageManager()
						.getApplicationIcon(app.getName()));

			} catch (NameNotFoundException e) {
				Log.w("AppListItemAdapter",
						"Couldn't load icons for app: " + e.getMessage());
			}
		} else {
			buttonInstall.setText("Installieren");
			// TODO: implement (get icon from PlayStore API?!)
		}

		// Set the icons for locks and stars according to their amount
		ratingView.setImageResource(new RatingController()
				.getIconRatingLocks(app.getPrivacyRating()));
		ratingViewPS.setImageResource(new RatingController()
				.getIconRatingStars(app.getFunctionalRating()));

		// Set name and developer
		nameView.setText(app.getLabel());
		developerView.setText(app.getName());

		return rowView;
	}

}
