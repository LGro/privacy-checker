package de.otaris.zertapps.privacychecker.database;

import java.util.ArrayList;
import java.util.List;

import de.otaris.zertapps.privacychecker.AppsListOrder;
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
			App.PRIVACY_RATING, App.INSTALLED, App.FUNCTIONAL_RATING };

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
		app.setPrivacyRating(cursor.getFloat(4));
		// convert integer from SQLite to boolean in model representation
		app.setInstalled(cursor.getInt(5) != 0);
		app.setFunctionalRating(cursor.getFloat(6));

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
			float privacyRating, boolean installed, float functionalRating) {
		// set values for columns
		ContentValues values = new ContentValues();
		values.put(App.NAME, name);
		values.put(App.LABEL, label);
		values.put(App.VERSION, version);
		values.put(App.PRIVACY_RATING, privacyRating);
		values.put(App.INSTALLED, installed);
		values.put(App.FUNCTIONAL_RATING, functionalRating);

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
		return getAllApps(AppsListOrder.PRIVACY_RATING, false);
	}

	/**
	 * get all Apps from the DB, ordered by given order ascending or descending
	 * depending on second argument
	 * 
	 * @param order
	 * @param ascending
	 * 
	 * @return sorted list of all apps
	 */
	public List<App> getAllApps(AppsListOrder order, boolean ascending) {
		List<App> apps = new ArrayList<App>();

		// set primary order depending on argument
		String orderBy = (ascending) ? order + " ASC, " : order + " DESC, ";
		// order case insensitive
		orderBy = orderBy + App.LABEL + " COLLATE NOCASE ASC";

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
		return getInstalledApps(AppsListOrder.PRIVACY_RATING, true);
	}

	/**
	 * get all Apps from the DB that are marked as installed, ordered by given
	 * order ascending or descending depending on second argument
	 * 
	 * @param order
	 * @param ascending
	 * 
	 * @return sorted list of all locally installed Apps
	 */
	public List<App> getInstalledApps(AppsListOrder order, boolean ascending) {
		List<App> apps = new ArrayList<App>();

		// build query
		String whereClause = App.INSTALLED + " = 1";
		// set primary order depending on argument
		String orderBy = (ascending) ? order + " ASC, " : order + " DESC, ";
		// order case insensitive
		orderBy = orderBy + App.LABEL + " COLLATE NOCASE ASC";
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
}
