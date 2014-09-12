package de.otaris.zertapps.privacychecker;

import java.io.ByteArrayOutputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class IconController {

	public static Bitmap byteArrayToBitmap(byte[] bytes) {

		Bitmap bitMapImage = BitmapFactory.decodeByteArray(bytes, 0,
				bytes.length);

		return bitMapImage;
	}

	public static byte[] drawableToByteArray(Drawable d) {
		BitmapDrawable bitDw = ((BitmapDrawable) d);

		Bitmap bitmap = bitDw.getBitmap();

		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

		return stream.toByteArray();
	}

	public static Drawable byteArrayToDrawable(Resources res, byte[] icon) {
		Bitmap bitmap = IconController.byteArrayToBitmap(icon);
		return new BitmapDrawable(res, bitmap);
	}
}
