package my.photoalbum;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;

public class PhotoLoadTask extends AsyncTask<String, PhotoInfo, Void>{
	private static final String TAG = PhotoLoadTask.class.getSimpleName();
	private static int thumbnailWidth = 100;
    private static int thumbnailHeight = 100;
    private Context context;
  //  private ImageAdapter adapter;
    private ImageAdapter3 adapter;
    
    public PhotoLoadTask(Context context,ImageAdapter3 adapter) {
		this.context = context;
		this.adapter = adapter;
	} 
    
	@Override
	protected Void doInBackground(String... path) {
		
//		Bitmap bm = zoomImage(
//				BitmapFactory.decodeFile(path[0]),
//				thumbnailWidth, thumbnailHeight);
		//PhotoInfo photo = new PhotoInfo(path[0]);		
		publishProgress(new PhotoInfo(path[0]));
		return null;
	}

	@Override
	protected void onPreExecute() {
		Log.i(TAG, "onPreExecute()");
		adapter.clear();
	}

	@Override
	protected void onProgressUpdate(PhotoInfo... bitmap) {
		Log.i(TAG, "onProgressUpdate()");
    	if(isCancelled()) return;
    	
    	PhotoInfo bm= bitmap[0];
		adapter.add(bm);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}


	public Bitmap zoomImage(Bitmap bgimage, int newWidth, int newHeight) {

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

}
