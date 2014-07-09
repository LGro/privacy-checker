package de.otaris.zertapps.privacychecker;

import java.util.ArrayList;

import de.otaris.zertapps.privacychecker.database.App;
import de.otaris.zertapps.privacychecker.database.AppDataSource;
import android.app.Activity;
import android.app.Fragment;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
		
		ListView detailListView = (ListView) findViewById(R.id.app_details_activity_head_listView);
		
		ArrayList<Detail> details = getDetails(); 
		ArrayAdapter<Detail> adapter = new AppDetailListItemAdapter(this, details);
		detailListView.setAdapter(adapter);
		return true;
	}
	
	private ArrayList<Detail> getDetails() {
		int id = getIntent().getIntExtra("id", -1);
		ArrayList<Detail> details = new ArrayList<Detail>(); 
		AppDataSource appDataSource = new AppDataSource(this);
		appDataSource.open();
		App app = appDataSource.getAppById(id);
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
