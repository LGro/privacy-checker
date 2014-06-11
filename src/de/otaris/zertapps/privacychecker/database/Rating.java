package de.otaris.zertapps.privacychecker.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Rating {

	// Database tableCOMM
	public static final String TABLE_RATING = "RATING";
	// Columns
	public static final String RATING_ID = "RATING_ID";
	public static final String RATING_NUMBER = "number";
	public static final String RATING_APP_ID = "app_id";

	// Creation statement
	private static final String Create_Rating_Table = "CREATE TABLE "
			+ TABLE_RATING + "(" + RATING_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + RATING_NUMBER
			+ " DOUBLE, " + RATING_APP_ID + " INTEGER FOREIGN KEY);";

	// empty constructor
	public Rating() {

	}

	// create table if it isn't existing yet
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(Create_Rating_Table);
	}

	// upgrade Method
	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		Log.w(App.class.getCanonicalName(), "upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATING);
		onCreate(db);
	}

}
