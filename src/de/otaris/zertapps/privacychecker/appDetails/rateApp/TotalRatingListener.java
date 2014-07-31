package de.otaris.zertapps.privacychecker.appDetails.rateApp;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import de.otaris.zertapps.privacychecker.R;

/**
 * handles clicks on locks for total rating by updating the images
 */
public class TotalRatingListener implements View.OnClickListener {
	
	@Override
	public void onClick(View v) {

		int tag = Integer.parseInt((String) v.getTag());
		ToggleButton button1 = (ToggleButton) v.getRootView().findViewById(
				R.id.app_detail_rate_app_overlay_rating_1);
		ToggleButton button2 = (ToggleButton) v.getRootView().findViewById(
				R.id.app_detail_rate_app_overlay_rating_2);
		ToggleButton button3 = (ToggleButton) v.getRootView().findViewById(
				R.id.app_detail_rate_app_overlay_rating_3);
		ToggleButton button4 = (ToggleButton) v.getRootView().findViewById(
				R.id.app_detail_rate_app_overlay_rating_4);
		ToggleButton button5 = (ToggleButton) v.getRootView().findViewById(
				R.id.app_detail_rate_app_overlay_rating_5);
		TextView valueText = (TextView) v.getRootView().findViewById(
				R.id.app_detail_rate_app_value_text);
		valueText.setVisibility(View.VISIBLE);

		// sets the chosen locks green
		// for (int i = 1; i <= tag; i++) {

		switch (tag) {

		case 1:
			button1.setChecked(true);
			button2.setChecked(false);
			button3.setChecked(false);
			button4.setChecked(false);
			button5.setChecked(false);
			valueText.setText(R.string.app_detail_rate_app_value_1);
			break;
		case 2:
			button1.setChecked(true);
			button2.setChecked(true);
			button3.setChecked(false);
			button4.setChecked(false);
			button5.setChecked(false);
			valueText.setText(R.string.app_detail_rate_app_value_2);
			break;
		case 3:
			button1.setChecked(true);
			button2.setChecked(true);
			button3.setChecked(true);
			button4.setChecked(false);
			button5.setChecked(false);
			valueText.setText(R.string.app_detail_rate_app_value_3);
			break;
		case 4:
			button1.setChecked(true);
			button2.setChecked(true);
			button3.setChecked(true);
			button4.setChecked(true);
			button5.setChecked(false);
			valueText.setText(R.string.app_detail_rate_app_value_4);
			break;
		case 5:
			button1.setChecked(true);
			button2.setChecked(true);
			button3.setChecked(true);
			button4.setChecked(true);
			button5.setChecked(true);
			valueText.setText(R.string.app_detail_rate_app_value_5);
			break;
		default:
			break;
		}

	}

}
