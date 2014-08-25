package de.otaris.zertapps.privacychecker.appDetails.rateApp.comment;

import android.content.Context;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingElement;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.RatingValidationException;
import de.otaris.zertapps.privacychecker.appDetails.rateApp.Registry;
import de.otaris.zertapps.privacychecker.database.dataSource.CommentDataSource;
import de.otaris.zertapps.privacychecker.database.model.AppExtended;

public class Comment extends RatingElement {

	protected String comment = "";

	public Comment(AppExtended app, boolean mandatory) {
		super(app, mandatory);
	}

	@Override
	public boolean validate() throws RatingValidationException {
		if (!mandatory) {
			return true;
		}

		// else exception
		return false;
	}

	@Override
	public void save(Context context) {

		CommentDataSource commentData = new CommentDataSource(context);
		commentData.open();

		Registry reg = Registry.getInstance();
		String isExpertString = reg
				.get("de.otaris.zertapps.privacychecker.appDetails.rateApp.expertMode",
						"isExpert");
		int isExpert = (isExpertString.equals("1")) ? 1 : 0;

		// Get current timestamp
		long currentTimestamp = System.currentTimeMillis() / 1000;

		// save comment without unnecessary spaces
		comment = comment.trim();

		// check if comment content is not empty
		if (comment.length() > 0) {

			// create new commentData
			commentData.createComment(comment, app.getVersion(),
					currentTimestamp, (isExpert == 1), app.getId());
		}
		// else do not create new commentData

		commentData.close();
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

}
