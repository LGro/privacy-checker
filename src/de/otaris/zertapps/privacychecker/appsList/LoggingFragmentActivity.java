package de.otaris.zertapps.privacychecker.appsList;

import de.otaris.zertapps.privacychecker.UserStudyLogger;
import android.support.v4.app.FragmentActivity;

public abstract class LoggingFragmentActivity extends FragmentActivity {

	@Override
	public void onResume() {
		super.onResume();
		
		UserStudyLogger.getInstance().log(this.getClass().getName());
	}
	
}
