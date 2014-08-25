package de.otaris.zertapps.privacychecker.appDetails.comment;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.database.model.Comment;

/**
 * Adapter that handles the headlines of comments (date + time)
 *
 */
public class CommentAdapter extends ArrayAdapter<Comment> {

	private final Context context;
	private final List<Comment> values;

	public CommentAdapter(Context context, List<Comment> values) {
		super(context, R.layout.app_detail_comment_item, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.app_detail_comment_item,
				parent, false);

		// get views from layout
		TextView contentView = (TextView) rowView
				.findViewById(R.id.app_detail_comment_item_content);
		TextView versionView = (TextView) rowView
				.findViewById(R.id.app_detail_comment_item_version);
		TextView dateView = (TextView) rowView
				.findViewById(R.id.app_detail_comment_item_date);

		// get Date
		Date date = new Date((long) values.get(position).getTimestamp() * 1000L);
		Log.i("commentAdapter", date + "");
		String stringDate = date + "";
		String[] parts = stringDate.split(" ");
		stringDate = parts[2] + "." + parts[1] + " " + parts[5];

		// set the apropriate informationen to the views
		dateView.setText(stringDate);
		versionView.setText(values.get(position).getVersion() + "");
		contentView.setText(values.get(position).getContent() + "");

		// rowView.setTag(values.get(position).getId());
		return rowView;
	}
}
