package de.otaris.zertapps.privacychecker.database;

/**
 * represents all similarities between AppCompact and AppExtended
 *
 */
public interface App {

	public int getId();

	public int getCategoryId();

	public String getVersion();

	public String getName();

	public String getLabel();

	public boolean getIsInstalled();
	
	public Long getTimestamp();

	public float getPrivacyRating();

	public float getFunctionalRating();
	
	public String getDescription();
	
	public void setId(int id);

	public void setCategoryId(int categoryId);

	public void setLabel(String label);

	public void setVersion(String version);

	public void setName(String name);

	public void setIsInstalled(boolean installed);

	public void setPrivacyRating(float privacyRating);

	public void setFunctionalRating(float functionalRating);

	public void setTimestamp(Long timestamp);
	
	public void setDescription(String description);


	
}
