package de.otaris.zertapps.privacychecker.appDetails.rateApp.permissionsRating;

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

public class PermissionsRating extends RatingElement {

	HashMap<Permission, Boolean> permissionsRating;

	public boolean expectedPermission(Permission permission) {
		Boolean expected = permissionsRating.get(permission);
		return (expected == null) ? false : expected;
	}

	public void setPermissionExpected(Permission permission, boolean expected) {
		permissionsRating.put(permission, expected);
	}

	public PermissionsRating(AppExtended app, boolean mandatory) {
		super(app, mandatory);

		permissionsRating = new HashMap<Permission, Boolean>();
	}

	@Override
	public boolean validate() throws RatingValidationException {
		if (!mandatory)
			return true;

		// TODO: add behavior in case this is mandatory

		return false;
	}

	@Override
	public void save(Context context) {

		RatingPermissionDataSource ratingPermissionData = new RatingPermissionDataSource(
				context);
		AppPermissionDataSource appPermissionData = new AppPermissionDataSource(
				context);
		ratingPermissionData.open();
		appPermissionData.open();

		Registry reg = Registry.getInstance();
		String isExpertString = reg
				.get("de.otaris.zertapps.privacychecker.appDetails.rateApp.expertMode",
						"isExpert");

		Iterator<Entry<Permission, Boolean>> it = permissionsRating.entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<Permission, Boolean> pair = it.next();

			AppPermission appPermission = appPermissionData
					.getAppPermissionByAppAndPermissionId(app.getId(), pair
							.getKey().getId());
			int rating = (pair.getValue()) ? 0 : 1;
			ratingPermissionData.createRatingPermission(rating,
					appPermission.getId(), isExpertString.equals("1"));
		}

		appPermissionData.close();
		appPermissionData.close();

	}
}
