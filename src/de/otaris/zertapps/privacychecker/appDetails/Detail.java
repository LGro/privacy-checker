package de.otaris.zertapps.privacychecker.appDetails;

import de.otaris.zertapps.privacychecker.database.model.AppCompact;

public abstract class Detail {

	protected final AppCompact app;
	
	public Detail(AppCompact app){
		this.app = app;
	}
	
	public AppCompact getApp() {
		return app;
	}

}
