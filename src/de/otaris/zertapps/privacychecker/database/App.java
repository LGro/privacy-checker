package de.otaris.zertapps.privacychecker.database;

import java.sql.Timestamp;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class App {

	// Database table
	public static final String TABLE = "tbl_app";

	// Columns
	public static final String ID = "app_id";
	public static final String NAME = "name";
	public static final String LABEL = "label";
	public static final String VERSION = "version";
	public static final String RATING = "rating";
	public static final String INSTALLED = "installed";
	public static final String TIMETSTAMP = "timestamp";

	// Creation statement
	private static final String Create_App_Table = "CREATE TABLE " + TABLE
			+ "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME
			+ " TEXT, " + LABEL + " TEXT, " + VERSION + " TEXT, " + RATING + " FLOAT, " + 
			INSTALLED + " INT, " + TIMETSTAMP + " Timestamp);";

	// params
	int id;
	String name;
	String label;
	String version;
	float rating;
	boolean installed;
	Timestamp timestamp;

	// empty constructor
	public App() {

	}

	// Constructor
	public App(String name, String label, String version, float rating, boolean installed) {
		this.name = name;
		this.label = label;
		this.version = version;
		this.rating = rating;
		this.installed = installed;
	}

	// create table if it isn't existing yet
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(Create_App_Table);
	}

	// upgrade Method
	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		Log.w(App.class.getCanonicalName(), "upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");

		db.execSQL("DROP TABLE IF EXISTS " + TABLE);

		onCreate(db);
	}

	// setter
	public void setId(int id) {
		this.id = id;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setInstalled(boolean installed) {
		this.installed = installed;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}
	
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	// getter
	public int getId() {
		return id;
	}

	public String getVersion() {
		return version;
	}

	public String getName() {
		return name;
	}
	
	public String getLabel() {
		return label;
	}

	public float getRating() {
		return rating;
	}

	public boolean isInstalled() {
		return installed;
	}
	
	public Timestamp getTimestamp() {
		return timestamp;
	}

}
