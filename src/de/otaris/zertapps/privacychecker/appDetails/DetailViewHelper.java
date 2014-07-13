package de.otaris.zertapps.privacychecker.appDetails;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;


public abstract class DetailViewHelper {
	public abstract View getView(Context context, ViewGroup parent, Detail detail) throws IllegalArgumentException;
}


