package de.otaris.zertapps.privacychecker.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Category {

	// Database table
	public static final String TABLE = "category";
	// Columns
	public static final String ID = "category_id";
	public static final String NAME = "name";
	public static final String LABEL = "label";
	public static final String ORDER = "position";

	// Creation statement
	private static final String Create_Category_Table = "CREATE TABLE " + TABLE
			+ "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME
			+ " TEXT UNIQUE ON CONFLICT REPLACE, " + LABEL + " TEXT, " + ORDER
			+ " INTEGER);";

	// attributes
	private int id;
	private String name;
	private String label;
	private int order;

	// empty constructor
	public Category() {

	}

	public Category(int id, String name, String label, int order) {
		this.id = id;
		this.name = name;
		this.label = label;
		this.order = order;
	}

	// create table if it isn't existing yet
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(Create_Category_Table);
	}

	// upgrade Method
	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		Log.w(App.class.getCanonicalName(), "upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE);
		onCreate(db);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public int getOrder() {
		return order;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
