package de.otaris.zertapps.privacychecker.appDetails.permissions;

import java.util.ArrayList;

import de.otaris.zertapps.privacychecker.appDetails.Detail;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;
import de.otaris.zertapps.privacychecker.database.model.Permission;

public class Permissions extends Detail {
	
	private ArrayList <Permission> permissionList;

	public Permissions(AppExtended app) {
		super(app);
		// TODO Auto-generated constructor stub
	}

}
