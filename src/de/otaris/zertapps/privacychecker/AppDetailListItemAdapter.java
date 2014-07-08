package de.otaris.zertapps.privacychecker;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.otaris.zertapps.privacychecker.database.App;

public class AppDetailListItemAdapter extends ArrayAdapter<App> {
	private final Context context;
	private final List<App> values;
	private final PackageManager pm;
	
	
	public AppDetailListItemAdapter(Context context, PackageManager pm,
			List<App> values) {
		super(context, R.layout.app_list_item, values);
		this.context = context;
		this.values = values;
		this.pm = pm;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.app_list_item, parent, false);

		// get views from layout
		TextView textView = (TextView) rowView
				.findViewById(R.id.app_list_item_name);
		TextView ratingView = (TextView) rowView
				.findViewById(R.id.app_list_item_rating);
		ImageView imageView = (ImageView) rowView
				.findViewById(R.id.app_list_item_icon);

		// set app icon
		if (values.get(position).isInstalled()) {
			try {
				imageView.setImageDrawable(pm.getApplicationIcon(values.get(
						position).getName()));
			} catch (NameNotFoundException e) {
				Log.w("AppListItemAdapter",
						"Couldn't load icon for app: " + e.getMessage());
			}
		} else {
			// TODO: implement (get icon from PlayStore API?!)
		}

		// set app title
		textView.setText(values.get(position).getLabel());

		// set app rating
		ratingView.setText("" + values.get(position).getRating());

		return rowView;
	}
}
