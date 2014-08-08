package de.otaris.zertapps.privacychecker.appDetails.rateApp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.AppDetailsActivity;
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
				// get permissions for the app
				ArrayList<Permission> permissions = appPermissionData
						.getPermissionsByAppId(appId);
				overlay.addView(layout);

				// Fill the listview with permissions
				ListView permissionListView = (ListView) v.getRootView()
						.findViewById(R.id.app_detail_rate_app_overlay_list);
				RateAppListItemAdapter raliAdapter = new RateAppListItemAdapter(
						v.getContext(), v.getContext().getPackageManager(),
						permissions);
				permissionListView.setAdapter(raliAdapter);
				
				/*
				 * scale list depending on its size,
				 * but only if there are less than 5 items
				 */
				if(permissions.size() < 5){
					ViewGroup.LayoutParams updatedLayout = permissionListView.getLayoutParams();
					final float scale = v.getContext().getResources().getDisplayMetrics().density;
					int pixels = (int) (40 * scale);
					updatedLayout.height = pixels * permissionListView.getCount();
					permissionListView.setLayoutParams(updatedLayout);
				}
				

				// loop over locks to set TotalRatingListener
				for (int i = 1; i <= 5; i++) {

					String packageName = v.getContext().getPackageName();
					String attribute2 = "app_detail_rate_app_overlay_rating_"
							+ i;
					int ratingIdentifierId = v.getResources().getIdentifier(
							attribute2, "id", packageName);

					layout.findViewById(ratingIdentifierId).setOnClickListener(
							new TotalRatingListener());
				}
				appPermissionData.close();

				// functionality and design of overlay after pushing the send
				// button
				Button sendButton = (Button) v.getRootView().findViewById(
						R.id.app_detail_rate_app_overlay_send);
				sendButton.setTag(appId);

				/*
				 * "Senden"-button logic
				 */
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
						appPermissionData.open();

						/*
						 * Count locks for the rating.
						 */
						int count = 0;
						int appId = (Integer) v1.getTag();
						String packageName = v1.getContext().getPackageName();

						// loop over locks to retrieve app rating
						for (int i = 1; i <= 5; i++) {

							// get total rating
							String attribute = "app_detail_rate_app_overlay_rating_"
									+ i;

							int ratingIdentifier2 = v1
									.getResources()
									.getIdentifier(attribute, "id", packageName);

							ToggleButton button = (ToggleButton) v1
									.getRootView().findViewById(
											ratingIdentifier2);

							if (button.isChecked()) {
								count++;
							}
						}
						
						/* ----- ----- ----- ----- ----- ----- ----- ----- ----- 
						 * ----- ----- ----- ----- ----- ----- ----- ----- ----- 
						 * TODO: Permission Rating
						 *
						//Get all the permissions and associated radiobuttons
						ArrayList<Permission> permissions1 = appPermissionData
								.getPermissionsByAppId(appId);

						// loop over permissions and their radiobuttons to
						// retrieve rating of the permission
						for (int i = 1; i <= (Math.min(5, permissions1.size())); i++) {

							// get rating for permission
							String attributePermission = "app_detail_rate_app_overlay_permission_radiogroup"
									+ i;
							int permissionIdentifier2 = v1.getResources()
									.getIdentifier(attributePermission, "id",
											packageName);

							RadioGroup radioGroup = (RadioGroup) v1
									.getRootView().findViewById(
											permissionIdentifier2);

							int buttonId = radioGroup.getCheckedRadioButtonId();
							RadioButton radioButton = (RadioButton) v1
									.getRootView().findViewById(buttonId);

							String buttonTagString = (String) radioButton
									.getTag();
							Integer buttonTag = Integer
									.parseInt(buttonTagString);

							// create RatingPermissions and therefore retrieve
							// permissions
							String attributePermission2 = "app_detail_rate_app_overlay_permission"
									+ i;
							int resourceId4 = v1.getResources().getIdentifier(
									attributePermission2, "id", packageName);
							TextView permissionRadio = (TextView) v1
									.getRootView().findViewById(resourceId4);

							// String permissionIdString = (String)
							// permissionRadio.getTag();
							// Integer permissionId =
							// Integer.parseInt(permissionIdString);
							Integer permissionId = (Integer) permissionRadio
									.getTag();

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
						*
						* ----- ----- ----- ----- ----- ----- ----- ----- ----- 
						* ----- ----- ----- ----- ----- ----- ----- ----- -----  
						*/
						
						// create new RatingApp with default isExpert = false
						RatingApp newRatingApp = ratingAppData.createRatingApp(
								count, appId, false);

						ratingAppData.close();
						ratingPermissionData.close();
						appPermissionData.close();

						// close overlay
						((AppDetailsActivity) v1.getContext()).hideOverlay(v1);
					}

				});
			}

		});

		return rowView;
	}
}
