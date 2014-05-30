package de.otaris.zertapps.privacychecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class InstalledAppsActivity extends ListActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * // prepared for tab layout needed in future
		 * setContentView(R.layout.activity_installed_apps);
		 * 
		 * // Set up the action bar. final ActionBar actionBar = getActionBar();
		 * actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		 * 
		 * // Create the adapter that will return a fragment for each of the
		 * three // primary sections of the activity. mSectionsPagerAdapter =
		 * new SectionsPagerAdapter(getFragmentManager());
		 * 
		 * // Set up the ViewPager with the sections adapter. mViewPager =
		 * (ViewPager) findViewById(R.id.pager);
		 * mViewPager.setAdapter(mSectionsPagerAdapter);
		 * 
		 * // When swiping between different sections, select the corresponding
		 * // tab. We can also use ActionBar.Tab#select() to do this if we have
		 * // a reference to the Tab. mViewPager .setOnPageChangeListener(new
		 * ViewPager.SimpleOnPageChangeListener() {
		 * 
		 * @Override public void onPageSelected(int position) {
		 * actionBar.setSelectedNavigationItem(position); } });
		 * 
		 * // For each of the sections in the app, add a tab to the action bar.
		 * for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) { //
		 * Create a tab with text corresponding to the page title defined by //
		 * the adapter. Also specify this Activity object, which implements //
		 * the TabListener interface, as the callback (listener) for when //
		 * this tab is selected. actionBar.addTab(actionBar.newTab()
		 * .setText(mSectionsPagerAdapter.getPageTitle(i))
		 * .setTabListener(this)); }
		 */

		// set custom list adapter to display apps with icon, name and rating
		ArrayAdapter<ApplicationInfo> adapter = new AppListItemAdapter(this, getPackageManager(),
				getInstalledApps(getPackageManager()));
		setListAdapter(adapter);
	}

	// retrieve all locally installed apps from android API
	private ApplicationInfo[] getInstalledApps(PackageManager pm) {
		// initialize list with all installed apps
		List<ApplicationInfo> apps = pm
				.getInstalledApplications(PackageManager.GET_META_DATA);
		
		// create result list without apps with no name (system apps?)
		List<ApplicationInfo> results = new ArrayList<ApplicationInfo>();
		for (int i = 0; i < apps.size(); i++) {
			if (apps.get(i).className != null && apps.get(i).className != "")
				results.add(apps.get(i));
		}
		
		return results.toArray(new ApplicationInfo[0]);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.i("InstalledAppsAcitivity", "clicked on installed app");
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
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_privacy);
			case 1:
				return getString(R.string.title_alphabet);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_installed_apps,
					container, false);
			return rootView;
		}
	}

}
