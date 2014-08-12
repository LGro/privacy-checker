package de.otaris.zertapps.privacychecker.database.dataSource;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import de.otaris.zertapps.privacychecker.database.DatabaseHelper;
import de.otaris.zertapps.privacychecker.database.model.Comment;

/**
 * Handles requests concerning Comments to the database.
 */
public class CommentDataSource extends DataSource<Comment> {

	private String[] allColumns = { Comment.ID, Comment.CONTENT,
			Comment.VERSION, Comment.DATE, Comment.APP_ID };

	public CommentDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	/**
	 * converts database result to comment object
	 * @param cursor
	 * @return cursor data as Comment object
	 */
	protected Comment cursorToModel(Cursor cursor) {
		Comment comment = new Comment();

		comment.setId(cursor.getInt(0));
		comment.setContent(cursor.getString(1));
		comment.setVersion(cursor.getString(2));
		comment.setDate(cursor.getLong(3));
		comment.setAppId(cursor.getInt(4));
		
		return comment;
	}

	/**
	 *  creates a comment 
	 * @param content
	 * @param version
	 * @param date
	 * @param appId
	 * @return the new Comment
	 */
	public Comment createComment(String content, String version,
			long date, int appId) {
		// set values for columns
		ContentValues values = new ContentValues();
		values.put(Comment.CONTENT, content);
		values.put(Comment.VERSION, version);
		values.put(Comment.DATE, date);
		values.put(Comment.APP_ID, appId);
		
		// insert into DB
		long insertId = database.insert(Comment.TABLE, null, values);

		// get recently inserted Comment by ID
		return getCommentById(insertId);
	}

	/**
	 * calls the method with parameter of type long
	 * @param commentId
	 * @return 
	 */
	public Comment getCommentById(int commentId) {
		return getCommentById((long) commentId);
	}

	/**
	 * returns comment by its id
	 * @param commentId
	 * @return comment
	 */
	protected Comment getCommentById(long commentId) {
		// build database query
		Cursor cursor = database.query(Comment.TABLE, allColumns,
				Comment.ID + " = " + commentId, null, null, null, null);
		cursor.moveToFirst();

		// convert to Comment object
		Comment newComment = cursorToModel(cursor);
		cursor.close();

		// return comment object
		return newComment;
	}


	
	/**
	 * returns all comments from an app 
	 * @param appId
	 * @return list of comments
	 */
	public ArrayList<Comment> getCommentsByAppId(int appId) {
		ArrayList<Comment> comments = new ArrayList<Comment>();

		// build query
		String whereClause = Comment.APP_ID + " = " + appId;

		Cursor cursor = database.query(Comment.TABLE, allColumns,
				whereClause, null, null, null, null);
		// put values in list
		
		return comments;
	}

	
}