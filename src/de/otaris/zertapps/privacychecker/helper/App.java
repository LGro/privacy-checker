package de.otaris.zertapps.privacychecker.helper;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class App {
	
	//Database table
	public static final String TABLE_APP = "tbl_app";
	//Columns
	public static final String APP_ID = "app_id";
	public static final String APP_NAME = "name";
	public static final String APP_VERSION = "version";
	public static final String APP_RATING = "rating";
	public static final String APP_INSTALLED = "installed";
	
	//Creation statement
	private static final String Create_App_Table = "CREATE TABLE " + TABLE_APP
			+ "(" + APP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + APP_NAME
			+ " TEXT, " + APP_VERSION + " TEXT, " + APP_RATING + " FLOAT, "
					 + APP_INSTALLED + " INT);";
	
	
	//params
	int id;
	String name;
	String version;
	float rating;
	boolean installed;
	
	
	//empty constructor
	public App(){
		
	}
	
	//Constructor
	public App(String name, String version, float rating, boolean installed){
		this.name = name;
		this.version = version;
		this.rating = rating;
		this.installed = installed;
	}
	
	//create table if it isn't existing yet
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(Create_App_Table);
	}
	
	//upgrade Method
	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		Log.w(App.class.getCanonicalName(), "upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP);
		onCreate(db);
	}
	//setter
	public void setId(int id){
		this.id = id;
	}
	public void setVersion(String version){
		this.version= version;
	}
	public void setName(String name){
		this.name = name;
	}
	
		
	
	public void setInstalled(boolean installed) {
		this.installed = installed;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	//getter
	public int getId(){
		return id;
	}
	public String getVersion(){
		return version;
	}
	public String getName(){
		return name;
	}
	public float getRating() {
		return rating;
	}
	public boolean isInstalled() {
		return installed;
	}

}
