package de.otaris.zertapps.privacychecker.database.dataSource;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import de.otaris.zertapps.privacychecker.database.DatabaseHelper;
import de.otaris.zertapps.privacychecker.database.model.Category;

/**
 * 
 * Handles requests concerning Categories on the Database
 *
 */
public class CategoryDataSource extends DataSource<Category> {

	private String[] allColumns = { Category.ID, Category.NAME, Category.LABEL,
			Category.ORDER };

	public CategoryDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	/**
	 * convert database result cursor (pointing to a result set) to category
	 * object
	 * 
	 * @param cursor
	 * @return cursor data as Category object
	 */

	protected Category cursorToModel(Cursor cursor) {
		Category category = new Category();

		category.setId(cursor.getInt(0));
		category.setName(cursor.getString(1));
		category.setLabel(cursor.getString(2));
		category.setOrder(cursor.getInt(3));
		category.setAverage_auto_rating(cursor.getFloat(4));

		return category;
	}

	/**
	 * get all Categories from the DB, ordered ascendingly by column order
	 * 
	 * 
	 * @return sorted list of all categories
	 */
	public List<Category> getAllCategories() {
		Cursor cursor = database.query(Category.TABLE, allColumns, null, null,
				null, null, Category.ORDER + " ASC");

		return cursorToModelList(cursor);
	}

	/**
	 * create category in DB by all attributes
	 * 
	 * 
	 * @param name
	 * @param label
	 * @param order
	 * 
	 * @return category object of the newly created category
	 */
	public Category createCategory(String name, String label, int order,
			float avgAutoRating) {
		// set values for columns
		ContentValues values = new ContentValues();
		values.put(Category.NAME, name);
		values.put(Category.LABEL, label);
		values.put(Category.ORDER, order);
		values.put(Category.AVERAGEAUTORATING, avgAutoRating);

		// insert into DB
		long insertId = database.insert(Category.TABLE, null, values);

		// get recently inserted App by ID
		return getCategoryById(insertId);
	}

	/**
	 * gets single category by id from database
	 * 
	 * @param categoryId
	 * 
	 * @return category object with the given id
	 */
	public Category getCategoryById(long categoryId) {
		// build database query
		Cursor cursor = database.query(Category.TABLE, allColumns, Category.ID
				+ " = " + categoryId, null, null, null, null);
		cursor.moveToFirst();

		// convert to App object
		Category newCategory = cursorToModel(cursor);
		cursor.close();

		// return app object
		return newCategory;
	}

	/**
	 * gets the averageRating for a Category By Id
	 * 
	 * @param categoryId
	 * @return float with average category Id
	 */
	public float getAverageRatingForCategory(int categoryId) {
		Category cat = getCategoryById(categoryId);
		return cat.getAverage_auto_rating();
	}

}
