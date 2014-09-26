package de.otaris.zertapps.privacychecker.appsList;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import de.otaris.zertapps.privacychecker.ImprintActivity;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.database.dataSource.CategoryDataSource;

/**
 * is called by HomeActivity, handles display of installed apps
 */
public class AppsByCategoryActivity extends SortableTabbedAppListActivity {

	@Override
	protected AppsListOrder[] getTabOrder() {
		return new AppsListOrder[] { AppsListOrder.ALPHABET.ascending(),
				AppsListOrder.PRIVACY_RATING.descending(),
				AppsListOrder.FUNCTIONAL_RATING.descending() };
	}

	@Override
	protected AppsList configureAppsList(AppsList appsList) {
		Intent intent = getIntent();
		appsList.setCageoryId(intent.getIntExtra("id", -1));
		return appsList;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// prepared for tab layout needed in future
		setContentView(R.layout.activity_apps_by_category);

		// Set up the action bar.
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		viewPager = (ViewPager) findViewById(R.id.appsByCategoryPager);

		// set action bar name
		CategoryDataSource categoryData = new CategoryDataSource(this);
		categoryData.open();
		Intent myIntent = getIntent();
		int categoryId = myIntent.getIntExtra("id", 3);
		String name = categoryData.getCategoryById(categoryId).getLabel();
		categoryData.close();

		actionBar.setTitle(name);

		// always load all 3 fragments at once; suppress dynamic fragment
		// loading (otherwise would mess up getItem below)
		viewPager.setOffscreenPageLimit(tabOrder.length);

		viewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar = getActionBar();
						actionBar.setSelectedNavigationItem(position);
						int sortingIcon = 0;
						// set icon for tab
						switch (position) {
						case 0:
							sortingIcon = (tabOrder[position]
									.isOrderedAscending()) ? R.drawable.name_ascending
									: R.drawable.name_descending;
							break;
						case 1:
							sortingIcon = (tabOrder[position]
									.isOrderedAscending()) ? R.drawable.privacyrating_descending
									: R.drawable.privacyrating_ascending;
							break;
						case 2:
							sortingIcon = (tabOrder[position]
									.isOrderedAscending()) ? R.drawable.popularityrating_descending
									: R.drawable.popularityrating_ascending;
							break;
						}
						actionBar.getTabAt(position).setIcon(sortingIcon);
					}
				});

		tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(tabPagerAdapter);

		// For each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setIcon(R.drawable.name_ascending)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setTabListener(this)
				.setIcon(R.drawable.privacyrating_default));
		actionBar.addTab(actionBar.newTab().setTabListener(this)
				.setIcon(R.drawable.popularityrating_default));

		// restore the selected tab (e.g. after a rotation)
		if (savedInstanceState != null
				&& savedInstanceState.containsKey("activeTabPosition")) {
			int tabIndex = savedInstanceState.getInt("activeTabPosition");
			actionBar.getTabAt(tabIndex).select();
			updateListView(actionBar.getTabAt(tabIndex), tabOrder[tabIndex],
					tabOrder[tabIndex].isOrderedAscending());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
			if (i >= 0 && i < 3)
				return updateListView(actionBar.getTabAt(i), tabOrder[i],
						tabOrder[i].isOrderedAscending());

			return null;
		}

		@Override
		public int getCount() {
			return tabOrder.length;
		}
	}

}
