package de.otaris.zertapps.privacychecker.appDetails.privacyRating;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.RatingController;
import de.otaris.zertapps.privacychecker.UserStudyLogger;
import de.otaris.zertapps.privacychecker.appDetails.Detail;
import de.otaris.zertapps.privacychecker.appDetails.DetailViewHelper;
import de.otaris.zertapps.privacychecker.appDetails.LoggingShowMore;
import de.otaris.zertapps.privacychecker.appDetails.permissions.PermissionsListItemAdapter;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;
import de.otaris.zertapps.privacychecker.database.model.Permission;

public class PrivacyRatingViewHelper extends DetailViewHelper {

	private double round(float f) {
		return (java.lang.Math.round(f * 10) / 10.0);
	}

	@Override
	public View getView(Context context, ViewGroup parent, Detail detail)
			throws IllegalArgumentException {
		if (!(detail instanceof PrivacyRating))
			throw new IllegalArgumentException(
					"Illegal Detail Object. Expected Rating.");

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.app_detail_privacy_rating,
				parent, false);

		// TextView textView = (TextView) rowView
		// .findViewById(R.id.app_detail_list_item_name);
		// get permissions list from permissions detail
		PrivacyRating rating = (PrivacyRating) detail;
		AppExtended app = rating.getApp();
		TextView privacyRatingTextView = (TextView) rowView
				.findViewById(R.id.app_detail_privacy_rating_value);

		privacyRatingTextView.setText(round(app.getRating()) + "");

		TextView privacyRatingCount = (TextView) rowView
				.findViewById(R.id.app_detail_privacy_rating_count);
		privacyRatingCount.setText("("
				+ (app.getNonExpertRating().size() + app.getExpertRating()
						.size()) + ")");

		TextView automaticRating = (TextView) rowView
				.findViewById(R.id.app_detail_privacy_rating_automatic_text);
		automaticRating.setText(round(app.getAutomaticRating()) + "");

		TextView nonExpertRating = (TextView) rowView
				.findViewById(R.id.app_detail_privacy_rating_nonexpert_text);
		nonExpertRating.setText(round(app.getTotalNonExpertRating()) + " ("
				+ app.getNonExpertRating().size() + ")");

		TextView expertRating = (TextView) rowView
				.findViewById(R.id.app_detail_privacy_rating_expert_text);
		expertRating.setText(round(app.getTotalExpertRating()) + " ("
				+ app.getExpertRating().size() + ")");

		ImageView privacyRatingIcon = (ImageView) rowView
				.findViewById(R.id.app_detail_privacy_rating_image);
		privacyRatingIcon.setImageResource(new RatingController()
				.getIconRatingLocks(app.getPrivacyRating()));

		List<Permission> permissionList = app.getPermissionList();

		// get permission list view
		ListView listView = (ListView) rowView
				.findViewById(R.id.app_detail_rating_permissions_list);

		// add list item adapter for permissions
		listView.setAdapter(new PermissionsListItemAdapter(context,
				permissionList));
		listView.setScrollContainer(false);

		// scale list depending on its size
		ViewGroup.LayoutParams updatedLayout = listView.getLayoutParams();
		final float scale = context.getResources().getDisplayMetrics().density;
		int pixels = (int) (49 * scale);
		updatedLayout.height = pixels * listView.getCount();
		listView.setLayoutParams(updatedLayout);

		// set click listener for list items
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// get overlay from details fragment
				RelativeLayout overlay = (RelativeLayout) parent.getRootView()
						.findViewById(R.id.app_detail_overlay);

				// show overlay
				overlay.setVisibility(View.VISIBLE);

				// inflate specific permissions overlay layout from xml
				LayoutInflater inflater = LayoutInflater.from(parent
						.getContext());
				RelativeLayout layout = (RelativeLayout) inflater.inflate(
						R.layout.app_detail_rating_permission_overlay, overlay,
						false);

				// get previously selected permission that need to be displayed
				Permission permission = (Permission) parent
						.getItemAtPosition(position);

				// set permission label
				TextView permissionLabelText = (TextView) layout
						.findViewById(R.id.app_detail_rating_permission_name);
				permissionLabelText.setText(permission.getLabel());

				// set permission description
				TextView permissionDescriptionText = (TextView) layout
						.findViewById(R.id.app_detail_rating_permission_description);
				permissionDescriptionText.setText(permission.getDescription());

				// add view to overlay
				overlay.addView(layout);

				// log
				UserStudyLogger.getInstance().log(
						"overlay_permission-" + permission.getName());
			}
		});

		// get "show more" button
		ToggleButton showMoreButton = (ToggleButton) rowView
				.findViewById(R.id.app_detail_privacy_rating_more);
		// set click listener
		abstract class LoggingOnCheckedChangeListener extends LoggingShowMore {
		}
		showMoreButton
				.setOnCheckedChangeListener(new LoggingOnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton toggleButton,
							boolean isChecked) {
						super.onCheckedChanged(toggleButton, isChecked);

						// get explanation text view
						TextView explanation = (TextView) ((View) toggleButton
								.getParent())
								.findViewById(R.id.app_detail_privacy_rating_explanation);

						// get permissions list
						ListView permissions = (ListView) ((View) toggleButton
								.getParent())
								.findViewById(R.id.app_detail_rating_permissions_list);

						if (isChecked) {
							// show explanation and permissions list
							permissions.setVisibility(View.VISIBLE);
							explanation.setVisibility(View.VISIBLE);
						} else {
							// hide explanation and permissions list
							permissions.setVisibility(View.GONE);
							explanation.setVisibility(View.GONE);
						}

					}
				});

		return rowView;
	}
}
