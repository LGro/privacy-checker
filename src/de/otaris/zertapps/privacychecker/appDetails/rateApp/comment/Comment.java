package de.otaris.zertapps.privacychecker.appDetails.rateApp.comment;

import android.content.Context;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElement;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingValidationException;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.Registry;
import de.otaris.zertapps.privacychecker.database.dataSource.CommentDataSource;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;

/**
 * Enables the user to comment on the privacy friendliness of a given app.
 */
public class Comment extends RatingElement {

	protected String comment = "";

	public Comment(AppExtended app, boolean mandatory) {
		super(app, mandatory);
	}

	@Override
	public void validate() throws RatingValidationException {

	}

	@Override
	public void save(Context context) {

		// save comment without unnecessary spaces
		comment = comment.trim();

		// replace two or more line breaks with a single one
		comment = (comment.replaceAll("\n[\n]+", "\n"));

		// create comment if it isn't empty
		if (comment.length() > 0) {
			// get expert flag from registry
			Registry reg = Registry.getInstance();
			String isExpertString = reg
					.get("de.otaris.zertapps.privacychecker.appDetails.rateApp.expertMode",
							"isExpert");
			int isExpert = (isExpertString.equals("1")) ? 1 : 0;

			// get current unix timestamp
			long currentTimestamp = System.currentTimeMillis() / 1000;

			CommentDataSource commentData = new CommentDataSource(context);
			commentData.open();

			// create new comment
			commentData.createComment(comment, app.getVersion(),
					currentTimestamp, (isExpert == 1), app.getId());

			commentData.close();
		}
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

}
