package de.otaris.zertapps.privacychecker.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * represents the App entity in the database
 * 
 * Attention: When adding a new column, mind adding it in the matching
 * DataSource's cursorTo... method.
 */
public class App {

	// table name
	public static final String TABLE = "tbl_app";

	// table columns
	public static final String ID = "app_id";
	public static final String NAME = "name";
	public static final String LABEL = "label";
	public static final String VERSION = "version";
	public static final String RATING = "rating";
	public static final String INSTALLED = "installed";
	public static final String TIMETSTAMP = "timestamp";

	// SQL statement to create table
	private static final String Create_App_Table = "CREATE TABLE " + TABLE
			+ "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT UNIQUE ON CONFLICT REPLACE, " + LABEL + " TEXT, "
			+ VERSION + " TEXT, " + RATING + " FLOAT, " + INSTALLED + " INT, " + TIMETSTAMP + " LONG);";

	// attributes
	private int id;
	private String name;
	private String label;
	private String version;
	private float rating;
	private boolean installed;
	private Long timestamp;

	public App() {

	}

	public App(String name, String label, String version, float rating, boolean installed, Long timestamp) {

		this.name = name;
		this.label = label;
		this.version = version;
		this.rating = rating;
		this.installed = installed;
		this.timestamp = timestamp;
	}

	/**
	 * create table if it isn't existing yet
	 * 
	 * @param db
	 *            Database to create the table in
	 */
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(Create_App_Table);
	}

	/**
	 * upgrade an older version of this table currently drops the old table and
	 * calls onCreate
	 * 
	 * @param db
	 *            Database to update
	 * @param oldVersion
	 *            old database version number
	 * @param newVersion
	 *            new database version number
	 */
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
	
	public void setTimestamp(Long timestamp) {
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
	
	public Long getTimestamp() {
		return timestamp;
	}

}
