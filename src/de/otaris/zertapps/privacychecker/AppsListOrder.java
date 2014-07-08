package de.otaris.zertapps.privacychecker;

import de.otaris.zertapps.privacychecker.database.App;

/**
 * provides criteria for sorting any list of apps
 */
public enum AppsListOrder {

	PRIVACY_RATING,
	ALPHABET,
	FUNCTIONAL_RATING;

	@Override
	public String toString() {
		switch (this) {
		case PRIVACY_RATING:
			return App.PRIVACY_RATING;

		case ALPHABET:
			// order case insensitive
			return App.LABEL + " COLLATE NOCASE";

		case FUNCTIONAL_RATING:
			return App.FUNCTIONAL_RATING;
		}
		return "";

	}
}
