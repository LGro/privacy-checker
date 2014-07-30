package de.otaris.zertapps.privacychecker.appDetails.rateApp;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import de.otaris.zertapps.privacychecker.R;

/**
 * handles clicks on locks for total rating by updating the images
 */
public class TotalRatingListener implements View.OnClickListener {

	@Override
	public void onClick(View v) {

		int tag = Integer.parseInt((String) v.getTag());
		ImageButton button = (ImageButton) v.getRootView().findViewById(
				R.id.app_detail_rate_app_overlay_rating_1);

		// sets the chosen locks green
		for (int i = 1; i <= tag; i++) {

			switch (i) {

			case 2:
				button = (ImageButton) v.getRootView().findViewById(
						R.id.app_detail_rate_app_overlay_rating_2);
				break;
			case 3:
				button = (ImageButton) v.getRootView().findViewById(
						R.id.app_detail_rate_app_overlay_rating_3);
				break;
			case 4:
				button = (ImageButton) v.getRootView().findViewById(
						R.id.app_detail_rate_app_overlay_rating_4);
				break;
			case 5:
				button = (ImageButton) v.getRootView().findViewById(
						R.id.app_detail_rate_app_overlay_rating_5);
				break;
			default:
				break;
			}

			button.setImageResource(R.drawable.lock_green);
		}

		// (re-)sets the unchosen locks white
		for (int i = 5; i > tag; i--) {

			switch (i) {

			case 2:
				button = (ImageButton) v.getRootView().findViewById(
						R.id.app_detail_rate_app_overlay_rating_2);
				break;
			case 3:
				button = (ImageButton) v.getRootView().findViewById(
						R.id.app_detail_rate_app_overlay_rating_3);
				break;
			case 4:
				button = (ImageButton) v.getRootView().findViewById(
						R.id.app_detail_rate_app_overlay_rating_4);
				break;
			case 5:
				button = (ImageButton) v.getRootView().findViewById(
						R.id.app_detail_rate_app_overlay_rating_5);
				break;
			default:
				break;
			}
			button.setImageResource(R.drawable.lock_white);
		}

		//sets the text beneath the locks depending on the rating
		TextView valueText = (TextView) v.getRootView().findViewById(
				R.id.app_detail_rate_app_value_text);
		valueText.setVisibility(View.VISIBLE);
		switch (tag) {
		case 1:
			valueText.setText("Scheiﬂe!");
			break;
		case 2:
			valueText.setText("Besser...");
			break;
		case 3:
			valueText.setText("naja");
			break;
		case 4:
			valueText.setText("Ganz ok");
			break;
		case 5:
			valueText.setText("Spitze!");
			break;
		}
	}
	
}
