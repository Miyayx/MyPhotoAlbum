package my.photoalbum.activity;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;

import my.photoalbum.decoder.ThumbnailDecoder;
import my.photoalbum.view.ImageGridView;

import org.apache.http.client.HttpClient;

import com.footmark.utils.cache.FileCache;
import com.footmark.utils.image.ImageManager;

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
import android.widget.ArrayAdapter;

public class PhotoViewerActivity extends BaseActivity {

	private static final int REFRESH = 0;
	private static final int SCROLLTO = 1;
	File imgDir;
	AsyncTask<String, Integer, Long> at;
	Stack<File> history = new Stack<File>();

	ArrayList<CharSequence> subfolders = new ArrayList<CharSequence>();

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
				File[] files = getImageFileList(imgDir);
				if (files == null) {
					return;
				}

				for (File f : files)
					igv.append(f.getAbsolutePath());

				File[] subdirs = getSubDirList(imgDir);

				subfolders.clear();

				subfolders.add(imgDir.getName());

				for (File f : subdirs) {
					subfolders.add(f.getName());
				}
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
		Bundle bundle = intent.getBundleExtra("path");
		String albumPath = bundle.getString("album_path");

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		ThumbnailDecoder.dm(metrics);

		mgr = new ImageManager(ThumbnailDecoder.class, new FileCache(
				getCacheDir()), http);
		igv = new ImageGridView(this, mgr);

		root.addView(igv);

		igv.setVisibility(View.VISIBLE);

		imgDir = new File(albumPath);

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

	private File[] getImageFileList(File path) {

		File[] mDirectoryFiles = path.listFiles(new FileFilter() {

			public boolean accept(File file) {
				return (!file.isHidden() && !file.isDirectory() && (

				file.getPath().endsWith(".png")
						|| file.getPath().endsWith(".jpg")
						|| file.getPath().endsWith(".bmp")
						|| file.getPath().endsWith(".jpeg") || false));
			}

		});

		if (mDirectoryFiles == null)
			return null;

		if (mDirectoryFiles.length > 0)
			Arrays.sort(mDirectoryFiles, new Comparator<File>() {

				public int compare(File src, File target) {
					return src.getName().compareTo(target.getName());
				}

			});

		return mDirectoryFiles;

	}

	private File[] getSubDirList(File path) {

		File[] mDirectoryFiles = path.listFiles(new FileFilter() {

			public boolean accept(File file) {
				return (!file.isHidden() && file.isDirectory()
						&& file.list() != null && file.canRead());
			}

		});

		if (mDirectoryFiles == null)
			return null;

		if (mDirectoryFiles.length > 0)
			Arrays.sort(mDirectoryFiles, new Comparator<File>() {

				public int compare(File src, File target) {
					return src.getName().compareTo(target.getName());
				}

			});

		return mDirectoryFiles;

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
