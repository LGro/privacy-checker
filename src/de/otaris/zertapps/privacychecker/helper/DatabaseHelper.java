package de.otaris.zertapps.privacychecker.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "Privacy-Checker-Information";
	
	//this table doesn't need a private class
	private static final String TABLE_AppPermission = "AppPermission";
	
	//App_Permission Table - Column names
	private static final String APP_PERMISSION_ID = "APP_PERMISSION_ID";
	private static final String KEY_APP_ID = "app_id";
	private static final String KEY_PERMISSION_ID = "permission_id";
	
	private static final String Create_App_Permission_Table = "CREATE TABLE"
			+ TABLE_AppPermission + "(" + APP_PERMISSION_ID
			+ " INTEGER PRIMARY KEY," + KEY_APP_ID + " INTEGER FOREIGN KEY, "
			+ KEY_PERMISSION_ID + " INTEGER FOREIGN KEY),";
	
	
	
	
//	//Table Names
//	private static final String TABLE_APP = "App";
//	private static final String TABLE_PERMISSION = "Permission";
//	private static final String TABLE_RATING = "Rating";
//	private static final String TABLE_COMMENT = "Comment";
//	
//	private static final String TABLE_CATEGORY = "Category";
//	
//	//common column names
//	private static final String Name = "name";
//	
//	//APP Table - Column names
//	private static final String APP_ID = "id";
//	//name	
//	private static final String Version = "version";
//	
//	//PERMISSION Table - Column names
//	private static final String PERMISSION_ID = "id";
//	//name
//	private static final String Description = "description";
//	
//	
//	//RATING Table - Column names
//	
//	//COMMENT Table - Column names
//	
//	//CATEGORY Table - Column names
//	

//	
//	//All Create Statements
//	//App-Table create statement
//	
//	//Permission create statement
//	
//	
//	//Comment create statement
//	
//	//Rating create statement
//	
//	//App-Permission create statement
//	
//	private static final String LOG = null;
//	
	
	//constructor
	public DatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	//on Create -> is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase db){
		App.onCreate(db);
		Permission.onCreate(db);
		Comment.onCreate(db);
		Category.onCreate(db);
		Rating.onCreate(db);
		//creating all required tables
		db.execSQL(Create_App_Permission_Table);
		
	}
	
	//Upgrade the Database to the latest Version
	// e.g. if you increase the database version
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		App.onUpgrade(db, oldVersion, newVersion);
		Permission.onUpgrade(db, oldVersion, newVersion);
		Comment.onUpgrade(db, oldVersion, newVersion);
		Category.onUpgrade(db, oldVersion, newVersion);
		Rating.onUpgrade(db, oldVersion, newVersion);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_AppPermission);
		
		//create new tables
		onCreate(db);
		
	}
	/**
	 * Creating a new App Item 
	 * -Entry at the App Table
	 * 
	 * @param app
	 * @param tag_ids
	 * @return
	 */
//	public long createApp(App app, long[] tag_ids){
//		SQLiteDatabase db = this.getWritableDatabase();
//		
//		ContentValues values = new ContentValues();
//		//values.put(APP_ID, app.getID());
//		values.put(Name, app.getName());
//		values.put(Version, app.getVersion());
//		
//		//insert row
//		long app_id = db.insert(TABLE_APP, null, values);
//		
//		// //assigning App to Permission
//		
//		//sth is wroong here TODO
//		for (long app_id : permission_id){
//			createPermissionTag(app_id, permission_id);
//		}
//		return app_id;
//		}
//	/**
//	 * get a single app by a given id
//	 * @param app_id
//	 * @return
//	 */
//	public App getApp(long app_id){
//		SQLiteDatabase db = this.getReadableDatabase();
//		
//		String selectQuerey = "SELECT * FROM " + TABLE_APP + "WHERE " + APP_ID + " = " + app_id;
//		
//		Log.e(LOG, selectQuerey);
//		Cursor c = db.rawQuery(selectQuerey, null);
//		
//		if(c != null){
//			c.moveToFirst();
//		}
//		App newApp = new App();
//		newApp.setID(c.getInt(c.getColumnIndex(APP_ID)));
//		newApp.setName(c.getString(c.getColumnIndex(Name)));
//		newApp.setVersion(c.getString(c.getColumnIndex(Version)));
//		
//		return newApp;
//		}
	
	
	/**
	 * Fetching all Apps: reading all App rows and adding them to a list array
	 */
	
	public List<App> getAllApps(){
		List<App> apps = new ArrayList<App>();
		String selectQuery = "SELECT * FROM " + App.TABLE_APP;
		
		//Log.e(LOG, selectQuery);
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		
		//loop for all rows
		if(c.moveToFirst()){
			do {
				App newApp = new App();
				newApp.setID(c.getInt(c.getColumnIndex(App.APP_ID)));
				newApp.setName(c.getString(c.getColumnIndex(App.APP_NAME)));
				newApp.setVersion(c.getString(c.getColumnIndex(App.APP_VERSION)));
				
				apps.add(newApp);
			}while (c.moveToNext());
		}
		return apps;
	}
				
	
	//More methods that we might need can be added here
	
	
	
	// closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


	
}
