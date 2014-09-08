package de.otaris.zertapps.privacychecker.database.dataSource;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import de.otaris.zertapps.privacychecker.appsList.AppsListOrder;
import de.otaris.zertapps.privacychecker.database.DatabaseHelper;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;
import de.otaris.zertapps.privacychecker.database.model.Category;
import de.otaris.zertapps.privacychecker.database.model.Permission;

/**
 * Handles requests concerning the App with all related information (ratings,
 * permissions, comments) on the Database
 *
 */
public class AppExtendedDataSource extends DataSource<AppExtended> implements
		AppDataSource<AppExtended> {

	private AppCompactDataSource appData;
	private AppPermissionDataSource appPermissionData;
	private RatingAppDataSource ratingAppData;
	private CategoryDataSource categoryData;

	public AppExtendedDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
		appData = new AppCompactDataSource(context);
		appPermissionData = new AppPermissionDataSource(context);
		ratingAppData = new RatingAppDataSource(context);
		categoryData = new CategoryDataSource(context);
	}

	@Override
	protected AppExtended cursorToModel(Cursor cursor) {
		return null;
	}

	/**
	 * populates an extended app with all relational data
	 * 
	 * @param app
	 *            extended app to populate
	 * @return populated extended app
	 */
	protected AppExtended populateApp(AppExtended app) {
		appPermissionData.open();
		ArrayList<Permission> permissions = appPermissionData
				.getPermissionsByAppId(app.getId());
		appPermissionData.close();

		ratingAppData.open();
		ArrayList<Integer> ratingsExperts = ratingAppData
				.getExpertValuesById(app.getId());
		ArrayList<Integer> ratingsNonExperts = ratingAppData
				.getNonExpertValuesById(app.getId());
		ratingAppData.close();

		categoryData.open();
		Category category = categoryData.getCategoryById(app.getCategoryId());
		categoryData.close();

		app.setPermissionList(permissions);
		// order is important, setRating() depends on the other ratings
		app.setExpertRating(ratingsExperts);
		app.setNonExpertRating(ratingsNonExperts);
		// TODO: refactor, bad logic here
		app.setRating();
		app.setCategory(category);

		return app;
	}

	/**
	 * transforms an AppCompact to an AppExtended and adds all relevant
	 * relational data
	 * 
	 * @param app
	 *            compact app to be extended
	 * @return extended compact app
	 */
	public AppExtended extendAppCompact(AppCompact app) {
		AppExtended appExtended = new AppExtended(app);

		appExtended = populateApp(appExtended);

		return appExtended;
	}

	/**
	 * extends a list of multiplae AppCompact objects to a list of AppExtended
	 * object using this.extendAppCompact(AppCompact) for each list element
	 * 
	 * @param apps
	 *            list of compact apps
	 * @return list of extended apps
	 */
	protected List<AppExtended> extendAppCompactList(List<AppCompact> apps) {
		List<AppExtended> extendedApps = new ArrayList<AppExtended>(apps.size());

		for (AppCompact app : apps)
			extendedApps.add(extendAppCompact(app));

		return extendedApps;
	}

	/**
	 * creates an AppExtended and sets all their fields
	 * 
	 * @param appId
	 * @return the new App
	 */
	@Override
	public AppExtended getAppById(int appId) {
		appData.open();
		AppCompact app = appData.getAppById(appId);
		appData.close();

		return extendAppCompact(app);
	}

	@Override
	public List<AppExtended> getInstalledApps(AppsListOrder order,
			boolean ascending) {
		appData.open();
		List<AppCompact> apps = appData.getInstalledApps();
		appData.close();

		return extendAppCompactList(apps);
	}

	@Override
	public List<AppExtended> getAppsByCategory(int categoryId,
			AppsListOrder order, boolean ascending) {
		appData.open();
		List<AppCompact> apps = appData.getAppsByCategory(categoryId, order,
				ascending);
		appData.close();

		return extendAppCompactList(apps);
	}

	@Override
	public List<AppExtended> getAllApps(AppsListOrder order, boolean ascending) {
		appData.open();
		List<AppCompact> apps = appData.getAllApps();
		appData.close();

		return extendAppCompactList(apps);
	}

	@Override
	public List<AppExtended> getLastUpdatedApps(int n) {
		appData.open();
		List<AppCompact> apps = appData.getLastUpdatedApps(n);
		appData.close();

		return extendAppCompactList(apps);
	}

	@Override
	public AppExtended updateAppById(int appId, int categoryId, String name,
			String label, String version, float privacyRating,
			boolean installed, float functionalRating, String description,
			byte[] icon, float automaticRating, float categoryWeightedAutoRating) {

		appData.open();
		AppCompact app = appData.updateAppById(appId, categoryId, name, label,
				version, privacyRating, installed, functionalRating,
				description, icon, automaticRating, categoryWeightedAutoRating);
		appData.close();

		return extendAppCompact(app);
	}

}
