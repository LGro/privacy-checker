package de.otaris.zertapps.privacychecker.appDetails.rateApp;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Registry that stores key value pairs and RatingElement objects for static
 * access.
 */
public class Registry {

	private static Registry instance = null;

	private HashMap<String, String> values;

	private HashMap<Class<?>, RatingElement> ratingElements;

	protected Registry() {
	}

	/**
	 * validates source package
	 * 
	 * @param sourcePackage
	 *            full class name
	 * @return matches java package name conventions
	 */
	private boolean validateSourcePackage(String sourcePackage) {
		return sourcePackage
				.matches("^[a-zA-Z_\\$][\\w\\$]*(?:\\.[a-zA-Z_\\$][\\w\\$]*)*$");
	}

	/**
	 * processes RatingElements HashMap to ArrayList
	 * 
	 * @return ArrayList of RatingElements
	 */
	public ArrayList<RatingElement> getRatingElements() {
		ArrayList<RatingElement> ratingElements = new ArrayList<RatingElement>();

		Iterator<Entry<Class<?>, RatingElement>> it = this.ratingElements
				.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Class<?>, RatingElement> pair = it.next();
			ratingElements.add(pair.getValue());
		}

		return ratingElements;
	}

	/**
	 * add rating element to registry hash map
	 * 
	 * @param element
	 */
	public void addRatingElement(RatingElement element) {
		if (ratingElements == null)
			ratingElements = new HashMap<Class<?>, RatingElement>();

		ratingElements.put(element.getClass(), element);
	}

	/**
	 * sets a value for a given key and requires a source package to prevent
	 * collisions
	 * 
	 * @param sourcePackage
	 * @param key
	 * @param value
	 * @throws InvalidParameterException
	 */
	public void set(String sourcePackage, String key, String value)
			throws InvalidParameterException {

		if (!validateSourcePackage(sourcePackage))
			throw new InvalidParameterException("Invalid Source Package: "
					+ sourcePackage);

		// initialize if not exists
		if (values == null)
			values = new HashMap<String, String>();

		values.put(sourcePackage + key, value);
	}

	/**
	 * gets a value for a given key and requires a source package to prevent
	 * collisions
	 * 
	 * @param sourcePackage
	 * @param key
	 * @return value for the given key and source class
	 * @throws InvalidParameterException
	 */
	public String get(String sourcePackage, String key)
			throws InvalidParameterException {
		if (!validateSourcePackage(sourcePackage))
			throw new InvalidParameterException("Invalid Source Package: "
					+ sourcePackage);

		// initialize if not exists
		if (values == null)
			values = new HashMap<String, String>();

		return values.get(sourcePackage + key);
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

	public RatingElement getRatingElement(Class<?> classObj) {
		return ratingElements.get(classObj);
	}

	/**
	 * Method to update a single Rating Element
	 * 
	 * @param classObj
	 * @param ratingElement
	 */

	public void updateRatingElement(Class<?> classObj,
			RatingElement ratingElement) {
		ratingElements.put(classObj, ratingElement);
	}

}
