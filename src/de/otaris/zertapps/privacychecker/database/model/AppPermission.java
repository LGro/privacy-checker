package de.otaris.zertapps.privacychecker.database.model;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AppPermission {

	// table name
	public static final String TABLE = "tbl_app_permission";

	// table columns
	public static final String ID = "app_permission_id";
	public static final String APP_ID = "app_id";
	public static final String PERMISSION_ID = "permission_id";

	// SQL statement to create table
	private static final String Create_App_Permission_Table = "CREATE TABLE " + TABLE
			+ "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + APP_ID
			+ " INTEGER, " + PERMISSION_ID + " INTEGER);";

	// attributes
	private int id;
	private int appId;
	private int permissionId;

	public AppPermission() {

	}

	public AppPermission(int id, int appId, int permissionId) {
		this.id = id;
		this.appId = appId;
		this.permissionId = permissionId;
	}

	/**
	 * create table if it isn't existing yet
	 * 
	 * @param db
	 *            Database to create the table in
	 */
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(Create_App_Permission_Table);
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
		Log.w(AppCompact.class.getCanonicalName(),
				"upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");

		db.execSQL("DROP TABLE IF EXISTS " + TABLE);

		onCreate(db);
	}

	// getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public int getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(int permissionId) {
		this.permissionId = permissionId;
	}

}
