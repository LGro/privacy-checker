package de.otaris.zertapps.privacychecker.appDetails.rateApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.Detail;
import de.otaris.zertapps.privacychecker.appDetails.DetailViewHelper;
import de.otaris.zertapps.privacychecker.database.dataSource.RatingAppDataSource;
import de.otaris.zertapps.privacychecker.database.dataSource.RatingPermissionDataSource;
import de.otaris.zertapps.privacychecker.database.model.Permission;

public class RateAppViewHelper extends DetailViewHelper{

	@Override
	public View getView(Context context, ViewGroup parent, Detail detail)
			throws IllegalArgumentException {
		
		if (!(detail instanceof RateApp))
			throw new IllegalArgumentException(
					"Illegal Detail Object. Expected Rating.");

	

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.app_detail_rate_app,
				parent, false);
		
		Button button = (Button) rowView.findViewById(R.id.app_detail_rate_app_button);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RelativeLayout overlay = (RelativeLayout) v.getRootView()
						.findViewById(R.id.app_detail_overlay);
				overlay.setVisibility(View.VISIBLE);
				LayoutInflater inflater = LayoutInflater.from(v
						.getContext());
				RelativeLayout layout = (RelativeLayout) inflater.inflate(
						R.layout.app_detail_rate_app_overlay, overlay,
						false);
//				Permission permission = (Permission) v
//						.getItemAtPosition(position);
//
//				TextView permissionLabelText = (TextView) layout
//						.findViewById(R.id.app_detail_rating_permission_name);
//				permissionLabelText.setText(permission.getLabel());
//
//				TextView permissionDescriptionText = (TextView) layout
//						.findViewById(R.id.app_detail_rating_permission_description);
//				permissionDescriptionText.setText(permission.getDescription());

				overlay.addView(layout);
				

				layout.findViewById(R.id.app_detail_rate_app_overlay_rating_1).setOnClickListener(new TotalRatingListener());
				layout.findViewById(R.id.app_detail_rate_app_overlay_rating_2).setOnClickListener(new TotalRatingListener());
				layout.findViewById(R.id.app_detail_rate_app_overlay_rating_3).setOnClickListener(new TotalRatingListener());
				layout.findViewById(R.id.app_detail_rate_app_overlay_rating_4).setOnClickListener(new TotalRatingListener());
				layout.findViewById(R.id.app_detail_rate_app_overlay_rating_5).setOnClickListener(new TotalRatingListener());
			}
		});
		
		return rowView;
	}
	
	


}
