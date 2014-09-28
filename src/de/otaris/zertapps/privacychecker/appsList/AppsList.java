package de.otaris.zertapps.privacychecker.appsList;

import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import de.otaris.zertapps.privacychecker.appDetails.AppDetailsActivity;
import de.otaris.zertapps.privacychecker.database.model.AppCompact;

public class AppsList extends ListFragment {

	@Override
	public void onListItemClick(ListView list, View v, int position, long id) {
		Intent intent = new Intent(getActivity(), AppDetailsActivity.class);
		AppCompact app = (AppCompact) list.getItemAtPosition(position);
		intent.putExtra("AppCompact", app);
		startActivity(intent);
	}
}
