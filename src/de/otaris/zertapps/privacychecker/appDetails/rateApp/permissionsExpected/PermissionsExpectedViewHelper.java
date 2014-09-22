package de.otaris.zertapps.privacychecker.appDetails.rateApp.permissionsExpected;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import de.otaris.zertapps.privacychecker.PrivacyCheckerAlert;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElement;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElementViewHelper;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;
import de.otaris.zertapps.privacychecker.database.model.Permission;

/**
 * handles display of all permission-rating elements including description for
 * the user what to do list of permission + checkbox
 */
public class PermissionsExpectedViewHelper extends RatingElementViewHelper {

	protected TextView explanation;
	protected ListView permissionsList;
	protected ToggleButton showMoreButton;
	protected RelativeLayout permissionsGroup;
	protected int numberOfPermissionsToShow;

	/**
	 * initialize all relevant views
	 * 
	 * @param contextView
	 *            view that wraps the relevant subviews
	 */
	protected void initializeViews(View contextView) {

		explanation = (TextView) contextView
				.findViewById(R.id.app_detail_rate_app_permissions_explanation);

		permissionsList = (ListView) contextView
				.findViewById(R.id.app_detail_rate_app_permissions_list);

		showMoreButton = (ToggleButton) contextView
				.findViewById(R.id.app_detail_rate_app_permissions_more);

		permissionsGroup = (RelativeLayout) contextView
				.findViewById(R.id.app_detail_rate_app_permissions_group);

	}

	@Override
	public View getView(Context context, ViewGroup parent,
			RatingElement ratingElement) {

		if (!(ratingElement instanceof PermissionsExpected))
			throw new IllegalArgumentException(
					"Illegal Detail Object. Expected PermissionsRating.");

		AppExtended app = ratingElement.getApp();

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(
				R.layout.app_detail_rate_app_permissions, parent, false);

		initializeViews(rowView);

		// dont show permissionsGroup if there are no permissions
		if (app.getPermissionList().isEmpty()) {
			permissionsGroup.setVisibility(ViewGroup.GONE);
			explanation
					.setText(R.string.app_details_privacy_rating_permissions_title_no_permissions);

		} else {
			// set adapter
			PermissionsExpectedItemAdapter adapter = new PermissionsExpectedItemAdapter(
					context, app.getPermissionList());
			permissionsList.setAdapter(adapter);

			// set onclick behavior for permissions list
			permissionsList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Log.i("PermissionExpectedViewHelper", "clicked permission");
					// get previously selected permission that need to be
					// displayed
					Permission permission = (Permission) parent
							.getItemAtPosition(position);

					// display permission as alert dialog
					PrivacyCheckerAlert.callPermissionDialogPermission(
							permission, view.getContext());
				}
			});

			// only show 5 permissions to rate, even if there are more available
			if (app.getPermissionList().size() > 5) {
				// set maximum number of comments to 5
				numberOfPermissionsToShow = 5;

				showMoreButton
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton showMoreButton,
									boolean isChecked) {
								ListView listView = (ListView) showMoreButton
										.getRootView()
										.findViewById(
												R.id.app_detail_rate_app_permissions_list);

								PermissionsExpectedItemAdapter adapter = (PermissionsExpectedItemAdapter) listView
										.getAdapter();

								if (isChecked) {
									// show total List of comments
									setListViewHeigthBasedOnChildren(listView,
											adapter.getCount());
								} else {
									// show the first comments
									setListViewHeigthBasedOnChildren(listView,
											numberOfPermissionsToShow);
								}
							}
						});

			} else {
				// there are 5 or less permissions
				numberOfPermissionsToShow = app.getPermissionList().size();
				showMoreButton.setVisibility(View.GONE);
			}
			// show the first comments
			setListViewHeigthBasedOnChildren(permissionsList,
					numberOfPermissionsToShow);
		}

		return rowView;
	}

	/**
	 * Scale a listView depending on the number of elements you want to display.
	 * 
	 * @param listView
	 * @param numberOfElements
	 *            the number of list elements to be displayed at maximum
	 */
	private void setListViewHeigthBasedOnChildren(ListView listView,
			int numberOfElements) {

		// get adapter from list view
		PermissionsExpectedItemAdapter adapter = (PermissionsExpectedItemAdapter) listView
				.getAdapter();
		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.AT_MOST);

		// accumulate total height by measuring each list element
		for (int i = 0; i < numberOfElements; i++) {
			View listItem = adapter.getView(i, null, listView);
			listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}

		// update list layout to maximum height
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (adapter.getCount() - 1));
		listView.setLayoutParams(params);

		// notify list view about layout changes
		listView.requestLayout();
	}
}
