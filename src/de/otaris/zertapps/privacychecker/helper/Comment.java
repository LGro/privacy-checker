package de.otaris.zertapps.privacychecker.helper;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Comment {
	// Database table
		public static final String TABLE_COMMENT = "COMMENT";
		// Columns
		public static final String COMMENT_ID = "COMMENT_ID";
		public static final String COMMENT_CONTENT = "name";
		public static final String COMMENT_VERSION = "version";
		public static final String COMMENT_DATE = "date";
		public static final String COMMENT_APP_ID = "app_id";

		// Creation statement
		private static final String Create_Comment_Table = "CREATE TABLE"
				+ TABLE_COMMENT + "(" + COMMENT_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COMMENT_CONTENT
				+ " TEXT, " + COMMENT_VERSION + " TEXT, " + COMMENT_DATE + " TEXT, " + COMMENT_APP_ID + " INTEGER FOREIGN KEY);";

		// empty constructor
		public Comment() {

		}

		// create table if it isn't existing yet
		public static void onCreate(SQLiteDatabase db) {
			db.execSQL(Create_Comment_Table);
		}

		// upgrade Method
		public static void onUpgrade(SQLiteDatabase db, int oldVersion,
				int newVersion) {
			Log.w(App.class.getCanonicalName(), "upgrading database from version "
					+ oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENT);
			onCreate(db);
		}

}
