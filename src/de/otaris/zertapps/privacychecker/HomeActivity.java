package de.otaris.zertapps.privacychecker;

import java.util.List;

import de.otaris.zertapps.privacychecker.database.App;
import de.otaris.zertapps.privacychecker.database.AppDataSource;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		// test example for DB access
		// AppDataSource helper = new AppDataSource(this);
		// helper.open();
		// helper.createApp("myapp3", "1.4", true, 5);
		// helper.createApp("myapp4", "1.6", false, 2);
		// List<App> apps = helper.getAllApps();
		// helper.close();
		// for (App app : apps) {
		//
		// Log.i("HomeActivity", "App:" + app.getName());
		// }

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
	 * A placeholder fragment containing a simple view.
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

	public void displayInstalledApps(View view) {
		Log.i("HomeActivity", "called display installed apps");
		Intent intent = new Intent(this, InstalledAppsActivity.class);
		startActivity(intent);
	}

	public void displayAllApps(View view) {
		Log.i("HomeActivity", "called display all apps");
	}

}
