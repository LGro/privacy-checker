package de.otaris.zertapps.privacychecker.appDetails.privacyRating;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.Detail;
import de.otaris.zertapps.privacychecker.appDetails.DetailViewHelper;
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
		// get permissions list from permissions detail
		PrivacyRating rating = (PrivacyRating) detail;
		AppExtended app = rating.getApp();
		TextView privacyRatingTextView = (TextView) rowView
			.findViewById(R.id.app_detail_privacy_rating_value);
		
		privacyRatingTextView.setText(round(app.getRating())+ "");
		
		
		
		TextView privacyRatingCount = (TextView) rowView
				.findViewById(R.id.app_detail_privacy_rating_count);
		privacyRatingCount.setText("(" + (app.getNonExpertRating().size() + app.getExpertRating().size()) + ")");
		
		TextView automaticRating = (TextView) rowView
				.findViewById(R.id.app_detail_privacy_rating_automatic_text);
		automaticRating.setText(round(app.getAutomaticRating()) + "");
		
		TextView nonExpertRating = (TextView) rowView
				.findViewById(R.id.app_detail_privacy_rating_nonexpert_text);
		nonExpertRating.setText(round(app.getTotalNonExpertRating()) + " (" + app.getNonExpertRating().size() + ")");
		
		TextView expertRating = (TextView) rowView
				.findViewById(R.id.app_detail_privacy_rating_expert_text);
		expertRating.setText(round(app.getTotalExpertRating()) + " (" + app.getExpertRating().size() + ")");
		
		TextView explanationRating = (TextView) rowView
				.findViewById(R.id.app_detail_privacy_rating_explanation);
		explanationRating.setText("Die Bewertung setzt sich aus drei Komponenten zusammen. Die erste ist eine automatische Bewertung anhand der geforderten Berechtigungen. Die zweite Komponente ist die Bewertung von anderen Nutzern und die dritte setzt sich aus der Bewertung qualifizierter Experten zusammen. ");
		
		List<Permission> permissionList = app.getPermissionList();

		// get permission list view
		ListView listView = (ListView) rowView
				.findViewById(R.id.app_detail_rating_permissions_list);

		// add list item adapter for permissions
		listView.setAdapter(new PermissionsListItemAdapter(context,
				permissionList));
		listView.setScrollContainer(false);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				RelativeLayout overlay = (RelativeLayout) parent.getRootView()
						.findViewById(R.id.app_detail_overlay);
				overlay.setVisibility(View.VISIBLE);
				LayoutInflater inflater = LayoutInflater.from(parent
						.getContext());
				RelativeLayout layout = (RelativeLayout) inflater.inflate(
						R.layout.app_detail_rating_permission_overlay, overlay,
						false);
				Permission permission = (Permission) parent
						.getItemAtPosition(position);

				TextView permissionLabelText = (TextView) layout
						.findViewById(R.id.app_detail_rating_permission_name);
				permissionLabelText.setText(permission.getLabel());

				TextView permissionDescriptionText = (TextView) layout
						.findViewById(R.id.app_detail_rating_permission_description);
				permissionDescriptionText.setText(permission.getDescription());

				overlay.addView(layout);
			}
		});
		
			
		
		ToggleButton button = (ToggleButton) rowView
				.findViewById(R.id.app_detail_privacy_rating_more);
		ListView permissions = (ListView) rowView.findViewById(R.id.app_detail_rating_permissions_list);
		button.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			

			public void onCheckedChanged(CompoundButton toggleButton,
					boolean isChecked) {
				
				TextView explanation = (TextView) ((View) toggleButton
						.getParent()).findViewById(R.id.app_detail_privacy_rating_explanation);
				
				ListView permissions = (ListView) ((View) toggleButton
						.getParent()).findViewById(R.id.app_detail_rating_permissions_list);
				//changes fulltext to shorten version 
				if (isChecked) {
					permissions.setVisibility(View.VISIBLE);
					explanation.setVisibility(View.VISIBLE);
				} else {
					permissions.setVisibility(View.INVISIBLE);
					explanation.setVisibility(View.INVISIBLE);
				}


			}
			});
		return rowView;
	}
}
