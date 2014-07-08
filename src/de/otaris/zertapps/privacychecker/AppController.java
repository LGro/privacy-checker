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
import de.otaris.zertapps.privacychecker.database.Category;
import de.otaris.zertapps.privacychecker.database.CategoryDataSource;

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
	public void putInstalledAppsInDatabase(AppDataSource appData,
			CategoryDataSource categoryData, PackageManager pm) {

		appData.open();
		categoryData.open();

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
				
				appData.createApp(categoryId, app.packageName,
						pinfo.applicationInfo.loadLabel(pm).toString(),
						pinfo.versionCode + "", pRating, true, fRating);
			} catch (NameNotFoundException e) {
				Log.e("AppController",
						"NameNotFoundException: " + e.getMessage());
			}
		}
		
		appData.close();
		categoryData.close();

	}

}
