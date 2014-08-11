package de.otaris.zertapps.privacychecker.appDetails.header;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import de.otaris.zertapps.privacychecker.IconController;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.RatingController;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;

/**
 * specific Header implementation to display:
 * <ul>
 * <li>app icon</li>
 * <li>app name</li>
 * <li>developer</li>
 * <li>privacy rating</li>
 * <li>funcational/PlayStore rating</li>
 * <li>install/uninstall button</li>
 * <li>"to Playstore" button</li>
 * </ul>
 */
public class ExtendedHeader implements Header {
	
	private void initializeGuiElements() {
		// TODO ????
	}

	@Override
	public View getView(Activity activity, AppExtended app)
			throws IllegalArgumentException {

		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.app_detail_header, null);

		// set some id
		// TODO: Is there a way of doing this so there is nExtendedHeadero conflict possible?
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
		// TODO: where is this information located? where to get from?
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
			iconView.setImageBitmap(IconController.byteArrayToBitmap(app
					.getIcon()));
		}

		buttonPs.setTag(app);
		buttonPs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppExtended app = (AppExtended) v.getTag();
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id=" + app.getName()));
				v.getContext().startActivity(intent);
			}
		});

		// Set the icons for locks and stars according to their amount
		ratingView.setImageResource(new RatingController()
				.getIconRatingLocks(app.getPrivacyRating()));
		ratingViewPS.setImageResource(new RatingController()
				.getIconRatingStars(app.getFunctionalRating()));
		int totalNumberOfPrivacyRatings = app.getNonExpertRating().size()
				+ app.getExpertRating().size();
		privacyRatingAmount.setText(totalNumberOfPrivacyRatings + "");

		// Set name and developer
		nameView.setText(app.getLabel());
		developerView.setText(app.getName());

		return rowView;
	}
}
