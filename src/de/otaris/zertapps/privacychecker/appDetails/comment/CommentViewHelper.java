package de.otaris.zertapps.privacychecker.appDetails.comment;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ToggleButton;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.Detail;
import de.otaris.zertapps.privacychecker.appDetails.DetailViewHelper;
import de.otaris.zertapps.privacychecker.database.dataSource.CommentDataSource;
import de.otaris.zertapps.privacychecker.database.model.Comment;

/**
 * Displays user Comments, by clicking the show more button all user comments
 * that are in the database are shown
 *
 */
public class CommentViewHelper extends DetailViewHelper {

	protected ListView commentListView;
	protected ToggleButton showMoreButton;

	@Override
	protected void initializeViews(View contextView) {
		commentListView = (ListView) contextView
				.findViewById(R.id.app_detail_comment_list);
		showMoreButton = (ToggleButton) contextView
				.findViewById(R.id.app_detail_comment_more_button);
	}

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

		initializeViews(rowView);

		/*
		 * Get the list of comments, create an adapter, set the adapter to the
		 * list
		 */
		ArrayList<Comment> comments = new ArrayList<Comment>();
		CommentDataSource commentData = new CommentDataSource(context);
		commentData.open();
		comments = commentData.getCommentsByAppId(detail.getApp().getId());

		CommentAdapter adapter = new CommentAdapter(context,
				context.getPackageManager(), comments);

		commentListView.setAdapter(adapter);

		// scale list depending on its size
		ViewGroup.LayoutParams updatedLayout = commentListView
				.getLayoutParams();
		final float scale = context.getResources().getDisplayMetrics().density;
		int pixels = (int) (49 * scale);
		updatedLayout.height = pixels * 2;
		commentListView.setLayoutParams(updatedLayout);

		// set click listener
		showMoreButton
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton toggleButton,
							boolean isChecked) {

						ListView listView = (ListView) toggleButton
								.getRootView().findViewById(
										R.id.app_detail_comment_list);
						ViewGroup.LayoutParams updatedLayout = listView
								.getLayoutParams();
						final float scale = toggleButton.getRootView()
								.getContext().getResources()
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
