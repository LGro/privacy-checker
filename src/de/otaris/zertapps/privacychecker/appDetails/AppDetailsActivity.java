package de.otaris.zertapps.privacychecker.appDetails;

import java.util.ArrayList;

import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.R.id;
import de.otaris.zertapps.privacychecker.R.layout;
import de.otaris.zertapps.privacychecker.R.menu;
import de.otaris.zertapps.privacychecker.appDetails.description.Description;
import de.otaris.zertapps.privacychecker.database.dataSource.AppCompactDataSource;
import de.otaris.zertapps.privacychecker.database.dataSource.AppExtendedDataSource;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;
import android.R.drawable;
import android.app.Activity;
import android.app.Fragment;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
		
		// Get the App ID from the intent, which was passed from the previous activity
		Intent intent = getIntent();
		Integer appID = intent.getIntExtra("id", -1);
		
		// Open DB and and retrieve the App by ID
		AppCompactDataSource appData = new AppCompactDataSource(this);
		appData.open();
		AppCompact app = appData.getAppById(appID);
		appData.close();
		
		// Get all the views ...
		TextView nameView = (TextView) findViewById(R.id.app_details_activity_head_name);
		TextView developerView = (TextView) findViewById(R.id.app_details_activity_head_developer);
		ImageView ratingView = (ImageView) findViewById(R.id.app_details_activity_head_rating);
		ImageView iconView = (ImageView) findViewById(R.id.app_details_activity_head_icon);
		Button button = (Button) findViewById(R.id.app_details_activity_head_button_install);
		
		// ... and fill them with the right information about the app.
		// Set icon, button and rating.
		if (app.isInstalled()) {
			button.setText("Deinstallieren");
			try {
				iconView.setImageDrawable(getPackageManager().getApplicationIcon(app.getName()));
				ratingView.setImageResource(getIconRating(app.getPrivacyRating()));
			} catch (NameNotFoundException e) {
				Log.w("AppListItemAdapter",
						"Couldn't load icons for app: " + e.getMessage());
			}
		} else {
			button.setText("Installieren");
			// TODO: implement (get icon from PlayStore API?!)
		}
		// Set name and developer
		nameView.setText(app.getLabel());
		developerView.setText(app.getName());
		
		// Find the listView and set a custom adapter to it.
		ListView detailListView = (ListView) findViewById(R.id.app_details_activity_head_listView);
		
		ArrayList<Detail> details = getDetails(); 
		ArrayAdapter<Detail> adapter = new AppDetailListItemAdapter(this, details);
		detailListView.setAdapter(adapter);
		return true;
	}
	
	private ArrayList<Detail> getDetails() {
		int id = getIntent().getIntExtra("id", -1);
		ArrayList<Detail> details = new ArrayList<Detail>(); 
		AppExtendedDataSource appDataSource = new AppExtendedDataSource(this);
		appDataSource.open();
		AppExtended app = appDataSource.getAppById(id);
		appDataSource.close();
		details.add(new Description(app));
		//TODO: Add more Details here
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

	
	private Integer getIconRating(float rating){
		if (rating > 4.5 && rating <= 5) {
			return R.drawable.lock5;
		} else if (rating > 3.5 && rating <= 4.5) {
			return R.drawable.lock4;
		} else if (rating > 2.5 && rating <= 3.5) {
			return R.drawable.lock3;
		} else if (rating > 1.5 && rating <= 2.5) {
			return R.drawable.lock2;
		} else if (rating > 0 && rating <= 1.5) {
			return R.drawable.lock1;
		} else {
			throw new IllegalArgumentException("Rating not is not between 0 and 5.");
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
