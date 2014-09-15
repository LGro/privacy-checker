package de.otaris.zertapps.privacychecker.appDetails.rateApp.permissionsExpected;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
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

		ListView permissionsList = (ListView) rowView
				.findViewById(R.id.app_detail_rate_app_permissions_list);

		ToggleButton showMoreButton = (ToggleButton) rowView
				.findViewById(R.id.app_detail_rate_app_permissions_more);

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
				// get previously selected permission that need to be displayed
				Permission permission = (Permission) parent
						.getItemAtPosition(position);

				// display permission as alert dialog
				PrivacyCheckerAlert.callPermissionDialogPermission(permission,
						view.getContext());
			}
		});

		showMoreButton
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton showMoreButton,
							boolean isChecked) {
						ListView listView = (ListView) showMoreButton
								.getRootView()
								.findViewById(
										R.id.app_detail_rate_app_permissions_list);
						ViewGroup.LayoutParams updatedLayout = listView
								.getLayoutParams();
						final float scale = showMoreButton.getRootView()
								.getContext().getResources()
								.getDisplayMetrics().density;
						int pixels = (int) (49 * scale);

						if (isChecked) {
							updatedLayout.height = pixels * listView.getCount();
							listView.setLayoutParams(updatedLayout);
						} else {
							// default: show 4 permissions
							updatedLayout.height = pixels * 4;
							listView.setLayoutParams(updatedLayout);
						}
					}
				});

		return rowView;
	}

}
