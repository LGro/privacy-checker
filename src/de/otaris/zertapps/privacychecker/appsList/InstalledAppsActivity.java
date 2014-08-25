package de.otaris.zertapps.privacychecker.appsList;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import de.otaris.zertapps.privacychecker.R;

/**
 * is called by HomeActivity, handles display of installed apps
 */
public class InstalledAppsActivity extends SortableAppListActivity implements
		ActionBar.TabListener {

	final static int TAB_COUNT = 3;

	ViewPager viewPager;
	TabPagerAdapter tabPagerAdapter;
	ActionBar actionBar;

	@Override
	protected AppsList configureAppsList(AppsList appsList) {
		appsList.setInstalledOnly();
		return appsList;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// prepared for tab layout needed in future
		setContentView(R.layout.activity_installed_apps);

		// Set up the action bar.
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		viewPager = (ViewPager) findViewById(R.id.installedAppsPager);

		// always load all 3 fragments at once; suppress dynamic fragment
		// loading (otherwise would mess up getItem below)
		viewPager.setOffscreenPageLimit(TAB_COUNT);

		viewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar = getActionBar();
						actionBar.setSelectedNavigationItem(position);
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

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// remove icon
		tab.setIcon(null);
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	private class TabPagerAdapter extends FragmentStatePagerAdapter {
		public TabPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			Log.i("InstalledAppsActivity", "Tab selected: " + i);

			switch (i) {
			case 0:
				// change sorting direction
				alphabetIsAscending = !alphabetIsAscending;
				return updateListView(actionBar.getTabAt(i),
						AppsListOrder.ALPHABET, alphabetIsAscending);
			case 1:
				// change sorting direction
				privacyIsAscending = !privacyIsAscending;
				return updateListView(actionBar.getTabAt(i),
						AppsListOrder.PRIVACY_RATING, privacyIsAscending);
			case 2:
				// change sorting direction
				functionalIsAscending = !functionalIsAscending;
				return updateListView(actionBar.getTabAt(i),
						AppsListOrder.FUNCTIONAL_RATING, functionalIsAscending);
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
