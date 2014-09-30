package de.otaris.zertapps.privacychecker.database.dataSource;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.util.Log;
import de.otaris.zertapps.privacychecker.database.DatabaseHelper;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;
import de.otaris.zertapps.privacychecker.database.model.AppPermission;
import de.otaris.zertapps.privacychecker.database.model.Permission;
import de.otaris.zertapps.privacychecker.database.model.RatingApp;
import de.otaris.zertapps.privacychecker.database.model.AppPermissionRating;

/**
 * handles requests concerning the app rating to the databse
 */
public class RatingAppDataSource extends DataSource<RatingApp> {

	private String[] allColumns = { RatingApp.ID, RatingApp.VALUE,
			RatingApp.APP_ID, RatingApp.USER_TYPE };
	private AppPermissionDataSource appPermissionData;

	public RatingAppDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
		appPermissionData = new AppPermissionDataSource(context);

	}

	/**
	 * creates a RatingApp
	 * 
	 * @param value
	 * @param appId
	 * @param isExpert
	 * @return the RatingApp
	 */
	public RatingApp createRatingApp(int value, int appId, boolean isExpert) {
		// set values for columns
		ContentValues values = new ContentValues();
		values.put(RatingApp.VALUE, value);
		values.put(RatingApp.APP_ID, appId);
		values.put(RatingApp.USER_TYPE, isExpert);

		// insert into DB
		long insertId = database.insert(RatingApp.TABLE, null, values);

		// get recently inserted App by ID
		return getRatingAppById(insertId);
	}

	@Override
	protected RatingApp cursorToModel(Cursor cursor) {
		if (cursor.getCount() == 0)
			return null;

		RatingApp ratingApp = new RatingApp();

		ratingApp.setId(cursor.getInt(0));
		ratingApp.setValue(cursor.getInt(1));
		ratingApp.setAppId(cursor.getInt(2));
		// 1 = isExpert
		ratingApp.setExpert((cursor.getInt(3) != 0));

		return ratingApp;
	}

	/**
	 * retrieve all values from an app id from experts
	 * 
	 * @param appId
	 * @return a list of values
	 */
	public ArrayList<Integer> getExpertValuesById(long appId) {
		ArrayList<Integer> values = new ArrayList<Integer>();

		// build query
		String whereClause = RatingApp.USER_TYPE + " = 1" + " AND "
				+ RatingApp.APP_ID + " = " + appId;

		Cursor cursor = database.query(RatingApp.TABLE, allColumns,
				whereClause, null, null, null, null);
		// put values in list
		for (RatingApp ratingApp : cursorToModelList(cursor)) {
			values.add(ratingApp.getValue());
		}

		return values;
	}

	/**
	 * retrieve all values from an app id from nonExperts
	 * 
	 * @param appId
	 * @return a list of values
	 */
	public ArrayList<Integer> getNonExpertValuesById(long appId) {
		ArrayList<Integer> values = new ArrayList<Integer>();

		// build query
		String whereClause = RatingApp.USER_TYPE + " = 0" + " AND "
				+ RatingApp.APP_ID + " = " + appId;

		Cursor cursor = database.query(RatingApp.TABLE, allColumns,
				whereClause, null, null, null, null);

		// put values in list
		for (RatingApp ratingApp : cursorToModelList(cursor)) {
			values.add(ratingApp.getValue());
		}

		return values;
	}

	/**
	 * retrieves a rating by RatingApp_id
	 * 
	 * @param ratingId
	 * @return the RatingApp
	 */
	public RatingApp getRatingAppById(long ratingId) {
		// build database query
		Cursor cursor = database.query(RatingApp.TABLE, allColumns,
				RatingApp.ID + " = " + ratingId, null, null, null, null);
		cursor.moveToFirst();

		// convert to RatingApp object
		RatingApp newRating = cursorToModel(cursor);
		cursor.close();

		// return RatingApp object
		return newRating;
	}

	/**
	 * calculates a total rating for a given app regarding the expert rating and
	 * the non-expert rating
	 * 
	 * @param appId
	 * @return rating
	 */
	public float calculateTotalExpertAndNonExpertRating(int appId) {
		float expertRating = 0;
		float nonExpertRating = 0;
		int experts = 1;
		int nonExperts = 1;
		// calculates mean for expert rating
		for (int i : getExpertValuesById(appId)) {
			expertRating += i;
			experts++;
		}
		if (experts > 1) {
			experts--;
		}
		expertRating = expertRating / experts;
		// calculates mean for non-expert rating
		for (int i : getNonExpertValuesById(appId)) {
			nonExpertRating += i;
			nonExperts++;
		}
		if (nonExperts > 1) {
			nonExperts--;
		}
		nonExpertRating = nonExpertRating / nonExperts;

		return (expertRating + nonExpertRating) / 3;
	}

	public float getTotalExpertAndNonExpertRatingByAppId(int appId) {

		return calculateTotalExpertAndNonExpertRating(appId);

	}

}
