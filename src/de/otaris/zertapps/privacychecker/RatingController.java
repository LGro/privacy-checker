package de.otaris.zertapps.privacychecker;

public class RatingController {

	/**
	 * Return the locks icon according to the rating
	 */
	public Integer getIconRatingLocks(float rating) {
		if (rating == 5) {
			return R.drawable.locks_50;
		} else if (rating >= 4.5 && rating < 5.0) {
			return R.drawable.locks_45;
		} else if (rating >= 4.0 && rating < 4.5) {
			return R.drawable.locks_40;
		} else if (rating >= 3.5 && rating < 4.0) {
			return R.drawable.locks_35;
		} else if (rating >= 3.0 && rating < 3.5) {
			return R.drawable.locks_30;
		} else if (rating >= 2.5 && rating < 3.0) {
			return R.drawable.locks_25;
		} else if (rating >= 2.0 && rating < 2.5) {
			return R.drawable.locks_20;
		} else if (rating >= 1.5 && rating < 2.0) {
			return R.drawable.locks_15;
		} else if (rating >= 1.0 && rating < 1.5) {
			return R.drawable.locks_10;
		} else if (rating >= 0 && rating < 1.0) {
			return R.drawable.locks_05;
		} else {
			throw new IllegalArgumentException(
					"Privacy rating not is not between 0 and 5." + rating);
		}
	}

	/**
	 * Return the stars icon according to the rating
	 */
	public Integer getIconRatingStars(float rating) {
		if (rating == 5) {
			return R.drawable.stars_50;
		} else if (rating >= 4.5 && rating < 5.0) {
			return R.drawable.stars_45;
		} else if (rating >= 4.0 && rating < 4.5) {
			return R.drawable.stars_40;
		} else if (rating >= 3.5 && rating < 4.0) {
			return R.drawable.stars_35;
		} else if (rating >= 3.0 && rating < 3.5) {
			return R.drawable.stars_30;
		} else if (rating >= 2.5 && rating < 3.0) {
			return R.drawable.stars_25;
		} else if (rating >= 2.0 && rating < 2.5) {
			return R.drawable.stars_20;
		} else if (rating >= 1.5 && rating < 2.0) {
			return R.drawable.stars_15;
		} else if (rating >= 1.0 && rating < 1.5) {
			return R.drawable.stars_10;
		} else if (rating >= 0 && rating < 1.0) {
			return R.drawable.stars_05;
		} else {
			throw new IllegalArgumentException(
					"Play-Store rating not is not between 0 and 5." + rating);
		}
	}
}
