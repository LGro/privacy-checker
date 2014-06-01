package de.otaris.zertapps.privacychecker.helper;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class App {
	
	//Database table
	public static final String TABLE_APP = "app";
	//Columns
	public static final String APP_ID = "APP_ID";
	public static final String APP_NAME = "name";
	public static final String APP_VERSION = "version";
	
	//Creation statement
	private static final String Create_App_Table = "CREATE TABLE" + TABLE_APP
			+ "(" + APP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + APP_NAME
			+ " TEXT, " + APP_VERSION + " TEXT);";
	
	
	//params
	int ID;
	String Name;
	String Version;
	
	
	//empty constructor
	public App(){
		
	}
	
	//Constructor
	public App(String name, String version){
		this.Name = name;
		this.Version = version;
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
	public void setID(int ID){
		this.ID = ID;
	}
	public void setVersion(String Version){
		this.Version= Version;
	}
	public void setName(String Name){
		this.Name = Name;
	}
	
	//getter
	public int getID(){
		return ID;
	}
	public String getVersion(){
		return Version;
	}
	public String getName(){
		return Name;
	}
}
