package de.otaris.zertapps.privacychecker.appsList;

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
import de.otaris.zertapps.privacychecker.IconController;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.RatingController;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;

/**
 * adapter to show a list of apps with icon, name and rating
 */
public class AppListItemAdapter extends ArrayAdapter<AppCompact> {
	private final Context context;
	private final List<AppCompact> values;
	private final PackageManager pm;

	public AppListItemAdapter(Context context, PackageManager pm,
			List<AppCompact> values) {
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
		ImageView imageView = (ImageView) rowView
				.findViewById(R.id.app_list_item_icon);
		ImageView ratingImage = (ImageView) rowView
				.findViewById(R.id.app_list_item_privacy_rating);
		ImageView psImage = (ImageView) rowView
				.findViewById(R.id.app_list_item_ps_rating);

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
			imageView.setImageBitmap(IconController.byteArrayToBitmap(values
					.get(position).getIcon()));
		}

		// set app title
		textView.setText(values.get(position).getLabel());
		rowView.setTag(values.get(position).getId());
		RatingController ratingController = new RatingController();
		float privacyRating = values.get(position).getPrivacyRating();
		ratingImage.setImageResource(ratingController
				.getIconRatingLocks(privacyRating));
		float funcRating = values.get(position).getFunctionalRating();
		psImage.setImageResource(ratingController
				.getIconRatingStars(funcRating));

		return rowView;
	}
}
