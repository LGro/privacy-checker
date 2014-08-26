package de.otaris.zertapps.privacychecker.database.dataSource;

import java.util.List;

import de.otaris.zertapps.privacychecker.appsList.AppsListOrder;
import de.otaris.zertapps.privacychecker.database.interfaces.App;

public interface AppDataSource<T extends App> {

	public T getAppById(int appId);

	public List<T> getInstalledApps(AppsListOrder order, boolean ascending);

	public List<T> getAppsByCategory(int categoryId, AppsListOrder order,
			boolean ascending);

	public List<T> getAllApps(AppsListOrder order, boolean ascending);

	public List<T> getLastUpdatedApps(int n);

	public T updateAppById(int appId, int categoryId, String name,
			String label, String version, float privacyRating,
			boolean installed, float functionalRating, String description,
			byte[] icon, float automaticRating,
			float automaticRatingRelativeToCategory);
}
