package de.otaris.zertapps.privacychecker.database.model;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * represents the rating of an app in the database
 * 
 * Attention: When adding a new column, mind adding it in the matching
 * DataSource's cursorTo... method.
 */
public class RatingApp {

	// Database table
	public static final String TABLE = "tbl_rating_app";
	// Columns
	public static final String ID = "rating_app_id";
	public static final String VALUE = "value";
	public static final String APP_ID = "app_id";
	public static final String USER_TYPE = "user_type";
	public static final String ORIGIN = "origin";

	// Creation statement
	private static final String Create_Rating_Table = "CREATE TABLE "
			+ TABLE + "(" + ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + VALUE
			+ " DOUBLE, " + APP_ID + " INTEGER, " + USER_TYPE + " TEXT, " + ORIGIN + "TEXT);";

	private int id;
	private int value;
	private int appId;
	private boolean isExpert;
	private String origin;
	
	// empty constructor
	public RatingApp() {

	}

	public RatingApp(int id, int value, int appId, boolean isExpert, String origin){
		this.id = id;
		this.value = value;
		this.appId = appId;
		this.isExpert = isExpert;
		this.origin = origin;
	}
	
	// create table if it isn't existing yet
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(Create_Rating_Table);
	}

	// upgrade Method
	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		Log.w(AppCompact.class.getCanonicalName(), "upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE);
		onCreate(db);
	}

	//getters and setters
	public int getValue() {
		return value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public boolean isExpert() {
		return isExpert;
	}

	public void setExpert(boolean isExpert) {
		this.isExpert = isExpert;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	
	
	

}
