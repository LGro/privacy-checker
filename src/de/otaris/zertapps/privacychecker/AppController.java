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
import de.otaris.zertapps.privacychecker.database.dataSource.RatingAppDataSource;
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
		for (int i = 0; i < apps.size(); i++) {
			if (apps.get(i).packageName != null
					&& apps.get(i).packageName != "")
				results.add(apps.get(i));
		}
		return results.toArray(new ApplicationInfo[0]);
	}

	/**
	 * put the locally installed app in the database
	 * 
	 * @param appData
	 *            : AppDataSource
	 * @param pm
	 *            : Packagemanager
	 * @throws NameNotFoundException
	 *             , is thrown if app.packageName does not exist (there are
	 *             installed apps without package name)
	 */
	@Deprecated
	public void putInstalledAppsInDatabase(Context context, PackageManager pm) {

		AppCompactDataSource appData = new AppCompactDataSource(context);
		CategoryDataSource categoryData = new CategoryDataSource(context);
		AppPermissionDataSource appPermissionData = new AppPermissionDataSource(
				context);
		PermissionDataSource permissionData = new PermissionDataSource(context);
		RatingAppDataSource ratingData = new RatingAppDataSource(context);

		appData.open();
		permissionData.open();
		appPermissionData.open();
		categoryData.open();
		ratingData.open();

		ApplicationInfo[] apps = getInstalledApps(pm);
		for (int i = 0; i < apps.length; i++) {
			ApplicationInfo app = apps[i];

			// PackageInfo is for getting the versionCode and versionName
			PackageInfo pinfo;
			try {
				pinfo = pm.getPackageInfo(app.packageName, 0);

				// TODO: ADD Ratings here

				// statically assigned ratings for demo purposes
				float pRating = (app.packageName.charAt(0) == 'c') ? 2 : 4;
				float fRating = (app.packageName.charAt(0) == 'c') ? 3 : 5;

				// get all categories
				List<Category> categories = categoryData.getAllCategories();

				// set categoryId for apps programmatically
				int categoryId;
				if (i < 5) {
					categoryId = categories.get(0).getId();
				} else if (i < 10) {
					categoryId = categories.get(1).getId();
				} else if (i < 15) {
					categoryId = categories.get(2).getId();
				} else {
					categoryId = categories.get(3).getId();
				}

				AppCompact newApp = appData
						.createApp(
								categoryId,
								app.packageName,
								pinfo.applicationInfo.loadLabel(pm).toString(),
								pinfo.versionCode + "",
								pRating,
								true,
								fRating,
								"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
								IconController.drawableToByteArray(pm
										.getApplicationIcon(app.packageName)),
								pRating);
				String[] permissions = getPermissions(pm, app);
				for (String permission : permissions) {

					// get existing permission
					Permission existingPermission = permissionData
							.getPermissionByName(permission);

					// if permission does not exist yet create it
					if (existingPermission == null) {
						existingPermission = permissionData
								.createPermissionByName(permission);
					}
					appPermissionData.createAppPermission(newApp.getId(),
							existingPermission.getId());
				}
			} catch (NameNotFoundException e) {
				Log.e("AppController",
						"NameNotFoundException: " + e.getMessage());
			}
		}

		appData.close();
		permissionData.close();
		appPermissionData.close();
		categoryData.close();

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
	 * scans database and sets installed = 1 for all installed apps
	 * 
	 * @param appData
	 *            a data source that already has been opened (!)
	 */
	@Deprecated
	public void updateInstalledApps(AppCompactDataSource appData,
			PackageManager pm) {
		List<AppCompact> apps = appData.getAllApps();

		for (AppCompact app : apps) {
			try {
				pm.getApplicationInfo(app.getName(),
						PackageManager.GET_META_DATA);

				appData.updateAppById(app.getId(), app.getCategoryId(),
						app.getName(), app.getLabel(), app.getVersion(),
						app.getPrivacyRating(), true,
						app.getFunctionalRating(), app.getDescription(),
						app.getIcon(), app.getAutomaticRating());
			} catch (NameNotFoundException e) {
				// do nothing
			}
		}
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
					float privacyRating = getAutomaticPrivacyRating(context,
							pm, permissions);

					// create app
					app = appData.createApp(0, apps[i].packageName, apps[i]
							.loadLabel(pm).toString(), pInfo.versionCode + "",
							privacyRating, true, 0/* functionalRating */, "",
							IconController.drawableToByteArray(pm
									.getApplicationIcon(apps[i].packageName)),
							privacyRating);

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
						app.getIcon(), app.getAutomaticRating());
			}
		}

		// close datasources
		appData.close();
		appPermissionData.close();
		permissionData.close();
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

		float automaticPrivacyRating = 0;

		permissionData.open();
		for (String permission : permissions) {
			// get permission with criticality from database
			Permission p = permissionData.getPermissionByName(permission);

			// if requested permission doesn't exist -> create it
			if (p == null)
				p = permissionData.createPermission(permission, permission,
						permission, PERMISSION_MIN_CRITICALITY);

			// accumulate current permission's criticality
			automaticPrivacyRating += p.getCriticality();
		}
		permissionData.close();

		// normalize accumulated criticality to privacy rating within [0:5]
		automaticPrivacyRating /= permissions.length;
		automaticPrivacyRating /= PERMISSION_MIN_CRITICALITY;
		automaticPrivacyRating *= 5;

		return automaticPrivacyRating;
	}
}
