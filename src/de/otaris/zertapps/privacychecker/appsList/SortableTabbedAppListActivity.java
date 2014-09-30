package de.otaris.zertapps.privacychecker.appsList;

import java.util.List;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import de.otaris.zertapps.privacychecker.ImprintActivity;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;

/**
 * provides list rendering functionality for sortable lists
 */
public abstract class SortableTabbedAppListActivity extends FragmentActivity
		implements ActionBar.TabListener {

	protected ViewPager viewPager;
	protected ActionBar actionBar;
	protected ActionBar.Tab lastTabSelected = null;
	FragmentStatePagerAdapter tabPagerAdapter;

	protected List<AppCompact> apps;

	// attention: has to be initialized in subclass
	protected AppsListOrderCriterion[] tabOrder;

	protected abstract AppsListOrderCriterion[] getTabOrder();

	protected abstract List<AppCompact> getApps(
			AppsListOrderCriterion orderCriterion);

	public SortableTabbedAppListActivity() {
		tabOrder = getTabOrder();
	}

	@Override
	protected void onResume() {
		super.onResume();

		tabPagerAdapter.notifyDataSetChanged();
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// prepared for tab layout needed in future
		setContentView(R.layout.activity_apps_list);

		// Set up the action bar.
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		viewPager = (ViewPager) findViewById(R.id.appsListPager);

		// always load all fragments at once; suppress dynamic fragment
		// loading (otherwise would mess up getItem below)
		viewPager.setOffscreenPageLimit(tabOrder.length);

		viewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar = getActionBar();
						actionBar.setSelectedNavigationItem(position);

						updateTabIcon(actionBar.getTabAt(position));
					}
				});

		tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(tabPagerAdapter);

		for (AppsListOrderCriterion orderCriterion : tabOrder) {
			actionBar.addTab(actionBar.newTab()
					.setIcon(orderCriterion.getDefaultIcon())
					.setTabListener(this));
		}

		// restore the selected tab (e.g. after a rotation)
		if (savedInstanceState != null
				&& savedInstanceState.containsKey("activeTabPosition")) {
			int tabIndex = savedInstanceState.getInt("activeTabPosition");
			actionBar.getTabAt(tabIndex).select();
		}
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
	 * initializes or updates a app list to the target container
	 * 
	 * @param tab
	 *            the tab that's list should be updated
	 * @param order
	 *            the order criterion
	 * @param ascending
	 *            the order direction
	 */
	protected Fragment updateListView(ActionBar.Tab tab) {

		// initialize apps list and
		AppsList appsListFragment = new AppsList();

		apps = getApps(tabOrder[tab.getPosition()]);

		// set custom list adapter to display apps with icon, name and rating
		ArrayAdapter<AppCompact> adapter = new AppListItemAdapter(this,
				this.getPackageManager(), apps);
		appsListFragment.setListAdapter(adapter);

		return appsListFragment;
	}

	/**
	 * remember current tab and update icon to display sorting direction
	 */
	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		viewPager.setCurrentItem(tab.getPosition());
		lastTabSelected = tab;

		updateTabIcon(tab);
	}

	/**
	 * reset icon to default
	 */
	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		tab.setIcon(tabOrder[tab.getPosition()].getDefaultIcon());
	}

	/**
	 * set icon matching the sorting direction and the selected tab
	 * 
	 * @param tab
	 */
	protected void updateTabIcon(ActionBar.Tab tab) {
		int position = tab.getPosition();
		int sortingIcon = -1;

		AppsListOrderCriterion orderCriterion = tabOrder[position];

		sortingIcon = (orderCriterion.isOrderedAscending()) ? orderCriterion
				.getAscendingtIcon() : orderCriterion.getDescendingIcon();

		tab.setIcon(sortingIcon);
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// if tab has been really reselected
		if (lastTabSelected.equals(tab)) {
			// change sorting direction for current tab
			tabOrder[tab.getPosition()].invert();

			// set icon matching the sorting direction and the selected tab
			updateTabIcon(tab);

			// notify adapter about changed dataset
			tabPagerAdapter.notifyDataSetChanged();
		}
		viewPager.setCurrentItem(tab.getPosition());

		// remember this tab as the last one selected
		lastTabSelected = tab;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_imprint) {
			Intent intent = new Intent(this, ImprintActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * tab adapter that provides fragments for each tab
	 */
	protected class TabPagerAdapter extends FragmentStatePagerAdapter {
		public TabPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public Fragment getItem(int i) {
			if (0 <= i && i < tabOrder.length)
				return updateListView(actionBar.getTabAt(i));

			return null;
		}

		@Override
		public int getCount() {
			return tabOrder.length;
		}
	}
}
