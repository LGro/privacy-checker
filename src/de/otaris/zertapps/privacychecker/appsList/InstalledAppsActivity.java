package de.otaris.zertapps.privacychecker.appsList;

import java.util.List;

import de.otaris.zertapps.privacychecker.database.dataSource.AppCompactDataSource;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;

/**
 * is called by HomeActivity, handles display of installed apps (a sortable list
 * of apps)
 */
public class InstalledAppsActivity extends
		FilterableSortableTabbedAppListActivity {

	@Override
	protected AppsListOrderCriterion[] getTabOrder() {
		return new AppsListOrderCriterion[] {
				AppsListOrderCriterion.ALPHABET.ascending(),
				AppsListOrderCriterion.PRIVACY_RATING.descending(),
				AppsListOrderCriterion.FUNCTIONAL_RATING.descending(),
				AppsListOrderCriterion.ALPHABET.ascending() };
	}

	@Override
	protected List<AppCompact> getApps(AppsListOrderCriterion orderCriterion) {
		AppCompactDataSource appData = new AppCompactDataSource(this);
		appData.open();

		List<AppCompact> apps = appData.getInstalledApps(orderCriterion);

		return apps;
	}
}
