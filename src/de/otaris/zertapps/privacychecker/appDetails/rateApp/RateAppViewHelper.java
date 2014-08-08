package de.otaris.zertapps.privacychecker.appDetails.rateApp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.Detail;
import de.otaris.zertapps.privacychecker.appDetails.DetailViewHelper;
import de.otaris.zertapps.privacychecker.database.dataSource.AppPermissionDataSource;
import de.otaris.zertapps.privacychecker.database.dataSource.RatingAppDataSource;
import de.otaris.zertapps.privacychecker.database.dataSource.RatingPermissionDataSource;
import de.otaris.zertapps.privacychecker.database.model.Permission;
import de.otaris.zertapps.privacychecker.database.model.RatingApp;

public class RateAppViewHelper extends DetailViewHelper {

	@Override
	public View getView(Context context, ViewGroup parent, Detail detail)
			throws IllegalArgumentException {

		if (!(detail instanceof RateApp))
			throw new IllegalArgumentException(
					"Illegal Detail Object. Expected Rating.");

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.app_detail_rate_app, parent,
				false);
		
		Button button = (Button) rowView
				.findViewById(R.id.app_detail_rate_app_button);
		button.setTag(detail.getApp().getId());
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RelativeLayout overlay = (RelativeLayout) v.getRootView()
						.findViewById(R.id.app_detail_overlay);
				overlay.setVisibility(View.VISIBLE);
				LayoutInflater inflater = LayoutInflater.from(v.getContext());
				RelativeLayout layout = (RelativeLayout) inflater.inflate(
						R.layout.app_detail_rate_app_overlay, overlay, false);

				int appId = (Integer) v.getTag();
				AppPermissionDataSource appPermissionData = new AppPermissionDataSource(
						v.getRootView().getContext());
				appPermissionData.open();
				//get permissions for the app
				ArrayList<Permission> permissions = appPermissionData
						.getPermissionsByAppId(appId);

				overlay.addView(layout);
				List<TextView> permissionList = new ArrayList<TextView>();

				for (int i = 1; i <= (Math.min(5, permissions.size())); i++) {

					// set name and label for the permissions
					String attribute = "app_detail_rate_app_overlay_permission"
							+ i;

					String packageName = v.getContext().getPackageName();

					int resourceId = v.getResources().getIdentifier(attribute,
							"id", packageName);

					TextView permission = (TextView) v.getRootView()
							.findViewById(resourceId);
					permission.setVisibility(View.VISIBLE);
					permission.setText(permissions.get(i - 1).getLabel());
					permission.setTag(permissions.get(i - 1).getId());
					permissionList.add(permission);

					String attribute2 = "app_detail_rate_app_overlay_rating_"
							+ i;
					int resourceId2 = v.getResources().getIdentifier(
							attribute2, "id", packageName);

					layout.findViewById(resourceId2).setOnClickListener(
							new TotalRatingListener());
				}
				appPermissionData.close();

				Button sendButton = (Button) v.getRootView().findViewById(
						R.id.app_detail_rate_app_overlay_send);
				sendButton.setTag(appId);

				sendButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v1) {

						RatingAppDataSource ratingAppData = new RatingAppDataSource(
								v1.getContext());
						RatingPermissionDataSource ratingPermissionData = new RatingPermissionDataSource(
								v1.getContext());
						AppPermissionDataSource appPermissionData = new AppPermissionDataSource(
								v1.getContext());
						ratingAppData.open();
						ratingPermissionData.open();

						//to count how many locks are checked
						int count = 0;
						int appId = (Integer) v1.getTag();

						
						for (int i = 1; i <= 5; i++) {

							// get total rating
							String attribute = "app_detail_rate_app_overlay_rating_"
									+ i;

							String packageName = v1.getContext()
									.getPackageName();

							int resourceId10 = v1.getResources().getIdentifier(
									attribute, "id", packageName);

							ToggleButton button = (ToggleButton) v1
									.getRootView().findViewById(resourceId10);

							if (button.isChecked()) {
								count++;
							}

							// get rating for permission
							String attributePermission = "app_detail_rate_app_overlay_permission_radiogroup"
									+ i;
							int resourceId3 = v1.getResources().getIdentifier(
									attributePermission, "id", packageName);

							RadioGroup radioGroup = (RadioGroup) v1
									.getRootView().findViewById(resourceId3);
							int buttonId = radioGroup.getCheckedRadioButtonId();
							RadioButton radioButton = (RadioButton) v1
									.getRootView().findViewById(buttonId);
							Integer buttonTag = Integer.parseInt(radioButton
									.getTag().toString());

							// create RatingPermissions and therefore retrieve
							// permissions
							String attributePermission2 = "app_detail_rate_app_overlay_permission"
									+ i;
							int resourceId4 = v1.getResources().getIdentifier(
									attributePermission2, "id", packageName);
							TextView permission = (TextView) v1.getRootView()
									.findViewById(resourceId4);
							Integer permissionId = Integer.parseInt(permission
									.getTag().toString());

							int appPermissionId = appPermissionData
									.getAppPermissionByAppAndPermissionId(
											appId, permissionId).getId();

							// create new RatingPermission depending on the
							// checked radio button
							if (buttonTag % 2 == 0) {
								ratingPermissionData.createRatingPermission(0,
										appPermissionId, false);
							}

						}

						// create new RatingApp with default isExpert = false
						RatingApp newRatingApp = ratingAppData.createRatingApp(
								count, appId, false);

						ratingAppData.close();
						ratingPermissionData.close();
						appPermissionData.close();
					}

				});
			}

		});

		return rowView;
	}

}
