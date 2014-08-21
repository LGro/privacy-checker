package de.otaris.zertapps.privacychecker.appDetails.rateApp.permissionsRating;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElement;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElementViewHelper;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;

public class PermissionsRatingViewHelper extends RatingElementViewHelper {

	@Override
	public View getView(Context context, ViewGroup parent,
			RatingElement ratingElement) {

		if (!(ratingElement instanceof PermissionsRating))
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

		PermissionsRatingItemAdapter adapter = new PermissionsRatingItemAdapter(
				context, app.getPermissionList());

		permissionsList.setAdapter(adapter);

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
							updatedLayout.height = pixels * 4;
							listView.setLayoutParams(updatedLayout);
						}

					}
				});

		return rowView;
	}

}
