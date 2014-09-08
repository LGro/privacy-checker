package de.otaris.zertapps.privacychecker.totalPrivacyRatingAlgorithm;

import de.otaris.zertapps.privacychecker.database.model.AppExtended;

/**
 * calculates total privacy rating by weighting the following aspects:
 * <ul>
 * <li>30% Automatic Rating</li>
 * <li>50% Expert Rating</li>
 * <li>20% Non-Expert Rating</li>
 * </ul>
 * 
 * TODO: only include (non-)expert rating if more than 0 (non-)experts rated
 */
public class CheckerAlgo implements TotalPrivacyRatingAlgorithm {

	@Override
	public float calculate(AppExtended app) {

		float auto = app.getAutomaticRating();
		float expert = app.getTotalExpertRating();
		float novices = app.getTotalNonExpertRating();

		return auto * 0.3f + expert * 0.5f + novices * 0.2f;
	}

}
