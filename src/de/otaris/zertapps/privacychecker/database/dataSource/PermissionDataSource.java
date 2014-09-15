package de.otaris.zertapps.privacychecker.database.dataSource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import de.otaris.zertapps.privacychecker.database.DatabaseHelper;
import de.otaris.zertapps.privacychecker.database.model.Permission;

/**
 * Handles requests concerning Permissions to the database.
 * 
 */
public class PermissionDataSource extends DataSource<Permission> {

	private String[] allColumns = { Permission.ID, Permission.NAME,
			Permission.LABEL, Permission.DESCRIPTION, Permission.CRITICALITY, Permission.UNTERSTOOD_COUNTER, Permission.NOT_UNDERSTOOD_COUNTER };

	public PermissionDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	/**
	 * convert database result cursor (pointing to a result set) to app object
	 * 
	 * @param cursor
	 * @return cursor data as App object
	 */
	protected Permission cursorToModel(Cursor cursor) {

		if (cursor.getCount() == 0)
			return null;

		Permission permission = new Permission();

		permission.setId(cursor.getInt(0));
		permission.setName(cursor.getString(1));
		permission.setLabel(cursor.getString(2));
		permission.setDescription(cursor.getString(3));
		permission.setCriticality(cursor.getInt(4));
		permission.setUnderstoodCounter(cursor.getInt(5));
		permission.setNotUnderstoodCounter(cursor.getInt(6));

		return permission;
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
	public Permission createPermission(String name, String label,
			String description, int criticality, int counterYes, int CounterNo) {
		// set values for columns
		ContentValues values = new ContentValues();
		values.put(Permission.NAME, name);
		values.put(Permission.LABEL, label);
		values.put(Permission.DESCRIPTION, description);
		values.put(Permission.CRITICALITY, criticality);
		values.put(Permission.UNTERSTOOD_COUNTER, 0);
		values.put(Permission.NOT_UNDERSTOOD_COUNTER, 0);

		// insert into DB
		long insertId = database.insert(Permission.TABLE, null, values);

		// get recently inserted Permission by ID
		return getPermissionById(insertId);
	}

	/**
	 * Method to create a Permission only by it's name
	 * 
	 * @param name
	 *            -> String: Name of the permission
	 * @return a Permission with all attributes set correctly
	 */

	public Permission createPermissionByName(String name) {
		// set values for columns
		ContentValues values = new ContentValues();
		values.put(Permission.NAME, name);
		values.put(Permission.LABEL, name);
		values.put(Permission.DESCRIPTION, name);
		values.put(Permission.CRITICALITY, 50);
		values.put(Permission.UNTERSTOOD_COUNTER, 0);
		values.put(Permission.NOT_UNDERSTOOD_COUNTER, 0);

		// insert into DB
		long insertId = database.insert(Permission.TABLE, null, values);

		// get recently inserted Permission by ID
		return getPermissionById(insertId);
	}

	/**
	 * gets single app by id from database
	 * 
	 * @param appId
	 *            id to identify a single app
	 * @return app object for given id
	 */
	public Permission getPermissionById(long permissionId) {
		// build database query
		Cursor cursor = database.query(Permission.TABLE, allColumns,
				Permission.ID + " = " + permissionId, null, null, null, null);
		cursor.moveToFirst();

		// convert to Permission object
		Permission newPermission = cursorToModel(cursor);
		cursor.close();

		// return Permission object
		return newPermission;
	}

	public Permission getPermissionByName(String permissionName) {
		Cursor cursor = database.query(Permission.TABLE, allColumns,
				Permission.NAME + " = '" + permissionName + "'", null, null,
				null, null);
		cursor.moveToFirst();

		Permission permission = cursorToModel(cursor);
		cursor.close();

		return permission;
	}
	
	/**
	 * increases the counter, which tells how many understood the explanation
	 * @param permission
	 */
	public void increaseCounterYes(Permission permission){
		int c = permission.getUnderstoodCounter();
		permission.setUnderstoodCounter((c + 1));
	}
	
	/**
	 * increases the counter, which tells how many did not understand the explanation
	 * @param permission
	 */
	public void increaseCounterNo(Permission permission){
		int c = permission.getNotUnderstoodCounter();
		permission.setNotUnderstoodCounter((c + 1));
	}
}
