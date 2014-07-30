package de.otaris.zertapps.privacychecker;

public class RatingController {

	public Integer getIconRating(float rating) {
		if (rating > 4.5 && rating <= 5) {
			return R.drawable.lock5;
		} else if (rating > 3.5 && rating <= 4.5) {
			return R.drawable.lock4;
		} else if (rating > 2.5 && rating <= 3.5) {
			return R.drawable.lock3;
		} else if (rating > 1.5 && rating <= 2.5) {
			return R.drawable.lock2;
		} else if (rating > 0 && rating <= 1.5) {
			return R.drawable.lock1;
		} else {
			throw new IllegalArgumentException(
					"Rating not is not between 0 and 5.");
		}
	}
	
}

