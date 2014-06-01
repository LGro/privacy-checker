package de.otaris.zertapps.privacychecker.database;

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
	private String[] allColumns = { App.ID, App.NAME, App.VERSION,
			App.INSTALLED, App.RATING };

	public AppDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	/**
	 * open DB connection
	 * 
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	/**
	 * close DB connection
	 */
	public void close() {
		dbHelper.close();
	}

	/**
	 * convert database result cursor to app object
	 * 
	 * @param cursor
	 * @return cursor data as App object
	 */
	private App cursorToApp(Cursor cursor) {
		App app = new App();

		app.setId(cursor.getInt(0));
		app.setName(cursor.getString(1));
		app.setVersion(cursor.getString(2));
		// convert integer from SQLite to boolean in model representation
		app.setInstalled(cursor.getInt(3) != 0);
		app.setRating(cursor.getFloat(4));

		return app;
	}

	/**
	 * create app in DB by name
	 * 
	 * TODO: check duplicates
	 * 
	 * @param name
	 *            name of the App
	 * @return newly created App with ID
	 */
	public App createApp(String name, String version, boolean installed,
			float rating) {
		// set values for columns
		ContentValues values = new ContentValues();
		values.put(App.NAME, name);
		values.put(App.VERSION, version);
		values.put(App.INSTALLED, installed);
		values.put(App.RATING, rating);

		// insert into DB
		long insertId = database.insert(App.TABLE, null, values);

		// get recently inserted App by ID
		Cursor cursor = database.query(App.TABLE, allColumns, App.ID + " = "
				+ insertId, null, null, null, null);
		cursor.moveToFirst();

		// convert to App object
		App newApp = cursorToApp(cursor);
		cursor.close();

		// return newly inserted App
		return newApp;
	}

	/**
	 * get all Apps from the DB
	 * 
	 * @return list of all Apps
	 */
	public List<App> getAllApps() {
		List<App> apps = new ArrayList<App>();

		Cursor cursor = database.query(App.TABLE, allColumns, null, null, null,
				null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			App app = cursorToApp(cursor);
			apps.add(app);
			cursor.moveToNext();
		}
		cursor.close();
		return apps;
	}
}