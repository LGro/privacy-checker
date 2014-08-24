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
	 * method to get information about weather a permission is set as expected
	 * or not
	 * 
	 * @param permission
	 * @return true/false
	 */
	public boolean expectedPermission(Permission permission) {
		Boolean expected = permissionsRating.get(permission);
		return (expected == null) ? false : expected;
	}

	public void setPermissionExpected(Permission permission, boolean expected) {
		permissionsRating.put(permission, expected);
	}

	public PermissionsExpected(AppExtended app, boolean mandatory) {
		super(app, mandatory);

		// initialize permission storage
		permissionsRating = new HashMap<Permission, Boolean>();
	}

	@Override
	public boolean validate() throws RatingValidationException {
		if (!mandatory)
			return true;

		// implement behavior if this rating element is mandatory

		return false;
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

		// save each permission thats "expected" value has been modified at
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
			int rating = (pair.getValue()) ? 0 : 1;

			// create permission rating
			ratingPermissionData.createRatingPermission(rating,
					appPermission.getId(), isExpert);
		}

		// close datasources
		appPermissionData.close();
		appPermissionData.close();
	}
}
