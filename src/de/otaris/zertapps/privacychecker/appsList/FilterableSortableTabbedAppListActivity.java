package de.otaris.zertapps.privacychecker.appsList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
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
import de.otaris.zertapps.privacychecker.database.dataSource.AppPermissionDataSource;
import de.otaris.zertapps.privacychecker.database.dataSource.PermissionDataSource;
import de.otaris.zertapps.privacychecker.database.interfaces.App;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;
import de.otaris.zertapps.privacychecker.database.model.Permission;

public abstract class FilterableSortableTabbedAppListActivity extends
		SortableTabbedAppListActivity {

	// parameters to save filter settings
	protected HashSet<Permission> unselectedPermissions = new HashSet<Permission>();
	protected int minPrivacyRating = 0;
	protected int maxPrivacyRating = 5;
	protected int minFunctionalRating = 0;
	protected int maxFunctionalRating = 5;

	private final int filterTabPosition = 3;

	// is true if there have been filter settings selected
	protected boolean alreadyFiltered = false;

	/**
	 * Contains filter logic related to the filter overlay.
	 * 
	 * Removes the Apps form a given List of Apps which are out of the given
	 * filter bounds or require excluded permissions.
	 * 
	 * @param apps
	 *            list of apps
	 * @return apps filtered list
	 */
	protected List<AppCompact> filter(List<AppCompact> apps) {
		for (int i = 0; i < apps.size(); i++) {
			App app = apps.get(i);
			// the privacy rating doesn't lie in [min;max]
			boolean outOfPrivacyRatingBounds = app.getPrivacyRating() > maxPrivacyRating
					|| app.getPrivacyRating() < minPrivacyRating;

			// the functional rating doesn't exceed the maximum and
			boolean outOfFunctionalRatingBounds = app.getFunctionalRating() != -1
					&& app.getFunctionalRating() < minFunctionalRating
					|| app.getFunctionalRating() > maxFunctionalRating;

			// the app has no functional rating and there is set a minimum
			boolean noFunctionalRatingAvailableAndActiveFilter = app
					.getFunctionalRating() == -1 && minFunctionalRating > 0;

			// remove app if it doesn't match all the filter criteria
			if (outOfPrivacyRatingBounds || outOfFunctionalRatingBounds
					|| noFunctionalRatingAvailableAndActiveFilter) {
				apps.remove(i);
				i--;
				continue;
			}

			// get required permissions for current app
			AppPermissionDataSource appPermissionData = new AppPermissionDataSource(
					this);
			appPermissionData.openReadOnly();
			ArrayList<Permission> appPermissions = appPermissionData
					.getPermissionsByAppId(app.getId());
			appPermissionData.close();

			// remove app if the required permissions contain at least one
			// permission that has been excluded
			if (unselectedPermissions != null) {
				for (Permission permission : unselectedPermissions) {
					if (appPermissions.contains(permission)) {
						apps.remove(i);
						i--;
						break;
					}
				}
			}
		}

		return apps;
	}

	/**
	 * override icon at filterTabPosition
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		actionBar.getTabAt(filterTabPosition).setIcon(
				R.drawable.filter_text_default);
	}

	/**
	 * only update icon when current tab isn't the filter tab
	 * 
	 * @param tab
	 */
	@Override
	protected void updateTabIcon(ActionBar.Tab tab) {
		if (!(tab.getPosition() == filterTabPosition))
			super.updateTabIcon(tab);
	}

	/**
	 * only update icon when current tab isn't the filter tab
	 */
	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		if (!(tab.getPosition() == filterTabPosition))
			super.onTabUnselected(tab, fragmentTransaction);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		super.onTabSelected(tab, fragmentTransaction);

		if (tab.getPosition() == filterTabPosition && !alreadyFiltered)
			callFilterOverlay();
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// if tab has been really reselected
		if (lastTabSelected.equals(tab)
				&& tab.getPosition() == filterTabPosition)
			callFilterOverlay();

		super.onTabReselected(tab, fragmentTransaction);
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
		ListFragment appsListFragment = new ListFragment();

		apps = getApps(tabOrder[tab.getPosition()]);

		if (tab.getPosition() == filterTabPosition)
			apps = filter(apps);

		// set custom list adapter to display apps with icon, name and rating
		ArrayAdapter<AppCompact> adapter = new AppListItemAdapter(this,
				this.getPackageManager(), apps);
		appsListFragment.setListAdapter(adapter);

		return appsListFragment;
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
}
