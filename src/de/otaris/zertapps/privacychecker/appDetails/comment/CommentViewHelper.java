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
 * Displays first three comments and a show more button to display all comments.
 * Each comment has date, app version and text.
 */
public class CommentViewHelper extends DetailViewHelper {

	protected ListView commentListView;
	protected ToggleButton showMoreButton;
	protected int numberOfCommentsToShow;

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

		// get comments for app
		ArrayList<Comment> comments = new ArrayList<Comment>();
		CommentDataSource commentData = new CommentDataSource(context);
		commentData.open();
		comments = commentData.getCommentsByAppId(detail.getApp().getId());
		commentData.close();

		// set adapter
		CommentAdapter adapter = new CommentAdapter(context, comments,
				detail.getApp());
		commentListView.setAdapter(adapter);

		if (comments.size() > 3) {
			// set maximum number of comments to 3
			numberOfCommentsToShow = 3;

			// set click listener to show more than 3 comments
			showMoreButton
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(
								CompoundButton toggleButton, boolean isChecked) {

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
								setListViewHeigthBasedOnChildren(listView,
										numberOfCommentsToShow);
							}

						}
					});
		} else {
			// if there are not more than 3 comments, don not show show more
			// button
			showMoreButton.setVisibility(View.INVISIBLE);

			// set Number of Comments that are displayed
			numberOfCommentsToShow = comments.size();

		}

		// show the first comments
		setListViewHeigthBasedOnChildren(commentListView,
				numberOfCommentsToShow);

		return rowView;
	}

	/**
	 * Scale a listView depending on the number of elements you want to display.
	 * 
	 * @param listView
	 * @param numberOfElements
	 *            the number of list elements to be displayed at maximum
	 */
	private void setListViewHeigthBasedOnChildren(ListView listView,
			int numberOfElements) {

		// get adapter from list view
		CommentAdapter adapter = (CommentAdapter) listView.getAdapter();
		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.AT_MOST);

		// accumulate total height by measuring each list element
		for (int i = 0; i < numberOfElements; i++) {
			View listItem = adapter.getView(i, null, listView);
			listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}

		// update list layout to maximum height
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (adapter.getCount() - 1));
		listView.setLayoutParams(params);

		// notify list view about layout changes
		listView.requestLayout();

	}
}
