package de.otaris.zertapps.privacychecker.appDetails.header;

import android.app.Activity;
import android.view.View;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;

/**
 * interface for different header versions to display information about an app
 * in the app details activity
 */
public abstract class Header {

	protected abstract void initializeViews(View contextView);

	public abstract View getView(Activity activity, AppExtended app)
			throws IllegalArgumentException;

}