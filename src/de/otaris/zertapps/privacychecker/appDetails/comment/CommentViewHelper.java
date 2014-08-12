package de.otaris.zertapps.privacychecker.appDetails.comment;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.Detail;
import de.otaris.zertapps.privacychecker.appDetails.DetailViewHelper;
import de.otaris.zertapps.privacychecker.database.dataSource.CommentDataSource;
import de.otaris.zertapps.privacychecker.database.model.Comment;

public class CommentViewHelper extends DetailViewHelper {

	@Override
	public View getView(Context context, ViewGroup parent, Detail detail)
			throws IllegalArgumentException {

		if (!(detail instanceof de.otaris.zertapps.privacychecker.appDetails.comment.Comment))
			throw new IllegalArgumentException(
					"Illegal Detail Object. Expected Comment.");

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.app_detail_comment, parent,
				false);

		/*
		 * Get the list of comments, create an adapter, set the adapter to the list
		 */
		ArrayList<Comment> comments = new ArrayList<Comment>();
		CommentDataSource commentData = new CommentDataSource(context);
		commentData.open();
		comments = commentData.getCommentsByAppId(detail.getApp().getId());
		// TODO: remove added comments here
		comments.add(new Comment(83, "Hallo", "1. Version", 846, detail
				.getApp().getId()));
		comments.add(new Comment(42, "Was soll das hier?", "700. Version",
				1786876999, detail.getApp().getId()));
		comments.add(new Comment(3,
				"Was hfusgakdinehaudkfleuanskdoeuwendiezwoeneo das hier?",
				"700. Version", 23, detail.getApp().getId()));
		commentData.close();

		ListView listView = (ListView) rowView
				.findViewById(R.id.app_detail_comment_list);
		CommentAdapter adapter = new CommentAdapter(context,
				context.getPackageManager(), comments);

		listView.setAdapter(adapter);

		
		 // scale list depending on its size
		  ViewGroup.LayoutParams updatedLayout = listView.getLayoutParams();
		  final float scale = context.getResources().getDisplayMetrics().density; 
		  int pixels = (int) (49 * scale); 
		  updatedLayout.height = pixels * 2; 
		  listView.setLayoutParams(updatedLayout);
		 

		// get "show more" button
		ToggleButton showMoreButton = (ToggleButton) rowView
				.findViewById(R.id.app_detail_comment_more_button);
		// set click listener
		showMoreButton
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton toggleButton,
							boolean isChecked) {

						ListView listView = (ListView) toggleButton.getRootView().findViewById(R.id.app_detail_comment_list);
						ViewGroup.LayoutParams updatedLayout = listView
								.getLayoutParams();
						final float scale = toggleButton.getRootView().getContext().getResources()
								.getDisplayMetrics().density;
						int pixels = (int) (49 * scale);

						if (isChecked) {
							updatedLayout.height = pixels * listView.getCount();
							listView.setLayoutParams(updatedLayout);
						} else {
							updatedLayout.height = pixels * 2;
							listView.setLayoutParams(updatedLayout);
						}

					}
				});
		return rowView;
	}

}
