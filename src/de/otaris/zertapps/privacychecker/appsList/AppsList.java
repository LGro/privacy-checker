package de.otaris.zertapps.privacychecker.appsList;

import java.util.List;

import com.google.inject.Inject;

import de.otaris.zertapps.privacychecker.AppController;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.R.layout;
import de.otaris.zertapps.privacychecker.appDetails.AppDetailsActivity;
import de.otaris.zertapps.privacychecker.database.dataSource.AppCompactDataSource;
import de.otaris.zertapps.privacychecker.database.dataSource.CategoryDataSource;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AppsList extends ListFragment {

	private Activity rootActivity;
	private AppsListOrder order;
	
	private boolean installedOnly = false;
	private int categoryId = -1;

	@Inject
	private AppController appController = null;
	@Inject
	private AppCompactDataSource appDataSource = null;
	private boolean ascending;
	
	public void setCageoryId(int id) {
		categoryId = id;
	}

	public void setInstalledOnly() {
		installedOnly = true;
	}
	
	// lazy initialization getter for AppController
	public AppController getAppController() {
		if (appController == null)
			appController = new AppController();

		return appController;
	}

	public void setAppController(AppController appController) {
		this.appController = appController;
	}

	// lazy initialization getter for AppDataSource
	public AppCompactDataSource getAppDataSource() {
		if (appDataSource == null)
			appDataSource = new AppCompactDataSource(rootActivity);

		return appDataSource;
	}

	public void setAppDataSource(AppCompactDataSource appDataSource) {
		this.appDataSource = appDataSource;
	}

	public AppsList() {
	}

	public void setRootActivity(Activity rootActivity) {
		this.rootActivity = rootActivity;
	}

	public AppsListOrder getOrder() {
		return order;
	}

	public void setOrder(AppsListOrder order, boolean ascending) {
		this.order = order;
		this.ascending = ascending;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get all installed apps from database
		AppCompactDataSource appData = getAppDataSource();
		appData.open();
		
		List<AppCompact> apps;
		if (installedOnly) {
			apps = appData.getInstalledApps(order, ascending);
		} else {
			if (categoryId != -1) {
				apps = appData.getAppsByCategory(categoryId, order, ascending);
			} else {
				apps = appData.getAllApps(order, ascending);
			}
		}
		
		appData.close();

		// set custom list adapter to display apps with icon, name and rating
		ArrayAdapter<AppCompact> adapter = new AppListItemAdapter(rootActivity,
				rootActivity.getPackageManager(), apps);
		setListAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_fragment, container, false);
	}

	@Override
	public void onListItemClick(ListView list, View v, int position, long id) {
		Intent intent = new Intent(rootActivity, AppDetailsActivity.class);
		intent.putExtra("id", (Integer)v.getTag()); 
		startActivity(intent);
	}
}