package de.otaris.zertapps.privacychecker.database.dataSource;

import java.util.List;

import de.otaris.zertapps.privacychecker.appsList.AppsListOrderCriterion;
import de.otaris.zertapps.privacychecker.database.interfaces.App;

public interface AppDataSource<T extends App> {

	public T getAppById(int appId);

	public List<T> getInstalledApps(AppsListOrderCriterion order);

	public List<T> getAppsByCategory(int categoryId,
			AppsListOrderCriterion order);

	public List<T> getAllApps(AppsListOrderCriterion order);

	public List<T> getLastUpdatedApps(int n);

	public T update(T app);
}
