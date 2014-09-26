package de.otaris.zertapps.privacychecker.appsList;

import de.otaris.zertapps.privacychecker.database.model.AppCompact;

/**
 * provides criteria for sorting any list of apps and a sorting order
 */
public enum AppsListOrder {

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
	public AppsListOrder ascending() {
		orderAscending = true;
		return this;
	}

	/**
	 * marks AppsListOrder as ordered descending
	 * 
	 * @return this
	 */
	public AppsListOrder descending() {
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
}
