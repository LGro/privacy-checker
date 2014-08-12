package de.otaris.zertapps.privacychecker.appDetails;

import de.otaris.zertapps.privacychecker.database.model.AppExtended;

/**
 * Represents the data for an app detail to be displayed at the detail activity.
 *
 */
public abstract class Detail {

	protected final AppExtended app;

	public Detail(AppExtended app) {
		this.app = app;
	}

	public AppExtended getApp() {
		return app;
	}

}
