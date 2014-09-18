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
 * <li>"to PlayStore" button</li>
 * </ul>
 */
public class ExtendedHeader extends Header {

	protected TextView appNameView;
	protected TextView developerTextView;
	protected ImageView privacyRatingImageView;
	protected ImageView playStoreRatingImageView;
	protected ImageView appIconImageView;
	protected Button installUninstallButton;
	protected Button viewInPlayStoreButton;
	// TODO: this is currently not used; create attribute for app in database
	protected TextView playStoreRatingAmountTextView;
	protected TextView privacyRatingAmountTextView;

	protected void initializeViews(View contextView) {
		appNameView = (TextView) contextView
				.findViewById(R.id.app_detail_header_name);
		developerTextView = (TextView) contextView
				.findViewById(R.id.app_detail_header_developer);
		privacyRatingImageView = (ImageView) contextView
				.findViewById(R.id.app_detail_header_rating_pic);
		playStoreRatingImageView = (ImageView) contextView
				.findViewById(R.id.app_detail_header_ps_rating_image);
		appIconImageView = (ImageView) contextView
				.findViewById(R.id.app_detail_header_icon);
		installUninstallButton = (Button) contextView
				.findViewById(R.id.app_detail_header_button_install);
		viewInPlayStoreButton = (Button) contextView
				.findViewById(R.id.app_detail_header_button_ps);
		playStoreRatingAmountTextView = (TextView) contextView
				.findViewById(R.id.app_detail_header_ps_rating_amount);
		privacyRatingAmountTextView = (TextView) contextView
				.findViewById(R.id.app_detail_header_privacy_rating_amount);
	}

	@Override
	public View getView(Activity activity, AppExtended app)
			throws IllegalArgumentException {

		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.app_detail_header, null);

		// set some id
		// TODO: Is there a way of doing this so there is nExtendedHeadero
		// conflict possible?
		rowView.setId(170892);

		initializeViews(rowView);

		// has to be selected to scroll if the text is too long
		appNameView.setSelected(true);

		// ... and fill them with the right information about the app.
		// Set icon, button and rating.
		if (app.isInstalled()) {
			// TODO: if the app is not installed, the button is not visible.
			// Remove once the deinstall feature is implemented.
			// installUninstallButton.setText("Deinstallieren");
			installUninstallButton.setVisibility(View.GONE);
			try {
				// if installed, get the image from the device
				appIconImageView.setImageDrawable(activity.getPackageManager()
						.getApplicationIcon(app.getName()));

			} catch (NameNotFoundException e) {
				Log.w("AppListItemAdapter",
						"Couldn't load icons for app: " + e.getMessage());
			}
		} else {
			installUninstallButton.setText("Installieren");
			// if not installe,d get the image from the database
			appIconImageView.setImageBitmap(IconController
					.byteArrayToBitmap(app.getIcon()));
		}

		viewInPlayStoreButton.setTag(app);
		viewInPlayStoreButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppExtended app = (AppExtended) v.getTag();
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id=" + app.getName()));
				v.getContext().startActivity(intent);
			}
		});

		// Set the icons for locks and stars according to their amount
		privacyRatingImageView.setImageResource(new RatingController()
				.getIconRatingLocks(app.getPrivacyRating()));
		playStoreRatingImageView.setImageResource(new RatingController()
				.getIconRatingStars(app.getFunctionalRating()));
		int totalNumberOfPrivacyRatings = app.getNonExpertRating().size()
				+ app.getExpertRating().size();
		privacyRatingAmountTextView.setText(totalNumberOfPrivacyRatings + "");

		// Set name and developer
		appNameView.setText(app.getLabel());
		// TODO: set developer
		developerTextView.setText("");

		return rowView;
	}
}
