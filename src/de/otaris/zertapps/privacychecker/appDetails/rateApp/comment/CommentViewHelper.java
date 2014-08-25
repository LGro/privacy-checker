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
import de.otaris.zertapps.privacychecker.appDetails.rateApp.Registry;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.permissionsExpected.PermissionsExpected;
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
		
		// get comment from registry
		Registry reg = Registry.getInstance();
		Comment commentElement = (Comment) reg
				.getRatingElement(Comment.class);
		editText.setText(commentElement.getComment());
		
		//set cursor to end of text
		editText.setSelection(editText.getText().length());
		
		//display rest of possible characters
		textView.setText(String.valueOf(maxValue - commentElement.getComment().length()));
		
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
				// display rest of possible characters
				textView.setText(String.valueOf(maxValue - s.length()));
				
				//save comment in registry
				Registry reg = Registry.getInstance();
				Comment commentElement = (Comment) reg
						.getRatingElement(Comment.class);
				commentElement.setComment(String.valueOf(s));
				reg.updateRatingElement(Comment.class,
						commentElement);
				
				editText.setSelection(s.length());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) { 
				editText.setSelection(s.length());
				
			}

			@Override
			public void afterTextChanged(Editable s) {

				editText.setSelection(s.length());
					
			}
		});
		;

		return rowView;
	}

}
