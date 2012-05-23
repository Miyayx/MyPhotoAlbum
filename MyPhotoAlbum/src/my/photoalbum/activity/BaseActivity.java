package my.photoalbum.activity;

import my.photoalbum.R;
import my.photoalbum.renren.WebAlbumListActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class BaseActivity extends Activity{
	protected RelativeLayout root;
	protected Button photoalbum;
	protected Button upload;
	protected Button takephoto;
	protected Button setting;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.base_layout);
		root = (RelativeLayout) findViewById(R.id.base_view);
		photoalbum = (Button) findViewById(R.id.ic_photoalbum);
		upload = (Button) findViewById(R.id.ic_upload);
		takephoto = (Button) findViewById(R.id.ic_takephoto);
		setting = (Button) findViewById(R.id.ic_setting);
		
		photoalbum.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(BaseActivity.this, WebAlbumListActivity.class);
				BaseActivity.this.startActivity(intent);
			}
		});
		
		upload.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(BaseActivity.this, PhotoAlbumListActivity.class);
				BaseActivity.this.startActivity(intent);
			}
		});
		
		takephoto.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(BaseActivity.this, CameraActivity.class);
				BaseActivity.this.startActivity(intent);
			}
		});
		
		setting.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				//ToDo
			}
		});
		
	}
}