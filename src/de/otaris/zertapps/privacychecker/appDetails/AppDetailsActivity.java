package de.otaris.zertapps.privacychecker.appDetails;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.UserStudyLogger;
import de.otaris.zertapps.privacychecker.appDetails.description.Description;
import de.otaris.zertapps.privacychecker.appDetails.header.ExtendedHeader;
import de.otaris.zertapps.privacychecker.appDetails.header.Header;
import de.otaris.zertapps.privacychecker.appDetails.privacyRating.PrivacyRating;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RateApp;
import de.otaris.zertapps.privacychecker.database.dataSource.AppExtendedDataSource;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;

public class AppDetailsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_details);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.app_details, menu);

		// Get the App from the intent passed from the previous activity
		Intent intent = getIntent();
		AppCompact app = intent.getParcelableExtra("AppCompact");

		Header header = new ExtendedHeader();
		View headerView = header.getView(this, app);

		RelativeLayout relLayout = (RelativeLayout) findViewById(R.id.app_details_layout);
		relLayout.addView(headerView);
		int headerId = headerView.getId();

		ListView detailListView = (ListView) findViewById(R.id.app_detail_list);

		RelativeLayout.LayoutParams listLayout = new RelativeLayout.LayoutParams(
				detailListView.getLayoutParams());
		listLayout.addRule(RelativeLayout.BELOW, headerId);
		detailListView.setLayoutParams(listLayout);

		// Get all the details to be shown in the detail view.
		ArrayList<Detail> details = getDetails();
		ArrayAdapter<Detail> adapter = new AppDetailListItemAdapter(this,
				details);
		detailListView.setAdapter(adapter);
		return true;
	}

	/**
	 * Create a list of details.
	 */
	private ArrayList<Detail> getDetails() {
		AppCompact app = getIntent().getParcelableExtra("AppCompact");

		ArrayList<Detail> details = new ArrayList<Detail>();
		AppExtendedDataSource appDataSource = new AppExtendedDataSource(this);
		appDataSource.open();
		AppExtended appExtended = appDataSource.extendAppCompact(app);
		appDataSource.close();
		details.add(new Description(appExtended));
		// details.add(new Permissions(appExtended));
		details.add(new PrivacyRating(appExtended));
		details.add(new RateApp(appExtended));

		return details;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
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
			View rootView = inflater.inflate(R.layout.fragment_app_details,
					container, false);

			return rootView;
		}
	}

	/**
	 * onClick method for hiding the overlay RelativeLayout
	 * 
	 * @param v
	 */
	public void hideOverlay(View v) {
		RelativeLayout overlay = (RelativeLayout) findViewById(R.id.app_detail_overlay);
		overlay.removeAllViews();
		overlay.setVisibility(ViewGroup.INVISIBLE);
	}

	/**
	 * checks weather the overlay is visible or not
	 * 
	 * @return vsiblity of overlay
	 */
	private boolean overlayIsVisible() {
		RelativeLayout overlay = (RelativeLayout) findViewById(R.id.app_detail_overlay);
		return overlay.getVisibility() == ViewGroup.VISIBLE;
	}

	@Override
	public void onBackPressed() {
		if (overlayIsVisible()) {
			hideOverlay(null);
		} else {
			finish();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();

		AppCompact app = getIntent().getParcelableExtra("AppCompact");
		
		UserStudyLogger.getInstance().log("activity_details-" + app.getLabel());
	}

}
