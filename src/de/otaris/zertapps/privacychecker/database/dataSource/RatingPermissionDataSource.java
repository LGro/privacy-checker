package de.otaris.zertapps.privacychecker.database.dataSource;

import de.otaris.zertapps.privacychecker.database.DatabaseHelper;
import de.otaris.zertapps.privacychecker.database.model.Permission;
import de.otaris.zertapps.privacychecker.database.model.RatingApp;
import de.otaris.zertapps.privacychecker.database.model.RatingPermission;
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
	
	public RatingPermission getRatingPermissionById(int id){
		return null;
	}
	
	public int getValueById(int appPermissionId){
		return 0;
	}

	
}
