package de.otaris.zertapps.privacychecker;

import de.otaris.zertapps.privacychecker.database.App;

public abstract class Detail {

	protected final App app;
	
	public Detail(App app){
		this.app = app;
	}
	
	public App getApp() {
		return app;
	}

}
