package de.otaris.zertapps.privacychecker;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import de.otaris.zertapps.privacychecker.database.Category;
import de.otaris.zertapps.privacychecker.database.CategoryDataSource;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class CategoryList extends ListFragment {

	private Activity rootActivity;

	@Inject
	private CategoryDataSource categoryDataSource = null;

	public CategoryList() {
	}

	public void setRootActivity(Activity rootActivity) {
		this.rootActivity = rootActivity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get all categories from database
		CategoryDataSource categoryData = new CategoryDataSource(rootActivity);
		categoryData.open();
		List<Category> categories = categoryData.getAllCategories();
		categoryData.close();

		// initialize category list item adapter
		CategoryListItemAdapter adapter = new CategoryListItemAdapter(rootActivity, categories);
		
		setListAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_fragment, container, false);
	}

	@Override
	public void onListItemClick(ListView list, View v, int position, long id) {
		Intent intent = new Intent(rootActivity, AppsByCategoryActivity.class);
		
		// put id from rowView tag as extra to the AppsByCategoryActivity
		intent.putExtra("id", (Integer)v.getTag());
		
		startActivity(intent);
	}
}