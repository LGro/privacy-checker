package de.otaris.zertapps.privacychecker.database.dataSource;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.util.Log;
import de.otaris.zertapps.privacychecker.database.DatabaseHelper;
import de.otaris.zertapps.privacychecker.database.model.AppPermission;
import de.otaris.zertapps.privacychecker.database.model.Permission;

/**
 * 
 * Handles requests concerning the App Permission relation on the Database
 * 
 */
public class AppPermissionDataSource extends DataSource<AppPermission> {

	private String[] allColumns = { AppPermission.ID, AppPermission.APP_ID,
			AppPermission.PERMISSION_ID };

	private PermissionDataSource permissionData;

	public AppPermissionDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
		permissionData = new PermissionDataSource(context);
	}

	@Override
	protected AppPermission cursorToModel(Cursor cursor) {
		AppPermission appPermission = new AppPermission();

		appPermission.setId(cursor.getInt(0));
		appPermission.setAppId(cursor.getInt(1));
		appPermission.setPermissionId(cursor.getInt(2));

		return appPermission;
	}

	/**
	 * retrieves the permissions of an App
	 * 
	 * @param appId
	 * @return list of Permissions by an appId
	 */
	public ArrayList<Permission> getPermissionsByAppId(int appId) {
		ArrayList<Permission> permissions = new ArrayList<Permission>();

		// build query
		String whereClause = AppPermission.APP_ID + " = " + appId;
		Cursor cursor = database.query(AppPermission.TABLE, allColumns,
				whereClause, null, null, null, null);

		List<AppPermission> appPermissions = cursorToModelList(cursor);

		permissionData.open();
		for (AppPermission appPermission : appPermissions) {
			try {
				permissions.add(permissionData.getPermissionById(appPermission
						.getPermissionId()));
			} catch (CursorIndexOutOfBoundsException e) {
				Log.e("PermissionDataSource CURSOR ERROR", e.getMessage());
			}
		}
		permissionData.close();

		permissions = sortPermissions(permissions);
		return permissions;
	}

	/**
	 * sorts a list of Permission by their criticality by using quicksort
	 * 
	 * @param permissions
	 *            the list to sort
	 * @return a sorted list of Permission
	 */
	private ArrayList<Permission> sortPermissions(
			ArrayList<Permission> permissions) {

		if (permissions.size() <= 1)
			return permissions;

		int firstCriticality = permissions.get(0).getCriticality();

		ArrayList<Permission> moreCritical = new ArrayList<Permission>(
				permissions.size());
		ArrayList<Permission> lessCritical = new ArrayList<Permission>(
				permissions.size());

		for (int i = 1; i < permissions.size(); i++) {
			if (permissions.get(i).getCriticality() > firstCriticality)
				moreCritical.add(permissions.get(i));
			else
				lessCritical.add(permissions.get(i));
		}

		moreCritical = sortPermissions(moreCritical);
		lessCritical = sortPermissions(lessCritical);

		moreCritical.addAll(lessCritical);

		return moreCritical;
	}

	/**
	 * creates an AppPermission
	 * 
	 * @param appId
	 * @param permissionId
	 * @return recently inserted AppPermission
	 */
	public AppPermission createAppPermission(int appId, int permissionId) {

		ContentValues values = new ContentValues();
		values.put(AppPermission.APP_ID, appId);
		values.put(AppPermission.PERMISSION_ID, permissionId);

		// insert into DB
		long insertId = database.insert(AppPermission.TABLE, null, values);

		// get recently inserted AppPermission by ID
		return getAppPermissionById(insertId);

	}

	/**
	 * retrieves an AppPermission from an id
	 * 
	 * @param appPermissionId
	 *            the id of the AppPermission to retrieve
	 * @return the AppPermission
	 */
	private AppPermission getAppPermissionById(long appPermissionId) {
		Cursor cursor = database.query(AppPermission.TABLE, allColumns,
				AppPermission.ID + " = " + appPermissionId, null, null, null,
				null);
		cursor.moveToFirst();

		// convert to AppPermission object
		AppPermission newAppPermission = cursorToModel(cursor);
		cursor.close();

		// return AppPermission object
		return newAppPermission;

	}

	public AppPermission getAppPermissionByAppAndPermissionId(long appId,
			long permissionId) {
		Cursor cursor = database.query(AppPermission.TABLE, allColumns,
				AppPermission.APP_ID + "=" + appId + " AND "
						+ AppPermission.PERMISSION_ID + "=" + permissionId,
				null, null, null, null);
		cursor.moveToFirst();
		
		//convert to AppPermission object
		AppPermission newAppPermission = cursorToModel(cursor);
		cursor.close();
		
		return newAppPermission;
	}
}
