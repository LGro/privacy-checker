package de.otaris.zertapps.privacychecker.database.interfaces;

/**
 * represents all similarities between AppCompact and AppExtended
 *
 */
public interface App {

	public int getId();

	public void setId(int id);

	public byte[] getIcon();

	public void setIcon(byte[] icon);

	public int getCategoryId();

	public void setCategoryId(int categoryId);

	public String getVersion();

	public void setVersion(String version);

	public String getName();

	public void setName(String name);

	public String getLabel();

	public void setLabel(String label);

	public boolean isInstalled();

	public void setInstalled(boolean installed);

	public Long getTimestamp();

	public void setTimestamp(Long timestamp);

	public float getPrivacyRating();

	public void setPrivacyRating(float rating);

	public float getFunctionalRating();

	public void setFunctionalRating(float rating);

	public String getDescription();

	public void setDescription(String description);

	public float getAutomaticRating();

	public void setAutomaticRating(float rating);

	public float getAutomaticRatingRelativeToCategory();

	public void setAutomaticRatingRelativeToCategory(float rating);

}
