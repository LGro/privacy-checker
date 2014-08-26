package de.otaris.zertapps.privacychecker.database.model;

import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import de.otaris.zertapps.privacychecker.database.interfaces.App;

/**
 * represents the App entity in the database
 * 
 * Attention: When adding a new column, mind adding it in the matching
 * DataSource's cursorTo... method.
 */
public class AppCompact implements App, Parcelable {

	// table name
	public static final String TABLE = "tbl_app";

	// table columns
	public static final String ID = "app_id";
	public static final String CATEGORY_ID = "category_id";
	public static final String NAME = "name";
	public static final String LABEL = "label";
	public static final String VERSION = "version";
	public static final String PRIVACY_RATING = "privacy_rating";
	public static final String INSTALLED = "installed";
	public static final String FUNCTIONAL_RATING = "functional_rating";
	public static final String TIMESTAMP = "timestamp";
	public static final String DESCRIPTION = "description";
	public static final String ICON = "icon";
	public static final String AUTOMATIC_RATING = "automatic_rating";
	public static final String AUTOMATIC_RATING_RELATIVE_TO_CATEGORY = "automatic_rating_relative_to_category";

	// TODO: fix: ON CONFLICT REPLACE updates the primary key (ID) and makes all
	// references invalid
	// SQL statement to create table
	private static final String Create_App_Table = "CREATE TABLE " + TABLE
			+ "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CATEGORY_ID
			+ " INTEGER, " + NAME + " TEXT UNIQUE ON CONFLICT REPLACE, "
			+ LABEL + " TEXT, " + VERSION + " TEXT, " + PRIVACY_RATING
			+ " FLOAT, " + INSTALLED + " INTEGER, " + FUNCTIONAL_RATING
			+ " FLOAT, " + TIMESTAMP + " LONG, " + DESCRIPTION + " TEXT, "
			+ ICON + " BLOB, " + AUTOMATIC_RATING + " FLOAT, "
			+ AUTOMATIC_RATING_RELATIVE_TO_CATEGORY + " FLOAT);";

	// attributes
	private int id;
	private int categoryId;
	private String name;
	private String label;
	private String version;
	private float privacyRating;
	private boolean isInstalled;
	private float functionalRating;
	private Long timestamp;
	private String description;
	private byte[] icon;
	private float automaticRating;
	private float automaticRatingRelativeToCategory;

	public AppCompact() {

	}

	public AppCompact(int id, int categoryId, String name, String label,
			String version, float rating, boolean installed, Long timestamp,
			String description, byte[] icon, float automaticRating) {

		this.id = id;
		this.categoryId = categoryId;
		this.name = name;
		this.label = label;
		this.version = version;
		this.privacyRating = rating;
		this.isInstalled = installed;
		this.timestamp = timestamp;
		this.description = description;
		this.icon = icon;
		this.automaticRating = automaticRating;
	}

	/**
	 * create table if it isn't existing yet
	 * 
	 * @param db
	 *            Database to create the table in
	 */
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(Create_App_Table);
	}

	/**
	 * upgrade an older version of this table currently drops the old table and
	 * calls onCreate
	 * 
	 * @param db
	 *            Database to update
	 * @param oldVersion
	 *            old database version number
	 * @param newVersion
	 *            new database version number
	 */
	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		Log.w(AppCompact.class.getCanonicalName(),
				"upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");

		db.execSQL("DROP TABLE IF EXISTS " + TABLE);

		onCreate(db);
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setInstalled(boolean isInstalled) {
		this.isInstalled = isInstalled;
	}

	@Override
	public void setPrivacyRating(float privacyRating) {
		this.privacyRating = privacyRating;
	}

	@Override
	public void setFunctionalRating(float functionalRating) {
		this.functionalRating = functionalRating;
	}

	@Override
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public void setIcon(byte[] icon) {
		this.icon = icon;
	}

	@Override
	public void setAutomaticRating(float automaticRating) {
		this.automaticRating = automaticRating;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public float getAutomaticRating() {
		return automaticRating;
	}

	@Override
	public int getCategoryId() {
		return categoryId;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public boolean isInstalled() {
		return isInstalled;
	}

	@Override
	public Long getTimestamp() {
		return timestamp;
	}

	@Override
	public float getPrivacyRating() {
		return privacyRating;
	}

	@Override
	public float getFunctionalRating() {
		return functionalRating;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public float getAutomaticRatingRelativeToCategory() {
		return automaticRatingRelativeToCategory;
	}

	@Override
	public void setAutomaticRatingRelativeToCategory(float rating) {
		this.automaticRatingRelativeToCategory = rating;
	}

	public byte[] getIcon() {
		return icon;
	}

	// parcelable requirements...

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(categoryId);
		dest.writeString(name);
		dest.writeString(label);
		dest.writeString(version);
		dest.writeFloat(privacyRating);
		dest.writeByte((byte) (isInstalled ? 1 : 0));
		dest.writeFloat(functionalRating);
		dest.writeLong(timestamp);
		dest.writeString(description);
		dest.writeInt(icon.length);
		dest.writeByteArray(icon);
		dest.writeFloat(automaticRating);
		dest.writeFloat(automaticRatingRelativeToCategory);
	}

	private void readFromParcel(Parcel in) {
		id = in.readInt();
		categoryId = in.readInt();
		name = in.readString();
		label = in.readString();
		version = in.readString();
		privacyRating = in.readFloat();
		isInstalled = in.readByte() != 0;
		functionalRating = in.readFloat();
		timestamp = in.readLong();
		description = in.readString();
		icon = new byte[in.readInt()];
		in.readByteArray(icon);
		automaticRating = in.readFloat();
		automaticRatingRelativeToCategory = in.readFloat();
	}

	// this is used to regenerate your object. All Parcelables must have a
	// CREATOR that implements these two methods
	public static final Parcelable.Creator<AppCompact> CREATOR = new Parcelable.Creator<AppCompact>() {
		public AppCompact createFromParcel(Parcel in) {
			return new AppCompact(in);
		}

		public AppCompact[] newArray(int size) {
			return new AppCompact[size];
		}
	};

	// constructor that takes a Parcel and gives you an object populated with
	// it's values
	private AppCompact(Parcel in) {
		readFromParcel(in);
	}

}
