package de.otaris.zertapps.privacychecker.appDetails.comment;

import java.util.Calendar;
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

import java.util.Date;

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

	@SuppressWarnings("deprecation")
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
		//get Date
		Date date = new Date((long) values.get(position).getDate());
		String stringDate = date + "";
		String[] parts = stringDate.split(" ");
		stringDate = parts[2] + "." + parts[1] + " " + parts[5] + " " + parts[3];
		// set comment title 
		dateView.setText(stringDate);
		//rowView.setTag(values.get(position).getId());
		
		versionView.setText(values.get(position).getVersion() + "");
		contentView.setText(values.get(position).getContent() + "");
		return rowView;
	}
}
