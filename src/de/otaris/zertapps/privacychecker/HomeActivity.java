package de.otaris.zertapps.privacychecker;

import java.util.ArrayList;
import java.util.List;

import de.otaris.zertapps.privacychecker.database.App;
import de.otaris.zertapps.privacychecker.database.AppDataSource;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;

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
		
		
		/*
		 * TODO: Find out when the layout has finished loading. This is where this belongs.
		 * Once the application is started, fill the list of latest apps.
		 */
		populateLatestAppList();
		/*
		 * After the internal list of latest apps was generated, 
		 * send them to the latest_apps_listview.
		 */
		populateLatestAppListView();
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
	}

	public void displayAllApps(View view) {
		Log.i("HomeActivity", "called display all apps");
	}
	
	/*
	 *  Create a list for the latest apps found. EMPTY.
	 *  TODO: Exchange TestApp with the final structure of a app. Or exchange with the internal database.
	 */
	private List<TestApp> latestAppsList = new ArrayList<TestApp>(); 
	
	/*
	 * This method fills the latest App List. 
	 * TODO: Get apps from the database. NOT hardcoded apps.
	 */
	private void populateLatestAppList() {
		latestAppsList.add(new TestApp("Test_App_01", 3, R.drawable.testapp01));
		latestAppsList.add(new TestApp("Test_App_02", 2, R.drawable.testapp02));
		latestAppsList.add(new TestApp("Test_App_03", 6, R.drawable.testapp03));
		latestAppsList.add(new TestApp("Test_App_04", 1, R.drawable.testapp04));
		latestAppsList.add(new TestApp("Test_App_05", 4, R.drawable.testapp05));
		latestAppsList.add(new TestApp("Test_App_06", 2, R.drawable.testapp06));
	}
	
	/*
	 * Find the listview (the one on the layout) to work on, and start processing.
	 * TODO: Exchange TestApp class, with the final structure of our apps.
	 */
	private void populateLatestAppListView() {
		ArrayAdapter<TestApp> adapter = new MyListAdapter();
		ListView lalist = (ListView) findViewById(R.id.latest_apps_listview); 
		lalist.setAdapter(adapter);		
	}
	
	/*
	 * Do some magic
	 * TODO: Exchange TestApp class
	 */
	private class MyListAdapter extends ArrayAdapter<TestApp> {
		public MyListAdapter() {
			super (HomeActivity.this, R.layout.item_view, latestAppsList);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			//Get the current view and if its empty fill it.
			View itemView = convertView;
			if(itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
			}
			
			//Get the current test app, as to get the information we need from it.
			TestApp currentApp = latestAppsList.get(position);
			
			/*
			 * Now fill the different attributes of a item_view.
			 * Step 1: from the current item, get me the attribute
			 * Step 2: fill this attribute with the info from the current app
			 */
			
			// ---> ICON
			ImageView iconView = (ImageView) itemView.findViewById(R.id.item_icon); 
			iconView.setImageResource(currentApp.getIconID());
			
			// ---> NAME
			TextView nameText = (TextView) itemView.findViewById(R.id.item_name); 
			nameText.setText(currentApp.getName());
			
			// ---> RATING
			// TODO: Use a proper int to string conversion
			TextView ratingText = (TextView) itemView.findViewById(R.id.item_rating); 
			ratingText.setText("" + currentApp.getRating());
			
			
			return itemView;
		}
	}
	
}
