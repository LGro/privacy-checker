package de.otaris.zertapps.privacychecker;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppListItemAdapter extends ArrayAdapter<ApplicationInfo> {
	private final Context context;
	private final ApplicationInfo[] values;
	private final PackageManager pm;

	public AppListItemAdapter(Context context, PackageManager pm, ApplicationInfo[] values) {
		super(context, R.layout.app_list_item, values);
		this.context = context;
		this.values = values;
		this.pm = pm;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.app_list_item, parent,
				false);
		
		// get views from layout
		TextView textView = (TextView) rowView
				.findViewById(R.id.app_list_item_name);
		TextView ratingView = (TextView) rowView
				.findViewById(R.id.app_list_item_rating);
		ImageView imageView = (ImageView) rowView
				.findViewById(R.id.app_list_item_icon);

		// TODO: get from DB
		ratingView.setText("5,0");
		
		// set app title
		textView.setText(values[position].loadLabel(pm).toString());

		// set app icon
		imageView.setImageDrawable(values[position]
				.loadIcon(pm));

		return rowView;
	}
}
