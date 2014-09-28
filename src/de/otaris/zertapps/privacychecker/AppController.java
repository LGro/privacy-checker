package de.otaris.zertapps.privacychecker;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import de.otaris.zertapps.privacychecker.automaticRatingAlgorithm.AutomaticRatingAlgorithm;
import de.otaris.zertapps.privacychecker.automaticRatingAlgorithm.AutomaticRatingAlgorithmFactory;
import de.otaris.zertapps.privacychecker.database.dataSource.AppCompactDataSource;
import de.otaris.zertapps.privacychecker.database.dataSource.AppExtendedDataSource;
import de.otaris.zertapps.privacychecker.database.dataSource.AppPermissionDataSource;
import de.otaris.zertapps.privacychecker.database.dataSource.PermissionDataSource;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;
import de.otaris.zertapps.privacychecker.database.model.Permission;
import de.otaris.zertapps.privacychecker.totalPrivacyRatingAlgorithm.TotalPrivacyRatingAlgorithm;
import de.otaris.zertapps.privacychecker.totalPrivacyRatingAlgorithm.TotalPrivacyRatingAlgorithmFactory;

/**
 * A class to handle Database communication
 */
public class AppController {

	// the criticalities (starting from 1 as the most critical) up to this value
	// are considered critical
	public static final int CRITICALITY_LIMIT = 50;

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
	public void insertUncoveredPermissions(Context context, String[] permissions) {
		PermissionDataSource permissionData = new PermissionDataSource(context);

		permissionData.open();
		for (String permission : permissions) {
			// get permission with criticality from database
			Permission p = permissionData.getPermissionByName(permission);

			// if requested permission doesn't exist -> create it
			if (p == null) {
				p = permissionData.createPermission(permission, "", permission,
						CRITICALITY_LIMIT, 0, 0);
			}
		}
		permissionData.close();
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

					insertUncoveredPermissions(context, permissions);

					// create app with default categoryId 1, -1 to mark
					// functional rating as not available and an empty
					// description as well as an empty developer and -1 as
					// default functinal rating counter
					app = appData.createApp(1, apps[i].packageName, apps[i]
							.loadLabel(pm).toString(), pInfo.versionCode + "",
							0, true, -1, "",
							IconController.drawableToByteArray(pm
									.getApplicationIcon(apps[i].packageName)),
							0, -1, "");

					// link all required permissions to the newly created app
					for (String permission : permissions)
						appPermissionData.createAppPermission(app.getId(),
								permissionData.getPermissionByName(permission)
										.getId());

					AppExtendedDataSource appExtendedData = new AppExtendedDataSource(
							context);
					AppExtended appExtended = appExtendedData
							.extendAppCompact(app);

					// calculate and set automatic rating
					appExtended = setAutomaticRating(appExtended);

					// calculate and set weighted automatic rating
					appExtended = setCategoryWeightedAutoRating(appExtended);

					// calculate and set total privacy rating
					appExtended = setTotalPrivacyRating(appExtended);

					// update app to set auto and total privacy rating
					appExtendedData.open();
					appExtendedData.update(appExtended);
					appExtendedData.close();

				} catch (NameNotFoundException e) {
					// close datasources
					appData.close();
					appPermissionData.close();
					permissionData.close();

					e.printStackTrace();
					Log.e("AppController",
							"Error: Cannot find package info for "
									+ apps[i].packageName + " - skipping app");
				}
			} else {
				// ... otherwise, if the app is installed: set installed = 1
				app.setInstalled(true);
				app = appData.update(app);
			}
		}

		// close datasources
		appData.close();
		appPermissionData.close();
		permissionData.close();
	}

	/**
	 * Calculates the automatic rating by using a AutomaticRatingAlgorithm.
	 * 
	 * @param appExtended
	 * @return updated app object containing the auto rating
	 */
	private AppExtended setAutomaticRating(AppExtended appExtended) {
		AutomaticRatingAlgorithmFactory autoRatingFactory = new AutomaticRatingAlgorithmFactory();
		AutomaticRatingAlgorithm autoRatingAlgo = autoRatingFactory
				.makeAlgorithm();
		float autoRating = autoRatingAlgo.calculate(appExtended);
		appExtended.setAutomaticRating(autoRating);

		return appExtended;
	}

	/**
	 * Calculates the weighted automatic rating by using a
	 * AutomaticRatingAlgorithm.
	 * 
	 * @param appExtended
	 * @return updated app object containing the weighted auto rating
	 */
	private AppExtended setCategoryWeightedAutoRating(AppExtended appExtended) {
		AutomaticRatingAlgorithmFactory autoRatingFactory = new AutomaticRatingAlgorithmFactory();
		AutomaticRatingAlgorithm weightedAutoRatingAlgo = autoRatingFactory
				.makeWeightedAlgorithm();
		float weightedAutoRating = weightedAutoRatingAlgo
				.calculate(appExtended);
		appExtended.setCategoryWeightedAutoRating(weightedAutoRating);

		return appExtended;
	}

	/**
	 * Calculates the total privacy rating by using a
	 * TotalPrivacyRatingAlgorithm.
	 * 
	 * @param appExtended
	 * @return updated app object containing the total privacy rating
	 */
	private AppExtended setTotalPrivacyRating(AppExtended appExtended) {
		TotalPrivacyRatingAlgorithmFactory totalRatingFactory = new TotalPrivacyRatingAlgorithmFactory();
		TotalPrivacyRatingAlgorithm totalPrivacyRatingAlgo = totalRatingFactory
				.makeAlgorithm();
		float totalPrivacyRating = totalPrivacyRatingAlgo
				.calculate(appExtended);
		appExtended.setPrivacyRating(totalPrivacyRating);

		return appExtended;
	}

}
