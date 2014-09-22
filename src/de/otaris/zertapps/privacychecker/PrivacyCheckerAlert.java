package de.otaris.zertapps.privacychecker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import de.otaris.zertapps.privacychecker.database.dataSource.PermissionDataSource;
import de.otaris.zertapps.privacychecker.database.model.Permission;
import de.otaris.zertapps.privacychecker.database.model.PermissionExtended;

public class PrivacyCheckerAlert {

	/**
	 * Custom Alert dialog in privacy-checker style.
	 * 
	 * @param title
	 * @param message
	 * @param context
	 * @param finishActivity
	 *            flag that indicates wheather to close the activity after
	 *            confirming the alert dialog
	 */
	

	public static void callPermissionDialogPermissionExtended(
			PermissionExtended permission, final Context context) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.app_detail_permission_alert_dialog);
		dialog.getWindow()
				.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

		TextView titleTextView = (TextView) dialog
				.findViewById(R.id.app_detail_permission_alert_dialog_textview_title);
		titleTextView.setText(permission.getPermission().getLabel());

		TextView messageTextview = (TextView) dialog
				.findViewById(R.id.app_detail_permission_alert_dialog_textview_description);
		messageTextview.setText(permission.getPermission().getDescription());

		// set the buttons to confirm oder decline understanding or cancel
		Button cancelButton = (Button) dialog
				.findViewById(R.id.app_detail_permission_alert_dialog_button_cancel);
		Button yesButton = (Button) dialog
				.findViewById(R.id.app_detail_permission_alert_dialog_button_yes);
		yesButton.setTag(permission.getPermission());
		Button noButton = (Button) dialog
				.findViewById(R.id.app_detail_permission_alert_dialog_button_no);
		noButton.setTag(permission.getPermission());

		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}

		});
		yesButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Context context = v.getContext();
				PermissionDataSource permissionData = new PermissionDataSource(
						context);
				permissionData.open();
				Permission permission = (Permission) v.getTag();
				permissionData.increaseCounterYes(permission);
				permissionData.close();
				dialog.dismiss();
			}

		});
		noButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Context context = v.getContext();
				PermissionDataSource permissionData = new PermissionDataSource(
						context);
				permissionData.open();
				Permission permission = (Permission) v.getTag();
				permissionData.increaseCounterNo(permission);
				permissionData.close();
				dialog.dismiss();
			}

		});
		dialog.show();
	}

	public static void callPermissionDialogPermission(Permission permission,
			final Context context) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.app_detail_permission_alert_dialog);
		dialog.getWindow()
				.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

		TextView titleTextView = (TextView) dialog
				.findViewById(R.id.app_detail_permission_alert_dialog_textview_title);
		titleTextView.setText(permission.getLabel());

		TextView messageTextview = (TextView) dialog
				.findViewById(R.id.app_detail_permission_alert_dialog_textview_description);
		messageTextview.setText(permission.getDescription());

		// set the buttons to confirm oder decline understanding or cancel
		Button cancelButton = (Button) dialog
				.findViewById(R.id.app_detail_permission_alert_dialog_button_cancel);
		Button yesButton = (Button) dialog
				.findViewById(R.id.app_detail_permission_alert_dialog_button_yes);
		yesButton.setTag(permission);
		Button noButton = (Button) dialog
				.findViewById(R.id.app_detail_permission_alert_dialog_button_no);
		noButton.setTag(permission);

		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}

		});
		yesButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Context context = v.getContext();
				PermissionDataSource permissionData = new PermissionDataSource(
						context);
				permissionData.open();
				Permission permission = (Permission) v.getTag();
				permissionData.increaseCounterYes(permission);
				permissionData.close();
				dialog.dismiss();
			}

		});
		noButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Context context = v.getContext();
				PermissionDataSource permissionData = new PermissionDataSource(
						context);
				permissionData.open();
				Permission permission = (Permission) v.getTag();
				permissionData.increaseCounterNo(permission);
				permissionData.close();
				dialog.dismiss();
			}

		});
		dialog.show();
	}
}
