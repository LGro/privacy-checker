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
 * Keeps the relations if there is one or two factors empty (no ratings exist).
 */
public class CheckerAlgo implements TotalPrivacyRatingAlgorithm {

	@Override
	public float calculate(AppExtended app) {

		float totalRating = 0;

		float expert = app.getTotalExpertRating();
		float novices = app.getTotalNonExpertRating();
		float auto = app.getCategoryWeightedAutoRating();

		if (app.getExpertRating().size() > 0
				&& app.getNonExpertRating().size() > 0) {
			// expert and non expert ratings exist
			totalRating = auto * 0.3f + expert * 0.5f + novices * 0.2f;
		} else if (app.getExpertRating().size() > 0) {
			// only expert ratings exist
			totalRating = auto * 0.375f + expert * 0.625f;
		} else if (app.getNonExpertRating().size() > 0) {
			// only non expert ratings exist
			totalRating = auto * 0.6f + novices * 0.4f;
		} else {
			// neither expert nor non expert ratings exist
			totalRating = auto;
		}

		return totalRating;
	}
}
