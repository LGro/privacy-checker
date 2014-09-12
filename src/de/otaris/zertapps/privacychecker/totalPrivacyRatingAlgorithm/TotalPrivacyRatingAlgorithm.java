package de.otaris.zertapps.privacychecker.totalPrivacyRatingAlgorithm;

import de.otaris.zertapps.privacychecker.database.model.AppExtended;

public interface TotalPrivacyRatingAlgorithm {

	/**
	 * Calculates total privacy rating for a given app.
	 * 
	 * @param app
	 * @return total privacy rating
	 */
	public float calculate(AppExtended app);

}
