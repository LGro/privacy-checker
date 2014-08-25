package de.otaris.zertapps.privacychecker.appDetails.comment;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
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
	protected int  numberOfCommentsToShow;

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

		CommentAdapter adapter = new CommentAdapter(context, comments);
		//set Number of Comments that are displayed
		numberOfCommentsToShow= (comments.size()>3) ? 3 : comments.size();
		//if there are not more than 3 comments, don not show show more button
		if (comments.size() < 4){
			showMoreButton.setVisibility(View.INVISIBLE);
		}
		commentListView.setAdapter(adapter);
		
		

		// show the first comments
		setListViewHeigthBasedOnChildren(commentListView, numberOfCommentsToShow);

		// set click listener
		showMoreButton
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton toggleButton,
							boolean isChecked) {

						ListView listView = (ListView) toggleButton
								.getRootView().findViewById(
										R.id.app_detail_comment_list);

						CommentAdapter adapter = (CommentAdapter) listView
								.getAdapter();

						if (isChecked) {

							// show total List of comments
							setListViewHeigthBasedOnChildren(listView,
									adapter.getCount());

						} else {
							// show the first comments
							setListViewHeigthBasedOnChildren(listView, numberOfCommentsToShow);
						}

					}
				});
		return rowView;
	}

	/**
	 * Method to scale a listView depending on the number of Elements you want
	 * to display
	 * 
	 * @param listView
	 * @param numberOfChildren
	 */

	private void setListViewHeigthBasedOnChildren(ListView listView,
			int numberOfChildren) {

		CommentAdapter adapter = (CommentAdapter) listView.getAdapter();
		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.AT_MOST);

		for (int i = 0; i < numberOfChildren; i++) {
			View listItem = adapter.getView(i, null, listView);
			listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (adapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();

	}
}
