package de.otaris.zertapps.privacychecker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RatingViewHelper extends DetailViewHelper {
	
	@Override
	public View getView(Context context, ViewGroup parent, Detail detail) throws IllegalArgumentException {
		if (!(detail instanceof Rating))
			throw new IllegalArgumentException("Illegal Detail Object. Expected Rating.");
		
		Rating rating = (Rating) detail;
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.app_detail_list_item, parent,
				false);
		TextView textView = (TextView) rowView
				.findViewById(R.id.app_detail_list_item_name);

		return rowView;
	}

}
