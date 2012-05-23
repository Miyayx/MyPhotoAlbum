package my.photoalbum.renren;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.renren.api.connect.android.photos.AlbumBean;

import my.photoalbum.AlbumListAdapter;
import my.photoalbum.album.AlbumInfo;
import my.photoalbum.album.WebAlbumInfo;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class WebAlbumLoadTask extends AsyncTask<AlbumBean, AlbumInfo, Void> {
	
	private static final String TAG = WebAlbumLoadTask.class.getSimpleName();
    private Context mContext;
    private AlbumListAdapter mAdapter;
    
    public WebAlbumLoadTask(Context context, AlbumListAdapter adapter) {
    	mContext = context;
    	mAdapter = adapter;
	} 
    
	@Override
	protected Void doInBackground(AlbumBean... beans) {
		AlbumBean bean = beans[0];
		WebAlbumInfo album = new WebAlbumInfo(bean);	
		publishProgress(album);
		
		return null;
	}
	
	@Override
	protected void onProgressUpdate(AlbumInfo... values) {
		Log.i(TAG, "onProgressUpdate()");
    	if(isCancelled()) return;
    	
		AlbumInfo bean = values[0];
		mAdapter.add(bean);
		mAdapter.notifyDataSetChanged();
	}

}
