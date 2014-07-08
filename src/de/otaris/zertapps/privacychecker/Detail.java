package de.otaris.zertapps.privacychecker;

public abstract class Detail {

	private int appId;
	
	private String detailName;
	
	public void setDetailName(String detailName){
		this.detailName = detailName;
	}
	
	public String getDetailName(){
		return this.detailName;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}
}
