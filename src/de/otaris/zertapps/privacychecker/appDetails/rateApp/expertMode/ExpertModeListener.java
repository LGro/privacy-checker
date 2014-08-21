package de.otaris.zertapps.privacychecker.appDetails.rateApp.expertMode;

import de.otaris.zertapps.privacychecker.appDetails.rateApp.Registry;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class ExpertModeListener implements OnClickListener {

	@Override
	public void onClick(View v) {
		String value = (((CheckBox)v).isChecked())?"1":"0";
		Registry reg = Registry.getInstance();
		reg.set(this.getClass().getPackage().getName(), "isExpert", value);
	}

}
