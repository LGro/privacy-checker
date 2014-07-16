package de.otaris.zertapps.privacychecker.appDetails;

import de.otaris.zertapps.privacychecker.database.model.AppExtended;

public abstract class Detail {

	protected final AppExtended app;
	
	public Detail(AppExtended app){
		this.app = app;
	}
	
	public AppExtended getApp() {
		return app;
	}

}
