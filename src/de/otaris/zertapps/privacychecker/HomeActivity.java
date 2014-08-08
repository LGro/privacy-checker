package de.otaris.zertapps.privacychecker;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.inject.Inject;

import de.otaris.zertapps.privacychecker.appDetails.AppDetailsActivity;
import de.otaris.zertapps.privacychecker.appsList.AllAppsActivity;
import de.otaris.zertapps.privacychecker.appsList.AppListItemAdapter;
import de.otaris.zertapps.privacychecker.appsList.InstalledAppsActivity;
import de.otaris.zertapps.privacychecker.database.DatabaseHelper;
import de.otaris.zertapps.privacychecker.database.dataSource.AppCompactDataSource;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;

/**
 * Is called when app is started, handles navigation to installedAppsActivity
 * and AllAppsActivity
 */
public class HomeActivity extends Activity {

	private List<AppCompact> latestAppsList;
	@Inject
	private AppController appController = null;

	// lazy initialization getter for AppController
	public AppController getAppController() {
		if (appController == null)
			appController = new AppController();

		return appController;
	}

	public void setAppController(AppController appController) {
		this.appController = appController;
	}

	/**
	 * Connect to local database and retrieve last updated apps. Store them in a
	 * list. Do this at this stage to avoid retrieving those apps everytime you
	 * return to the homescreen.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		// DatabaseHelper dbHelper = new DatabaseHelper(this);
		// dbHelper.fillDatabaseFromDevice();
		// dbHelper.recalculateAutomaticRatingForAllApps();
		// dbHelper.exportDatabase(this);

		SharedPreferences wmbPreference = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true);
		if (isFirstRun) {
			// Code to run once
			SharedPreferences.Editor editor = wmbPreference.edit();
			editor.putBoolean("FIRSTRUN", false);
			editor.commit();

			try {
				DatabaseHelper dbHelper = new DatabaseHelper(this);
				dbHelper.importDatabase();
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("HomeActivity", "DB import failed: " + e.getMessage());
			}

			getAppController().insertUncoveredInstalledApps(this,
					getPackageManager());
		}

		AppCompactDataSource appData = new AppCompactDataSource(this);
		appData.open();

		latestAppsList = appData.getLastUpdatedApps(4);
		appData.close();

		// enable/disable logging
		UserStudyLogger.LOGGING_ENABLED = true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);

		return true;
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
	 * Everytime you return to the homescreen the list is displayed. Populate
	 * now and not earlier to avoid null, or in other words populate once the
	 * homescreen/app was completely initialised.
	 */
	@Override
	public void onResume() {
		super.onResume();

		UserStudyLogger.getInstance().log("activity_home");

		populateLatestAppListView();
	}

	/**
	 * Auto-generated code A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_home, container,
					false);
			return rootView;
		}
	}

	/**
	 * Method to display installed apps by starting InstalledAppsActivity
	 * 
	 * @param view
	 *            : View
	 */
	public void displayInstalledApps(View view) {
		Log.i("HomeActivity", "called display installed apps");

		// to pass the information to run InstalledAppActivity
		Intent intent = new Intent(this, InstalledAppsActivity.class);
		startActivity(intent);
	}

	/**
	 * Method to display in LogCat that Display allApps is called
	 * 
	 * @param view
	 *            : View
	 */
	public void displayAllApps(View view) {
		Log.i("HomeActivity", "called display all apps");
		Intent intent = new Intent(this, AllAppsActivity.class);
		startActivity(intent);
	}

	/**
	 * Show latest apps in the list view. The list of apps is created on start.
	 */
	private void populateLatestAppListView() {
		// Setup custom list adapter to display apps with icon, name and rating.
		AppListItemAdapter adapter = new AppListItemAdapter(this,
				getPackageManager(), latestAppsList);
		ListView laList = (ListView) findViewById(R.id.latest_apps_listview);
		laList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(view.getContext(),
						AppDetailsActivity.class);
				AppCompact app = (AppCompact) parent
						.getItemAtPosition(position);
				intent.putExtra("AppCompact", app);
				startActivity(intent);
			}

		});
		laList.setAdapter(adapter);
	}

}
