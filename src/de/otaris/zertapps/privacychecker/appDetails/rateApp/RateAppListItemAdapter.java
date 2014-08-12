package de.otaris.zertapps.privacychecker.appDetails.rateApp;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.database.model.Permission;

public class RateAppListItemAdapter extends ArrayAdapter<Permission> {
	private final Context context;
	private final List<Permission> values;
	private final PackageManager pm;

	public RateAppListItemAdapter(Context context, PackageManager pm,
			List<Permission> values) {
		super(context, R.layout.app_detail_list_item, values);
		this.context = context;
		this.pm = pm;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(
				R.layout.app_detail_rate_app_overlay_item, parent, false);

		// Get the objects from the view
		TextView textViewNumber = (TextView) rowView
				.findViewById(R.id.app_detail_rate_app_overlay_item_number);
		TextView textViewName = (TextView) rowView
				.findViewById(R.id.app_detail_rate_app_overlay_item_name);
		// RadioButton radio0 = (RadioButton) rowView
		// .findViewById(R.id.app_detail_rate_app_overlay_item_radio_expected);
		// RadioButton radio1 = (RadioButton) rowView
		// .findViewById(R.id.app_detail_rate_app_overlay_item_radio_unexpected);

		// Set the appropriate information to the objects
		textViewNumber.setText((position + 1) + ".");
		textViewName.setText(values.get(position).getLabel());

		return rowView;
	}
}
