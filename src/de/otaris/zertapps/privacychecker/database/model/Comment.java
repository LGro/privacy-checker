package de.otaris.zertapps.privacychecker.database.model;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Comment {
	// Database table
	public static final String TABLE = "tbl_comment";
	// Columns
	public static final String ID = "comment_id";
	public static final String CONTENT = "content";
	public static final String VERSION = "version";
	public static final String TIMESTAMP = "timestamp";
	public static final String USER_TYPE = "user_type";
	public static final String APP_ID = "app_id";

	// Creation statement
	private static final String Create_Comment_Table = "CREATE TABLE " + TABLE
			+ "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CONTENT
			+ " TEXT, " + VERSION + " TEXT, " + TIMESTAMP + " LONG, "
			+ USER_TYPE + " INTEGER, " + APP_ID + " INTEGER );";

	// empty constructor
	public Comment() {

	}

	// attributes
	private int id;
	private String content;
	private String version;
	private long timestamp;
	private boolean isExpert;
	private int appId;

	public Comment(int id, String content, String version, long date, int appId) {
		this.id = id;
		this.content = content;
		this.version = version;
		this.timestamp = date;
		this.appId = appId;
	}

	// create table if it isn't existing yet
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(Create_Comment_Table);
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

	// getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long date) {
		this.timestamp = date;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}
	public boolean isExpert() {
		return isExpert;
	}

	public void setExpert(boolean isExpert) {
		this.isExpert = isExpert;
	}
}
