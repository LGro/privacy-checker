package de.otaris.zertapps.privacychecker.database.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import de.otaris.zertapps.privacychecker.database.interfaces.App;

/**
 * represents the extended App entity in the database an extended App has all
 * the information that belong to an App that is: Permissions, Comments,
 * Ratings, Details
 *
 */
public class AppExtended implements App, Parcelable {

	private AppCompact appCompact;
	private ArrayList<Permission> permissionList;
	// Array with permissionID and RatingPermission
	// TODO: better use PermissionRating object containing Permission Object?!
	private int[][] permissionRating;
	private ArrayList<Integer> expertRating;
	private float totalExpertRating;
	private ArrayList<Integer> nonExpertRating;
	private float totalNonExpertRating;
	private Category category;

	// getters and setters

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public AppExtended(AppCompact appCompact) {
		this.appCompact = appCompact;
	}

	public ArrayList<Permission> getPermissionList() {
		return permissionList;
	}

	public float getTotalExpertRating() {
		return totalExpertRating;
	}

	private void calculateTotalExpertRating(ArrayList<Integer> expertRating) {

		float totalExpertRating = 0;
		int i = expertRating.size();
		if (i == 0)
			this.totalExpertRating = 0;
		else {

			for (int rating : expertRating) {
				totalExpertRating += rating;
			}

			totalExpertRating /= i;

			this.totalExpertRating = totalExpertRating;
		}
	}

	public float getTotalNonExpertRating() {
		return totalNonExpertRating;
	}

	private void calculateTotalNonExpertRating(
			ArrayList<Integer> nonExpertRating) {

		float totalNonExpertRating = 0;
		int i = nonExpertRating.size();
		if (i == 0)
			this.totalNonExpertRating = 0;
		else {

			for (int rating : nonExpertRating) {
				totalNonExpertRating += rating;
			}

			totalNonExpertRating /= i;

			this.totalNonExpertRating = totalNonExpertRating;
		}
	}

	public int[][] getPermissionRating() {
		return permissionRating;
	}

	public void setPermissionRating(int[][] permissionRating) {
		this.permissionRating = permissionRating;
	}

	public ArrayList<Integer> getExpertRating() {
		return expertRating;
	}

	public void setExpertRating(ArrayList<Integer> expertRating) {
		this.expertRating = expertRating;
		calculateTotalExpertRating(expertRating);
	}

	public ArrayList<Integer> getNonExpertRating() {
		return nonExpertRating;
	}

	public void setNonExpertRating(ArrayList<Integer> nonExpertRating) {
		this.nonExpertRating = nonExpertRating;
		calculateTotalNonExpertRating(nonExpertRating);

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

	@Override
	public byte[] getIcon() {
		return appCompact.getIcon();
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

	@Override
	public void setIcon(byte[] icon) {
		appCompact.setIcon(icon);
	}

	@Override
	public float getAutomaticRating() {
		return appCompact.getAutomaticRating();
	}

	@Override
	public void setAutomaticRating(float automaticRating) {
		appCompact.setAutomaticRating(automaticRating);
	}

	@Override
	public void setCategoryWeightedAutoRating(float rating) {
		appCompact.setCategoryWeightedAutoRating(rating);
	}

	@Override
	public float getCategoryWeightedAutoRating() {
		return appCompact.getCategoryWeightedAutoRating();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(appCompact, 0);
		dest.writeList(permissionList);
		dest.writeArray(permissionRating);
		dest.writeList(expertRating);
		dest.writeFloat(totalExpertRating);
		dest.writeList(nonExpertRating);
		dest.writeFloat(totalNonExpertRating);
	}

	private void readFromParcel(Parcel in) {
		appCompact = in.readParcelable(AppCompact.class.getClassLoader());
		permissionList = new ArrayList<Permission>();
		in.readTypedList(permissionList, Permission.CREATOR);
		// TODO: fix for two dimensional array
		// permissionRating = in.readArray(Integer.class.getClassLoader());
		expertRating = (ArrayList<Integer>) in.readSerializable();
		totalExpertRating = in.readFloat();
		nonExpertRating = (ArrayList<Integer>) in.readSerializable();
	}

	// this is used to regenerate your object. All Parcelables must have a
	// CREATOR that implements these two methods
	public static final Parcelable.Creator<AppExtended> CREATOR = new Parcelable.Creator<AppExtended>() {
		public AppExtended createFromParcel(Parcel in) {
			return new AppExtended(in);
		}

		public AppExtended[] newArray(int size) {
			return new AppExtended[size];
		}
	};

	// example constructor that takes a Parcel and gives you an object populated
	// with it's values
	private AppExtended(Parcel in) {
		readFromParcel(in);
	}

}
