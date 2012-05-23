package my.photoalbum;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.footmark.utils.cache.MemCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter3 extends BaseAdapter {
	private final String TAG = ImageAdapter3.class.getSimpleName();
	private LayoutInflater inflater;
	private Context mContext;
	private List<String> paths;
	MemCache<Bitmap> imageCache;

	public ImageAdapter3(Context context, List<String> paths) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
		imageCache = new MemCache<Bitmap>();
		this.paths = paths;
	}

	public void clear() {
		notifyDataSetChanged();
	}

	public void add(PhotoInfo p){
		
		imageCache.put(p.getPath(), p.getThumbnail());
		Log.d(TAG, "add photpo:" + imageCache.size());
	}

	@Override
	public int getCount() {
		return imageCache.size();
	}

	@Override
	public Object getItem(int position) {
		return imageCache.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "position:" + position);
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.photo_item, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.photo_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String path = paths.get(position);
		Bitmap bm = imageCache.get(path);
		holder.image.setImageBitmap(bm);
		return convertView;
	}

	static class ViewHolder {
		ImageView image;
	}

}
