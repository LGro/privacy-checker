package de.otaris.zertapps.privacychecker;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class PrivacyCheckerAlert {

	/**
	 * Custom Alert dialog in privacy-checker style.
	 * 
	 * @param title
	 * @param message
	 * @param context
	 */
	public static void callInfoDialog(String title, String message,
			final Context context) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.app_detail_alert_dialog);
		dialog.getWindow()
				.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

		TextView titleTextView = (TextView) dialog
				.findViewById(R.id.app_detail_alert_dialog_textview_title);
		titleTextView.setText(title);

		TextView messageTextview = (TextView) dialog
				.findViewById(R.id.app_detail_alert_dialog_textview_description);
		messageTextview.setText(message);

		Button okButton = (Button) dialog
				.findViewById(R.id.app_detail_alert_dialog_button_ok);
		okButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
}
