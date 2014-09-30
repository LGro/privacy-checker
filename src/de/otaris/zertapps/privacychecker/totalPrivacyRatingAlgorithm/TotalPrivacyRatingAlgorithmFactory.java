package de.otaris.zertapps.privacychecker.totalPrivacyRatingAlgorithm;

/**
 * Factory to create instance of specific TotalPrivacyCheckerAlgorithm
 * implementation.
 */
public class TotalPrivacyRatingAlgorithmFactory {

	/**
	 * @return instance of specific PrivacyCheckerAlgorithm implementation
	 */
	public TotalPrivacyRatingAlgorithm makeAlgorithm() {
		return new CheckerAlgo();
	}

}
