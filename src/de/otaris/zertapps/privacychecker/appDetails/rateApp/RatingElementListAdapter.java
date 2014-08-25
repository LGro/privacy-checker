package de.otaris.zertapps.privacychecker.appDetails.rateApp;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import de.otaris.zertapps.privacychecker.R;

/**
 * Handles getting of Views for all Rating Elements
 *
 */
public class RatingElementListAdapter extends ArrayAdapter<RatingElement> {
	private final Context context;
	private final List<RatingElement> values;

	/**
	 * Constructor to fill context and values
	 * 
	 * @param context
	 * @param values
	 */
	public RatingElementListAdapter(Context context, List<RatingElement> values) {
		super(context, R.layout.app_detail_rate_app_overlay, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// get class name of the detail object at "position"
		String elementViewClass = values.get(position).getClass().getName();

		// infer specific detail ViewHelper class name
		String ratingElementViewClassFullPath = elementViewClass + "ViewHelper";
		try {
			// get specific detail dependent ViewHelper class
			Class<?> viewClass = Class.forName(ratingElementViewClassFullPath);
			// instantiate the ViewHelper matching the current detail object
			RatingElementViewHelper ratingElementViewHelper = (RatingElementViewHelper) viewClass
					.newInstance();

			// return the view returned by the ViewHelper given the Detail at
			// "position"
			return ratingElementViewHelper.getView(context, parent,
					values.get(position));
		} catch (ClassNotFoundException e) {
			Log.e("RatingElementListAdapter", e.getMessage());
			e.printStackTrace();
		} catch (InstantiationException e) {
			Log.e("RatingElementListAdapter", e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			Log.e("RatingElementListAdapter", e.getMessage());
			e.printStackTrace();
		}

		Log.e("RatingElementListAdapter",
				"Error: Rating Element View Class Not Found. Expected "
						+ ratingElementViewClassFullPath);
		return null;
	}

}
