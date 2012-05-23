package my.photoalbum;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import my.photoalbum.album.AlbumInfo;
import my.photoalbum.album.LocalAlbumInfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class AlbumLoadTask extends AsyncTask<String, AlbumInfo, List<String>> {
	private static final String TAG = AlbumLoadTask.class.getSimpleName();

	private Context mContext;
	private AlbumListAdapter adapter;
	private AlbumLoadTask task;

	public AlbumLoadTask(Context context, AlbumListAdapter adapter) {
		Log.d(TAG, "AlbumLoadTask");

		this.mContext = context;
		this.adapter = adapter;
	}

	@Override
	protected void onPreExecute() {
		Log.d(TAG, "onPreExecute()");

		//adapter.clear();
	}

	@Override
	protected List<String> doInBackground(String... paths) {
		Log.i(TAG, "doInBackground()");
	
		String currentPath = paths[0];
		List<String> it = getSubAlbumPath(currentPath);
		
		if(haveImageFile(currentPath)){
		AlbumInfo ai = new LocalAlbumInfo(currentPath);
		publishProgress(ai);
		}
		
		return it;
	}

	@Override
	protected void onProgressUpdate(AlbumInfo... values) {
		Log.d(TAG, "onProgressUpdate()");
    	if(isCancelled()) return;
    	
		AlbumInfo bean = values[0];
		adapter.add(bean);
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onPostExecute(List<String> paths) {
		Log.d(TAG, "onPostExecute");
		if(paths.size() > 0){
			for(String path:paths){
				
				task = new AlbumLoadTask(mContext, adapter);
				task.execute(path);
			}
		}
	}
	
	private List<String> getSubAlbumPath(String filePath) {
		Log.d(TAG, "getSubAlbumPath");

		List<String> it = new ArrayList<String>();

		File f = new File(filePath);
		System.out.println("filepath = " + filePath);
		File[] subfiles = f.listFiles(new FolderFilter());

		if (subfiles.length == 0)
			return it;
		if(subfiles[0].getAbsolutePath().split("/").length > 4)
			return it;
	
		for (int i = 0; i < subfiles.length; i++) 
			it.add(subfiles[i].getAbsolutePath());
		
		return it;
	}
	
	private boolean haveImageFile(String filePath) {
		Log.d(TAG, "haveImageFile");

		ImageNameFilter imageFilter = new ImageNameFilter();
		
		File f = new File(filePath);
		String[] filesname = f.list(imageFilter);

		if(filesname.length > 0)
			return true;
		else
			return false;
		
	}

}
