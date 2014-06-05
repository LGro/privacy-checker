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

public class AppController {
	/**
	 * retrieve installed apps from API
	 * 
	 * @param pm
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
	 * @param pm
	 */
	public void putInstalledAppsInDatabase(AppDataSource helper,
			PackageManager pm) {

		helper.open();

		for (ApplicationInfo app : getInstalledApps(pm)) {
			// PackageInof is for getting the versionCode and versionName
			PackageInfo pinfo;
			try {
				pinfo = pm.getPackageInfo(app.packageName, 0);

				// TODO: ADD Rating here
				// TODO: ADD category
				helper.createApp(app.packageName, pinfo.applicationInfo
						.loadLabel(pm).toString(), pinfo.versionCode + "",
						true, 3);
			} catch (NameNotFoundException e) {
				Log.e("AppController",
						"NameNotFoundException: " + e.getMessage());
			}
		}

	}

}
