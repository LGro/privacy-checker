package de.otaris.zertapps.privacychecker;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteOpenHelper;
import de.otaris.zertapps.privacychecker.appsList.AppsListOrder;
import de.otaris.zertapps.privacychecker.database.dataSource.AppExtendedDataSource;
import de.otaris.zertapps.privacychecker.database.dataSource.PermissionDataSource;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;
import de.otaris.zertapps.privacychecker.database.model.Permission;

public class Algorithm {

	public static final int PERMISSION_MIN_CRITICALITY = 50;

	public Algorithm() {

	}

	/**
	 * Calculate automatic privacy rating for a given application.
	 * 
	 * This method has to ensure that all new permissions are inserted into the
	 * database! Because InsertIncoveredInstalledApps relies on this.
	 * 
	 * @param context
	 * @param pm
	 *            package manager
	 * @param appInfo
	 *            application thats privacy rating should be calculated
	 * 
	 * @return value between 0 and 5 that represents the privacy friendliness of
	 *         the given app
	 */
	public float getAutomaticPrivacyRating(Context context, PackageManager pm,
			String[] permissions) {

		// return 5 (max privacy rating) if no permissions are required
		if (permissions.length == 0)
			return 5.0f;

		PermissionDataSource permissionData = new PermissionDataSource(context);

		float automaticPrivacyRating = 0;
		int numberOfNonCriticalPermissions = 0;
		int numberOfCriticalPermissions = 0;

		permissionData.open();
		for (String permission : permissions) {
			// get permission with criticality from database
			Permission p = permissionData.getPermissionByName(permission);

			// if requested permission doesn't exist -> create it
			if (p == null) {
				p = permissionData.createPermission(permission, permission,
						permission, PERMISSION_MIN_CRITICALITY);
			}

			// accumulate current permission's criticality
			if (p.getCriticality() >= PERMISSION_MIN_CRITICALITY) {
				numberOfNonCriticalPermissions++;
			} else {
				numberOfCriticalPermissions++;
				automaticPrivacyRating += p.getCriticality();
			}
		}
		permissionData.close();

		if (numberOfCriticalPermissions == 0)
			if (numberOfNonCriticalPermissions == 0) {
				return 5;
			} else
				return 5 - (float) (1 / (Math
						.exp(0.15 * numberOfNonCriticalPermissions)));

		// normalize accumulated criticality to privacy rating within [0:5]
		automaticPrivacyRating /= numberOfCriticalPermissions;
		automaticPrivacyRating /= PERMISSION_MIN_CRITICALITY;
		automaticPrivacyRating *= 4;
		automaticPrivacyRating += (float) (1 / (Math
				.exp(0.15 * numberOfNonCriticalPermissions)));

		return automaticPrivacyRating;
	}

	// public void recalculateAutomaticRatingForAllApps(Context context) {
	//
	// AppExtendedDataSource appData = new AppExtendedDataSource(context);
	// appData.open();
	//
	// List<AppExtended> apps = appData.getAllApps(AppsListOrder.ALPHABET,
	// true);
	//
	// // float automaticPrivacyRating = 0;
	// // int numberOfNonCriticalPermissions = 0;
	// // int numberOfCriticalPermissions = 0;
	// //
	// // for (AppExtended app : apps) {
	// //
	// // for (Permission permission : app.getPermissionList()) {
	// // if (permission.getCriticality() >= PERMISSION_MIN_CRITICALITY) {
	// // numberOfNonCriticalPermissions++;
	// // } else
	// // numberOfCriticalPermissions++;
	// // automaticPrivacyRating += permission.getCriticality();
	// // }
	// //
	// // // normalize accumulated criticality to privacy rating within [0:5]
	// // automaticPrivacyRating /= numberOfCriticalPermissions;
	// // automaticPrivacyRating /= PERMISSION_MIN_CRITICALITY;
	// // automaticPrivacyRating *= 4;
	// // automaticPrivacyRating += (float) (1 / (Math
	// // .exp(0.15 * numberOfNonCriticalPermissions)));
	// // if (numberOfCriticalPermissions == 0)
	// // if (numberOfNonCriticalPermissions == 0) {
	// // automaticPrivacyRating = 5;
	// // } else
	// // automaticPrivacyRating = 5 - (float) (1 / (Math
	// // .exp(0.15 * numberOfNonCriticalPermissions)));
	//
	// automaticPrivacyRating = 1;
	// appData.updateAppById(app.getId(), app.getCategoryId(),
	// app.getName(), app.getLabel(), app.getVersion(),
	// app.getPrivacyRating(), app.isInstalled(),
	// app.getFunctionalRating(), app.getDescription(),
	// app.getIcon(), automaticPrivacyRating);
	// }
	//
	// appData.close();
	//
	// }

}
