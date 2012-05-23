package my.photoalbum.activity;

import my.photoalbum.R;
import my.photoalbum.renren.WebAlbumListActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity{
	
	Button webBtn;
	Button localBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		webBtn = (Button)findViewById(R.id.web_albums);
		localBtn = (Button)findViewById(R.id.local_albums);
		
		webBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, WebAlbumListActivity.class);
				MainActivity.this.startActivity(intent);
				
			}
		});
		
		localBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, PhotoAlbumListActivity.class);

				MainActivity.this.startActivity(intent);
				
			}
		});
	}

}
