package de.otaris.zertapps.privacychecker.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AppDataSource {

	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private String[] allColumns = { App.APP_ID, App.APP_NAME, App.APP_VERSION,
			App.APP_INSTALLED, App.APP_RATING };

	public AppDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public App createApp(String name) {
	    ContentValues values = new ContentValues();
	    values.put(App.APP_NAME, name);
	    long insertId = database.insert(App.TABLE_APP, null,
	        values);
	    Cursor cursor = database.query(App.TABLE_APP,
	        allColumns, App.APP_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    App newApp = cursorToApp(cursor);
	    cursor.close();
	    return newApp;
	  } 

	public List<App> getAllApps(){
		List<App> apps = new ArrayList<App>();
		
		Cursor cursor = database.query(App.TABLE_APP, allColumns, 
				null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			App app = cursorToApp(cursor);
			apps.add(app);
			cursor.moveToNext();
		}
		cursor.close();
		return apps;
	}
	
	private App cursorToApp(Cursor cursor){
		App app = new App();
		app.setId(cursor.getInt(0));
		app.setName(cursor.getString(1));
		app.setVersion(cursor.getString(2));
		app.setInstalled(cursor.getInt(3) != 0);
		app.setRating(cursor.getFloat(4));
		
		return app;
	}
}
