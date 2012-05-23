package my.photoalbum.album;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.renren.api.connect.android.photos.AlbumBean;

public class WebAlbumInfo extends AlbumInfo{

	public WebAlbumInfo(AlbumBean bean){
		Bitmap bm = iconDecoder.zoomImage(iconDecoder.decode(bean.getUrl()));
		albumId =bean.getAid();
		albumName = bean.getName();
		albumBitmap = bm;
		photoNum = bean.getSize();
		
	}

}
