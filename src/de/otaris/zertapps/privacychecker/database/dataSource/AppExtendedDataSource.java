package de.otaris.zertapps.privacychecker.database.dataSource;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import de.otaris.zertapps.privacychecker.database.DatabaseHelper;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;
import de.otaris.zertapps.privacychecker.database.model.Permission;

/**
 * Handles requests concerning the App with all related information (ratings,
 * permissions, comments) on the Database
 *
 */
public class AppExtendedDataSource extends DataSource<AppExtended> {

	private AppCompactDataSource appData;
	private AppPermissionDataSource appPermissionData;

	public AppExtendedDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
		appData = new AppCompactDataSource(context);
		appPermissionData = new AppPermissionDataSource(context);
	}

	@Override
	protected AppExtended cursorToModel(Cursor cursor) {
		// TODO Auto-generated method stub
		return null;
	}

	public AppExtended getAppById(int appId) {
		appData.open();
		AppCompact app = appData.getAppById(appId);
		appData.close();

		appPermissionData.open();
		ArrayList<Permission> permissions = appPermissionData.getPermissionsByAppId(appId);
		appPermissionData.close();

		AppExtended appExtended = new AppExtended(app);
		
		appExtended.setPermissionList(permissions);
		
		return appExtended;
	}

}
