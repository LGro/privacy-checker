package de.otaris.zertapps.privacychecker.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import de.otaris.zertapps.privacychecker.AppController;
import de.otaris.zertapps.privacychecker.appsList.AppsListOrder;
import de.otaris.zertapps.privacychecker.database.dataSource.AppCompactDataSource;
import de.otaris.zertapps.privacychecker.database.dataSource.AppExtendedDataSource;
import de.otaris.zertapps.privacychecker.database.dataSource.CategoryDataSource;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;
import de.otaris.zertapps.privacychecker.database.model.AppPermission;
import de.otaris.zertapps.privacychecker.database.model.Category;
import de.otaris.zertapps.privacychecker.database.model.Permission;
import de.otaris.zertapps.privacychecker.database.model.RatingApp;
import de.otaris.zertapps.privacychecker.database.model.AppPermissionRating;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "DB_privacy-checker_apps";

	private final Context context;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		AppCompact.onCreate(db);
		Category.onCreate(db);
		Permission.onCreate(db);
		AppPermission.onCreate(db);
		// Comment.onCreate(db);
		RatingApp.onCreate(db);
		AppPermissionRating.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		AppCompact.onUpgrade(db, oldVersion, newVersion);
		Category.onUpgrade(db, oldVersion, newVersion);
		Permission.onUpgrade(db, oldVersion, newVersion);
		AppPermission.onUpgrade(db, oldVersion, newVersion);
		// Comment.onUpgrade(db, oldVersion, newVersion);
		RatingApp.onUpgrade(db, oldVersion, newVersion);
		AppPermissionRating.onUpgrade(db, oldVersion, newVersion);

		onCreate(db);
	}

	public void exportDatabase(Activity activity) {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String currentDBPath = "//data//"
						+ "de.otaris.zertapps.privacychecker" + "//databases//"
						+ "DB_privacy-checker_apps";
				String backupDBPath = sd + "/Pca/pca.db";
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(backupDBPath);

				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupDB).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
				Toast.makeText(activity.getBaseContext(), backupDB.toString(),
						Toast.LENGTH_LONG).show();

			} else {
				Log.e("exportDB", "sdcard nicht beschreibbar");
			}
		} catch (Exception e) {

			Toast.makeText(activity.getBaseContext(), e.toString(),
					Toast.LENGTH_LONG).show();

		}
	}

	public void importDatabase() throws IOException {
		// important: there has to be opened and closed a connection
		// before the import to ensure that a database exists that can
		// be overwritten otherwise the import will fail
		getWritableDatabase();
		close();

		// Open your local db as the input stream
		InputStream myInput = context.getAssets().open("pca.db");

		// Path to the just created empty db
		String outFileName = "//data//data//"
				+ "de.otaris.zertapps.privacychecker" + "//databases//"
				+ "DB_privacy-checker_apps";

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void fillDatabaseFromDevice() {
		// fill db-category-table with entries
		CategoryDataSource categoryData = new CategoryDataSource(context);
		AppCompactDataSource appData = new AppCompactDataSource(context);

		categoryData.open();
		categoryData.createCategory("games", "Spiele", 10);
		categoryData.createCategory("weather", "Wetter", 20);
		categoryData.createCategory("categoryA", "Kategorie A", 30);
		categoryData.createCategory("categoryB", "Kategorie B", 40);
		categoryData.createCategory("categoryC", "Kategorie C", 50);
		categoryData.close();

		// insert all installed apps into database
		AppController appController = new AppController();
		appController.putInstalledAppsInDatabase(context,
				context.getPackageManager());
	}

	public void recalculateAutomaticRatingForAllApps() {
		AppExtendedDataSource appData = new AppExtendedDataSource(context);
		appData.open();

		List<AppExtended> apps = appData.getAllApps(AppsListOrder.ALPHABET,
				true);

		for (AppExtended app : apps) {
			float automaticPrivacyRating = 0;

			for (Permission permission : app.getPermissionList())
				automaticPrivacyRating += permission.getCriticality();

			// normalize accumulated criticality to privacy rating within [0:5]
			automaticPrivacyRating /= app.getPermissionList().size();
			automaticPrivacyRating /= AppController.PERMISSION_MIN_CRITICALITY;
			automaticPrivacyRating *= 5;

			appData.updateAppById(app.getId(), app.getCategoryId(),
					app.getName(), app.getLabel(), app.getVersion(),
					app.getPrivacyRating(), app.isInstalled(),
					app.getFunctionalRating(), app.getDescription(),
					app.getIcon(), automaticPrivacyRating);
		}

		appData.close();

	}

}
