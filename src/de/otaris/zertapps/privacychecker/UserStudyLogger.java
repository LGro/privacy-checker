package de.otaris.zertapps.privacychecker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;

public class UserStudyLogger {

	public static boolean LOGGING_ENABLED = false;

	private final String FILE_NAME;
	private static UserStudyLogger instance = null;

	protected UserStudyLogger() {
		FILE_NAME = "pca_navigation-log_" + System.currentTimeMillis() + ".txt";
	}

	public static UserStudyLogger getInstance() {
		if (instance == null) {
			instance = new UserStudyLogger();
		}
		return instance;
	}

	public UserStudyLogger(String message) {
		this();
		log(message);
	}

	public void log(String message) {
		if (!LOGGING_ENABLED) return;
		
		File logfile = new File(Environment.getExternalStorageDirectory(),
				FILE_NAME);

		if (!logfile.exists()) {
			try {
				logfile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			FileWriter fw = new FileWriter(logfile, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(message + "\n");
			bw.close();
		} catch (Exception e) {

		}
	}

	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

}
