package my.photoalbum.album;

import java.io.File;

import my.photoalbum.ImageNameFilter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

public class LocalAlbumInfo extends AlbumInfo{
	private static final String TAG = LocalAlbumInfo.class.getSimpleName();
	
	public LocalAlbumInfo(String name,String bitmap,int num){	
		super(name,BitmapFactory.decodeFile(bitmap),num);
	}
	
	public LocalAlbumInfo(String albumAddress){
		super();
		Log.d(TAG, "LocalAlbumInfo(String albumAddress)");	
		albumPath = albumAddress;
		albumName = albumAddress.substring(
				albumAddress.lastIndexOf("/")+1,
				albumAddress.length());
		String firstPhotoPath = getFirstImage(albumAddress);
		albumBitmap = iconDecoder.zoomImage(iconDecoder.decode(firstPhotoPath));
		photoNum = getPhotoNum(albumAddress);
	}
	
	private String getFirstImage(String albumAddress){
		File f = new File(albumAddress);
		ImageNameFilter filter = new ImageNameFilter();
		String[] filenames = f.list(filter );
		return albumAddress+"/"+filenames[0];
	}
	
	private int getPhotoNum(String albumAddress){
		File f = new File(albumAddress);
		ImageNameFilter filter = new ImageNameFilter();
		String[] filenames = f.list(filter );
		
		return filenames.length;
	}
	
}
