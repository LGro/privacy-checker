package de.otaris.zertapps.privacychecker.appDetails.privacyRating;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.database.model.PermissionExtended;

/**
 * simple array adapter for displaying the labels of permissions
 * 
 * uses android.R.layout.simple_list_item_1 as item layout
 */
public class PermissionsListItemAdapter extends
		ArrayAdapter<PermissionExtended> {

	private final Context context;
	private final List<PermissionExtended> values;

	public PermissionsListItemAdapter(Context context,
			List<PermissionExtended> values) {
		super(context, R.layout.app_detail_privacy_rating_permission_list_item,
				values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(
				R.layout.app_detail_privacy_rating_permission_list_item,
				parent, false);

		// get the views
		TextView permissionName = (TextView) rowView
				.findViewById(R.id.app_detail_privacy_rating_permission_list_item_name);
		TextView permissionNonExpertPercent = (TextView) rowView
				.findViewById(R.id.app_detail_privacy_rating_permission_list_item_nonexpert_percent);
		TextView permissionNonExpertCount = (TextView) rowView
				.findViewById(R.id.app_detail_privacy_rating_permission_list_item_nonexpert_count);
		TextView permissionExpertPercent = (TextView) rowView
				.findViewById(R.id.app_detail_privacy_rating_permission_list_item_expert_percent);
		TextView permissionExpertCount = (TextView) rowView
				.findViewById(R.id.app_detail_privacy_rating_permission_list_item_expert_count);

		// set permission label for list item
		PermissionExtended perm = values.get(position);
		permissionName.setText(perm.getPermission().getLabel());

		// calculate and set the percentages and the counts
		// ...for non-experts
		int nonExpertPercent = 0;
		if ((perm.getNonExpertPermissionExpected() != 0)
				|| (perm.getNonExpertPermissionUnexpected() != 0)) {
			nonExpertPercent = (int) (((float) perm
					.getNonExpertPermissionUnexpected() / (float) (perm
					.getNonExpertPermissionExpected() + perm
					.getNonExpertPermissionUnexpected())) * 100f);
		}
		permissionNonExpertPercent.setText(nonExpertPercent + "%");
		permissionNonExpertCount.setText(perm
				.getNonExpertPermissionUnexpected()
				+ perm.getNonExpertPermissionExpected() + "");

		// ...for experts
		int expertPercent = 0;
		if ((perm.getExpertPermissionExpected() != 0)
				|| (perm.getExpertPermissionUnexpected() != 0)) {
			expertPercent = (int) (((float) perm
					.getExpertPermissionUnexpected() / (float) (perm
					.getExpertPermissionExpected() + perm
					.getExpertPermissionUnexpected())) * 100f);
		}
		permissionExpertPercent.setText(expertPercent + "%");
		permissionExpertCount.setText(perm.getExpertPermissionUnexpected()
				+ perm.getExpertPermissionExpected() + "");

		return rowView;
	}
}
