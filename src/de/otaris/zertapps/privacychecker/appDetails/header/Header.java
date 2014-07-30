package de.otaris.zertapps.privacychecker.appDetails.header;

import android.app.Activity;
import android.view.View;
import de.otaris.zertapps.privacychecker.database.interfaces.App;

public interface Header {

	public View getView(Activity activity, App app)
			throws IllegalArgumentException;
}