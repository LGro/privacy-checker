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
import de.otaris.zertapps.privacychecker.database.dataSource.AppDataSource;
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
			if (apps.get(i).className != null && apps.get(i).className != "")
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
										.getApplicationIcon(app.packageName)));
				String[] permissions = getPermissions(pm, app);
				for (String permission : permissions) {
					String[] permissionArray = permission.split("\\.");
					String permissionWithoutClassName = (permissionArray.length < 1) ? ""
							: permissionArray[permissionArray.length - 1];

					// get existing permission
					Permission existingPermission = permissionData
							.getPermissionByName(permissionWithoutClassName);

					// if permission does not exist yet create it
					if (existingPermission == null) {
						existingPermission = permissionData
								.createPermissionByName(permissionWithoutClassName);
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

		return requestedPermissions;
	}

	/**
	 * scans database and sets installed = 1 for all installed apps
	 * 
	 * @param appData
	 *            a data source that already has been opened (!)
	 */
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
						app.getIcon());
			} catch (NameNotFoundException e) {
				// do nothing
			}
		}
	}
}
