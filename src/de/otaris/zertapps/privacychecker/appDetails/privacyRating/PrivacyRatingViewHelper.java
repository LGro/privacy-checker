package de.otaris.zertapps.privacychecker.appDetails.privacyRating;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import de.otaris.zertapps.privacychecker.PrivacyCheckerAlert;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.RatingController;
import de.otaris.zertapps.privacychecker.appDetails.Detail;
import de.otaris.zertapps.privacychecker.appDetails.DetailViewHelper;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;
import de.otaris.zertapps.privacychecker.database.model.Permission;

/**
 * Displays the total privacy rating and its three components (automatic,
 * non-expert and expert rating).
 * 
 * After clicking a "show more" button to extend the detail view, an explanation
 * of the privacy rating and a list of permissions is shown.
 * 
 * When the user selects a permission from this list, an overlay displaying the
 * permission's label and explanation is shown.
 *
 */
public class PrivacyRatingViewHelper extends DetailViewHelper {

	protected TextView privacyRatingTextView;
	protected TextView privacyRatingCountTextView;
	protected TextView automaticRatingTextView;
	protected TextView nonExpertRatingTextView;
	protected TextView expertRatingTextView;
	protected ImageView privacyRatingIconTextView;
	protected ListView permissionListView;

	/**
	 * initialize all relevant views
	 * 
	 * @param contextView
	 *            view that wraps the relevant subviews
	 */
	protected void initializeViews(View contextView) {
		privacyRatingTextView = (TextView) contextView
				.findViewById(R.id.app_detail_privacy_rating_value);
		privacyRatingCountTextView = (TextView) contextView
				.findViewById(R.id.app_detail_privacy_rating_count);
		automaticRatingTextView = (TextView) contextView
				.findViewById(R.id.app_detail_privacy_rating_automatic_text);
		nonExpertRatingTextView = (TextView) contextView
				.findViewById(R.id.app_detail_privacy_rating_nonexpert_text);
		expertRatingTextView = (TextView) contextView
				.findViewById(R.id.app_detail_privacy_rating_expert_text);
		privacyRatingIconTextView = (ImageView) contextView
				.findViewById(R.id.app_detail_privacy_rating_image);
		permissionListView = (ListView) contextView
				.findViewById(R.id.app_detail_rating_permissions_list);
	}

	private double roundToOneDecimalPlace(float f) {
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

		initializeViews(rowView);

		// privacy rating
		PrivacyRating rating = (PrivacyRating) detail;
		AppExtended app = rating.getApp();
		privacyRatingTextView.setText(roundToOneDecimalPlace(app
				.getPrivacyRating()) + "");

		// privacy rating count
		privacyRatingCountTextView.setText("("
				+ (app.getNonExpertRating().size() + app.getExpertRating()
						.size()) + ")");

		// automatic rating
		automaticRatingTextView.setText(roundToOneDecimalPlace(app
				.getAutomaticRating()) + "");

		// non-expert rating
		nonExpertRatingTextView.setText(roundToOneDecimalPlace(app
				.getTotalNonExpertRating())
				+ " ("
				+ app.getNonExpertRating().size() + ")");

		// expert rating
		expertRatingTextView.setText(roundToOneDecimalPlace(app
				.getTotalExpertRating())
				+ " ("
				+ app.getExpertRating().size()
				+ ")");

		// privacy rating locks-icon
		privacyRatingIconTextView.setImageResource(new RatingController()
				.getIconRatingLocks(app.getPrivacyRating()));

		List<Permission> permissionList = app.getPermissionList();

		// add list item adapter for permissions
		permissionListView.setAdapter(new PermissionsListItemAdapter(context,
				permissionList));
		permissionListView.setScrollContainer(false);

		// scale list depending on its size
		ViewGroup.LayoutParams updatedLayout = permissionListView
				.getLayoutParams();
		int pixels = (int) (49 * context.getResources().getDisplayMetrics().density);
		updatedLayout.height = pixels * permissionListView.getCount();
		permissionListView.setLayoutParams(updatedLayout);

		// set click listener for list items
		permissionListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// get previously selected permission that need to be displayed
				Permission permission = (Permission) parent
						.getItemAtPosition(position);

				// display permission as alert dialog
				PrivacyCheckerAlert.callInfoDialog(permission.getLabel(),
						permission.getDescription(), view.getContext());
			}
		});

		// get "show more" button
		ToggleButton showMoreButton = (ToggleButton) rowView
				.findViewById(R.id.app_detail_privacy_rating_more);
		// set click listener
		showMoreButton
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton toggleButton,
							boolean isChecked) {

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
