package de.otaris.zertapps.privacychecker.database.model;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * represents the rating of an AppPermission in the database
 * 
 * Attention: When adding a new column, mind adding it in the matching
 * DataSource's cursorTo... method.
 */
public class RatingPermission {
	// Database tableCOMM
		public static final String TABLE = "tbl_rating_permission";
		// Columns
		public static final String ID = "rating_permission_id";
		public static final String VALUE = "value";
		public static final String APP_PERMISSION_ID = "app_permission_id";
		public static final String USER_TYPE = "user_type";
		public static final String ORIGIN = "origin";

		// Creation statement
		private static final String Create_Rating_Table = "CREATE TABLE "
				+ TABLE + "(" + ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + VALUE
				+ " DOUBLE, " + APP_PERMISSION_ID + " INTEGER, " + USER_TYPE + " INTEGER, " + ORIGIN + " TEXT);";

		
		private int id;
		private int value;
		private int appPermissionId;
		private boolean isExpert;
		private String origin;
		
		// empty constructor
		public RatingPermission() {

		}
		
		public RatingPermission(int id, int value, int appPermissionId, boolean isExpert, String origin){
			this.id = id;
			this.value = value; 
			this.appPermissionId = appPermissionId;
			this.isExpert = isExpert; 
			this.origin = origin;
		}
		
		
		// create table if it isn't existing yet
		public static void onCreate(SQLiteDatabase db) {
			db.execSQL(Create_Rating_Table);
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

		//getters and setters
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public int getAppPermissionId() {
			return appPermissionId;
		}

		public void setAppPermissionId(int appPermissionId) {
			this.appPermissionId = appPermissionId;
		}

		public boolean isExpert() {
			return isExpert;
		}

		public void setExpert(boolean isExpert) {
			this.isExpert = isExpert;
		}

		public String getOrigin() {
			return origin;
		}

		public void setOrigin(String origin) {
			this.origin = origin;
		}

			
		

}
