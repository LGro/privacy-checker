package de.otaris.zertapps.privacychecker.appDetails;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import de.otaris.zertapps.privacychecker.PrivacyCheckerAlert;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElement;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElementListAdapter;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.Registry;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.expertMode.ExpertMode;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.permissionsExpected.PermissionsExpected;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.totalPrivacyRating.TotalPrivacyRating;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;
import de.otaris.zertapps.privacychecker.database.model.Permission;

/**
 * Handles the click on the app detail's "rate now" button.
 */
public class RateAppOverlayOnClickListener implements OnClickListener {

	@Override
	public void onClick(View v) {
		// Create overlay
		RelativeLayout overlay = (RelativeLayout) v.getRootView().findViewById(
				R.id.app_detail_overlay);
		overlay.setVisibility(View.VISIBLE);
		LayoutInflater inflater = LayoutInflater.from(v.getContext());
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.app_detail_rate_app_overlay, overlay, false);
		overlay.addView(layout);

		// Get all the rating elements to be shown in the rating
		// overlay.
		ArrayList<RatingElement> ratingElements = getRatingElements((AppExtended) v
				.getTag());
		ListView ratingElementListView = (ListView) overlay
				.findViewById(R.id.app_detail_rate_app_overlay_list);

		// set list adapter
		ArrayAdapter<RatingElement> adapter = new RatingElementListAdapter(
				v.getContext(), ratingElements);
		ratingElementListView.setAdapter(adapter);

		Button sendRatingButton = (Button) overlay
				.findViewById(R.id.app_detail_rate_app_overlay_send);
		sendRatingButton.setOnClickListener(new RateAppOnClickListener());
	}

	/**
	 * defines what rating elements are part of the total rating and therefore
	 * are displayed within the rate app overlay
	 * 
	 * @param app
	 * @return list of rating elements
	 */
	private ArrayList<RatingElement> getRatingElements(AppExtended app) {
		Registry registry = Registry.getInstance();
		ArrayList<RatingElement> ratingElements = new ArrayList<RatingElement>();

		// add objects to registry and to array list (just for displaying
		// purposes) - second argument determines if rating element is mandatory
		registry.addRatingElement(new ExpertMode(app, false));
		ratingElements.add(new ExpertMode(app, false));

		registry.addRatingElement(new PermissionsExpected(app, false));
		ratingElements.add(new PermissionsExpected(app, false));

		registry.addRatingElement(new TotalPrivacyRating(app, true));
		ratingElements.add(new TotalPrivacyRating(app, true));

		return ratingElements;
	}
}
