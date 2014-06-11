package de.otaris.zertapps.privacychecker.test;

import com.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import de.otaris.zertapps.privacychecker.HomeActivity;
import de.otaris.zertapps.privacychecker.InstalledAppsActivity;

public class HomeActivityTest extends
		ActivityInstrumentationTestCase2<HomeActivity> {

	private HomeActivity hActivity;
	private Solo solo;

	public HomeActivityTest() {
		super(HomeActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// initialize Robotium solo
		solo = new Solo(getInstrumentation(), getActivity());

		setActivityInitialTouchMode(false);

		hActivity = getActivity();
	}

	public void testPreConditions() {
		assertTrue(hActivity != null);
		// check that we have the right activity
		solo.assertCurrentActivity("wrong activity", HomeActivity.class);
	}

	public void testNavigateToInstalledApps() {
		// check start activity
		solo.assertCurrentActivity("wrong activity", HomeActivity.class);

		// click installed apps button
		View button = solo
				.getView(de.otaris.zertapps.privacychecker.R.id.installed_apps_button);
		solo.clickOnView(button);

		// check target activity
		solo.assertCurrentActivity("wrong activity",
				InstalledAppsActivity.class);
	}

	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
}
