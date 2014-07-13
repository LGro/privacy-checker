package de.otaris.zertapps.privacychecker.appDetails.description;

import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.R.id;
import de.otaris.zertapps.privacychecker.R.layout;
import de.otaris.zertapps.privacychecker.appDetails.Detail;
import de.otaris.zertapps.privacychecker.appDetails.DetailViewHelper;
import de.otaris.zertapps.privacychecker.database.dataSource.AppCompactDataSource;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

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

		TextView descriptionTextView = (TextView) rowView
				.findViewById(R.id.app_detail_description_text);

		descriptionTextView.setText(description.getApp().getDescription());

		ToggleButton button = (ToggleButton) rowView
				.findViewById(R.id.app_detail_description_more);
		button.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton toggleButton,
					boolean isChecked) {
				TextView descriptionTextView = (TextView) ((View) toggleButton
						.getParent())
						.findViewById(R.id.app_detail_description_text);
				//changes fulltext to shorten version 
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
