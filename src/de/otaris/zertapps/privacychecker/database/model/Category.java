package de.otaris.zertapps.privacychecker.database.model;

import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Category implements Parcelable {

	// Database table
	public static final String TABLE = "tbl_category";
	// Columns
	public static final String ID = "category_id";
	public static final String NAME = "name";
	public static final String LABEL = "label";
	public static final String ORDER = "position";
	public static final String AVERAGEAUTORATING = "average_auto_rating";

	// Creation statement
	private static final String Create_Category_Table = "CREATE TABLE " + TABLE
			+ "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME
			+ " TEXT UNIQUE ON CONFLICT REPLACE, " + LABEL + " TEXT, " + ORDER
			+ " INTEGER, " + AVERAGEAUTORATING + " FLOAT);";

	// attributes
	private int id;
	private String name;
	private String label;
	private int order;
	private float averageAutoRating;

	// empty constructor
	public Category() {

	}

	public Category(int id, String name, String label, int order, float avgAutoRating) {
		this.id = id;
		this.name = name;
		this.label = label;
		this.order = order;
		this.averageAutoRating = avgAutoRating;
	}

	// create table if it isn't existing yet
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(Create_Category_Table);
	}

	// upgrade Method
	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		Log.w(AppCompact.class.getCanonicalName(),
				"upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
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
	public float getAverageAutoRating() {
		return averageAutoRating;
	}

	public void setAverageAutoRating(float averageAutoRating) {
		this.averageAutoRating = averageAutoRating;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(label);
		dest.writeInt(order);
		dest.writeFloat(averageAutoRating);
	}

	private void readFromParcel(Parcel in) {
		id = in.readInt();
		name = in.readString();
		label = in.readString();
		order = in.readInt();
		averageAutoRating = in.readFloat();
	}

	// this is used to regenerate your object. All Parcelables must have a
	// CREATOR that implements these two methods
	public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
		public Category createFromParcel(Parcel in) {
			return new Category(in);
		}

		public Category[] newArray(int size) {
			return new Category[size];
		}
	};

	// constructor that takes a Parcel and gives you an object populated with
	// it's values
	private Category(Parcel in) {
		readFromParcel(in);
	}



}
