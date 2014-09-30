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
import de.otaris.zertapps.privacychecker.ImprintActivity;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.comment.Comment;
import de.otaris.zertapps.privacychecker.appDetails.description.Description;
import de.otaris.zertapps.privacychecker.appDetails.header.ExtendedHeader;
import de.otaris.zertapps.privacychecker.appDetails.header.Header;
import de.otaris.zertapps.privacychecker.appDetails.privacyRating.PrivacyRating;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RateApp;
import de.otaris.zertapps.privacychecker.database.dataSource.AppExtendedDataSource;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;

/**
 * display basic information and multiple details of a selected app
 * 
 * expects an AppCompact object as intent
 */
public class AppDetailsActivity extends Activity {

	/**
	 * to change the displayed header, return another object that implements
	 * Header
	 * 
	 * @return specific Header
	 */
	private Header getHeader() {
		return new ExtendedHeader();
	}

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
	protected void onResume() {
		super.onResume();

		// Get the App from the intent passed from the previous activity
		AppCompact app = getIntent().getParcelableExtra("AppCompact");
		// add missing information to the compact app
		AppExtendedDataSource appDataSource = new AppExtendedDataSource(this);
		appDataSource.open();
		AppExtended appExtended = appDataSource.getAppById(app.getId());
		appDataSource.close();

		// get header to display the basic app details
		View headerView = getHeader().getView(this, appExtended);
		RelativeLayout relLayout = (RelativeLayout) findViewById(R.id.app_details_header_layout);
		// add it to the details layout
		relLayout.addView(headerView);

		// get details list view by id
		ListView detailListView = (ListView) findViewById(R.id.app_detail_list);

		// Get all the details to be shown in the detail view.
		ArrayList<Detail> details = getDetails(appExtended);
		ArrayAdapter<Detail> adapter = new AppDetailListItemAdapter(this,
				details);
		detailListView.setAdapter(adapter);
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	/**
	 * Create a list of details.
	 * 
	 * To add a new detail or change the order of the details, modify/create
	 * lines in the format of:
	 * 
	 * details.add(new <instance of Detail>(AppExtended));
	 * 
	 * @param appExtended
	 *            app that is passed to the details
	 * @return list of details
	 */
	private ArrayList<Detail> getDetails(AppExtended appExtended) {
		ArrayList<Detail> details = new ArrayList<Detail>();

		// add several details in the order they should be displayed
		details.add(new Description(appExtended));
		details.add(new PrivacyRating(appExtended));
		details.add(new RateApp(appExtended));
		details.add(new Comment(appExtended));

		return details;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_imprint:
			Intent intent = new Intent(this, ImprintActivity.class);
			startActivity(intent);
			return true;
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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

}
