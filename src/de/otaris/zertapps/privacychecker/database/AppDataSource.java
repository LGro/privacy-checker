package de.otaris.zertapps.privacychecker.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Handles requests concerning Apps to the database.
 */
public class AppDataSource {

	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private String[] allColumns = { App.ID, App.NAME, App.LABEL, App.VERSION,
			App.INSTALLED, App.RATING, App.TIMETSTAMP };

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
	 * convert database result cursor (pointing to a result set) to app object
	 * 
	 * @param cursor
	 * @return cursor data as App object
	 */
	private App cursorToApp(Cursor cursor) {
		App app = new App();

		app.setId(cursor.getInt(0));
		app.setName(cursor.getString(1));
		app.setLabel(cursor.getString(2));
		app.setVersion(cursor.getString(3));
		// convert integer from SQLite to boolean in model representation
		app.setInstalled(cursor.getInt(4) != 0);
		app.setRating(cursor.getFloat(5));
		app.setTimestamp(cursor.getLong(6));

		return app;
	}

	/**
	 * create app in DB by all attributes
	 * 
	 * @param name
	 * @param label
	 * @param version
	 * @param installed
	 * @param rating
	 * @return app object of the newly created app
	 */

	public App createApp(String name, String label, String version,
			boolean installed, float rating) {
		// set values for columns
		ContentValues values = new ContentValues();
		values.put(App.NAME, name);
		values.put(App.LABEL, label);
		values.put(App.VERSION, version);
		values.put(App.INSTALLED, installed);
		values.put(App.RATING, rating);
		
		// get data for the timestamp
		Date date = new Date();
		long currentTimestamp = date.getTime()/1000;
		
		values.put(App.TIMETSTAMP, currentTimestamp);

		// insert into DB
		long insertId = database.insert(App.TABLE, null, values);

		// get recently inserted App by ID
		return getAppById(insertId);
	}

	/**
	 * gets single app by id from database
	 * 
	 * @param appId
	 *            id to identify a single app
	 * @return app object for given id
	 */
	public App getAppById(long appId) {
		// build database query
		Cursor cursor = database.query(App.TABLE, allColumns, App.ID + " = "
				+ appId, null, null, null, null);
		cursor.moveToFirst();

		// convert to App object
		App newApp = cursorToApp(cursor);
		cursor.close();

		// return app object
		return newApp;
	}

	/**
	 * get all Apps from the DB, ordered by privacy rating (high to low) and
	 * label (alphabetically)
	 * 
	 * @return list of all Apps
	 */
	public List<App> getAllApps() {
		List<App> apps = new ArrayList<App>();

		String orderBy = App.RATING + " DESC, " + App.LABEL + " ASC";
		Cursor cursor = database.query(App.TABLE, allColumns, null, null, null,
				null, orderBy);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			App app = cursorToApp(cursor);
			apps.add(app);
			cursor.moveToNext();
		}
		cursor.close();
		return apps;
	}

	/**
	 * get all Apps from the DB that are marked as installed, ordered by privacy
	 * rating (high to low) and label (alphabetically)
	 * 
	 * @return sorted list of all locally installed Apps
	 */
	public List<App> getInstalledApps() {
		List<App> apps = new ArrayList<App>();

		// build query
		String whereClause = App.INSTALLED + " = 1";
		String orderBy = App.RATING + " DESC, " + App.LABEL + " ASC";
		Cursor cursor = database.query(App.TABLE, allColumns, whereClause,
				null, null, null, orderBy);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			App app = cursorToApp(cursor);
			apps.add(app);
			cursor.moveToNext();
		}
		cursor.close();
		return apps;
	}


	/**
	 * Get the last updated apps from the database.
	 * 
	 * @return Returns a list of n(4) apps ordered by date.
	 */
	public List<App> getLastUpdatedApps() {
		List<App> apps = new ArrayList<App>();
		
		String orderby = "timestamp ASC LIMIT 4";
		Cursor cursor = database.query(App.TABLE, allColumns, null, null, null, null, orderby);
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()) {
			App app = cursorToApp(cursor);
			apps.add(app);
			cursor.moveToNext();
		}
		
		cursor.close();
		return apps;
	}
}
