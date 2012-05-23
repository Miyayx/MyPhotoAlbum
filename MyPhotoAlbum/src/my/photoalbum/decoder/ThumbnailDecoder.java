package my.photoalbum.decoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import com.footmark.utils.image.decoder.BitmapDecodeException;
import com.footmark.utils.image.decoder.BitmapDecoder;

public class ThumbnailDecoder extends BitmapDecoder {
	
	public static final long MAX_WIDTH_DEFAULT = 256;
	public static final long MAX_HEIGHT_DEFAULT = 256;

	public static long MAX_WIDTH = MAX_WIDTH_DEFAULT;
	public static long MAX_HEIGHT = MAX_HEIGHT_DEFAULT;
	
	public static void dm(DisplayMetrics m) {
		MAX_WIDTH = (long) (MAX_WIDTH_DEFAULT * m.densityDpi / DisplayMetrics.DENSITY_HIGH);
		MAX_HEIGHT = (long) (MAX_HEIGHT_DEFAULT * m.densityDpi / DisplayMetrics.DENSITY_HIGH);
	}

	@Override
	public Bitmap decode(String path) {
		
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY / 2);
		
		Bitmap rtn = null;
		
		try{
			
		    BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;      
		    BitmapFactory.decodeFile(path, options);
	
		    long ratio = Math.min(options.outWidth/MAX_WIDTH, 
		    		options.outHeight/MAX_HEIGHT);
		    int sampleSize = Integer.highestOneBit((int)Math.floor(ratio));
		    if(sampleSize == 0){
		        sampleSize = 1;
		    }
	
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			opts.inSampleSize = sampleSize;
			
			rtn = BitmapFactory.decodeFile(path, opts);
		
		}catch(OutOfMemoryError oom){
			System.gc();
		}
		
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
		
		if(rtn == null)
			throw new BitmapDecodeException();
		
		return rtn;
	}

}
