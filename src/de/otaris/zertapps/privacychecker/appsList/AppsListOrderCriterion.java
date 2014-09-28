package de.otaris.zertapps.privacychecker.appsList;

import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;

/**
 * provides criteria for sorting any list of apps and a sorting order
 */
public enum AppsListOrderCriterion {

	PRIVACY_RATING, ALPHABET, FUNCTIONAL_RATING;

	private boolean orderAscending = true;

	public boolean isOrderedAscending() {
		return orderAscending;
	}

	/**
	 * marks AppsListOrder as ordered ascending
	 * 
	 * @return this
	 */
	public AppsListOrderCriterion ascending() {
		orderAscending = true;
		return this;
	}

	/**
	 * marks AppsListOrder as ordered descending
	 * 
	 * @return this
	 */
	public AppsListOrderCriterion descending() {
		orderAscending = false;
		return this;
	}

	/**
	 * inverts sorting direction
	 */
	public void invert() {
		orderAscending = !orderAscending;
	}

	@Override
	public String toString() {
		switch (this) {
		case PRIVACY_RATING:
			return AppCompact.PRIVACY_RATING;

		case ALPHABET:
			// order case insensitive
			return AppCompact.LABEL + " COLLATE NOCASE";

		case FUNCTIONAL_RATING:
			return AppCompact.FUNCTIONAL_RATING;
		}
		return "";

	}

	public int getDefaultIcon() {
		switch (this) {
		case PRIVACY_RATING:
			return R.drawable.privacyrating_default;

		case ALPHABET:
			return R.drawable.name_default;

		case FUNCTIONAL_RATING:
			return R.drawable.popularityrating_default;

		default:
			return -1;

		}
	}

	public int getAscendingtIcon() {
		switch (this) {
		case PRIVACY_RATING:
			return R.drawable.privacyrating_ascending;

		case ALPHABET:
			return R.drawable.name_ascending;

		case FUNCTIONAL_RATING:
			return R.drawable.popularityrating_ascending;

		default:
			return -1;

		}
	}

	public int getDescendingIcon() {
		switch (this) {
		case PRIVACY_RATING:
			return R.drawable.privacyrating_descending;

		case ALPHABET:
			return R.drawable.name_descending;

		case FUNCTIONAL_RATING:
			return R.drawable.popularityrating_descending;

		default:
			return -1;

		}
	}

	public int getCurrentIcon() {
		return (orderAscending) ? getAscendingtIcon() : getDescendingIcon();
	}
}
