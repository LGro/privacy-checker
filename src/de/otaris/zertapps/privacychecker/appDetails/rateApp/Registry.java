package de.otaris.zertapps.privacychecker.appDetails.rateApp;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Registry that stores key value pairs for static access.
 *
 */
public class Registry {

	private static Registry instance = null;

	private HashMap<String, String> values;

	private HashMap<Class<?>, RatingElement> ratingElements;

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
		try {
			Class.forName(sourceClass);
			return true;
		} catch (ClassNotFoundException ex) {
			return false;
		}
	}

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

	public RatingElement getRatingElement(int position) {
		return ratingElements.get(position);
	}

	public void addRatingElement(RatingElement element) {
		if (ratingElements == null)
			ratingElements = new HashMap<Class<?>, RatingElement>();

		ratingElements.put(element.getClass(), element);
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

	public RatingElement getRatingElement(Class<?> classObj) {
		return ratingElements.get(classObj);
	}

	public void updateRatingElement(Class<?> classObj,
			RatingElement ratingElement) {
		ratingElements.put(classObj, ratingElement);
	}

}
