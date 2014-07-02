package de.otaris.zertapps.privacychecker;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * is called by HomeActivity, handles display of installed apps
 */
public class InstalledAppsActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// prepared for tab layout needed in future
		setContentView(R.layout.activity_installed_apps);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// For each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setText(R.string.title_privacy)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_alphabet)
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

		switch (tab.getPosition()) {
		case 0:
			AppsList installedAppsList2 = new AppsList();
			installedAppsList2.setInstalledOnly();
			installedAppsList2.setOrder(AppsListOrder.PRIVACY_RATING, true);
			installedAppsList2.setRootActivity(this);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.installedAppsContainer, installedAppsList2)
					.commit();
			break;
		case 1:
			AppsList installedAppsList1 = new AppsList();
			installedAppsList1.setInstalledOnly();
			installedAppsList1.setOrder(AppsListOrder.ALPHABET, true);
			installedAppsList1.setRootActivity(this);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.installedAppsContainer, installedAppsList1)
					.commit();
			break;
		default:
			break;
		}
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {

		switch (tab.getPosition()) {
		case 0:
			AppsList installedAppsList2 = new AppsList();
			installedAppsList2.setInstalledOnly();
			installedAppsList2.setOrder(AppsListOrder.PRIVACY_RATING, false);
			installedAppsList2.setRootActivity(this);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.installedAppsContainer, installedAppsList2)
					.commit();
			break;
		case 1:
			AppsList installedAppsList1 = new AppsList();
			installedAppsList1.setInstalledOnly();
			installedAppsList1.setOrder(AppsListOrder.ALPHABET, false);
			installedAppsList1.setRootActivity(this);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.installedAppsContainer, installedAppsList1)
					.commit();
			break;
		default:
			break;
		}
	}

	/**
	 * Auto-generated code A placeholder fragment containing a simple view.
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
