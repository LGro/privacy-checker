package de.otaris.zertapps.privacychecker;

import de.otaris.zertapps.privacychecker.database.AppDataSource;

public class Rating extends Detail {

	private int appId;
	private AppDataSource appDataSource;

	public Rating(){
		
	}
	
	public Rating(int id, AppDataSource appDataSource){
		this.appId = id;
		this.appDataSource = appDataSource;
	}
	
	public Rating getRatingByAppId(){
		appDataSource.getAppById(appId);
		
		return null;
		
	}
}
