package de.otaris.zertapps.privacychecker.appDetails;

import de.otaris.zertapps.privacychecker.UserStudyLogger;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class LoggingShowMore implements OnCheckedChangeListener {

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		UserStudyLogger.getInstance().log(this.getClass().getName());
	}

	
}
