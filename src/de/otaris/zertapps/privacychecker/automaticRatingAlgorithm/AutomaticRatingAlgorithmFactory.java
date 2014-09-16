package de.otaris.zertapps.privacychecker.automaticRatingAlgorithm;

/**
 * Provides a specific instance of AutomaticRatingAlgorithm.
 */
public class AutomaticRatingAlgorithmFactory {

	public AutomaticRatingAlgorithm makeAlgorithm() {
		return new FirstAlgo();
	}

	public AutomaticRatingAlgorithm makeWeightedAlgorithm() {
		return new CategoryWeightedAlgo();
	}

}
