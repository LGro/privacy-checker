package de.otaris.zertapps.privacychecker;

/*
 * This class defines a default app for testing purposes.
 */
public class TestApp {
	private String name;
	private int rating;
	private int iconID;
	
	
	public TestApp(String name, int rating, int iconID) {
		super();
		this.name = name;
		this.rating = rating;
		this.iconID = iconID;
	}


	public String getName() {
		return name;
	}


	public int getRating() {
		return rating;
	}


	public int getIconID() {
		return iconID;
	}
	
	
}
