package de.otaris.zertapps.privacychecker.appDetails.rateApp.permissionsExpected;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.Registry;
import de.otaris.zertapps.privacychecker.database.model.Permission;

/**
 * handles In/Export between informations about Permissions (expected/not
 * expected) and the registry
 *
 */

public class PermissionsExpectedItemAdapter extends ArrayAdapter<Permission> {
	private final Context context;
	private final List<Permission> permissionsList;

	public PermissionsExpectedItemAdapter(Context context,
			List<Permission> permissionsList) {
		super(context, R.layout.app_detail_list_item, permissionsList);
		this.context = context;
		this.permissionsList = permissionsList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(
				R.layout.app_detail_rate_app_overlay_item, parent, false);

		// Get the objects from the view
		TextView permissionsNumberTextView = (TextView) rowView
				.findViewById(R.id.app_detail_rate_app_permissions_number);
		TextView permissionLabelTextView = (TextView) rowView
				.findViewById(R.id.app_detail_rate_app_permissions_label);
		CheckBox expectedCheckBox = (CheckBox) rowView
				.findViewById(R.id.app_detail_rate_app_permissions_expected_checkbox);

		// Set the appropriate information to the objects
		permissionsNumberTextView.setText((position + 1) + ".");
		permissionLabelTextView.setText(permissionsList.get(position)
				.getLabel());

		// get permission rating object from registry
		Registry reg = Registry.getInstance();
		PermissionsExpected permissionsRatingElement = (PermissionsExpected) reg
				.getRatingElement(PermissionsExpected.class);

		// get stored expected value for current permission
		expectedCheckBox.setChecked(permissionsRatingElement
				.expectedPermission(permissionsList.get(position)));

		expectedCheckBox.setTag(permissionsList.get(position));
		expectedCheckBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// get permission rating object from registry
						Registry reg = Registry.getInstance();
						PermissionsExpected permissionsRatingElement = (PermissionsExpected) reg
								.getRatingElement(PermissionsExpected.class);
						permissionsRatingElement.setPermissionExpected(
								(Permission) buttonView.getTag(), isChecked);
						reg.updateRatingElement(PermissionsExpected.class,
								permissionsRatingElement);
					}
				});

		return rowView;
	}
}
