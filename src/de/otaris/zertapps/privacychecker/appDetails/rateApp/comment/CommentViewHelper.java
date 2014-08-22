package de.otaris.zertapps.privacychecker.appDetails.rateApp.comment;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElement;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElementViewHelper;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.totalPrivacyRating.TotalPrivacyRating;

public class CommentViewHelper extends RatingElementViewHelper {

	protected EditText editText;
	protected TextView textView;
	//max Value of character input for comment
	protected int maxValue = 255; 

	protected void initializeViews(View contextView) {
		editText = (EditText) contextView
				.findViewById(R.id.app_detail_rate_app_comment_textfield);
		textView = (TextView) contextView
				.findViewById(R.id.app_detail_rate_app_comment_character_amount);
	}

	@Override
	public View getView(Context context, ViewGroup parent,
			RatingElement ratingElement) {

		if (!(ratingElement instanceof Comment))
			throw new IllegalArgumentException(
					"Illegal Detail Object. Expected Comment.");
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.app_detail_rate_app_comment,
				parent, false);

		initializeViews(rowView);
		//display 255 (maxValueofChar)
		textView.setText(String.valueOf(maxValue));
		
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
				// show rest of possible characters
				textView.setText(String.valueOf(maxValue-s.length()));
				

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				

			}
		});
		;

		return rowView;
	}

}
