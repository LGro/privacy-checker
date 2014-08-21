package de.otaris.zertapps.privacychecker.appDetails.rateApp;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Registry that stores key value pairs for static access.
 *
 */
public class Registry {

	private static Registry instance = null;

	private HashMap<String, String> values;

	// use hashmap instead String className, RatingElement
	private ArrayList<RatingElement> ratingElements;

	protected Registry() {
	}

	/**
	 * validates source class
	 * 
	 * @param sourceClass
	 *            full class name
	 * @return matches java class name conventions
	 */
	private boolean validateSourceClass(String sourceClass) {
		String regExp = "(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)+\\p{javaJavaIdentifi‌​erStart}\\p{javaJavaIdentifierPart}*";

		return sourceClass.matches(regExp);
	}

	public ArrayList<RatingElement> getRatingElements() {
		return ratingElements;
	}

	public RatingElement getRatingElement(int position) {
		return ratingElements.get(position);
	}

	public void updateRatingElement(RatingElement element, int position) {
		ratingElements.set(position, element);
	}

	public void addRatingElement(RatingElement element) {
		if (ratingElements == null)
			ratingElements = new ArrayList<RatingElement>();

		ratingElements.add(element);
	}

	/**
	 * sets a value for a given key and requires a source class to prevent
	 * collisions
	 * 
	 * @param sourceClass
	 * @param key
	 * @param value
	 * @throws InvalidParameterException
	 */
	public void set(String sourceClass, String key, String value)
			throws InvalidParameterException {
		if (!validateSourceClass(sourceClass))
			throw new InvalidParameterException("Invalid Source Class: "
					+ sourceClass);

		values.put(sourceClass + key, value);
	}

	/**
	 * gets a value for a given key and requires a source class to prevent
	 * collisions
	 * 
	 * @param sourceClass
	 * @param key
	 * @return value for the given key and source class
	 * @throws InvalidParameterException
	 */
	public String get(String sourceClass, String key)
			throws InvalidParameterException {
		if (!validateSourceClass(sourceClass))
			throw new InvalidParameterException("Invalid Source Class: "
					+ sourceClass);

		return values.get(sourceClass + key);
	}

	/**
	 * Singleton getter
	 * 
	 * @return singleton instance of Registry
	 */
	public static Registry getInstance() {
		if (instance == null)
			instance = new Registry();

		return instance;
	}

}
