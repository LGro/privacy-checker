package de.otaris.zertapps.privacychecker;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.otaris.zertapps.privacychecker.database.Category;

/**
 * adapter to show a list of category names  
 */
public class CategoryListItemAdapter extends ArrayAdapter<Category> {
	private final Context context;
	private final List<Category> values;
		

	public CategoryListItemAdapter(Context context,	List<Category> values) {
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


		return rowView;
	}
}
