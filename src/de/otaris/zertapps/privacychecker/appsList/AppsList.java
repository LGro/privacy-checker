package de.otaris.zertapps.privacychecker.appsList;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.AppDetailsActivity;
import de.otaris.zertapps.privacychecker.database.dataSource.AppCompactDataSource;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;

public class AppsList extends ListFragment {

	private Activity rootActivity;
	private AppsListOrder order;
	
	private boolean installedOnly = false;
	private int categoryId = -1;

	private boolean ascending;
	
	public void setCageoryId(int id) {
		categoryId = id;
	}

	public void setInstalledOnly() {
		installedOnly = true;
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
		AppCompactDataSource appData = new AppCompactDataSource(rootActivity);
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