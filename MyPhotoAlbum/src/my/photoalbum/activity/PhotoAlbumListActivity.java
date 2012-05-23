package my.photoalbum.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.photoalbum.AlbumListAdapter;
import my.photoalbum.AlbumLoadTask;
import my.photoalbum.FolderFilter;
import my.photoalbum.ImageNameFilter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class PhotoAlbumListActivity extends AbstractAlbumListActivity {
	private static final String TAG = PhotoAlbumListActivity.class
			.getSimpleName();
	private String rootPath = "/sdcard";
	private AlbumLoadTask task;
	private List<String> albumPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		checkSD();
	
		task = new AlbumLoadTask(this, albumAdapter);
		task.execute(rootPath);

		albumList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// Intent intent = new Intent(PhotoAlbumListActivity.this,
				// MyPhotoList.class);
				Intent intent = new Intent(PhotoAlbumListActivity.this,
						PhotoViewerActivity.class);
				Bundle b = new Bundle();
				b.putString("album_path", albumAdapter.getItem(position)
						.getAlbumPath());
				intent.putExtra("path", b);
				PhotoAlbumListActivity.this.startActivity(intent);
			}
		});
	}

	private void checkSD() {
		Log.d(TAG, "checkSD()");
		try {

			String status = Environment.getExternalStorageState(); // 获取SD卡状态
			// SD卡处于插入状态，并被机器识别
			if (status.equals(Environment.MEDIA_MOUNTED)) {
				Toast.makeText(PhotoAlbumListActivity.this, "SD卡",
						Toast.LENGTH_SHORT);
			}
			// SD卡未插入，或已被移除
			else if (status.equals(Environment.MEDIA_REMOVED)
					|| status.equals(Environment.MEDIA_BAD_REMOVAL)) {

				new AlertDialog.Builder(this)
						.setTitle("警告")
						.setMessage("SD卡未被插入")
						.setPositiveButton("关闭",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										return;
									}
								}).show();
			}
			// 正在检查SD卡
			else if (status.equals(Environment.MEDIA_CHECKING)) {
				Thread.sleep(500); // 延迟
				status = Environment.getExternalStorageState(); // 继续获取SD卡状态
				this.checkSD(); // 重新调用checksd（）
			}
			// SD卡不被识别
			else {
				new AlertDialog.Builder(this)
						.setTitle("警告")
						.setMessage("SD卡不正确")
						.setPositiveButton("关闭",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										return;
									}
								}).show();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private List<String> getAlbumPathFromSD(String filePath) {
		Log.d(TAG, "getAlbumPathFromSD");

		int count = 0;
		List<String> it = new ArrayList<String>();

		it = getAlbumPathFromPath(filePath, count);
		return it;
	}

	private List<String> getAlbumPathFromPath(String filePath, int time) {
		Log.d(TAG, "getAlbumPathFromPath");

		int count = time;
		List<String> it = new ArrayList<String>();

		File f = new File(filePath);
		System.out.println("filepath = " + filePath);
		File[] subfiles = f.listFiles(new FolderFilter());

		if (count == 3)
			return it;
		if (subfiles == null)
			return it;

		if (haveImageFile(filePath)) {
			it.add(filePath);
			task = new AlbumLoadTask(this, albumAdapter);
			task.execute(filePath);
		}
		
		for (int i = 0; i < subfiles.length; i++) {

				File file = subfiles[i];
					List<String> newIt = getAlbumPathFromPath(file.getAbsolutePath(),
							count + 1);
					if (newIt.size() == 0)
						continue;
					it.addAll(newIt);
		}
		return it;
	}
	
//	private List<String> getAlbumPathFromPath(String filePath, int time) {
//		Log.d(TAG, "getAlbumPathFromPath");
//
//		int count = time;
//		List<String> it = new ArrayList<String>();
//
//		File f = new File(filePath);
//		System.out.println("filepath = " + filePath);
//		File[] files = f.listFiles();
//
//		if (count == 3)
//			return it;
//		if (filePath.contains("."))
//			return it;
//		if (files == null)
//			return it;
//
//		if (haveImageFile(filePath)) {
//			it.add(filePath);
//		}
//
//		for (int i = 0; i < files.length; i++) {
//			if (files[i].getAbsolutePath().contains("."))
//				continue;
//			else {
//				File file = files[i];
//				if (file.isDirectory()) {
//					List<String> newIt = getAlbumPathFromPath(file.getAbsolutePath(),
//							count + 1);
//					if (newIt.size() == 0)
//						continue;
//					it.addAll(newIt);
//				}
//			}
//		}
//		return it;
//	}

//	 private boolean haveImageFile(String filePath) {
//	 Log.d(TAG, "haveImageFile");
//	
//	 File f = new File(filePath);
//	 File[] files = f.listFiles();
//	
//	 if (files == null)
//	 return false;
//	
//	 for (int i = 0; i < files.length; i++) {
//	 File file = files[i];
//	 String fName = file.getPath();
//	 String end = fName.substring(fName.lastIndexOf(".") + 1,
//	 fName.length()).toLowerCase();
//	
//	 if (end.endsWith("jpg") || end.endsWith("png")
//	 || end.endsWith("bmp") || end.endsWith("jpeg")
//	 || end.endsWith("gif"))
//	 return true;
//	 }
//	 return false;
//	 }

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
