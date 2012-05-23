package my.photoalbum;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

public class PhotoInfo {
	private static final String TAG = PhotoInfo.class.getSimpleName();
	private Bitmap thumbnail;
	private String path;
	private static int thumbnailWidth = 100;
	private static int  thumbnailHeight = 100;
	
	public PhotoInfo(String path) {
		Log.d(TAG, "PhotoInfo(String path) path = "+path);
		this.path = path;
		//this.thumbnail = zoomImage(BitmapFactory.decodeFile(path),100,100);
		this.thumbnail = zoomImage2(path);
	}
	
	public Bitmap zoomImage(Bitmap bgimage, int newWidth, int newHeight) {
		Log.d(TAG, "zoomImage");

		int width = bgimage.getWidth();
		int height = bgimage.getHeight();

		int imageLength = Math.min(width, height);
		int x = (width - imageLength) / 2;
		int y = (height - imageLength) / 2;
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) newWidth) / imageLength;
		float scaleHeight = ((float) newHeight) / imageLength;
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, x, y, imageLength,
				imageLength, matrix, true);
		return bitmap;
	}
	
	public Bitmap zoomImage2(String path){
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 16;
		try {
			options.inJustDecodeBounds = true;
			options.outWidth = 0;
			options.outHeight = 0;
			options.inSampleSize = 1;
			
			BitmapFactory.decodeFile(path, options);			
			if (options.outWidth > 0 && options.outHeight > 0) {
				// Now see how much we need to scale it down.
				int widthFactor = (options.outWidth + thumbnailWidth - 1) / thumbnailWidth;
				int heightFactor = (options.outHeight + thumbnailHeight - 1) / thumbnailHeight;
				widthFactor = Math.max(widthFactor, heightFactor);
				widthFactor = Math.max(widthFactor, 1);
				
				// Now turn it into a power of two.
				if (widthFactor > 1) {
					if ((widthFactor & (widthFactor-1)) != 0) {
						while ((widthFactor & (widthFactor-1)) != 0) {
							widthFactor &= widthFactor-1;
						}
						widthFactor <<= 1;
					}
				}
				options.inSampleSize = widthFactor;
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeFile(path, options);			
				if (bitmap != null) {
					bitmap = zoomImage(bitmap, thumbnailWidth, thumbnailHeight);
					return bitmap;
				}
			}
		} catch (Exception e) {
			// That's okay, guess it just wasn't a bitmap.
		}
		
		return bitmap;
	}

	public Bitmap getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Bitmap thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
}
