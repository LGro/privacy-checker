package de.otaris.zertapps.privacychecker.appsList;

import java.util.HashSet;
import java.util.List;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Spinner;
import de.otaris.zertapps.privacychecker.R;
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
	protected HashSet<Permission> unselectedPermissions = new HashSet<Permission>();
	protected int minPrivacyRating = 0;
	protected int maxPrivacyRating = 5;
	protected int minFunctionalRating = 0;
	protected int maxFunctionalRating = 5;

	// is true if there have been filter settings selected
	protected boolean alreadyFiltered = false;

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

		appsList = configureAppsList(appsList, tab.getPosition() == 3);

		return appsList;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		viewPager.setCurrentItem(tab.getPosition());
		lastTabSelected = tab;

		updateTabIcon(tab);

		if (tab.getPosition() == 3 && !alreadyFiltered)
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
		case 3:
			tab.setIcon(R.drawable.filter_text_default);
			break;
		}
	}

	/**
	 * set icon matching the sorting direction and the selected tab
	 * 
	 * @param tab
	 */
	protected void updateTabIcon(ActionBar.Tab tab) {
		int position = tab.getPosition();
		Drawable sortingIcon = null;

		switch (position) {
		case 0:
			sortingIcon = (tabOrder[0].isOrderedAscending()) ? getResources()
					.getDrawable(R.drawable.name_ascending) : getResources()
					.getDrawable(R.drawable.name_descending);
			break;
		case 1:
			sortingIcon = (tabOrder[1].isOrderedAscending()) ? getResources()
					.getDrawable(R.drawable.privacyrating_ascending)
					: getResources().getDrawable(
							R.drawable.privacyrating_descending);
			break;
		case 2:
			sortingIcon = (tabOrder[2].isOrderedAscending()) ? getResources()
					.getDrawable(R.drawable.popularityrating_ascending)
					: getResources().getDrawable(
							R.drawable.popularityrating_descending);
			break;
		case 3:
			sortingIcon = actionBar.getTabAt(position).getIcon();
			break;
		}

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

			if (tab.getPosition() == 3)
				callFilterOverlay();

			// notify adapter about changed dataset
			tabPagerAdapter.notifyDataSetChanged();
		}
		viewPager.setCurrentItem(tab.getPosition());

		// remember this tab as the last one selected
		lastTabSelected = tab;
	}

	/**
	 * Sets the filter values to the attribute values and therefore either set
	 * default values or reset to previously selected filter settings.
	 * 
	 * @param dialog
	 *            filter dialog to derive the UI elements from
	 * @param permissions
	 *            all permissions the current app requires
	 */
	@SuppressWarnings("unchecked")
	private void setValuesForFilterOverlay(Dialog dialog,
			List<Permission> permissions) {

		Spinner minPrivacyRatingSpinner = (Spinner) dialog
				.findViewById(R.id.filter_overlay_privacy_option_number_picker1);
		Spinner maxPrivacyRatingSpinner = (Spinner) dialog
				.findViewById(R.id.filter_overlay_privacy_option_number_picker2);
		Spinner minFunctionalRatingSpinner = (Spinner) dialog
				.findViewById(R.id.filter_overlay_functional_option_number_picker1);
		Spinner maxFunctionalRatingSpinner = (Spinner) dialog
				.findViewById(R.id.filter_overlay_functional_option_number_picker2);

		ArrayAdapter<String> spinnerAdapter = null;
		int spinnerPosition = -1;

		// set min/max privacy/functional rating
		spinnerAdapter = (ArrayAdapter<String>) minPrivacyRatingSpinner
				.getAdapter();
		spinnerPosition = spinnerAdapter.getPosition(minPrivacyRating + "");
		minPrivacyRatingSpinner.setSelection(spinnerPosition);

		spinnerAdapter = (ArrayAdapter<String>) maxPrivacyRatingSpinner
				.getAdapter();
		spinnerPosition = spinnerAdapter.getPosition(maxPrivacyRating + "");
		maxPrivacyRatingSpinner.setSelection(spinnerPosition);

		spinnerAdapter = (ArrayAdapter<String>) minFunctionalRatingSpinner
				.getAdapter();
		spinnerPosition = spinnerAdapter.getPosition(minFunctionalRating + "");
		minFunctionalRatingSpinner.setSelection(spinnerPosition);

		spinnerAdapter = (ArrayAdapter<String>) maxFunctionalRatingSpinner
				.getAdapter();
		spinnerPosition = spinnerAdapter.getPosition(maxFunctionalRating + "");
		maxFunctionalRatingSpinner.setSelection(spinnerPosition);

		ListView permissionsList = (ListView) dialog
				.findViewById(R.id.filter_overlay_permissions);

		// check all permissions that aren't in unselectedPermissions set
		for (int i = 0; i < permissions.size(); i++) {
			Permission permission = permissions.get(i);
			permissionsList.setItemChecked(i,
					!unselectedPermissions.contains(permission));
		}
	}

	/**
	 * display filter overlay
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

			/**
			 * initialize all elements of the filter overlay
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
			}

			@Override
			public void onClick(View v) {
				initializeViews(v.getRootView());

				// save filter values
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

				// reload tabs and therefore apps lists
				tabPagerAdapter.notifyDataSetChanged();

				alreadyFiltered = true;

				// hide filter overlay
				dialog.hide();
			}
		});

		// set onclick listener for permissions
		permissionsList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// get selected permission
				Permission permission = (Permission) parent
						.getItemAtPosition(position);
				CheckedTextView textView = (CheckedTextView) view
						.findViewById(android.R.id.text1);

				// update unselectedPermissions list according to
				// CheckedTextView's state
				if (textView.isChecked()) {
					unselectedPermissions.remove(permission);
				} else {
					unselectedPermissions.add(permission);
				}
			}
		});

		// get a list of all translated permissions
		PermissionDataSource permissionData = new PermissionDataSource(this);
		permissionData.openReadOnly();
		List<Permission> permissions = permissionData
				.getTranslatedPermissions();
		permissionData.close();
		// set adapter with translated permissions list
		permissionsList.setAdapter(new PermissionsAdapter(this, permissions));

		// set values to default or previously selected values
		setValuesForFilterOverlay(dialog, permissions);

		// display filter overlay
		dialog.show();
	}

	/**
	 * Class to handle the selectable list of permissions in the filter overlay.
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

			return rowView;

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
