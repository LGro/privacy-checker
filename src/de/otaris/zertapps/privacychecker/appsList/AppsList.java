package de.otaris.zertapps.privacychecker.appsList;

import java.util.ArrayList;
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
import de.otaris.zertapps.privacychecker.database.dataSource.AppPermissionDataSource;
import de.otaris.zertapps.privacychecker.database.interfaces.App;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;
import de.otaris.zertapps.privacychecker.database.model.Permission;

public class AppsList extends ListFragment {

	private Activity rootActivity;
	private AppsListOrder order;

	private boolean installedOnly = false;
	private int categoryId = -1;

	private boolean ascending;

	private boolean isFiltered = false;
	private List<Permission> unselectedPermissions;
	private int minPrivacyRating;
	private int maxPrivacyRating;
	private int minFunctionalRating;
	private int maxFunctionalRating;

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
	}

	@Override
	public void onResume() {
		super.onResume();

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

		if (isFiltered)
			apps = filter(apps);

		appData.close();

		// set custom list adapter to display apps with icon, name and rating
		ArrayAdapter<AppCompact> adapter = new AppListItemAdapter(rootActivity,
				rootActivity.getPackageManager(), apps);
		setListAdapter(adapter);
	}

	private List<AppCompact> filter(List<AppCompact> apps) {
		for (int i = 0; i < apps.size(); i++) {
			App app = apps.get(i);
			if (app.getPrivacyRating() > maxPrivacyRating
					|| app.getPrivacyRating() < minPrivacyRating)
				apps.remove(i);

			if (app.getFunctionalRating() > maxFunctionalRating
					|| app.getFunctionalRating() < minFunctionalRating)
				apps.remove(i);

			AppPermissionDataSource appPermissionData = new AppPermissionDataSource(
					rootActivity);
			appPermissionData.openReadOnly();
			ArrayList<Permission> appPermissions = appPermissionData
					.getPermissionsByAppId(app.getId());
			appPermissionData.close();

			for (Permission permission : unselectedPermissions) {
				if (appPermissions.contains(permission))
					apps.remove(i);
			}
		}

		return apps;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_fragment, container, false);
	}

	@Override
	public void onListItemClick(ListView list, View v, int position, long id) {
		Intent intent = new Intent(rootActivity, AppDetailsActivity.class);
		AppCompact app = (AppCompact) list.getItemAtPosition(position);
		intent.putExtra("AppCompact", app);
		startActivity(intent);
	}

	public void setFilterPermissions(List<Permission> unselectedPermissions) {
		isFiltered = true;
		this.unselectedPermissions = unselectedPermissions;
	}

	public void setPrivacyRatingBounds(int minPrivacyRating,
			int maxPrivacyRating) {
		isFiltered = true;
		this.minPrivacyRating = minPrivacyRating;
		this.maxPrivacyRating = maxPrivacyRating;
	}

	public void setFunctionalRatingBounds(int minFunctionalRating,
			int maxFunctionalRating) {
		isFiltered = true;
		this.minFunctionalRating = minFunctionalRating;
		this.maxFunctionalRating = maxFunctionalRating;

	}
}