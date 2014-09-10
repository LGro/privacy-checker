package de.otaris.zertapps.privacychecker.database.dataSource;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import de.otaris.zertapps.privacychecker.database.DatabaseHelper;
import de.otaris.zertapps.privacychecker.database.model.AppPermission;
import de.otaris.zertapps.privacychecker.database.model.AppPermissionRating;
import de.otaris.zertapps.privacychecker.database.model.PermissionExtended;

public class PermissionExtendedDataSource extends DataSource<PermissionExtended> {

	private PermissionDataSource permissionData;
	private AppPermissionDataSource appPermissionData;
	private RatingPermissionDataSource ratingPermissionData;
	private RatingAppDataSource ratingAppData;

	public PermissionExtendedDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
		permissionData = new PermissionDataSource(context);
		appPermissionData = new AppPermissionDataSource(context);
		ratingPermissionData = new RatingPermissionDataSource(context);
		ratingAppData = new RatingAppDataSource(context);
	}

	@Override
	protected PermissionExtended cursorToModel(Cursor cursor) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * populates an extended permission with all relational data
	 * 
	 * @param permission
	 *            extended permission to populate
	 * @return populated extended permission
	 */
	protected PermissionExtended populatePermission(
			PermissionExtended permission) {
		int expertPermissionUnexpected = 0;
		int nonExpertPermissionUnexpected = 0;
		int expertPermissionExpected = 0;
		int nonExpertPermissionExpected = 0;


		// Retrieve list of app permission ratings
		ratingPermissionData.open();
		ArrayList<AppPermissionRating> appPermissionRatingList = ratingPermissionData
				.getRatingPermissionByAppPermissionId(permission
						.getAppPermission().getId());
		
		// increase the values depending on their user-type and answer
		for (AppPermissionRating appPermissionRating : appPermissionRatingList) {
			if (appPermissionRating.getValue() == true) {
				if (appPermissionRating.isExpert() == true) {
					expertPermissionUnexpected++;
				} else {
					nonExpertPermissionUnexpected++;
				}
			} else {
				if (appPermissionRating.isExpert() == true) {
					expertPermissionExpected++;
				} else {
					nonExpertPermissionExpected++;
				}
			}
		}
		ratingPermissionData.close();
		
		permission.setExpertPermissionExpected(expertPermissionExpected);
		permission.setExpertPermissionUnexpected(expertPermissionUnexpected);
		permission.setNonExpertPermissionExpected(nonExpertPermissionExpected);
		permission.setNonExpertPermissionUnexpected(nonExpertPermissionUnexpected);
		
		return permission;
	}
	
	/**
	 * transforms an Permission to an PermissionExtended and adds all relevant
	 * relational data
	 * 
	 * @param permission
	 *            permission to be extended
	 * @return extended permission
	 */
	public PermissionExtended extendPermission(AppPermission appPermission) {
		PermissionExtended permissionExtended = new PermissionExtended(appPermission);

		
		permissionExtended.setPermissionId(appPermission.getPermissionId());
		permissionExtended.setAppId(appPermission.getAppId());
		permissionData.open();
		permissionExtended.setPermission(permissionData.getPermissionById(appPermission.getPermissionId()));
		permissionData.close();
		permissionExtended = populatePermission(permissionExtended);
		
		return permissionExtended;
	}
}
