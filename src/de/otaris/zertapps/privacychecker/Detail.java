package de.otaris.zertapps.privacychecker;

import de.otaris.zertapps.privacychecker.database.AppCompact;

public abstract class Detail {

	protected final AppCompact app;
	
	public Detail(AppCompact app){
		this.app = app;
	}
	
	public AppCompact getApp() {
		return app;
	}

}
