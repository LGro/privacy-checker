package de.otaris.zertapps.privacychecker.database.dataSource;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import de.otaris.zertapps.privacychecker.database.DatabaseHelper;

/**
 * provides abstract functionality for processing of database results
 *
 * @param <T>
 *            model type
 */
public abstract class DataSource<T> {

	protected SQLiteDatabase database;
	protected DatabaseHelper dbHelper;

	/**
	 * convert cursor to model instance
	 * 
	 * @param cursor
	 * @return model with attributes
	 */
	protected abstract T cursorToModel(Cursor cursor);

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
	 * creates a list of models from a given type T by using the cursorToModel
	 * method
	 * 
	 * @param cursor
	 * @return list of model from type T
	 */
	public List<T> cursorToModelList(Cursor cursor) {
		List<T> list = new ArrayList<T>();

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			T model = cursorToModel(cursor);
			list.add(model);
			cursor.moveToNext();
		}
		cursor.close();

		return list;
	}

}
