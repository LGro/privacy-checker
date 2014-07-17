package de.otaris.zertapps.privacychecker.appDetails.permissions;

import java.util.ArrayList;
import java.util.List;

import de.otaris.zertapps.privacychecker.database.model.Category;
import de.otaris.zertapps.privacychecker.database.model.Permission;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PermissionsListItemAdapter extends ArrayAdapter<Permission>{
	
	
	private final Context context;
	private final List<Permission> values;
		

	public PermissionsListItemAdapter(Context context,	List<Permission> values) {
		super(context, android.R.layout.simple_list_item_1, values);
		this.context = context;
		this.values = values;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

		// get views from layout
		TextView textView = (TextView) rowView
				.findViewById(android.R.id.text1);
		

		// set category label
		textView.setText(values.get(position).getLabel());

		// set categoryID as tag
		rowView.setTag(values.get(position).getId());

		return rowView;
	}
}
