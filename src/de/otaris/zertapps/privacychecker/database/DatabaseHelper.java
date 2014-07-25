package de.otaris.zertapps.privacychecker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;
import de.otaris.zertapps.privacychecker.database.model.AppPermission;
import de.otaris.zertapps.privacychecker.database.model.Category;
import de.otaris.zertapps.privacychecker.database.model.Permission;
import de.otaris.zertapps.privacychecker.database.model.RatingApp;
import de.otaris.zertapps.privacychecker.database.model.RatingPermission;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "DB_privacy-checker_apps";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		AppCompact.onCreate(db);
		Category.onCreate(db);
		Permission.onCreate(db);
		AppPermission.onCreate(db);
		// Comment.onCreate(db);
		RatingApp.onCreate(db);
		RatingPermission.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		AppCompact.onUpgrade(db, oldVersion, newVersion);
		Category.onUpgrade(db, oldVersion, newVersion);
		Permission.onUpgrade(db, oldVersion, newVersion);
		AppPermission.onUpgrade(db, oldVersion, newVersion);
		// Comment.onUpgrade(db, oldVersion, newVersion);
		RatingApp.onUpgrade(db, oldVersion, newVersion);
		RatingPermission.onUpgrade(db, oldVersion, newVersion);

		onCreate(db);
	}

}
