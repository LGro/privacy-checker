package de.otaris.zertapps.privacychecker.database;

import java.util.ArrayList;


/**
 * represents the extended App entity in the database
 * an extended App has all the information that belong to an App
 * that is: Permissions, Comments, Ratingdetails
 *
 */
public class AppExtended implements App {

	private int id;
	private int categoryId;
	private String name;
	private String label;
	private String version;
	private float privacyRating;
	private boolean isInstalled;
	private float functionalRating;
	private Long timestamp;
	private String description;
	private ArrayList<Permission> permissionList;
		
	
	
	//getters and setters
	public ArrayList<Permission> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(ArrayList<Permission> permissionList) {
		this.permissionList = permissionList;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public int getCategoryId() {
		return categoryId;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public boolean getIsInstalled() {
		return isInstalled;
	}

	@Override
	public Long getTimestamp() {
		return timestamp;
	}

	@Override
	public float getPrivacyRating() {
		return privacyRating;
	}

	@Override
	public float getFunctionalRating() {
		return functionalRating;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setIsInstalled(boolean isInstalled) {
		this.isInstalled = isInstalled;
	}

	@Override
	public void setPrivacyRating(float privacyRating) {
		this.privacyRating = privacyRating;
	}

	@Override
	public void setFunctionalRating(float functionalRating) {
		this.functionalRating = functionalRating;
	}

	@Override
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

}
