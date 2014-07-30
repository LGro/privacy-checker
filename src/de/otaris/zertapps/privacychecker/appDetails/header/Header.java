package de.otaris.zertapps.privacychecker.appDetails.header;

import android.app.Activity;
import android.view.View;
import de.otaris.zertapps.privacychecker.database.interfaces.App;

/**
 * interface for different header versions
 */
public interface Header {

	public View getView(Activity activity, App app)
			throws IllegalArgumentException;
}