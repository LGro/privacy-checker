package de.otaris.zertapps.privacychecker.appDetails.RateApp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.Detail;
import de.otaris.zertapps.privacychecker.appDetails.DetailViewHelper;
import de.otaris.zertapps.privacychecker.appDetails.privacyRating.PrivacyRating;
import de.otaris.zertapps.privacychecker.appsList.InstalledAppsActivity;

public class RateAppViewHelper extends DetailViewHelper{

	@Override
	public View getView(Context context, ViewGroup parent, Detail detail)
			throws IllegalArgumentException {
		
		if (!(detail instanceof RateApp))
			throw new IllegalArgumentException(
					"Illegal Detail Object. Expected Rating.");

	

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.app_detail_rate_app,
				parent, false);
		
		return rowView;
	}
	
	public void displayRateAppWindow(View view) {
		Log.i("AppDetail", "Button zum Bewerten gedrückt");

		// to pass the information to run InstalledAppActivity
//		Intent intent = new Intent(this, InstalledAppsActivity.class);
//		startActivity(intent);
	}


}
