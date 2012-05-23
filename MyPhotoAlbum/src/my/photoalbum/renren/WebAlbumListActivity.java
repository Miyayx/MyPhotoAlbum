package my.photoalbum.renren;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.exception.RenrenAuthError;
import com.renren.api.connect.android.exception.RenrenException;
import com.renren.api.connect.android.photos.AlbumBean;
import com.renren.api.connect.android.photos.AlbumGetRequestParam;
import com.renren.api.connect.android.photos.AlbumGetResponseBean;
import com.renren.api.connect.android.photos.PhotoGetRequestParam;
import com.renren.api.connect.android.photos.PhotoGetResponseBean;
import com.renren.api.connect.android.view.RenrenAuthListener;

import my.photoalbum.activity.AbstractAlbumListActivity;
import my.photoalbum.activity.PhotoViewerActivity;
import my.photoalbum.album.AlbumInfo;

public class WebAlbumListActivity extends AbstractAlbumListActivity {

	private static  final int LOADALBUMS = 1;
	private static final int LOADPHOTOS = 2;
	Renren renren;	
	AlbumGetResponseBean albumGetResponseBean;
	PhotoGetResponseBean photoGetResponseBean;
	List<AlbumBean> albumBeanList;
	private Handler threadNotify;
	private WebAlbumLoadTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		renren = new Renren(RenrenParam.API_KEY, RenrenParam.SECRET_KEY,
				RenrenParam.APP_ID, this);
		renren.authorize(this, listener);
		
		threadNotify = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				int key = msg.what;
				switch (key) {
				case LOADALBUMS:
					WebAlbumListActivity.this.dismissProgress();
					albumBeanList = albumGetResponseBean.getAlbums();
					if (albumBeanList.size() > 0) {
						for (AlbumBean bean : albumBeanList) {
							task = new WebAlbumLoadTask(WebAlbumListActivity.this,
									albumAdapter);
							task.execute(bean);
						}
					}
					break;

				case LOADPHOTOS:
					WebAlbumListActivity.this.dismissProgress();
					
					Intent intent = new Intent(WebAlbumListActivity.this,
							WebPhotoViewerActivity.class);
					Bundle b = new Bundle();
					b.putParcelable("photo_beans", photoGetResponseBean);
					intent.putExtra("photo_beans", b);
					WebAlbumListActivity.this.startActivity(intent);
					
					break;
				default:
					break;
				}
			
			}
		};

		

		albumList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				LoadWebPhotoInfo runnable = new LoadWebPhotoInfo(((AlbumInfo)arg0.getItemAtPosition(position)));
				Thread photoLoadThread= new Thread(runnable);
				WebAlbumListActivity.this.showProgress();
				photoLoadThread.start();
				
			}
		});

	}

	class LoadWebAlbumInfo implements Runnable {

		@Override
		public void run() {
			Log.i("LoadWebAlbumInfo", this.toString()+"is running");
			long userId = renren.getCurrentUid();
			AlbumGetRequestParam albumGetRequestParam = new AlbumGetRequestParam();
			albumGetRequestParam.setUid(userId);
			try {
				try {
					albumGetResponseBean = renren.getAlbums(albumGetRequestParam);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Message msg = new Message();
			msg.what = LOADALBUMS;
			threadNotify.sendMessage(msg);

		}

	}
	
	class LoadWebPhotoInfo implements Runnable{

		AlbumInfo albumInfo;
		public LoadWebPhotoInfo(AlbumInfo albumInfo) {
			this.albumInfo = albumInfo;
		}

		@Override
		public void run() {
			Log.i("LoadWebPhotoInfo", this.toString()+"is running");
			long userId = renren.getCurrentUid();
			long albumId = albumInfo.getAlbumId();
			PhotoGetRequestParam photoGetRequestParam = new PhotoGetRequestParam();
			photoGetRequestParam.setUid(userId);
			photoGetRequestParam.setAids(albumId);
			try {
				try {
					photoGetResponseBean = renren.getPhotos(photoGetRequestParam);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Message msg = new Message();
			msg.what = LOADPHOTOS ;
			threadNotify.sendMessage(msg);
			
		}
		
	}

	RenrenAuthListener listener = new RenrenAuthListener() {

		@Override
		public void onComplete(Bundle values) {
			Log.d("test", values.toString());
			Toast.makeText(WebAlbumListActivity.this, "onComplete",
					Toast.LENGTH_LONG);
			Log.d("renren", renren.getSessionKey());
			LoadWebAlbumInfo runnable = new LoadWebAlbumInfo();
			Thread albumLoadThread = new Thread(runnable);

			WebAlbumListActivity.this.showProgress();
			albumLoadThread.start();
		}

		@Override
		public void onRenrenAuthError(RenrenAuthError renrenAuthError) {
			Toast.makeText(WebAlbumListActivity.this, "onRenrenAuthError",
					Toast.LENGTH_LONG);
		}

		@Override
		public void onCancelLogin() {
			Toast.makeText(WebAlbumListActivity.this, "onCancelLogin",
					Toast.LENGTH_LONG);
		}

		@Override
		public void onCancelAuth(Bundle values) {
			Toast.makeText(WebAlbumListActivity.this, "onCancelAuth",
					Toast.LENGTH_LONG);
		}

	};

}
