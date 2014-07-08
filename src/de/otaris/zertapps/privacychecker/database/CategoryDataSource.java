package de.otaris.zertapps.privacychecker.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CategoryDataSource {

	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private String[] allColumns = { Category.ID, Category.NAME, Category.LABEL, Category.ORDER };
	public CategoryDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	/**
	 * open DB connection
	 * 
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	/**
	 * close DB connection
	 */
	public void close() {
		dbHelper.close();
	}

	/**
	 * convert database result cursor (pointing to a result set) to category
	 * object
	 * 
	 * @param cursor
	 * @return cursor data as Category object
	 */

	private Category cursorToCategory(Cursor cursor) {
		Category category = new Category();

		category.setId(cursor.getInt(0));
		category.setName(cursor.getString(1));
		category.setLabel(cursor.getString(2));
		category.setOrder(cursor.getInt(3));

		return category;
	}

	/**
	 * get all Categories from the DB, ordered ascendingly by column order
	 * 
	 * 
	 * @return sorted list of all categories
	 */
	public List<Category> getAllCategories() {
		List<Category> categories = new ArrayList<Category>();

		Cursor cursor = database.query(Category.TABLE, allColumns, null, null,
				null, null, Category.ORDER + " ASC");

		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			Category category = cursorToCategory(cursor);
			categories.add(category);
			cursor.moveToNext();
		}

		cursor.close();
		return categories;
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
	public Category createCategory(String name, String label,
			int order) {
		// set values for columns
		ContentValues values = new ContentValues();
		values.put(Category.NAME, name);
		values.put(Category.LABEL, label);
		values.put(Category.ORDER, order);

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
		Category newCategory = cursorToCategory(cursor);
		cursor.close();

		// return app object
		return newCategory;
	}

}
