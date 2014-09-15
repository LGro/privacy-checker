package de.otaris.zertapps.privacychecker.appDetails.rateApp.permissionsExpected;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.content.Context;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElement;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingValidationException;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.Registry;
import de.otaris.zertapps.privacychecker.database.dataSource.AppPermissionDataSource;
import de.otaris.zertapps.privacychecker.database.dataSource.RatingPermissionDataSource;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;
import de.otaris.zertapps.privacychecker.database.model.AppPermission;
import de.otaris.zertapps.privacychecker.database.model.Permission;

/**
 * enables the user to set weather he/she expected a specific permission app
 * combination or not
 */
public class PermissionsExpected extends RatingElement {

	// stores permissions and an expected flag
	HashMap<Permission, Boolean> permissionsRating;

	/**
	 * retrieves true, if a permission was expected, false if it was unexpected
	 * and null if there was no RadioButton chosen
	 * 
	 * @param permission
	 *            the permission
	 * @return true, false or null
	 */
	public Boolean expectedPermission(Permission permission) {
		return permissionsRating.get(permission);
	}

	public void setPermissionExpected(Permission permission, Boolean expected) {
		permissionsRating.put(permission, expected);
	}

	public void removePermission(Permission permission) {
		permissionsRating.remove(permission);
	}

	public PermissionsExpected(AppExtended app, boolean mandatory) {
		super(app, mandatory);

		// initialize permission storage
		permissionsRating = new HashMap<Permission, Boolean>();
	}

	@Override
	public void validate() throws RatingValidationException {

	}

	@Override
	public void save(Context context) {
		// initialize datasources
		RatingPermissionDataSource ratingPermissionData = new RatingPermissionDataSource(
				context);
		AppPermissionDataSource appPermissionData = new AppPermissionDataSource(
				context);
		ratingPermissionData.open();
		appPermissionData.open();

		// get expert flat from registry
		Registry reg = Registry.getInstance();
		String isExpertString = reg
				.get("de.otaris.zertapps.privacychecker.appDetails.rateApp.expertMode",
						"isExpert");

		boolean isExpert = false;
		if (isExpertString != null)
			isExpert = isExpertString.equals("1");

		// save each permission thats "unexpected" value has been modified at
		// least once; permissions that have not been touched, aren't rated
		Iterator<Entry<Permission, Boolean>> it = permissionsRating.entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<Permission, Boolean> pair = it.next();

			// get AppPermissionID
			AppPermission appPermission = appPermissionData
					.getAppPermissionByAppAndPermissionId(app.getId(), pair
							.getKey().getId());

			// set rating 0 for expected and 1 for unexpected
			Boolean rating = (pair.getValue()) ? false : true;

			// create permission rating
			ratingPermissionData.createRatingPermission(rating,
					appPermission.getId(), isExpert);
		}

		// close datasources
		appPermissionData.close();
		ratingPermissionData.close();
	}
}
