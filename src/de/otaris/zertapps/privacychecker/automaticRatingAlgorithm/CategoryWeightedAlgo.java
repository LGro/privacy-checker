package de.otaris.zertapps.privacychecker.automaticRatingAlgorithm;

import de.otaris.zertapps.privacychecker.database.model.AppExtended;

/**
 * weight automatic rating according to the category's average auto rating
 */
public class CategoryWeightedAlgo implements AutomaticRatingAlgorithm {

	@Override
	public float calculate(AppExtended app) {
		if (app.getCategory() == null)
			return app.getAutomaticRating();

		float avgCategoryRating = app.getCategory().getAverageAutoRating();
		float autoRating = app.getAutomaticRating();
		float delta = Math.abs(avgCategoryRating - autoRating);
		float weightedAutoRating = 0;

		if (autoRating > avgCategoryRating) {
			weightedAutoRating = (float) (autoRating + delta * 0.4);
			if (weightedAutoRating > 5)
				weightedAutoRating = 5;
		} else if (autoRating < avgCategoryRating) {
			weightedAutoRating = (float) (autoRating - delta * 0.8);
			if (weightedAutoRating < 0)
				weightedAutoRating = 0;
		} else
			return autoRating;

		return weightedAutoRating;
	}

}
