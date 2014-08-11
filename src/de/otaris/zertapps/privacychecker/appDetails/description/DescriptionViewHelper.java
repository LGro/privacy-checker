package de.otaris.zertapps.privacychecker.appDetails.description;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.Detail;
import de.otaris.zertapps.privacychecker.appDetails.DetailViewHelper;

/**
 * Displays an app description that is extendible by clicking a "show more"
 * button.
 *
 */
public class DescriptionViewHelper extends DetailViewHelper {

	@Override
	public View getView(Context context, ViewGroup parent, Detail detail)
			throws IllegalArgumentException {
		if (!(detail instanceof Description))
			throw new IllegalArgumentException(
					"Illegal Detail Object. Expected Description.");

		Description description = (Description) detail;

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.app_detail_description,
				parent, false);

		// get TextView
		TextView descriptionTextView = (TextView) rowView
				.findViewById(R.id.app_detail_description_text);

		// set app description as text for TextView
		descriptionTextView.setText(description.getApp().getDescription());

		// "show more" buttons
		ToggleButton button = (ToggleButton) rowView
				.findViewById(R.id.app_detail_description_more);
		button.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton toggleButton,
					boolean isChecked) {
				TextView descriptionTextView = (TextView) ((View) toggleButton
						.getParent())
						.findViewById(R.id.app_detail_description_text);

				// changes fulltext to shorten version and vice versa
				if (isChecked) {
					descriptionTextView.setMaxLines(200);
				} else {
					descriptionTextView.setMaxLines(2);
				}

			}
		});

		return rowView;
	}

}
