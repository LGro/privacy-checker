package de.otaris.zertapps.privacychecker.appDetails.comment;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.database.model.Comment;

public class CommentAdapter extends ArrayAdapter<Comment>{
	
	private final Context context;
	private final List<Comment> values;
	private final PackageManager pm;

	public CommentAdapter(Context context, PackageManager pm,
			List<Comment> values) {
		super(context, R.layout.app_detail_comment_item, values);
		this.context = context;
		this.values = values;
		this.pm = pm;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.app_detail_comment_item, parent, false);

		// get views from layout
		TextView contentView = (TextView) rowView
				.findViewById(R.id.app_detail_comment_item_content);
		TextView versionView = (TextView) rowView
				.findViewById(R.id.app_detail_comment_item_version);
		TextView dateView = (TextView) rowView
				.findViewById(R.id.app_detail_comment_item_date);
		
		// set comment title
		dateView.setText(values.get(position).getDate() + "");
		//rowView.setTag(values.get(position).getId());
		
		versionView.setText(values.get(position).getVersion() + "");
		contentView.setText(values.get(position).getContent() + "");
		return rowView;
	}
}
