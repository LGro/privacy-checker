package de.otaris.zertapps.privacychecker.appDetails.permissions;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ToggleButton;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.Detail;
import de.otaris.zertapps.privacychecker.appDetails.DetailViewHelper;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;
import de.otaris.zertapps.privacychecker.database.model.Permission;

public class PermissionsViewHelper extends DetailViewHelper {

	public PermissionsViewHelper() {

	}

	@Override
	public View getView(Context context, ViewGroup parent, Detail detail)
			throws IllegalArgumentException {
		if (!(detail instanceof Permissions))
			throw new IllegalArgumentException(
					"Illegal Detail Object. Expected Description.");

		// get permissions list from permissions detail
		Permissions permissions = (Permissions) detail;
		AppExtended app = permissions.getApp();
		List<Permission> permissionList = app.getPermissionList();

		// prepare layout
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.app_detail_permissions,
				parent, false);

		// get permission list view
		ListView listView = (ListView) rowView
				.findViewById(R.id.permissions_list);

		// add list item adapter for permissions
		listView.setAdapter(new PermissionsListItemAdapter(context,
				permissionList));
		listView.setScrollContainer(false);

		// show max 4 permissions
		ViewGroup.LayoutParams updatedLayout = listView.getLayoutParams();
		updatedLayout.height = (listView.getCount() > 4) ? 576 : 144 * listView
				.getCount();
		listView.setLayoutParams(updatedLayout);

		// get show more button
		ToggleButton button = (ToggleButton) rowView
				.findViewById(R.id.app_detail_permissions_more);

		// hide button when not more than 4 permissions are available
		if (listView.getCount() <= 4)
			button.setVisibility(View.GONE);

		button.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton toggleButton,
					boolean isChecked) {
				ListView permissionsListView = (ListView) ((View) toggleButton
						.getParent()).findViewById(R.id.permissions_list);
				// changes max 4 to all permissions
				if (isChecked) {
					ViewGroup.LayoutParams updatedLayout = permissionsListView
							.getLayoutParams();
					updatedLayout.height = 144 * permissionsListView.getCount();

					permissionsListView.setLayoutParams(updatedLayout);
				} else {
					ViewGroup.LayoutParams updatedLayout = permissionsListView
							.getLayoutParams();
					updatedLayout.height = (permissionsListView.getCount() > 4) ? 576
							: 144 * permissionsListView.getCount();

					permissionsListView.setLayoutParams(updatedLayout);
				}
			}
		});

		return rowView;

	}

}
