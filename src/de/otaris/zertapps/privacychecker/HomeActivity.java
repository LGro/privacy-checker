package de.otaris.zertapps.privacychecker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.inject.Inject;

import de.otaris.zertapps.privacychecker.appsList.AllAppsActivity;
import de.otaris.zertapps.privacychecker.appsList.AppListItemAdapter;
import de.otaris.zertapps.privacychecker.appsList.InstalledAppsActivity;
import de.otaris.zertapps.privacychecker.database.dataSource.AppCompactDataSource;
import de.otaris.zertapps.privacychecker.database.dataSource.CategoryDataSource;
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
	 * Auto generated code
	 * 
	 * + Insert all installed apps to database on start. This is done to make
	 * sure there are apps in the database.
	 * 
	 * + Connect to local database and retrieve last updated apps. Store them in
	 * a list. Do this at this stage to avoid retrieving those apps everytime
	 * you return to the homescreen.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		// TODO: find good place for this
		// fill db-category-table with entries
		CategoryDataSource categoryData = new CategoryDataSource(this);
		categoryData.open();
		categoryData.createCategory("games", "Spiele", 10);
		categoryData.createCategory("weather", "Wetter", 20);
		categoryData.createCategory("categoryA", "Kategorie A", 30);
		categoryData.createCategory("categoryB", "Kategorie B", 40);
		categoryData.createCategory("categoryC", "Kategorie C", 50);
		categoryData.close();

		// insert all installed apps into database
		 //AppController appController = getAppController();
		 //appController.putInstalledAppsInDatabase(this, getPackageManager());

		 //exportDB();
		try {
			importDB();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("HomeActivity", "DB import failed: " + e.getMessage());
		}

		// connect to database
		AppCompactDataSource appData = new AppCompactDataSource(this);
		appData.open();
		latestAppsList = appData.getLastUpdatedApps(4);
		appData.close();
	}

	private void exportDB() {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String currentDBPath = "//data//"
						+ "de.otaris.zertapps.privacychecker" + "//databases//"
						+ "DB_privacy-checker_apps";
				String backupDBPath = sd + "/Pca/pca.sqlite";
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(backupDBPath);

				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupDB).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
				Toast.makeText(getBaseContext(), backupDB.toString(),
						Toast.LENGTH_LONG).show();

			} else {
				Log.e("exportDB", "sdcard nicht beschreibbar");
			}
		} catch (Exception e) {

			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)
					.show();

		}
	}

	private void importDB() throws IOException {
		 //Open your local db as the input stream
		InputStream myInput = getAssets().open("pca.db");
		 
		// Path to the just created empty db
		String outFileName = "//data//data//"
				+ "de.otaris.zertapps.privacychecker" + "//databases//"
				+ "DB_privacy-checker_apps";
		 
		//Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);
		 
		//transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
		myOutput.write(buffer, 0, length);
		}
		 
		//Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
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
		// laList.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// Intent intent = new Intent(rootActivity, AppDetailsActivity.class);
		// intent.putExtra("id", (Integer)view.getTag());
		// startActivity(intent);
		// }
		//
		// });
		laList.setAdapter(adapter);
	}

}
