package de.otaris.zertapps.privacychecker.database.dataSource;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import de.otaris.zertapps.privacychecker.database.DatabaseHelper;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;

/**
 * Handles requests concerning the App with all related information (ratings,
 * permissions, comments) on the Database
 *
 */
public class AppExtendedDataSource extends DataSource<AppExtended> {

	private AppCompactDataSource appData;

	public AppExtendedDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
		appData = new AppCompactDataSource(context);
	}

	@Override
	protected AppExtended cursorToModel(Cursor cursor) {
		// TODO Auto-generated method stub
		return null;
	}

	public AppExtended getAppById(long appId) {
		appData.open();
		AppCompact app = appData.getAppById(appId);
		appData.close();

		AppExtended appExtended = new AppExtended(app);

		return appExtended;
	}

}
