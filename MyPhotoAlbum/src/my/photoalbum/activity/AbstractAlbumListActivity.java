package my.photoalbum.activity;

import java.io.IOException;

import my.photoalbum.AlbumListAdapter;
import my.photoalbum.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ListView;

public class AbstractAlbumListActivity extends BaseActivity{
	private static final String TAG = AbstractAlbumListActivity.class.getSimpleName();
	
	protected AlbumListAdapter albumAdapter;
	protected ListView albumList ;
	
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		albumList = new ListView(this);
		albumList.setCacheColorHint(0);
	//	albumList.setBackgroundResource(R.drawable.background_spring);
		root.addView(albumList);

		albumAdapter = new AlbumListAdapter(this);
		albumList.setAdapter(albumAdapter);
	}
	
	/**
	 * 显示等待框
	 */
	protected void showProgress() {
		showProgress("Please wait", "progressing");
	}

	/**
	 * 显示等待框
	 * 
	 * @param title
	 * @param message
	 */
	protected void showProgress(String title, String message) {
		progressDialog = ProgressDialog.show(this, title, message);
	}

	/**
	 * 取消等待框
	 */
	protected void dismissProgress() {
		if (progressDialog != null) {
			try {
				progressDialog.dismiss();
			} catch (Exception e) {

			}
		}
	}

}
