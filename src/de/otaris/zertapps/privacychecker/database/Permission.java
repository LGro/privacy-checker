package de.otaris.zertapps.privacychecker.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Permission {

	// Database table
	public static final String TABLE_PERMISSION = "permission";
	// Columns
	public static final String PERMISSION_ID = "PERMISSION_ID";
	public static final String PERMISSION_NAME = "name";
	public static final String PERMISSION_DESCRIPTION = "discription";

	// Creation statement
	private static final String Create_Permission_Table = "CREATE TABLE "
			+ TABLE_PERMISSION + "(" + PERMISSION_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + PERMISSION_NAME
			+ " TEXT, " + PERMISSION_DESCRIPTION + " TEXT);";

	// empty constructor
	public Permission() {

	}

	// create table if it isn't existing yet
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(Create_Permission_Table);
	}

	// upgrade Method
	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		Log.w(App.class.getCanonicalName(), "upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERMISSION);
		onCreate(db);
	}

}
