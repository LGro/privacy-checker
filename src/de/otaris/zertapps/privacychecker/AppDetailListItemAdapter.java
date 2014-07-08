package de.otaris.zertapps.privacychecker;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AppDetailListItemAdapter extends ArrayAdapter<Detail> {
	private final Context context;
	private final List<Detail> values;

	public AppDetailListItemAdapter(Context context,
			List<Detail> values) {
		super(context, R.layout.app_detail_list_item, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		String detailViewClass = values.get(position).getClass().getName();
		String detailViewClassFullPath = detailViewClass + "ViewHelper";
		try {
			Class viewClass = Class.forName(detailViewClassFullPath);
			DetailViewHelper detailViewHelper = (DetailViewHelper) viewClass.newInstance();
			
			return detailViewHelper.getView(context, parent, values.get(position));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.e("AppDetailListItemAdapter", "Error: Detail View Class Not Found. Expected " + detailViewClassFullPath);
		return null;
	}
}
