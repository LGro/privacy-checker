package de.otaris.zertapps.privacychecker.appDetails.rateApp.expertMode;

import android.content.Context;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElement;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingValidationException;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;

public class ExpertMode extends RatingElement{

	public ExpertMode(AppExtended app, boolean mandatory) {
		super(app, mandatory);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean validate() throws RatingValidationException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void save(Context context) {
		// TODO Auto-generated method stub
		
	}
	


}
