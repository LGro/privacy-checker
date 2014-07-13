package de.otaris.zertapps.privacychecker.appsList;

import de.otaris.zertapps.privacychecker.database.model.AppCompact;

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
