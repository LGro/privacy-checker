package de.otaris.zertapps.privacychecker.appDetails.comment;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

		View rowView = inflater.inflate(R.layout.app_detail_comment,
				parent, false);
		
		ArrayList<Comment> comments = new ArrayList<Comment>();
		CommentDataSource commentData = new CommentDataSource(context);
		commentData.open();
		comments = commentData.getCommentsByAppId(detail.getApp().getId());
		comments.add(commentData.createComment("Hallo", "1. Version", 846, detail.getApp().getId()));
		commentData.close();
				
		ListView listView = (ListView) parent.findViewById(R.id.app_detail_list);
		CommentAdapter adapter = new CommentAdapter(context, context.getPackageManager() ,comments);
		
		listView.setAdapter(adapter);
		return rowView;
	}

}
