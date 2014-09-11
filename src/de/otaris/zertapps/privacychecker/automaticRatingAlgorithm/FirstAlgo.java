package de.otaris.zertapps.privacychecker.automaticRatingAlgorithm;

import de.otaris.zertapps.privacychecker.AppController;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;
import de.otaris.zertapps.privacychecker.database.model.Permission;

/**
 * calculates an automatic rating based on the given permissions
 * 
 * 4/5 of this rating is based on the criticality of the most critical
 * permissions
 * 
 * 1/5 of this rating is based on the amount of non-critical permissions
 */
public class FirstAlgo implements AutomaticRatingAlgorithm {

	@Override
	public float calculate(AppExtended app) {
		// return 5 (max privacy rating) if no permissions are required
		if (app.getPermissionList().size() == 0)
			return 5.0f;

		float automaticPrivacyRating = 0;
		int numberOfNonCriticalPermissions = 0;
		int numberOfCriticalPermissions = 0;

		for (Permission p : app.getPermissionList()) {
			// accumulate current permission's criticality
			if (p.getCriticality() >= AppController.CRITICALITY_LIMIT) {
				numberOfNonCriticalPermissions++;
			} else {
				numberOfCriticalPermissions++;
				automaticPrivacyRating += p.getCriticality();
			}
		}

		if (numberOfCriticalPermissions == 0) {
			if (numberOfNonCriticalPermissions == 0) {
				return 5;
			} else {
				return 5 - (float) (1 / (Math
						.exp(0.15 * numberOfNonCriticalPermissions)));
			}
		}

		// normalize accumulated criticality to privacy rating within [0:5]
		automaticPrivacyRating /= numberOfCriticalPermissions;
		automaticPrivacyRating /= AppController.CRITICALITY_LIMIT;
		automaticPrivacyRating *= 4;
		// add the weighted amount of non critical permissions
		automaticPrivacyRating += (float) (1 / (Math
				.exp(0.15 * numberOfNonCriticalPermissions)));

		return automaticPrivacyRating;
	}

}
