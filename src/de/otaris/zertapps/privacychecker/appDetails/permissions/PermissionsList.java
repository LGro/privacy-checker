package de.otaris.zertapps.privacychecker.appDetails.permissions;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.AppDetailsActivity;
import de.otaris.zertapps.privacychecker.database.dataSource.AppCompactDataSource;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;
import de.otaris.zertapps.privacychecker.database.model.Permission;

public class PermissionsList extends ListFragment {

	private Activity rootActivity;
	public PermissionsList() {
	}

	public void setRootActivity(Activity rootActivity) {
		this.rootActivity = rootActivity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set custom list adapter to display apps with icon, name and rating
		ArrayAdapter<Permission> adapter = new PermissionsListItemAdapter(rootActivity,
				rootActivity.getPackageManager(), permissionsList);
		setListAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_fragment, container, false);
	}
//
//	@Override
//	public void onListItemClick(ListView list, View v, int position, long id) {
//		Intent intent = new Intent(rootActivity, AppDetailsActivity.class);
//		intent.putExtra("permission_id", (Integer)v.getTag());
//		startActivity(intent);
//	}
}