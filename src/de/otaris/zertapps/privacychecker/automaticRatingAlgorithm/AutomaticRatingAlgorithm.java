package de.otaris.zertapps.privacychecker.automaticRatingAlgorithm;

import de.otaris.zertapps.privacychecker.database.model.AppExtended;

/**
 * provides an automatic rating based on given properties of an app (e.g.
 * required permissions)
 */
public interface AutomaticRatingAlgorithm {

	/**
	 * Calculate automatic rating for given app.
	 * 
	 * @param app
	 * @return
	 */
	public float calculate(AppExtended app);

}
