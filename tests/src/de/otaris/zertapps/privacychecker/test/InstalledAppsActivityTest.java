package de.otaris.zertapps.privacychecker.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.mockito.Mockito;

import roboguice.test.RobolectricRoboTestRunner;

import com.robotium.solo.Solo;

import de.otaris.zertapps.privacychecker.AppController;
import de.otaris.zertapps.privacychecker.HomeActivity;
import de.otaris.zertapps.privacychecker.InstalledAppsActivity;
import de.otaris.zertapps.privacychecker.database.dataSource.AppCompactDataSource;
import de.otaris.zertapps.privacychecker.database.interfaces.App;
import android.test.ActivityInstrumentationTestCase2;

//@RunWith(RobolectricTestRunner.class)
public class InstalledAppsActivityTest extends
		ActivityInstrumentationTestCase2<InstalledAppsActivity> {

	private InstalledAppsActivity iaActivity;
	private Solo solo;

	public InstalledAppsActivityTest() {
		super(InstalledAppsActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		solo = new Solo(getInstrumentation(), getActivity());

		setActivityInitialTouchMode(false);

		iaActivity = getActivity();
		
//
//		AppDataSource appDataSourceMock = Mockito.mock(AppDataSource.class);
//		
//		App app1 = new App();
//		app1.setLabel("Test App 1");
//		app1.setRating(1);
//
//		App app2 = new App();
//		app1.setLabel("Test-App-2");
//		app1.setRating(2);
//
//		App app3 = new App();
//		app1.setLabel("Test Abb 3");
//		app1.setRating(3);
//		
//		List<App> appList = new ArrayList<App>();
//		appList.add(app1);
//		appList.add(app2);
//		appList.add(app3);
//		
//		//Mockito.when(appDataSourceMock.getInstalledApps()).thenReturn(appList);
//		Mockito.doReturn(appList).when(appDataSourceMock).getInstalledApps();
//		
//		iaActivity.setAppDataSource(appDataSourceMock);
	}

	public void testPreConditions() {
		assertTrue(iaActivity != null);
		// check that we have the right activity
	    solo.assertCurrentActivity("wrong activity", InstalledAppsActivity.class);
	}
	
//	public void testListView() {
//		solo.searchText("Test App 1");
//		solo.searchText("Test-App-2");
//		solo.searchText("Test Abb 3");
//	}
	
	@Override
	  public void tearDown() throws Exception {
	    solo.finishOpenedActivities();
	  }

}
