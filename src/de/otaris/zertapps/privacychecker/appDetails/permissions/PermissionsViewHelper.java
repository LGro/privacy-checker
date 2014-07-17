package de.otaris.zertapps.privacychecker.appDetails.permissions;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.Detail;
import de.otaris.zertapps.privacychecker.appDetails.DetailViewHelper;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;
import de.otaris.zertapps.privacychecker.database.model.Permission;

public class PermissionsViewHelper extends DetailViewHelper {


	private final Context context;
	private final List<Permission> values;
		

	public PermissionsViewHelper(Context context,	List<Permission> values) {
		super();
		this.context = context;
		this.values = values;
	}
	


	@Override
	public View getView(Context context, ViewGroup parent, Detail detail)
			throws IllegalArgumentException {
		if (!(detail instanceof Permissions))
			throw new IllegalArgumentException(
					"Illegal Detail Object. Expected Description.");

		Permissions permissions = (Permissions) detail;
		AppExtended app = permissions.getApp();
		
		List<Permission> permissionList = app.getPermissionList();
		
		String[] permis = new String[permissionList.size()]; 
		for (int i=0; i<permissionList.size(); i++) {
			permis[i]= permissionList.get(i).getLabel();
			}

	    final ListView listview = (ListView) findViewById(R.id.listview);
	    
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.app_detail_permissions,
				parent, false);

		TextView permissionsTextView = (TextView) rowView
				.findViewById(R.id.app_detail_permissions_text);

		permissionsTextView.setText("Hallo, Berechtigungen");

		ToggleButton button = (ToggleButton) rowView
				.findViewById(R.id.app_detail_permissions_more);
		button.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton toggleButton,
					boolean isChecked) {
				TextView descriptionTextView = (TextView) ((View) toggleButton
						.getParent())
						.findViewById(R.id.app_detail_permissions_text);
				//changes fulltext to shorten version 
				if (isChecked) {
					descriptionTextView.setMaxLines(200);
				} else {
					descriptionTextView.setMaxLines(2);
				}

			}
		});

		return rowView;
		
	}

}
