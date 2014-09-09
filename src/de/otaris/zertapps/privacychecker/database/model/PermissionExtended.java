package de.otaris.zertapps.privacychecker.database.model;

import java.util.ArrayList;

public class PermissionExtended {

	private AppPermission appPermission;
	private Integer expertPermissionExpected;
	
	// Getter and setter
	public Integer getExpertPermissionExpected() {
		return expertPermissionExpected;
	}
	public void setExpertPermissionExpected(Integer expertPermissionExpected) {
		this.expertPermissionExpected = expertPermissionExpected;
	}
	public Integer getNonExpertPermissionExpected() {
		return nonExpertPermissionExpected;
	}
	public void setNonExpertPermissionExpected(Integer nonExpertPermissionExpected) {
		this.nonExpertPermissionExpected = nonExpertPermissionExpected;
	}
	private Integer nonExpertPermissionExpected;
	
	
}
