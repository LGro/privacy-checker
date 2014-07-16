package de.otaris.zertapps.privacychecker.database.model;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Permission {

	// Database table
	public static final String TABLE = "tbl_permission";
	// Columns
	public static final String ID = "permission_id";
	public static final String NAME = "name";
	public static final String LABEL = "label";
	public static final String DESCRIPTION = "description";
	public static final String CRITICALITY = "criticality";

	// Creation statement
	private static final String Create_Permission_Table = "CREATE TABLE "
			+ TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME
			+ " TEXT UNIQUE ON CONFLICT IGNORE, " + LABEL + " TEXT, " + DESCRIPTION + " TEXT, "
			+ CRITICALITY + " INTEGER);";

	private int id;
	private String name;
	private String label;
	private String description;
	private int criticality;

	// empty constructor
	public Permission() {

	}

	public Permission(int id, String name, String label, String description,
			int criticality) {
		this.id = id;
		this.name = name;
		this.label = label;
		this.description = description;
		this.criticality = criticality;
	}

	// create table if it isn't existing yet
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(Create_Permission_Table);
	}

	// upgrade Method
	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		Log.w(AppCompact.class.getCanonicalName(),
				"upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE);
		onCreate(db);
	}

	// getter and setter
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

	public int getCriticality() {
		return criticality;
	}

	public void setCriticality(int criticality) {
		this.criticality = criticality;
	}

}
