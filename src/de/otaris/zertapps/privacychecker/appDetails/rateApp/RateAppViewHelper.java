package de.otaris.zertapps.privacychecker.appDetails.rateApp;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import de.otaris.zertapps.privacychecker.R;
import de.otaris.zertapps.privacychecker.appDetails.AppDetailsActivity;
import de.otaris.zertapps.privacychecker.appDetails.Detail;
import de.otaris.zertapps.privacychecker.appDetails.DetailViewHelper;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.totalPrivacyRating.TotalPrivacyRating;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;

/*
 * Inflates the layout containing the elements to rate a app.
 * Button "Jetzt Bewerten"
 * (click) -> open overlay "Rate App":
 * 				- Rate permissions
 * 				- Total Rating > locks
 * 				- Button "Senden"
 * 
 * (click) -> close overlay "Rate App"
 * 		   -> open overlay "Rating saved"
 * 				- Permission rating saved in database
 * 				- Lock rating saved in  database
 */
public class RateAppViewHelper extends DetailViewHelper {

	protected Button rateAppButton;

	protected void initializeViews(View contextView) {
		rateAppButton = (Button) contextView
				.findViewById(R.id.app_detail_rate_app_button);
	}

	@Override
	public View getView(Context context, ViewGroup parent, Detail detail) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.app_detail_rate_app, parent,
				false);

		initializeViews(rowView);

		rateAppButton.setTag(detail.getApp());
		rateAppButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Create overlay
				RelativeLayout overlay = (RelativeLayout) v.getRootView()
						.findViewById(R.id.app_detail_overlay);
				overlay.setVisibility(View.VISIBLE);
				LayoutInflater inflater = LayoutInflater.from(v.getContext());
				RelativeLayout layout = (RelativeLayout) inflater.inflate(
						R.layout.app_detail_rate_app_overlay, overlay, false);
				overlay.addView(layout);

				// Get all the rating elements to be shown in the rating
				// overlay.
				ArrayList<RatingElement> ratingElements = getRatingElements((AppExtended) v
						.getTag());
				ArrayAdapter<RatingElement> adapter = new RatingElementListAdapter(
						v.getContext(), ratingElements);
				ListView ratingElementListView = (ListView) overlay
						.findViewById(R.id.app_detail_rate_app_overlay_list);
				ratingElementListView.setAdapter(adapter);

				Button sendRatingButton = (Button) overlay
						.findViewById(R.id.app_detail_rate_app_overlay_send);
				sendRatingButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						ArrayList<String> errors = new ArrayList<String>();

						Registry reg = Registry.getInstance();
						ArrayList<RatingElement> ratingElements = reg
								.getRatingElements();
						// iterate over all rating elements
						for (RatingElement element : ratingElements) {
							// validate and save
							try {
								element.validate();
								element.save(v.getContext());
							} catch (RatingValidationException e) {
								errors.add((String) v.getResources().getText(
										e.getValidationErrorId()));
							}
						}

						if (errors.size() > 0) {
							// Create a message for a successful transmission
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(
									new ContextThemeWrapper(v.getContext(),
											R.style.AlertDialogCustom));
							String title = v.getResources().getText(
									R.string.validation_error_intro)
									+ "\n";
							for (String error : errors) {
								title += "- " + error + "\n";
							}

							alertDialog.setTitle(title);
							alertDialog.setIcon(R.drawable.ic_launcher);
							alertDialog.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// if this button is clicked, just
											// close the dialog box and do
											// nothing
											dialog.cancel();
										}
									});
							alertDialog.show();
						} else {

							// close overlay
							((AppDetailsActivity) v.getContext())
									.hideOverlay(v);

							// Create a message for a successful transmission
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(
									new ContextThemeWrapper(v.getContext(),
											R.style.AlertDialogCustom));
							alertDialog.setTitle(v.getResources().getText(
									R.string.app_detail_rate_app_success));
							alertDialog.setIcon(R.drawable.ic_launcher);
							alertDialog.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// if this button is clicked, just
											// close the dialog box and do
											// nothing
											dialog.cancel();
										}
									});
							alertDialog.show();
						}
					}
				});

			}

		});

		return rowView;
	}

	private ArrayList<RatingElement> getRatingElements(AppExtended app) {
		Registry registry = Registry.getInstance();

		// second argument determines if rating element is mandatory
		registry.addRatingElement(new TotalPrivacyRating(app, true));
		// TODO: add further...

		return registry.getRatingElements();
	}
}
