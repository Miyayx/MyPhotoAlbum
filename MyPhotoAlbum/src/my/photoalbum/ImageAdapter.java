package my.photoalbum;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class ImageAdapter extends BaseAdapter {
	private final String TAG = ImageAdapter.class.getSimpleName();
	private LayoutInflater inflater;
	private Context mContext;
	private ArrayList<Bitmap> photoItems;
	Map<String, SoftReference<Bitmap>> imageCache;

	public ImageAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
		photoItems = new ArrayList<Bitmap>();
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
	}

	public void clear() {
		photoItems.clear();
		notifyDataSetChanged();
	}

	public void add(Bitmap bean) {
		photoItems.add(bean);
	}
	public void add(String path, Bitmap bm){
		imageCache.put(path, new SoftReference<Bitmap>(bm));
	}

	@Override
	public int getCount() {

		return photoItems.size();
	}

	@Override
	public Object getItem(int position) {
		return photoItems.get(position);
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
		holder.image.setImageBitmap(photoItems.get(position));
		return convertView;
	}

	static class ViewHolder {
		ImageView image;
	}

}
