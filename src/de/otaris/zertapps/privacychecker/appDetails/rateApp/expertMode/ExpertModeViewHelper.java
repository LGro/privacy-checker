package de.otaris.zertapps.privacychecker.appDetails.rateApp.expertMode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElement;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElementViewHelper;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.Registry;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.totalPrivacyRating.TotalPrivacyRatingListener;

public class ExpertModeViewHelper extends RatingElementViewHelper {
	
	protected CheckBox expertCheckBox;
	
	protected void initializeViews(View contextView){
		expertCheckBox = (CheckBox) contextView.findViewById(R.id.app_detail_rate_app_expert_mode_checkBox);
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
		
		Registry reg = Registry.getInstance();
		reg.set(this.getClass().getPackage().getName(), "isExpert", "0");
		
		expertCheckBox.setOnClickListener(
				new ExpertModeListener());
		
		return rowView;
	}
}
