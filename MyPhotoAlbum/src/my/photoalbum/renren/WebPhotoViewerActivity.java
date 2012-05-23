package my.photoalbum.renren;

import java.util.List;

import my.photoalbum.activity.UploadPhotoActivity;
import my.photoalbum.decoder.ThumbnailDecoder;
import my.photoalbum.view.ImageGridView;

import org.apache.http.client.HttpClient;

import com.footmark.utils.cache.FileCache;
import com.footmark.utils.image.ImageManager;
import com.renren.api.connect.android.photos.AlbumBean;
import com.renren.api.connect.android.photos.PhotoBean;
import com.renren.api.connect.android.photos.PhotoGetResponseBean;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;

public class WebPhotoViewerActivity extends Activity{

	private static final int REFRESH = 0;
	private static final int SCROLLTO = 1;

	AsyncTask<String, Integer, Long> at;
	List<PhotoBean> photoList;;
	
	HttpClient http = ImageManager.defaultHttpClient();
	ImageManager mgr;
	ImageGridView igv;

	Handler hdl = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH:
				if (at != null)
					at.cancel(false);
				igv.clear();

				for (PhotoBean b: photoList)
					igv.append(b.getUrlHead());
				
				break;
			case SCROLLTO:
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getBundleExtra("photo_beans");
		PhotoGetResponseBean photoGetResponseBean= bundle.getParcelable("photo_beans");
		photoList = photoGetResponseBean.getPhotos();
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		ThumbnailDecoder.dm(metrics);

		mgr = new ImageManager(ThumbnailDecoder.class, new FileCache(
				getCacheDir()), http);
		igv = new ImageGridView(this, mgr);

		setContentView(igv);

		igv.setCacheColorHint(0);
		igv.setVisibility(View.VISIBLE);
		igv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1, int position,
					long id) {
				Intent intent = new Intent(WebPhotoViewerActivity .this,
						UploadPhotoActivity.class);
				Bundle b = new Bundle();			
				b.putString("photo_path", photoList.get((int) id).getUrlLarge());
				b.putString("caption", photoList.get((int) id).getCaption());
				intent.putExtra("path", b);
				WebPhotoViewerActivity.this.startActivity(intent);				
			}
			
		});
		
		hdl.sendEmptyMessage(0);

		configureMedia();

	}

	public void onDestroy() {
		if (at != null)
			at.cancel(true);
		unregisterReceiver(mReceiver1);
		mgr.shutdown();
		http.getConnectionManager().shutdown();
		super.onDestroy();
	}



	BroadcastReceiver mReceiver1;

	public void configureMedia() {

		mReceiver1 = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
					hdl.sendEmptyMessage(0);
				}
			}

		};

		IntentFilter ift = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
		ift.addDataScheme("file");
		registerReceiver(mReceiver1, ift);

	}
	
}
