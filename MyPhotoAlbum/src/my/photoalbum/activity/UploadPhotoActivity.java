package my.photoalbum.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import my.photoalbum.R;
import my.photoalbum.decoder.AlbumIconDecoder;
import my.photoalbum.decoder.ThumbnailDecoder;
import my.photoalbum.photo.SharedPhoto;
import my.photoalbum.sinaweibo.WeiboChooseDailog;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class UploadPhotoActivity extends Activity {
	EditText photoDescribe;
	Button uploadBtn;
	ImageView uploadImage;
	WeiboChooseDailog wDialog;
	String photoPath;
	String description;

	String ALBUM_PATH = "/sdcard/myAlbum/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getBundleExtra("path");
		photoPath = bundle.getString("photo_path");
		description = bundle.getString("caption");

		photoDescribe = (EditText) findViewById(R.id.upload_describe);
		if (description.length() > 0)
			photoDescribe.setText(description);
		uploadBtn = (Button) findViewById(R.id.upload_btn);
		uploadImage = (ImageView) findViewById(R.id.upload_image);

		AlbumIconDecoder decoder = new AlbumIconDecoder();		
		Bitmap bm = decoder.decode(photoPath);
		
		uploadImage.setImageBitmap(bm);

		uploadBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String new_description = photoDescribe.getText().toString();
				if(TextUtils.isEmpty(new_description)){
					Toast.makeText(UploadPhotoActivity.this, "描述不能为空", Toast.LENGTH_LONG).show();
					return;
				}
				//String newPhotoPath = createNewPhoto(photoPath);
				SharedPhoto sp = new SharedPhoto(photoPath, new_description);
				new WeiboChooseDailog(UploadPhotoActivity.this, R.style.weibo_choose_dialog,sp).show();
				
				photoDescribe.setText("");
			}
		});

	}

	private String createNewPhoto(String path) {
		File dirFile = new File(ALBUM_PATH);
		String fileName = (new File(path)).getName();
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		try {
			File myCaptureFile = new File(ALBUM_PATH + fileName);
			myCaptureFile.createNewFile();
			FileOutputStream fout = new FileOutputStream(myCaptureFile);
			ThumbnailDecoder decoder = new ThumbnailDecoder();
			Bitmap bm = decoder.decode(path);
			bm.compress(Bitmap.CompressFormat.JPEG, 80, fout);
			fout.flush();
			fout.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return ALBUM_PATH + fileName;
	}

}
