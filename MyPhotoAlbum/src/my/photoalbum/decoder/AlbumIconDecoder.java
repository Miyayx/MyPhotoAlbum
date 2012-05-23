package my.photoalbum.decoder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;

import com.footmark.utils.image.decoder.BitmapDecodeException;
import com.footmark.utils.image.decoder.BitmapDecoder;

public class AlbumIconDecoder extends BitmapDecoder {
	private static final String TAG = AlbumIconDecoder.class.getSimpleName();

	public static final long MAX_WIDTH_DEFAULT = 120;
	public static final long MAX_HEIGHT_DEFAULT = 120;

	public static long MAX_WIDTH = MAX_WIDTH_DEFAULT;
	public static long MAX_HEIGHT = MAX_HEIGHT_DEFAULT;

	public static final String HTTP_PREFIX = "http://";

	public static void dm(DisplayMetrics m) {
		MAX_WIDTH = (long) (MAX_WIDTH_DEFAULT * m.densityDpi / DisplayMetrics.DENSITY_HIGH);
		MAX_HEIGHT = (long) (MAX_HEIGHT_DEFAULT * m.densityDpi / DisplayMetrics.DENSITY_HIGH);
	}

	@Override
	public Bitmap decode(String path) {

		Bitmap bm = null;
		if (path.toLowerCase().startsWith(HTTP_PREFIX)) {
			bm = decodeFromHttp(path);
		} else {
			System.out.println(path);
			bm = decodeFromSD(path);		
		}

		if (bm == null)
			throw new BitmapDecodeException();

		return bm;
	}

	private Bitmap decodeFromSD(String path) {
		Log.d(TAG, "decodeFromSD");
		Bitmap bm = null;

		try {

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);

			long ratio = Math.min(options.outWidth / MAX_WIDTH,
					options.outHeight / MAX_HEIGHT);
			int sampleSize = Integer.highestOneBit((int) Math.floor(ratio));
			if (sampleSize == 0) {
				sampleSize = 1;
			}

			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			opts.inSampleSize = sampleSize;

			bm = BitmapFactory.decodeFile(path, opts);
//			if(bm != null)
//				bm = zoomImage(bm);

		} catch (OutOfMemoryError oom) {
			System.gc();
		}

		Thread.currentThread().setPriority(Thread.NORM_PRIORITY);

		return bm;

	}

	private Bitmap decodeFromHttp(String src) {
		Log.d(TAG, "decodeFromHttp");
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
//			if(myBitmap!=null)
//			return zoomImage(myBitmap);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// public Bitmap compressImage(String path){
	// Log.i(TAG, "compressImage");
	// Bitmap bitmap = null;
	// BitmapFactory.Options options = new BitmapFactory.Options();
	// options.inSampleSize = 16;
	// try {
	// options.inJustDecodeBounds = true;
	// options.outWidth = 0;
	// options.outHeight = 0;
	// options.inSampleSize = 1;
	//
	// BitmapFactory.decodeFile(path, options);
	// if (options.outWidth > 0 && options.outHeight > 0) {
	// // Now see how much we need to scale it down.
	// int widthFactor = (options.outWidth + thumbnailWidth - 1) /
	// thumbnailWidth;
	// int heightFactor = (options.outHeight + thumbnailHeight - 1) /
	// thumbnailHeight;
	// widthFactor = Math.max(widthFactor, heightFactor);
	// widthFactor = Math.max(widthFactor, 1);
	//
	// // Now turn it into a power of two.
	// if (widthFactor > 1) {
	// if ((widthFactor & (widthFactor-1)) != 0) {
	// while ((widthFactor & (widthFactor-1)) != 0) {
	// widthFactor &= widthFactor-1;
	// }
	// widthFactor <<= 1;
	// }
	// }
	// options.inSampleSize = widthFactor;
	// options.inJustDecodeBounds = false;
	// bitmap = BitmapFactory.decodeFile(path, options);
	// if (bitmap != null) {
	// bitmap = zoomImage(bitmap, thumbnailWidth, thumbnailHeight);
	// return bitmap;
	// }
	// }
	// } catch (Exception e) {
	// // That's okay, guess it just wasn't a bitmap.
	// }
	//
	// return bitmap;
	// }
	//
	public  Bitmap zoomImage(Bitmap bgimage) {

		int width = bgimage.getWidth();
		int height = bgimage.getHeight();

		int imageLength = Math.min(width, height);
		int x = (width - imageLength) / 2;
		int y = (height - imageLength) / 2;
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) MAX_WIDTH) / imageLength;
		float scaleHeight = ((float) MAX_HEIGHT) / imageLength;
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, x, y, imageLength,
				imageLength, matrix, true);
		return bitmap;

	}

}
