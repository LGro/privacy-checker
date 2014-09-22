package de.otaris.zertapps.privacychecker.appDetails.rateApp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import de.otaris.zertapps.privacychecker.IconController;
import de.otaris.zertapps.privacychecker.ImprintActivity;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.RateAppOnClickListener;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.comment.Comment;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.expertMode.ExpertMode;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.permissionsExpected.PermissionsExpected;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.totalPrivacyRating.TotalPrivacyRating;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;

/**
 * Activity that contains rating elements to rate an app and its permissions
 * 
 */
public class RateAppActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rate_app);

		if (savedInstanceState == null)
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();

		AppExtended appExtended = (AppExtended) getIntent().getParcelableExtra(
				"AppExtended");

		// set app label and app icon to action bar
		setTitle(appExtended.getLabel());
		Resources res = getResources();
		BitmapDrawable icon = new BitmapDrawable(res,
				IconController.byteArrayToBitmap(appExtended.getIcon()));
		getActionBar().setIcon(icon);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Get all the rating elements to be shown in the rating
		// overlay.
		AppExtended appExtended = (AppExtended) getIntent().getParcelableExtra(
				"AppExtended");

		ArrayList<RatingElement> ratingElements = getRatingElements(appExtended);
		ListView ratingElementListView = (ListView) findViewById(R.id.app_detail_rate_app_overlay_list);

		// set list adapter
		ArrayAdapter<RatingElement> adapter = new RatingElementListAdapter(
				this, ratingElements);
		ratingElementListView.setAdapter(adapter);

		Button sendRatingButton = (Button) findViewById(R.id.app_detail_rate_app_overlay_send);
		sendRatingButton.setOnClickListener(new RateAppOnClickListener());
	}

	/**
	 * Adds a rating element to a given ArrayList.
	 * 
	 * Prevents already existing RatingElements in the Registry that might
	 * contain information from being overwritten.
	 * 
	 * @param ratingElements
	 *            list that the rating element should be added to
	 * @param ratingElement
	 *            the rating element to add
	 * @return list with added rating element
	 */
	private ArrayList<RatingElement> addRatingElement(
			ArrayList<RatingElement> ratingElements, RatingElement ratingElement) {

		// get rating element from registry
		Registry registry = Registry.getInstance();
		RatingElement existingElement = registry.getRatingElement(ratingElement
				.getClass());

		if (existingElement == null) {
			// if there is no existing element: add new one
			registry.addRatingElement(ratingElement);
			ratingElements.add(ratingElement);
		} else {
			// if there is an existing element...
			if (existingElement.getApp().equals(ratingElement.getApp()) == false) {
				// ... and it doesn't belong to the same app as the current one:
				// create new
				registry.addRatingElement(ratingElement);
				ratingElements.add(ratingElement);
			} else {
				// keep existing element in registry and add to ratingElements
				ratingElements.add(existingElement);
			}
		}

		return ratingElements;
	}

	/**
	 * defines what rating elements are part of the total rating and therefore
	 * are displayed within the rate app overlay
	 * 
	 * @param app
	 * @return list of rating elements
	 */
	private ArrayList<RatingElement> getRatingElements(AppExtended app) {
		ArrayList<RatingElement> ratingElements = new ArrayList<RatingElement>();

		// add objects to registry and to array list (just for displaying
		// purposes) - second argument determines if rating element is mandatory
		ratingElements = addRatingElement(ratingElements, new ExpertMode(app,
				false));
		ratingElements = addRatingElement(ratingElements,
				new PermissionsExpected(app, false));
		ratingElements = addRatingElement(ratingElements,
				new TotalPrivacyRating(app, true));
		ratingElements = addRatingElement(ratingElements, new Comment(app,
				false));

		return ratingElements;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_imprint) {
			Intent intent = new Intent(this, ImprintActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_rate_app,
					container, false);

			return rootView;
		}
	}
}
