package de.otaris.zertapps.privacychecker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "DB_privacy-checker_apps";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		App.onCreate(db);
		Category.onCreate(db);
		// Permission.onCreate(db);
		// Comment.onCreate(db);
		// Rating.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		App.onUpgrade(db, oldVersion, newVersion);
		Category.onUpgrade(db, oldVersion, newVersion);
		// Permission.onUpgrade(db, oldVersion, newVersion);
		// Comment.onUpgrade(db, oldVersion, newVersion);		
		// Rating.onUpgrade(db, oldVersion, newVersion);

		onCreate(db);
	}

}
