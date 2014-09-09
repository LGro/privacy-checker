package de.otaris.zertapps.privacychecker.database.dataSource;

import android.database.Cursor;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;
import de.otaris.zertapps.privacychecker.database.model.PermissionExtended;

public class PermissionExtendedDataSource extends DataSource<AppExtended>{

	@Override
	protected AppExtended cursorToModel(Cursor cursor) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * populates an extended app with all relational data
	 * 
	 * @param app
	 *            extended app to populate
	 * @return populated extended app
	 */
	protected PermissionExtended populatePermission(PermissionExtended permission) {
		
		return permission;
	}
}
