package de.otaris.zertapps.privacychecker;

import java.util.ArrayList;
import java.util.List;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import de.otaris.zertapps.privacychecker.database.App;
import de.otaris.zertapps.privacychecker.database.AppDataSource;

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
	 * @param helper
	 *            : AppDataSource
	 * @param pm
	 *            : Packagemanager
	 * @throws NameNotFoundException
	 *             , is thrown if app.packageName does not exist (there are
	 *             installed apps without package name)
	 */
	public void putInstalledAppsInDatabase(AppDataSource helper,
			PackageManager pm) {

		helper.open();

		for (ApplicationInfo app : getInstalledApps(pm)) {
			// PackageInfo is for getting the versionCode and versionName
			PackageInfo pinfo;
			try {
				pinfo = pm.getPackageInfo(app.packageName, 0);

				// TODO: ADD Ratings here

				// statically assigned ratings for demo purposes
				float pRating = (app.packageName.charAt(0) == 'c') ? 2 : 4;
				float fRating = (app.packageName.charAt(0) == 'c') ? 3 : 5;
				//TODO: if number of categories changes modify 5
				helper.createApp((int) Math.random()*5, app.packageName, pinfo.applicationInfo
						.loadLabel(pm).toString(), pinfo.versionCode + "",
						pRating, true, fRating);
			} catch (NameNotFoundException e) {
				Log.e("AppController",
						"NameNotFoundException: " + e.getMessage());
			}
		}

	}

}
