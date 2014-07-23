package de.otaris.zertapps.privacychecker.database.model;

import java.util.ArrayList;

import de.otaris.zertapps.privacychecker.database.interfaces.App;

/**
 * represents the extended App entity in the database an extended App has all
 * the information that belong to an App that is: Permissions, Comments,
 * Ratings, Details
 *
 */
public class AppExtended implements App {

	private AppCompact appCompact;
	private ArrayList<Permission> permissionList;
	//Array with permissionID and RatingPermission
	private int[][] permissionRating;
	//weighted rating in total
	private float rating;
	private float expertRating;
	private float nonExpertRating;
	//automatic generated rating
	private float technicalRating;

	public AppExtended(AppCompact appCompact) {
		this.appCompact = appCompact;
	}

	// getters and setters
	
	
	public ArrayList<Permission> getPermissionList() {
		return permissionList;
	}

	public int[][] getPermissionRating() {
		return permissionRating;
	}

	public void setPermissionRating(int[][] permissionRating) {
		this.permissionRating = permissionRating;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public float getExpertRating() {
		return expertRating;
	}

	public void setExpertRating(float expertRating) {
		this.expertRating = expertRating;
	}

	public float getNonExpertRating() {
		return nonExpertRating;
	}

	public void setNonExpertRating(float nonExpertRating) {
		this.nonExpertRating = nonExpertRating;
	}

	public float getTechnicalRating() {
		return technicalRating;
	}

	public void setTechnicalRating(float technicalRating) {
		this.technicalRating = technicalRating;
	}

	@Override
	public int getId() {
		return appCompact.getId();
	}

	@Override
	public int getCategoryId() {
		return appCompact.getCategoryId();
	}

	@Override
	public String getVersion() {
		return appCompact.getVersion();
	}

	@Override
	public String getName() {
		return appCompact.getName();
	}

	@Override
	public String getLabel() {
		return appCompact.getLabel();
	}

	@Override
	public boolean isInstalled() {
		return appCompact.isInstalled();
	}

	@Override
	public Long getTimestamp() {
		return appCompact.getTimestamp();
	}

	@Override
	public float getPrivacyRating() {
		return appCompact.getPrivacyRating();
	}

	@Override
	public float getFunctionalRating() {
		return appCompact.getFunctionalRating();
	}

	@Override
	public String getDescription() {
		return appCompact.getDescription();
	}

	public void setPermissionList(ArrayList<Permission> permissionList) {
		this.permissionList = permissionList;
	}

	@Override
	public void setId(int id) {
		appCompact.setId(id);
	}

	@Override
	public void setCategoryId(int categoryId) {
		appCompact.setCategoryId(categoryId);
	}

	@Override
	public void setLabel(String label) {
		appCompact.setLabel(label);
	}

	@Override
	public void setVersion(String version) {
		appCompact.setVersion(version);
	}

	@Override
	public void setName(String name) {
		appCompact.setName(name);
	}

	@Override
	public void setInstalled(boolean isInstalled) {
		appCompact.setInstalled(isInstalled);
	}

	@Override
	public void setPrivacyRating(float privacyRating) {
		appCompact.setPrivacyRating(privacyRating);
	}

	@Override
	public void setFunctionalRating(float functionalRating) {
		appCompact.setFunctionalRating(functionalRating);
	}

	@Override
	public void setTimestamp(Long timestamp) {
		appCompact.setTimestamp(timestamp);
	}

	@Override
	public void setDescription(String description) {
		appCompact.setDescription(description);
	}

}
