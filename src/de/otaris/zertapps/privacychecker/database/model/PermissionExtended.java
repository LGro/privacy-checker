package de.otaris.zertapps.privacychecker.database.model;


public class PermissionExtended extends AppPermission{

	private AppPermission appPermission;
	private Permission permission;
	private Integer expertPermissionExpected;
	private Integer nonExpertPermissionExpected;
	private Integer expertPermissionUnexpected;
	private Integer nonExpertPermissionUnexpected;
	
	public PermissionExtended(AppPermission appPermission){
		this.appPermission = appPermission;
	}
	
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
	public AppPermission getAppPermission() {
		return appPermission;
	}
	public void setAppPermission(AppPermission appPermission) {
		this.appPermission = appPermission;
	}
	public Integer getExpertPermissionUnexpected() {
		return expertPermissionUnexpected;
	}
	public void setExpertPermissionUnexpected(Integer expertPermissionUnexpected) {
		this.expertPermissionUnexpected = expertPermissionUnexpected;
	}
	public Integer getNonExpertPermissionUnexpected() {
		return nonExpertPermissionUnexpected;
	}
	public void setNonExpertPermissionUnexpected(
			Integer nonExpertPermissionUnexpected) {
		this.nonExpertPermissionUnexpected = nonExpertPermissionUnexpected;
	}

	@Override
	public int getId() {
		return appPermission.getId();
	}
	
	@Override
	public void setId(int id) {
		appPermission.setId(id);
	}
	
	@Override
	public int getAppId() {
		return appPermission.getAppId();
	}
	
	@Override
	public void setAppId(int appId) {
		appPermission.setAppId(appId);
	}
	
	public Permission getPermission() {
		return permission;
	}
	
	public void setPermission(Permission permission) {
		this.permission = permission;
	}

		
}
