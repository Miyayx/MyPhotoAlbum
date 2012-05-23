package my.photoalbum.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import my.photoalbum.ImageAdapter3;
import my.photoalbum.PhotoLoadTask;
import my.photoalbum.R;
import my.photoalbum.R.id;
import my.photoalbum.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class MyPhotoList extends Activity {
	/** Called when the activity is first created. */

	private PhotoLoadTask task;
	private ImageAdapter3 imageAdapter;
	private List<String> photoPath;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_gridview);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getBundleExtra("path");
		String albumPath = bundle.getString("album_path");
		GridView gv = (GridView) findViewById(R.id.gridview);

		photoPath = getImageFromAlbum(albumPath);
		imageAdapter = new ImageAdapter3(this,photoPath);
		gv.setAdapter(imageAdapter);
		gv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent intent = new Intent(MyPhotoList.this,
						UploadPhotoActivity.class);
				Bundle b = new Bundle();			
				b.putString("photo_path", photoPath.get(position));
				intent.putExtra("path", b);
				MyPhotoList.this.startActivity(intent);
			}
		});

		for (int i = 0; i < photoPath.size(); i++) {
			task = new PhotoLoadTask(this, imageAdapter);
			task.execute(photoPath.get(i));
		}

	}

	private List<String> getImageFromAlbum(String filePath) {
		List<String> it = new ArrayList<String>();
		File f = new File(filePath);
		File[] files = f.listFiles();
		if (files == null)
			return it;

		for (int i = files.length - 1; i >= 0; i--) {
			File file = files[i];
			String newFilePath = file.getPath();
			if (isImageFile(newFilePath)) {
				it.add(newFilePath);
			}
		}
		return it;
	}

	private boolean isImageFile(String fName) {
		boolean re;
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		if (end.endsWith("jpg") || end.endsWith("png") || end.endsWith("bmp")
				|| end.endsWith("jpeg") || end.endsWith("gif"))
			re = true;
		else
			re = false;

		return re;
	}

}