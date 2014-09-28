package de.otaris.zertapps.privacychecker.appsList;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import de.otaris.zertapps.privacychecker.database.dataSource.AppCompactDataSource;
import de.otaris.zertapps.privacychecker.database.dataSource.CategoryDataSource;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;

/**
 * is called by HomeActivity, handles display of apps from one category
 */
public class AppsByCategoryActivity extends
		FilterableSortableTabbedAppListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set action bar title
		CategoryDataSource categoryData = new CategoryDataSource(this);
		categoryData.open();
		Intent myIntent = getIntent();
		int categoryId = myIntent.getIntExtra("id", 1);
		String name = categoryData.getCategoryById(categoryId).getLabel();
		categoryData.close();

		actionBar.setTitle(name);
	}

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

		Intent intent = getIntent();
		int categoryId = intent.getIntExtra("id", -1);

		AppCompactDataSource appData = new AppCompactDataSource(this);
		appData.openReadOnly();

		List<AppCompact> apps = (categoryId != -1) ? appData.getAppsByCategory(
				categoryId, orderCriterion) : appData
				.getAllApps(orderCriterion);

		appData.close();

		return apps;
	}

}
