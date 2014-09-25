package de.otaris.zertapps.privacychecker.appsList;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Spinner;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appsList.AppsByCategoryActivity.TabPagerAdapter;
import de.otaris.zertapps.privacychecker.database.dataSource.PermissionDataSource;
import de.otaris.zertapps.privacychecker.database.model.Permission;

/**
 * provides list rendering functionality for sortable lists
 */
public abstract class SortableTabbedAppListActivity extends FragmentActivity
		implements ActionBar.TabListener {

	protected ViewPager viewPager;
	protected ActionBar actionBar;
	protected ActionBar.Tab lastTabSelected = null;
	FragmentStatePagerAdapter tabPagerAdapter;

	// parameters to save filter settings
	protected List<Permission> unselectedPermissions = new ArrayList<Permission>();
	protected int minPrivacyRating = 0;
	protected int maxPrivacyRating = 5;
	protected int minFunctionalRating = 0;
	protected int maxFunctionalRating = 5;

	// attention: has to be initialized in subclass
	protected boolean[] tabOrderedAscending;

	protected abstract boolean[] getTabOrderedAscending();

	public SortableTabbedAppListActivity() {
		tabOrderedAscending = getTabOrderedAscending();
	}

	/**
	 * activity specific modification of the apps list
	 * 
	 * @param appsList
	 *            prepared unmodified apps list
	 * @return (if needed) modified apps list
	 */
	protected AppsList configureAppsList(AppsList appsList, boolean filter) {
		if (filter) {
			appsList.setFilterPermissions(unselectedPermissions);
			appsList.setPrivacyRatingBounds(minPrivacyRating, maxPrivacyRating);
			appsList.setFunctionalRatingBounds(minFunctionalRating,
					maxFunctionalRating);
		}

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
		appsList.setRootActivity(this);

		appsList = configureAppsList(appsList, tab.getPosition() == 3);

		return appsList;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		viewPager.setCurrentItem(tab.getPosition());
		lastTabSelected = tab;

		if (tab.getPosition() == 3)
			callFilterOverlay();
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
		}
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// if tab has been really reselected
		if (lastTabSelected.equals(tab)) {
			// change sorting direction for current tab
			tabOrderedAscending[tab.getPosition()] = !tabOrderedAscending[tab
					.getPosition()];

			// set icon matching the sorting direction and the selected tab
			int sortingIcon = 0;
			switch (tab.getPosition()) {
			case 0:
				sortingIcon = (tabOrderedAscending[tab.getPosition()]) ? R.drawable.name_ascending
						: R.drawable.name_descending;
				break;
			case 1:
				sortingIcon = (tabOrderedAscending[tab.getPosition()]) ? R.drawable.privacyrating_descending
						: R.drawable.privacyrating_ascending;
				break;
			case 2:
				sortingIcon = (tabOrderedAscending[tab.getPosition()]) ? R.drawable.popularityrating_descending
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

	/**
	 * display filter overlay
	 * 
	 */
	private void callFilterOverlay() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.filter_overlay);
		dialog.getWindow()
				.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

		ListView permissionsList = (ListView) dialog
				.findViewById(R.id.filter_overlay_permissions);
		Button submit = (Button) dialog
				.findViewById(R.id.filter_overlay_submit);

		submit.setOnClickListener(new OnClickListener() {
			Spinner minPrivacyRatingSpinner;
			Spinner maxPrivacyRatingSpinner;
			Spinner minFunctionalRatingSpinner;
			Spinner maxFunctionalRatingSpinner;
			ListView permissionsList;

			/**
			 * initalize all elements of the filter overlay
			 * 
			 * @param parent
			 */

			private void initializeViews(View parent) {

				minPrivacyRatingSpinner = (Spinner) parent
						.findViewById(R.id.filter_overlay_privacy_option_number_picker1);
				maxPrivacyRatingSpinner = (Spinner) parent
						.findViewById(R.id.filter_overlay_privacy_option_number_picker2);
				minFunctionalRatingSpinner = (Spinner) parent
						.findViewById(R.id.filter_overlay_functional_option_number_picker1);
				maxFunctionalRatingSpinner = (Spinner) parent
						.findViewById(R.id.filter_overlay_functional_option_number_picker2);
				permissionsList = (ListView) parent
						.findViewById(R.id.filter_overlay_permissions);
			}

			@Override
			public void onClick(View v) {
				initializeViews(v.getRootView());

				minPrivacyRating = Integer.parseInt(minPrivacyRatingSpinner
						.getSelectedItem().toString());
				maxPrivacyRating = Integer.parseInt(maxPrivacyRatingSpinner
						.getSelectedItem().toString());
				minFunctionalRating = Integer
						.parseInt(minFunctionalRatingSpinner.getSelectedItem()
								.toString());
				maxFunctionalRating = Integer
						.parseInt(maxFunctionalRatingSpinner.getSelectedItem()
								.toString());
 
				SparseBooleanArray checkedPermissions = permissionsList
						.getCheckedItemPositions();

				PermissionsAdapter adapter = (PermissionsAdapter) permissionsList
						.getAdapter();

				for (int i = 0; i < adapter.getCount(); i++)
					if (!checkedPermissions.get(i))
						unselectedPermissions.add(adapter.getItem(i));

				AppsList appsList = (AppsList) updateListView(lastTabSelected, AppsListOrder.ALPHABET, true);
				appsList.onResume();
				tabPagerAdapter.notifyDataSetChanged();
				dialog.hide();
			}
		});

		// get a list of all translated permissions
		PermissionDataSource permissionData = new PermissionDataSource(this);
		permissionData.openReadOnly();
		List<Permission> permissions = permissionData
				.getTranslatedPermissions();
		permissionData.close();
		permissionsList.setAdapter(new PermissionsAdapter(this, permissions));

		dialog.show();
	}

	/**
	 * Class do handle the selectable list of permissions in the filter overlay
	 *
	 */
	private class PermissionsAdapter extends ArrayAdapter<Permission> {
		private final Context context;
		private final List<Permission> permissionsList;

		public PermissionsAdapter(Context context,
				List<Permission> permissionsList) {
			super(context, android.R.layout.simple_list_item_multiple_choice,
					permissionsList);
			this.context = context;
			this.permissionsList = permissionsList;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(
					android.R.layout.simple_list_item_multiple_choice, parent,
					false);

			CheckedTextView textView = (CheckedTextView) rowView
					.findViewById(android.R.id.text1);
			textView.setText(permissionsList.get(position).getLabel());

			textView.setChecked(true);

			return rowView;

		}
	}

}
