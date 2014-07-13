package de.otaris.zertapps.privacychecker.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Permission {

	// Database table
	public static final String TABLE = "permission";
	// Columns
	public static final String ID = "permission_id";
	public static final String NAME = "name";
	public static final String LABEL = "label";
	public static final String DESCRIPTION = "description";

	// Creation statement
	private static final String Create_Permission_Table = "CREATE TABLE "
			+ TABLE + "(" + ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME
			+ " TEXT, " + LABEL + " TEXT, " + DESCRIPTION + " TEXT);";

	
	private int id;
	private String name;
	private String label;
	private String description;
	
	// empty constructor
		public Permission() {

		}
		
		public Permission(int id, String name, String label, String description){
			this.id = id;
			this.name = name;
			this.label = label;
			this.description = description;
		}

		// create table if it isn't existing yet
		public static void onCreate(SQLiteDatabase db) {
			db.execSQL(Create_Permission_Table);
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

	
	
	//getter and setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
