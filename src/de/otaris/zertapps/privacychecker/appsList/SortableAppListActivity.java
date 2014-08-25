package de.otaris.zertapps.privacychecker.appsList;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import de.otaris.zertapps.privacychecker.R;

/**
 * provides list rendering functionality for sortable app lists
 */
public abstract class SortableAppListActivity extends FragmentActivity {

	protected boolean privacyIsAscending = true;
	protected boolean alphabetIsAscending = true;
	protected boolean functionalIsAscending = false;

	/**
	 * activity specific modification of the apps list
	 * 
	 * @param appsList
	 *            prepared unmodified apps list
	 * @return (if needed) modified apps list
	 */
	protected AppsList configureAppsList(AppsList appsList) {
		return appsList;
	}

	/**
	 * initializes or updates a app list to the target container
	 * 
	 * @param tab
	 *            the tab that's list should be updated
	 * @param order
	 *            the order criterion
	 * @param ascending
	 *            the order direction
	 */
	protected Fragment updateListView(ActionBar.Tab tab, AppsListOrder order,
			boolean ascending) {

		// set direction icon
		if (ascending) {
			tab.setIcon(R.drawable.ascending);
		} else {
			tab.setIcon(R.drawable.descending);
		}

		// initialize apps list and
		AppsList appsList = new AppsList();
		appsList.setOrder(order, ascending);
		appsList.setRootActivity(this);

		appsList = configureAppsList(appsList);

		return appsList;
	}
}
