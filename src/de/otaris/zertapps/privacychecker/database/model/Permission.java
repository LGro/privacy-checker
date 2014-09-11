package de.otaris.zertapps.privacychecker.database.model;

import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
/**
 * represents the Permission entity in the database
 * 
 * Attention: When adding a new column, mind adding it in the matching
 * DataSource's cursorTo... method.
 */
public class Permission implements Parcelable {

	// Database table
	public static final String TABLE = "tbl_permission";
	// Columns
	public static final String ID = "permission_id";
	public static final String NAME = "name";
	public static final String LABEL = "label";
	public static final String DESCRIPTION = "description";
	public static final String CRITICALITY = "criticality";

	// Creation statement
	private static final String Create_Permission_Table = "CREATE TABLE "
			+ TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME
			+ " TEXT, " + LABEL + " TEXT, " + DESCRIPTION + " TEXT, "
			+ CRITICALITY + " INTEGER);";

	private int id;
	private String name;
	private String label;
	private String description;
	private int criticality;

	// empty constructor
	public Permission() {

	}

	public Permission(int id, String name, String label, String description,
			int criticality) {
		this.id = id;
		this.name = name;
		this.label = label;
		this.description = description;
		this.criticality = criticality;
	}

	// create table if it isn't existing yet
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(Create_Permission_Table);
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

	// getter and setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCriticality() {
		return criticality;
	}

	public void setCriticality(int criticality) {
		this.criticality = criticality;
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
		dest.writeString(description);
		dest.writeInt(criticality);
	}

	private void readFromParcel(Parcel in) {
		id = in.readInt();
		name = in.readString();
		label = in.readString();
		description = in.readString();
		criticality = in.readInt();
	}

	// this is used to regenerate your object. All Parcelables must have a
	// CREATOR that implements these two methods
	public static final Parcelable.Creator<Permission> CREATOR = new Parcelable.Creator<Permission>() {
		public Permission createFromParcel(Parcel in) {
			return new Permission(in);
		}

		public Permission[] newArray(int size) {
			return new Permission[size];
		}
	};

	// constructor that takes a Parcel and gives you an object populated with
	// it's values
	private Permission(Parcel in) {
		readFromParcel(in);
	}

}
