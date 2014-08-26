package de.otaris.zertapps.privacychecker.database.dataSource;

import de.otaris.zertapps.privacychecker.database.DatabaseHelper;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;
import de.otaris.zertapps.privacychecker.database.model.AppPermission;
import de.otaris.zertapps.privacychecker.database.model.Permission;
import de.otaris.zertapps.privacychecker.database.model.RatingApp;
import de.otaris.zertapps.privacychecker.database.model.AppPermissionRating;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * handles requests concerning the AppPermission rating to the databse
 */
public class AppPermissionRatingDataSource extends DataSource<AppPermissionRating> {

	
	private String[] allColumns = { AppPermissionRating.ID, AppPermissionRating.VALUE,
			AppPermissionRating.APP_PERMISSION_ID, AppPermissionRating.USER_TYPE};

	public AppPermissionRatingDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public AppPermissionRating createRatingPermission(int value, int appPermissionId, boolean isExpert) {
		// set values for columns
		ContentValues values = new ContentValues();
		values.put(AppPermissionRating.VALUE, value);
		values.put(AppPermissionRating.APP_PERMISSION_ID, appPermissionId);
		values.put(AppPermissionRating.USER_TYPE, isExpert);

		// insert into DB
		long insertId = database.insert(AppPermissionRating.TABLE, null, values);

		// get recently inserted App by ID
		return getRatingPermissionById(insertId);
	}

	
	@Override
	protected AppPermissionRating cursorToModel(Cursor cursor) {
		if (cursor.getCount() == 0)
			return null;

		AppPermissionRating ratingPermission = new AppPermissionRating();

		ratingPermission.setId(cursor.getInt(0));
		ratingPermission.setValue(cursor.getInt(1));
		ratingPermission.setAppPermissionId(cursor.getInt(2));
		ratingPermission.setExpert((cursor.getInt(3) != 0));
		
		return ratingPermission;
	}
	
	public AppPermissionRating getRatingPermissionById(long ratingId) {
		// build database query
		Cursor cursor = database.query(AppPermissionRating.TABLE, allColumns,
				AppPermissionRating.ID + " = " + ratingId, null, null, null, null);
		cursor.moveToFirst();

		// convert to RatingApp object
		AppPermissionRating newRating = cursorToModel(cursor);
		cursor.close();

		// return RatingApp object
		return newRating;
	}

	
	public int getValueById(int appPermissionId){
		return 0;
	}

	
}
