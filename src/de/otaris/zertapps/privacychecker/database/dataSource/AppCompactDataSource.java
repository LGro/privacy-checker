package de.otaris.zertapps.privacychecker.database.dataSource;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import de.otaris.zertapps.privacychecker.appsList.AppsListOrder;
import de.otaris.zertapps.privacychecker.database.DatabaseHelper;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;

/**
 * Handles requests concerning Apps to the database.
 */
public class AppCompactDataSource extends DataSource<AppCompact> implements
		AppDataSource<AppCompact> {

	private String[] allColumns = { AppCompact.ID, AppCompact.CATEGORY_ID,
			AppCompact.NAME, AppCompact.LABEL, AppCompact.VERSION,
			AppCompact.PRIVACY_RATING, AppCompact.INSTALLED,
			AppCompact.FUNCTIONAL_RATING, AppCompact.TIMESTAMP,
			AppCompact.DESCRIPTION, AppCompact.ICON,
			AppCompact.AUTOMATIC_RATING,
			AppCompact.CATEGORY_WEIGHTED_AUTOMATIC_RATING,
			AppCompact.FUNCTIONAL_RATING_COUNTER, AppCompact.DEVELOPER };

	public AppCompactDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	/**
	 * convert database result cursor (pointing to a result set) to app object
	 * 
	 * @param cursor
	 * @return cursor data as App object
	 */
	protected AppCompact cursorToModel(Cursor cursor) {
		if (cursor.getCount() == 0)
			return null;

		AppCompact app = new AppCompact(cursor.getInt(0), cursor.getInt(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getFloat(5), cursor.getInt(6) != 0, cursor.getFloat(7),
				cursor.getLong(8), cursor.getString(9), cursor.getBlob(10),
				cursor.getFloat(11), cursor.getFloat(12), cursor.getInt(13),
				cursor.getString(14));

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
	public AppCompact createApp(int categoryId, String name, String label,
			String version, float privacyRating, boolean installed,
			float functionalRating, String description, byte[] icon,
			float automaticRating, int functionRatingCounter, String developer) {
		// set values for columns
		ContentValues values = new ContentValues();
		values.put(AppCompact.CATEGORY_ID, categoryId);
		values.put(AppCompact.NAME, name);
		values.put(AppCompact.LABEL, label);
		values.put(AppCompact.VERSION, version);
		values.put(AppCompact.PRIVACY_RATING, privacyRating);
		values.put(AppCompact.INSTALLED, installed);
		values.put(AppCompact.FUNCTIONAL_RATING, functionalRating);
		// Gets current time in milliseconds since jan1,1970. The divide by 1000
		// turns it into unix seconds instead of milliseconds.
		long currentTimestamp = System.currentTimeMillis() / 1000;
		values.put(AppCompact.TIMESTAMP, currentTimestamp);
		values.put(AppCompact.DESCRIPTION, description);
		values.put(AppCompact.ICON, icon);
		values.put(AppCompact.AUTOMATIC_RATING, automaticRating);
		values.put(AppCompact.FUNCTIONAL_RATING_COUNTER, functionRatingCounter);
		values.put(AppCompact.DEVELOPER, developer);

		// insert into DB
		long insertId = database.insert(AppCompact.TABLE, null, values);

		// get recently inserted App by ID
		return getAppById(insertId);
	}

	/**
	 * gets single app by id from database
	 * 
	 * public alias for getAppById(long)
	 * 
	 * @param appId
	 *            id to identify a single app
	 * @return app object for given id
	 */
	public AppCompact getAppById(int appId) {
		return getAppById((long) appId);
	}

	/**
	 * gets single app by id from database
	 * 
	 * @param appId
	 *            id to identify a single app
	 * @return app object for given id
	 */
	protected AppCompact getAppById(long appId) {
		// build database query
		Cursor cursor = database.query(AppCompact.TABLE, allColumns,
				AppCompact.ID + " = " + appId, null, null, null, null);
		cursor.moveToFirst();

		// convert to App object
		AppCompact newApp = cursorToModel(cursor);
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
	public List<AppCompact> getAllApps() {
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
	public List<AppCompact> getAllApps(AppsListOrder order, boolean ascending) {
		// set primary order depending on argument
		String orderBy = (ascending) ? order + " ASC, " : order + " DESC, ";
		// order case insensitive
		orderBy = orderBy + AppCompact.LABEL + " COLLATE NOCASE ASC";

		Cursor cursor = database.query(AppCompact.TABLE, allColumns, null,
				null, null, null, orderBy);

		return cursorToModelList(cursor);
	}

	/**
	 * get all Apps from the DB that are marked as installed, ordered by privacy
	 * rating (high to low) and label (alphabetically)
	 * 
	 * @return sorted list of all locally installed Apps
	 */
	public List<AppCompact> getInstalledApps() {
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
	public List<AppCompact> getInstalledApps(AppsListOrder order,
			boolean ascending) {

		// build query
		String whereClause = AppCompact.INSTALLED + " = 1";
		// set primary order depending on argument
		String orderBy = (ascending) ? order + " ASC, " : order + " DESC, ";
		// order case insensitive
		orderBy = orderBy + AppCompact.LABEL + " COLLATE NOCASE ASC";
		Cursor cursor = database.query(AppCompact.TABLE, allColumns,
				whereClause, null, null, null, orderBy);

		return cursorToModelList(cursor);
	}

	/**
	 * Get the n most recently updated apps from the database.
	 * 
	 * @param n
	 *            the amount of apps to return
	 * 
	 * @return returns the n most recently updated apps
	 */
	public List<AppCompact> getLastUpdatedApps(int n) {
		// build query
		String orderBy = AppCompact.TIMESTAMP + " DESC" + " LIMIT " + n;
		Cursor cursor = database.query(AppCompact.TABLE, allColumns, null,
				null, null, null, orderBy);

		return cursorToModelList(cursor);
	}

	/**
	 * get all Apps from the DB that belong to the given category, ordered by
	 * given order ascending or descending depending on third argument
	 * 
	 * @param categoryId
	 * @param order
	 * @param ascending
	 * 
	 * @return sorted list of all Apps from category
	 */
	public List<AppCompact> getAppsByCategory(int categoryId,
			AppsListOrder order, boolean ascending) {
		// build query
		String whereClause = AppCompact.CATEGORY_ID + " = " + categoryId;
		// set primary order depending on argument
		String orderBy = (ascending) ? order + " ASC, " : order + " DESC, ";
		// order case insensitive
		orderBy = orderBy + AppCompact.LABEL + " COLLATE NOCASE ASC";
		Cursor cursor = database.query(AppCompact.TABLE, allColumns,
				whereClause, null, null, null, orderBy);

		return cursorToModelList(cursor);
	}

	/**
	 * Updates all attributes of a given app that are contained in
	 * app.modifiedAttributes.
	 */
	@Override
	public AppCompact update(AppCompact app) {

		String filter = AppCompact.ID + " = " + app.getId();

		// set values for columns
		ContentValues values = new ContentValues();

		long currentTimestamp = System.currentTimeMillis() / 1000;
		values.put(AppCompact.TIMESTAMP, currentTimestamp);

		HashMap<String, String> attributes = app.getModifiedAttributes();
		Iterator<Entry<String, String>> it = attributes.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> pair = it.next();

			String methodName = pair.getValue();
			methodName = "g" + methodName.substring(1);

			try {
				values.put(pair.getKey(), AppCompact.class
						.getMethod(methodName).invoke(app).toString());
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException e) {
				Log.e("AppCompactDataSource", "Getter '" + methodName
						+ "' could not be called.");
				e.printStackTrace();
			}
		}

		database.update(AppCompact.TABLE, values, filter, null);

		return getAppById(app.getId());
	}

	public AppCompact getAppByName(String name) {
		// build database query
		Cursor cursor = database.query(AppCompact.TABLE, allColumns,
				AppCompact.NAME + " = '" + name + "'", null, null, null, null);
		cursor.moveToFirst();

		// convert to App object
		AppCompact app = cursorToModel(cursor);
		cursor.close();

		// return app object
		return app;
	}
}
