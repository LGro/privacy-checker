package de.otaris.zertapps.privacychecker.database.dataSource;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import de.otaris.zertapps.privacychecker.database.DatabaseHelper;
import de.otaris.zertapps.privacychecker.database.model.AppPermissionRating;
import de.otaris.zertapps.privacychecker.database.model.Comment;

/**
 * handles requests concerning the AppPermission rating to the databse
 */
public class RatingPermissionDataSource extends DataSource<AppPermissionRating> {

	private String[] allColumns = { AppPermissionRating.ID,
			AppPermissionRating.VALUE, AppPermissionRating.APP_PERMISSION_ID,
			AppPermissionRating.USER_TYPE };

	public RatingPermissionDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public AppPermissionRating createRatingPermission(boolean value,
			int appPermissionId, boolean isExpert) {
		// set values for columns
		ContentValues values = new ContentValues();
		values.put(AppPermissionRating.VALUE, value);
		values.put(AppPermissionRating.APP_PERMISSION_ID, appPermissionId);
		values.put(AppPermissionRating.USER_TYPE, isExpert);

		// insert into DB
		long insertId = database
				.insert(AppPermissionRating.TABLE, null, values);

		// get recently inserted App by ID
		return getRatingPermissionById(insertId);
	}

	@Override
	protected AppPermissionRating cursorToModel(Cursor cursor) {
		if (cursor.getCount() == 0)
			return null;

		AppPermissionRating ratingPermission = new AppPermissionRating();

		ratingPermission.setId(cursor.getInt(0));
		ratingPermission.setValue((cursor.getInt(1) != 0));
		ratingPermission.setAppPermissionId(cursor.getInt(2));
		ratingPermission.setExpert((cursor.getInt(3) != 0));

		return ratingPermission;
	}

	/**
	 * retrieves an AppPermissionRating by its id
	 * @param ratingId
	 * @return AppPermissionRating
	 */
	public AppPermissionRating getRatingPermissionById(long ratingId) {
		// build database query
		Cursor cursor = database.query(AppPermissionRating.TABLE, allColumns,
				AppPermissionRating.ID + " = " + ratingId, null, null, null,
				null);
		cursor.moveToFirst();

		// convert to RatingPermission object
		AppPermissionRating newRating = cursorToModel(cursor);
		cursor.close();

		// return RatingPermission object
		return newRating;
	}

	/**
	 * retrieves all AppPermissionRatings from an AppPermission-id
	 * @param appPermissionId
	 * @return ArrayList of AppPermissionRating
	 */
	public ArrayList<AppPermissionRating> getRatingPermissionByAppPermissionId(
			long appPermissionId) {
		// build query
		String whereClause = AppPermissionRating.APP_PERMISSION_ID + " = "
				+ appPermissionId;

		Cursor cursor = database.query(AppPermissionRating.TABLE, allColumns,
				whereClause, null, null, null, null);

		return (ArrayList<AppPermissionRating>) cursorToModelList(cursor);
	}

	/**
	 * retrieves all values from an app id from experts
	 * 
	 * @param appId
	 * @return a list of values
	 */
	public ArrayList<Boolean> getExpertValuesById(long appId) {
		ArrayList<Boolean> values = new ArrayList<Boolean>();

		// build query
		String whereClause = AppPermissionRating.USER_TYPE + " = 1" + " AND "
				+ AppPermissionRating.APP_PERMISSION_ID + " = " + appId;

		Cursor cursor = database.query(AppPermissionRating.TABLE, allColumns,
				whereClause, null, null, null, null);
		// put values in list
		for (AppPermissionRating ratingPermission : cursorToModelList(cursor)) {
			values.add(ratingPermission.getValue());
		}

		return values;
	}

	/**
	 * retrieve all values from an app id from nonExperts
	 * 
	 * @param appId
	 * @return a list of values
	 */
	public ArrayList<Boolean> getNonExpertValuesById(long appId) {
		ArrayList<Boolean> values = new ArrayList<Boolean>();

		// build query
		String whereClause = AppPermissionRating.USER_TYPE + " = 0" + " AND "
				+ AppPermissionRating.APP_PERMISSION_ID + " = " + appId;

		Cursor cursor = database.query(AppPermissionRating.TABLE, allColumns,
				whereClause, null, null, null, null);

		// put values in list
		for (AppPermissionRating ratingPermission : cursorToModelList(cursor)) {
			values.add(ratingPermission.getValue());
		}

		return values;
	}

}
