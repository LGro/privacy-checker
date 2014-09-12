package de.otaris.zertapps.privacychecker;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import de.otaris.zertapps.privacychecker.database.dataSource.AppCompactDataSource;
import de.otaris.zertapps.privacychecker.database.dataSource.AppPermissionDataSource;
import de.otaris.zertapps.privacychecker.database.dataSource.CategoryDataSource;
import de.otaris.zertapps.privacychecker.database.dataSource.PermissionDataSource;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;
import de.otaris.zertapps.privacychecker.database.model.Category;
import de.otaris.zertapps.privacychecker.database.model.Permission;

/**
 * A class to handle Database communication
 */
public class AppController {

	public static final int PERMISSION_MIN_CRITICALITY = 50;

	/**
	 * retrieve installed apps from API
	 * 
	 * @param pm
	 *            : Packagemanager
	 * @return ApplicationInfo array of the locally installed apps
	 */
	public ApplicationInfo[] getInstalledApps(PackageManager pm) {
		// initialize list with all installed apps
		List<ApplicationInfo> apps = pm
				.getInstalledApplications(PackageManager.GET_META_DATA);

		// create result list without apps with no name (system apps?)
		List<ApplicationInfo> results = new ArrayList<ApplicationInfo>();

		for (ApplicationInfo app : apps) {
			// filter apps without a package name and labels that are empty or
			// equal to the package name
			if (app.packageName != null && app.packageName != ""
					&& app.packageName != app.loadLabel(pm).toString())
				results.add(app);
		}
		return results.toArray(new ApplicationInfo[0]);
	}

	/**
	 * get permissions for a given aplication info
	 * 
	 * @param pm
	 *            packagemanager used to retrieve the permissions
	 * @param appInfo
	 *            app thats permissions are requested
	 * @return array of strings ofor each permission that's required
	 */
	public String[] getPermissions(PackageManager pm, ApplicationInfo appInfo) {
		// initialize list with all installed apps
		PackageInfo packageInfo;
		String[] requestedPermissions = new String[0];
		try {
			packageInfo = pm.getPackageInfo(appInfo.packageName,
					PackageManager.GET_PERMISSIONS);
			// Get Permissions
			requestedPermissions = packageInfo.requestedPermissions;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return (requestedPermissions == null) ? new String[0]
				: requestedPermissions;
	}

	/**
	 * Scans all installed apps on the device and inserts the ones that aren't
	 * already covered by the privacy-checker database.
	 * 
	 * New apps are inserted into the database with an automatically generated
	 * privacy rating depending on its set of permissions.
	 * 
	 * @param context
	 * @param pm
	 */
	public void insertUncoveredInstalledApps(Context context, PackageManager pm) {
		// prepare datasources
		AppCompactDataSource appData = new AppCompactDataSource(context);
		AppPermissionDataSource appPermissionData = new AppPermissionDataSource(
				context);
		PermissionDataSource permissionData = new PermissionDataSource(context);
		appData.open();
		appPermissionData.open();
		permissionData.open();

		// iterate over all apps that are installed on the device
		ApplicationInfo[] apps = getInstalledApps(pm);
		for (int i = 0; i < apps.length; i++) {
			AppCompact app = appData.getAppByName(apps[i].packageName);

			// if the app isn't installed yet...
			if (app == null) {
				try {
					// get package info for package name
					PackageInfo pInfo = pm.getPackageInfo(apps[i].packageName,
							0);

					// get requested permissions
					String[] permissions = getPermissions(pm, apps[i]);

					// get automatic privacy rating depending on the requested
					// permissions
					float automaticRating = getAutomaticPrivacyRating(context,
							pm, permissions);

					// TODO: get app from playstore
					Category category = getCategory(context, "categoryName");

					// create app
					app = appData.createApp(
							category.getId(),
							apps[i].packageName,
							apps[i].loadLabel(pm).toString(),
							pInfo.versionCode + "",
							automaticRating,
							true,
							0/* TODO: functionalRating get from playstore */,
							"",
							IconController.drawableToByteArray(pm
									.getApplicationIcon(apps[i].packageName)),
							automaticRating,
							getAutomaticPrivacyRatingRelativeToCategory(
									automaticRating,
									category.getPrivacyRatingMean()));

					// link all required permissions to the newly created app
					for (String permission : permissions)
						appPermissionData.createAppPermission(app.getId(),
								permissionData.getPermissionByName(permission)
										.getId());

				} catch (NameNotFoundException e) {
					e.printStackTrace();
					Log.e("AppController",
							"Error: Cannot find package info for "
									+ apps[i].packageName + " - skipping app");
				}
			} else {
				// ... otherwise, if the app is installed: set installed = 1
				appData.updateAppById(app.getId(), app.getCategoryId(),
						app.getName(), app.getLabel(), app.getVersion(),
						app.getPrivacyRating(), true,
						app.getFunctionalRating(), app.getDescription(),
						app.getIcon(), app.getAutomaticRating(),
						app.getAutomaticRatingRelativeToCategory());
			}
		}

		// close datasources
		appData.close();
		appPermissionData.close();
		permissionData.close();
	}

	private float getAutomaticPrivacyRatingRelativeToCategory(
			float automaticRating, float privacyRatingMean) {

		float d = privacyRatingMean - automaticRating;

		if (d > 0) {
			automaticRating -= 0.8 * d;
		} else if (d < 0) {
			automaticRating += 0.4 * Math.abs(d);
		} else {
			automaticRating += 0.5;
		}

		// prevent from becoming negative or greater than 5
		if (automaticRating < 0)
			automaticRating = 0;
		else if (automaticRating > 5)
			automaticRating = 5;

		return automaticRating;
	}

	private Category getCategory(Context context, String categoryName) {
		// TODO: check if category already exists, create if otherwise

		CategoryDataSource categoryData = new CategoryDataSource(context);
		categoryData.open();

		Category category = categoryData.getCategoryById(1);

		categoryData.close();

		return category;

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
	private float getAutomaticPrivacyRating(Context context, PackageManager pm,
			String[] permissions) {

		// return 5 (max privacy rating) if no permissions are required
		if (permissions.length == 0)
			return 5;

		PermissionDataSource permissionData = new PermissionDataSource(context);

		float criticalPermissionsRating = 0;
		int uncriticalPermissionsCount = 0;

		permissionData.open();
		for (String permission : permissions) {
			// get permission with criticality from database
			Permission p = permissionData.getPermissionByName(permission);

			if (p == null || p.getCriticality() > 48) {
				// if requested permission doesn't exist -> create it
				if (p == null)
					p = permissionData.createPermission(permission, permission,
							permission, PERMISSION_MIN_CRITICALITY);

				uncriticalPermissionsCount++;
			} else {
				// accumulate current permission's criticality
				criticalPermissionsRating += p.getCriticality();
			}
		}
		permissionData.close();

		// normalize accumulated criticality to privacy rating within [0:4]
		criticalPermissionsRating /= permissions.length
				- uncriticalPermissionsCount;
		criticalPermissionsRating /= 48;
		criticalPermissionsRating *= 4;

		// add up to 1 for less uncritical permissions
		criticalPermissionsRating += 2 - (1 / (Math
				.exp(0.15 * uncriticalPermissionsCount)));

		return criticalPermissionsRating;
	}
}
