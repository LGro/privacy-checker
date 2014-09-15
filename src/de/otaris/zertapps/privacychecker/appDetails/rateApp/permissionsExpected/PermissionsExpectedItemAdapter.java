package de.otaris.zertapps.privacychecker.appDetails.rateApp.permissionsExpected;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

	TextView permissionsNumberTextView;
	TextView permissionLabelTextView;
	RadioGroup radioButtonGroup;
	RadioButton radioButtonExpected;
	RadioButton radioButtonUnexpected;

	public PermissionsExpectedItemAdapter(Context context,
			List<Permission> permissionsList) {
		super(context, R.layout.app_detail_list_item, permissionsList);
		this.context = context;
		this.permissionsList = permissionsList;
	}

	private void initializeViews(View contextView) {
		permissionsNumberTextView = (TextView) contextView
				.findViewById(R.id.app_detail_rate_app_permissions_number);
		permissionLabelTextView = (TextView) contextView
				.findViewById(R.id.app_detail_rate_app_permissions_label);
		radioButtonGroup = (RadioGroup) contextView
				.findViewById(R.id.app_detail_rate_app_overlay_item_radioGroup);
		radioButtonExpected = (RadioButton) contextView
				.findViewById(R.id.app_detail_rate_app_overlay_item_radio_expected);
		radioButtonUnexpected = (RadioButton) contextView
				.findViewById(R.id.app_detail_rate_app_overlay_item_radio_unexpected);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(
				R.layout.app_detail_rate_app_overlay_item, parent, false);

		initializeViews(rowView);

		// Set the appropriate information to the objects
		permissionsNumberTextView.setText((position + 1) + ".");
		permissionLabelTextView.setText(permissionsList.get(position)
				.getLabel());

		// get permission rating object from registry
		Registry reg = Registry.getInstance();
		PermissionsExpected permissionsRatingElement = (PermissionsExpected) reg
				.getRatingElement(PermissionsExpected.class);

		// get stored expected value for current permission
		if (permissionsRatingElement.expectedPermission(permissionsList
				.get(position)) != null) {
			boolean isExpected = permissionsRatingElement
					.expectedPermission(permissionsList.get(position));
			if (isExpected)
				radioButtonExpected.setChecked(true);
			else if (!isExpected)
				radioButtonUnexpected.setChecked(true);
		}
		radioButtonGroup.setTag(permissionsList.get(position));

		radioButtonExpected.setOnClickListener(new ExpectedRadioListener());
		radioButtonUnexpected.setOnClickListener(new ExpectedRadioListener());

		return rowView;
	}

	private class ExpectedRadioListener implements OnClickListener {

		@Override
		public void onClick(View expectedButton) {

			// get ui elements
			RadioButton activeButton = (RadioButton) expectedButton;
			RadioGroup radioGroup = (RadioGroup) expectedButton.getParent();
			Permission permission = (Permission) radioGroup.getTag();

			// get infos from registry
			Registry reg = Registry.getInstance();
			PermissionsExpected permissionsRatingElement = (PermissionsExpected) reg
					.getRatingElement(PermissionsExpected.class);

			// matches activeButton the expected or unexpected case
			int radioButtonTag = Integer.valueOf(activeButton.getTag()
					.toString());
			boolean expected = (radioButtonTag == 1);

			// check if reselected or selected for first time
			if ((permissionsRatingElement.expectedPermission(permission) != null)
					&& (permissionsRatingElement.expectedPermission(permission) == expected)) {

				// uncheck radio buttons of current radio group
				radioGroup.clearCheck();

				// remove permission
				permissionsRatingElement.removePermission(permission);
			} else {
				// update/insert permission with expected value
				permissionsRatingElement.setPermissionExpected(permission,
						expected);
			}

			// update registry
			reg.updateRatingElement(PermissionsExpected.class,
					permissionsRatingElement);
		}

	}
}
