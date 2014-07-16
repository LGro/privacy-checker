package de.otaris.zertapps.privacychecker.appDetails.permissions;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import de.otaris.zertapps.privacychecker.appDetails.Detail;
import de.otaris.zertapps.privacychecker.appDetails.DetailViewHelper;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;
import de.otaris.zertapps.privacychecker.database.model.Permission;

public class PermissionsViewHelper extends DetailViewHelper {

	@Override
	public View getView(Context context, ViewGroup parent, Detail detail)
			throws IllegalArgumentException {
		if (!(detail instanceof Permissions))
			throw new IllegalArgumentException(
					"Illegal Detail Object. Expected Description.");

		Permissions permissions = (Permissions) detail;
		AppExtended app = permissions.getApp();
		
		List<Permission> permissionList = app.getPermissionList();
		
		// TODO: Display
		
		return null;
	}

}
