package de.otaris.zertapps.privacychecker.database.dataSource;

import de.otaris.zertapps.privacychecker.database.DatabaseHelper;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;
import de.otaris.zertapps.privacychecker.database.model.AppPermission;
import de.otaris.zertapps.privacychecker.database.model.Permission;
import de.otaris.zertapps.privacychecker.database.model.RatingApp;
import de.otaris.zertapps.privacychecker.database.model.RatingPermission;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * handles requests concerning the AppPermission rating to the databse
 */
public class RatingPermissionDataSource extends DataSource<RatingPermission> {

	
	private String[] allColumns = { RatingPermission.ID, RatingPermission.VALUE,
			RatingPermission.APP_PERMISSION_ID, RatingPermission.USER_TYPE};

	public RatingPermissionDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public RatingPermission createRatingPermission(int value, int appPermissionId, boolean isExpert) {
		// set values for columns
		ContentValues values = new ContentValues();
		values.put(RatingPermission.VALUE, value);
		values.put(RatingPermission.APP_PERMISSION_ID, appPermissionId);
		values.put(RatingPermission.USER_TYPE, isExpert);

		// insert into DB
		long insertId = database.insert(RatingPermission.TABLE, null, values);

		// get recently inserted App by ID
		return getRatingPermissionById(insertId);
	}

	
	@Override
	protected RatingPermission cursorToModel(Cursor cursor) {
		if (cursor.getCount() == 0)
			return null;

		RatingPermission ratingPermission = new RatingPermission();

		ratingPermission.setId(cursor.getInt(0));
		ratingPermission.setValue(cursor.getInt(1));
		ratingPermission.setAppPermissionId(cursor.getInt(2));
		ratingPermission.setExpert((cursor.getInt(3) != 0));
		
		return ratingPermission;
	}
	
	public RatingPermission getRatingPermissionById(long ratingId) {
		// build database query
		Cursor cursor = database.query(RatingPermission.TABLE, allColumns,
				RatingPermission.ID + " = " + ratingId, null, null, null, null);
		cursor.moveToFirst();

		// convert to RatingApp object
		RatingPermission newRating = cursorToModel(cursor);
		cursor.close();

		// return RatingApp object
		return newRating;
	}

	
	public int getValueById(int appPermissionId){
		return 0;
	}

	
}
