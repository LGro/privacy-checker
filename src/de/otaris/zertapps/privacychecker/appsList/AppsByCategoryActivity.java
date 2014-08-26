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
import de.otaris.zertapps.privacychecker.R;

/**
 * is called by HomeActivity, handles display of installed apps
 */
public class AppsByCategoryActivity extends SortableTabbedAppListActivity {

	final static int TAB_COUNT = 3;

	@Override
	protected boolean[] getTabOrderedAscending() {
		// order ascending for alphabet, privacy rating, functional rating
		return new boolean[] { true, true, false };
	}

	// overwrite default privacy sorting direction
	protected boolean privacyIsAscending = false;

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

		// always load all 3 fragments at once; suppress dynamic fragment
		// loading (otherwise would mess up getItem below)
		viewPager.setOffscreenPageLimit(TAB_COUNT);

		viewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar = getActionBar();
						actionBar.setSelectedNavigationItem(position);

						int sortingIcon = (tabOrderedAscending[position]) ? R.drawable.ascending
								: R.drawable.descending;

						actionBar.getTabAt(position).setIcon(sortingIcon);

					}
				});

		tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(tabPagerAdapter);

		// For each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setText(R.string.title_alphabet)
				.setTabListener(this).setIcon(R.drawable.ascending));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_privacy)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_functional)
				.setTabListener(this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.installed_apps, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
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
			switch (i) {
			case 0:
				return updateListView(actionBar.getTabAt(i),
						AppsListOrder.ALPHABET, tabOrderedAscending[0]);
			case 1:
				return updateListView(actionBar.getTabAt(i),
						AppsListOrder.PRIVACY_RATING, tabOrderedAscending[1]);
			case 2:
				return updateListView(actionBar.getTabAt(i),
						AppsListOrder.FUNCTIONAL_RATING, tabOrderedAscending[2]);
			default:
				return null;
			}
		}

		@Override
		public int getCount() {
			return TAB_COUNT;
		}
	}

}
