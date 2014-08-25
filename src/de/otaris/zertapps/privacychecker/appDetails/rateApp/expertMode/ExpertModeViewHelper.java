package de.otaris.zertapps.privacychecker.appDetails.rateApp.expertMode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElement;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElementViewHelper;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.Registry;

/**
 * handles display of all expert-mode elements including a short text (am I
 * expert) and the checkbox it also handles the connection between Expert Mode
 * and registry
 */
public class ExpertModeViewHelper extends RatingElementViewHelper {

	protected CheckBox expertCheckBox;

	protected void initializeViews(View contextView) {
		expertCheckBox = (CheckBox) contextView
				.findViewById(R.id.app_detail_rate_app_expert_mode_checkBox);
	}

	@Override
	public View getView(Context context, ViewGroup parent,
			RatingElement ratingElement) throws IllegalArgumentException {

		if (!(ratingElement instanceof ExpertMode))
			throw new IllegalArgumentException(
					"Illegal Detail Object. Expected Expert Mode.");
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(
				R.layout.app_detail_rate_app_expert_mode, parent, false);

		initializeViews(rowView);

		// set isExpert to 0 as default in registry if it hasn't already been
		// set
		Registry reg = Registry.getInstance();
		String isExpert = reg.get(this.getClass().getPackage().getName(),
				"isExpert");

		if (isExpert == null) {
			reg.set(this.getClass().getPackage().getName(), "isExpert", "0");
		} else if (isExpert.equals("1")) {
			expertCheckBox.setChecked(true);
		}

		// add onclick listener
		expertCheckBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// get state of expert checkbox
				String isExpert = (((CheckBox) v).isChecked()) ? "1" : "0";

				// save expert state to registry
				Registry reg = Registry.getInstance();
				reg.set(this.getClass().getPackage().getName(), "isExpert",
						isExpert);
			}
		});

		return rowView;
	}
}
