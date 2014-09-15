package de.otaris.zertapps.privacychecker.appsList;

import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.UserStudyLogger;
import de.otaris.zertapps.privacychecker.R.drawable;
import de.otaris.zertapps.privacychecker.R.id;
import de.otaris.zertapps.privacychecker.R.layout;
import de.otaris.zertapps.privacychecker.R.menu;
import de.otaris.zertapps.privacychecker.R.string;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * is called by HomeActivity, handles display of installed apps
 */
public class AllAppsActivity extends SortableAppListActivity implements
		ActionBar.TabListener {
	
	// overwrite sorting direction for privacy rating
	protected boolean privacyIsAscending = false;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected int getTargetContainer() {
		return R.id.allAppsContainer;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// prepared for tab layout needed in future
		setContentView(R.layout.activity_all_apps);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// For each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setText(R.string.title_category)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_privacy)
				.setTabListener(this).setIcon(R.drawable.descending));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_functional)
				.setTabListener(this).setIcon(R.drawable.descending));
		
		UserStudyLogger.getInstance().log("activity_all");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.all_apps, menu);
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
			CategoryList categoryList = new CategoryList();
			categoryList.setRootActivity(this);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.allAppsContainer, categoryList).commit();
			break;
		case 1:
			updateListView(tab, AppsListOrder.PRIVACY_RATING, privacyIsAscending);
			break;
		case 2:
			updateListView(tab, AppsListOrder.FUNCTIONAL_RATING, functionalIsAscending);
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
			// do nothing ...
			break;
		case 1:
			// change sorting direction
			privacyIsAscending = !privacyIsAscending;
			updateListView(tab, AppsListOrder.PRIVACY_RATING, privacyIsAscending);
			break;
		case 2:
			// change sorting direction
			functionalIsAscending = !functionalIsAscending;
			updateListView(tab, AppsListOrder.FUNCTIONAL_RATING, functionalIsAscending);
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
			View rootView = inflater.inflate(R.layout.fragment_all_apps,
					container, false);
			return rootView;
		}
	}

}
