package de.otaris.zertapps.privacychecker.appDetails.privacyRating;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.otaris.zertapps.privacychecker.database.model.Permission;

/**
 * simple array adapter for displaying the labels of permissions
 * 
 * uses android.R.layout.simple_list_item_1 as item layout
 */
public class PermissionsListItemAdapter extends ArrayAdapter<Permission> {

	private final Context context;
	private final List<Permission> values;

	public PermissionsListItemAdapter(Context context, List<Permission> values) {
		super(context, android.R.layout.simple_list_item_1, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// use default list item containing only one TextView
		View rowView = inflater.inflate(android.R.layout.simple_list_item_1,
				parent, false);

		// get TextView
		TextView textView = (TextView) rowView.findViewById(android.R.id.text1);

		// set permission label for list item
		textView.setText(values.get(position).getLabel());

		return rowView;
	}
}
