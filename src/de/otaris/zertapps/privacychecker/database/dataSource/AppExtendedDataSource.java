package de.otaris.zertapps.privacychecker.database.dataSource;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import de.otaris.zertapps.privacychecker.database.DatabaseHelper;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;
import de.otaris.zertapps.privacychecker.database.model.Permission;
import de.otaris.zertapps.privacychecker.database.model.RatingApp;

/**
 * Handles requests concerning the App with all related information (ratings,
 * permissions, comments) on the Database
 *
 */
public class AppExtendedDataSource extends DataSource<AppExtended> {

	private AppCompactDataSource appData;
	private AppPermissionDataSource appPermissionData;
	private RatingAppDataSource ratingAppData;

	public AppExtendedDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
		appData = new AppCompactDataSource(context);
		appPermissionData = new AppPermissionDataSource(context);
		ratingAppData = new RatingAppDataSource(context);
	}

	@Override
	protected AppExtended cursorToModel(Cursor cursor) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * creates an AppExtended and sets all their fields
	 * @param appId
	 * @return the new App 
	 */
	public AppExtended getAppById(int appId) {
		appData.open();
		AppCompact app = appData.getAppById(appId);
		appData.close();

		appPermissionData.open();
		ArrayList<Permission> permissions = appPermissionData.getPermissionsByAppId(appId);
		appPermissionData.close();
		
		ratingAppData.open();
		ArrayList<Integer> ratingsExperts = ratingAppData.getExpertValuesById(appId);
		ArrayList<Integer> ratingsNonExperts = ratingAppData.getNonExpertValuesById(appId);
		float technicalRating = ratingAppData.generateAutomaticRatingById(appId);
		ratingAppData.close();

		AppExtended appExtended = new AppExtended(app);
		
		appExtended.setPermissionList(permissions);
		// order is important, setRating() depends on the other ratings
		appExtended.setExpertRating(ratingsExperts);
		appExtended.setNonExpertRating(ratingsNonExperts);
		appExtended.setAutomaticRating(technicalRating);
		appExtended.setRating();
		
		
		return appExtended;
	}

}
