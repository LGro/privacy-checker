package de.otaris.zertapps.privacychecker.appsList;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import de.otaris.zertapps.privacychecker.R;

/**
 * provides list rendering functionality for sortable lists
 */
public abstract class SortableTabbedAppListActivity extends FragmentActivity
		implements ActionBar.TabListener {

	protected ViewPager viewPager;
	protected ActionBar actionBar;
	protected ActionBar.Tab lastTabSelected = null;
	FragmentStatePagerAdapter tabPagerAdapter;

	// attention: has to be initialized in subclass
	protected AppsListOrder[] tabOrder;

	protected abstract AppsListOrder[] getTabOrder();

	public SortableTabbedAppListActivity() {
		tabOrder = getTabOrder();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		/*
		 * Save UI state changes to the savedInstanceState. This bundle will be
		 * passed to onCreate if the process is killed and restarted.
		 */
		savedInstanceState.putInt("activeTabPosition",
				lastTabSelected.getPosition());
	}

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

		// initialize apps list and
		AppsList appsList = new AppsList();
		appsList.setOrder(order, ascending);

		appsList = configureAppsList(appsList);

		return appsList;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		viewPager.setCurrentItem(tab.getPosition());
		lastTabSelected = tab;
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// set default icons
		switch (tab.getPosition()) {
		case 0:
			tab.setIcon(R.drawable.name_default);
			break;
		case 1:
			tab.setIcon(R.drawable.privacyrating_default);
			break;
		case 2:
			tab.setIcon(R.drawable.popularityrating_default);
			break;
		case 3:
			tab.setText(R.string.filter);
			break;
		}
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// if tab has been really reselected
		if (lastTabSelected.equals(tab)) {
			// change sorting direction for current tab
			tabOrder[tab.getPosition()].invert();

			// set icon matching the sorting direction and the selected tab
			int sortingIcon = 0;
			switch (tab.getPosition()) {
			case 0:
				sortingIcon = (tabOrder[tab.getPosition()].isOrderedAscending()) ? R.drawable.name_ascending
						: R.drawable.name_descending;
				break;
			case 1:
				sortingIcon = (tabOrder[tab.getPosition()].isOrderedAscending()) ? R.drawable.privacyrating_descending
						: R.drawable.privacyrating_ascending;
				break;
			case 2:
				sortingIcon = (tabOrder[tab.getPosition()].isOrderedAscending()) ? R.drawable.popularityrating_descending
						: R.drawable.popularityrating_ascending;
				break;

			}
			tab.setIcon(sortingIcon);

			// notify adapter about changed dataset
			tabPagerAdapter.notifyDataSetChanged();
		}
		viewPager.setCurrentItem(tab.getPosition());

		// remember this tab as the last one selected
		lastTabSelected = tab;
	}

}
