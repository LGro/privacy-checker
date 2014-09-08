package de.otaris.zertapps.privacychecker.appsList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import de.otaris.zertapps.privacychecker.R;

/**
 * Activity to display a list of categories with the help of CategoryList
 * ListFragment
 */
public class CategoryListActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set activity layout
		setContentView(R.layout.activity_category_list);

		// load fragment from CategoryList
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.categoryListContainer, new CategoryList(this))
				.commit();
	}

}
