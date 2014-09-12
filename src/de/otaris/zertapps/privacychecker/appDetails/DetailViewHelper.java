package de.otaris.zertapps.privacychecker.appDetails;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Abstract definition of a ViewHelper to display detail information in the app
 * details activity, given a specific instance of a Detail object.
 */
public abstract class DetailViewHelper {

	protected abstract void initializeViews(View contextView);

	public abstract View getView(Context context, ViewGroup parent,
			Detail detail) throws IllegalArgumentException;

}
