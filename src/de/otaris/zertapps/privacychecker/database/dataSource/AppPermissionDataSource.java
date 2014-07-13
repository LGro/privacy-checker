package de.otaris.zertapps.privacychecker.database.dataSource;

import java.util.ArrayList;

import android.database.Cursor;
import de.otaris.zertapps.privacychecker.database.model.AppPermission;

/**
 * 
 * Handles requests concerning the App Permission relation on the Database
 *
 */
public class AppPermissionDataSource extends DataSource<AppPermission> {

	public ArrayList<String> getPermissionsByAppId(int appId) {
		ArrayList<String> permissionList = new ArrayList<String>();

		return permissionList;
	}

	@Override
	protected AppPermission cursorToModel(Cursor cursor) {
		// TODO Auto-generated method stub
		return null;
	}

}
