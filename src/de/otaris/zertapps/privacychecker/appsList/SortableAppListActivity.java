package de.otaris.zertapps.privacychecker.appsList;

import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.UserStudyLogger;
import android.app.ActionBar;
import android.support.v4.app.FragmentActivity;

/**
 * provides list rendering functionality for sortable app lists
 */
public abstract class SortableAppListActivity extends FragmentActivity {

	protected boolean privacyIsAscending = true;
	protected boolean alphabetIsAscending = true;
	protected boolean functionalIsAscending = false;
	

	/**
	 * returns activity specific target container for apps list
	 */
	protected abstract int getTargetContainer();
	
	/**
	 * activity specific modification of the apps list
	 * 
	 * @param appsList prepared unmodified apps list
	 * @return (if needed) modified apps list
	 */
	protected AppsList configureAppsList(AppsList appsList) {
		return appsList;
	}
	
	/**
	 * initializes or updates a app list to the target container
	 * 
	 * @param tab the tab that's list should be updated
	 * @param order the order criterion
	 * @param ascending the order direction
	 */
	protected void updateListView(ActionBar.Tab tab, AppsListOrder order, boolean ascending){
		
		// set direction icon
		if (ascending) {
			tab.setIcon(R.drawable.ascending);
		} else {
			tab.setIcon(R.drawable.descending);
		}
		
		// initialize apps list and
		AppsList appsList = new AppsList();
		appsList.setOrder(order, ascending);
		
		appsList = configureAppsList(appsList);
		
		getSupportFragmentManager().beginTransaction()
				.replace(getTargetContainer(), appsList)
				.commit();
		
		UserStudyLogger.getInstance().log("appsList_updateOrder");
	}
}
