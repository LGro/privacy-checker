package de.otaris.zertapps.privacychecker.appDetails.rateApp;

import java.security.Permission;
import java.util.ArrayList;

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
import de.otaris.zertapps.privacychecker.database.model.RatingApp;

/**
 * 
 */
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
		// rowView.setTag(detail.getApp().getId());
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
				// AppPermissionDataSource appPermissionData = new
				// AppPermissionDataSource(v.getRootView().getContext());
				// appPermissionData.open();
				// ArrayList<de.otaris.zertapps.privacychecker.database.model.Permission>
				// permissions = appPermissionData.getPermissionsByAppId(appId);
				// TextView permission1 = (TextView)
				// v.getRootView().findViewById(R.id.app_detail_rate_app_overlay_permission1);
				// permission1.setText(permissions.get(0).getLabel());
				// permission1.setTag(permissions.get(0).getId());
				// TextView permission2 = (TextView)
				// v.getRootView().findViewById(R.id.app_detail_rate_app_overlay_permission2);
				// permission2.setText(appPermissionData.getPermissionsByAppId(appId).get(1).getLabel());
				//
				// TextView permission3 = (TextView)
				// v.getRootView().findViewById(R.id.app_detail_rate_app_overlay_permission3);
				// permission3.setText(appPermissionData.getPermissionsByAppId(appId).get(2).getLabel());
				//
				// TextView permission4 = (TextView)
				// v.getRootView().findViewById(R.id.app_detail_rate_app_overlay_permission4);
				// permission4.setText(appPermissionData.getPermissionsByAppId(appId).get(3).getLabel());
				//
				// TextView permission5 = (TextView)
				// v.getRootView().findViewById(R.id.app_detail_rate_app_overlay_permission5);
				// permission5.setText(appPermissionData.getPermissionsByAppId(appId).get(4).getLabel());
				// appPermissionData.close();
				overlay.addView(layout);

				layout.findViewById(R.id.app_detail_rate_app_overlay_rating_1)
						.setOnClickListener(new TotalRatingListener());
				layout.findViewById(R.id.app_detail_rate_app_overlay_rating_2)
						.setOnClickListener(new TotalRatingListener());
				layout.findViewById(R.id.app_detail_rate_app_overlay_rating_3)
						.setOnClickListener(new TotalRatingListener());
				layout.findViewById(R.id.app_detail_rate_app_overlay_rating_4)
						.setOnClickListener(new TotalRatingListener());
				layout.findViewById(R.id.app_detail_rate_app_overlay_rating_5)
						.setOnClickListener(new TotalRatingListener());

				Button sendButton = (Button) v.getRootView().findViewById(
						R.id.app_detail_rate_app_overlay_send);
				sendButton.setTag(appId);
				sendButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v1) {
//
//						// create total rating
//						int count = 0;
//						RatingAppDataSource ratingAppData = new RatingAppDataSource(
//								v1.getContext());
//						RatingPermissionDataSource ratingPermissionData = new RatingPermissionDataSource(
//								v1.getContext());
//						AppPermissionDataSource appPermissionData = new AppPermissionDataSource(
//								v1.getContext());
//						ratingAppData.open();
//						ratingPermissionData.open();
//						appPermissionData.open();
//						int appId = (Integer) v1.getTag();
//
//						for (int i = 1; i <= 5; i++) {
//
//							// get total rating
//							String attribute = "app_detail_rate_app_overlay_rating_"
//									+ i;
//
//							String packageName = v1.getContext()
//									.getPackageName();
//
//							int resourceId = v1.getResources().getIdentifier(
//									attribute, "id", packageName);
//
//							ToggleButton button = (ToggleButton) v1
//									.getRootView().findViewById(resourceId);
//
//							if (button.isChecked()) {
//								count++;
//							}
//							// get rating for permission
//							String attributePermission = "app_detail_rate_app_overlay_permission_radiogroup"
//									+ i;
//							int resourceId2 = v1.getResources().getIdentifier(
//									attributePermission, "id", packageName);
//							RadioGroup radioGroup = (RadioGroup) v1
//									.getRootView().findViewById(resourceId2);
//							int buttonId = radioGroup.getCheckedRadioButtonId();
//							RadioButton radioButton = (RadioButton) v1
//									.findViewById(buttonId);
//							// int buttonTag = (int) radioButton.getTag();
//
//							// if(buttonTag % 2 == 0){
//							// ratingPermissionData.createRatingPermission(0,
//							// appPermissionId, false);
//							// }
//						}
//
//						// value=, appId=, false=isExpert
//						RatingApp newRatingApp = ratingAppData.createRatingApp(
//								count, appId, false);
//						ratingAppData.close();
//
//						// create RatingPermissions
//
					}

				});
			}

		});

		return rowView;
	}

}
