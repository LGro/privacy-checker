package de.otaris.zertapps.privacychecker.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Category {

	// Database table
	public static final String TABLE_CATEGORY = "category";
	// Columns
	public static final String CATEGORY_ID = "CATEGORY_ID";
	public static final String CATEGORY_NAME = "name";
	public static final String CATEGORY_APP_ID = "app_id";

	// Creation statement
	private static final String Create_Category_Table = "CREATE TABLE "
			+ TABLE_CATEGORY + "(" + CATEGORY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + CATEGORY_NAME
			+ " TEXT, " + CATEGORY_APP_ID + " INTEGER FOREIGN KEY);";

	// empty constructor
	public Category() {

	}

	// create table if it isn't existing yet
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(Create_Category_Table);
	}

	// upgrade Method
	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		Log.w(App.class.getCanonicalName(), "upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
		onCreate(db);
	}

}
