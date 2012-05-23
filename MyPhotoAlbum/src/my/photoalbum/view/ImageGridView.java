package my.photoalbum.view;

import java.util.ArrayList;

import my.photoalbum.activity.UploadPhotoActivity;


import com.footmark.utils.image.ImageManager;
import com.footmark.utils.image.ImageSetter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

public class ImageGridView extends GridView {

	private FriendAdapter m_adapter = null;
	private final ImageManager mgr;
	private Context mContext;

	public ImageGridView(Context context, ImageManager mgr) {
		super(context);
		mContext = context;
		setNumColumns(3);
		this.mgr = mgr;
		this.setGravity(Gravity.CENTER);
		setAdapter(new FriendAdapter(context, 0,  new ArrayList<String>()));
		setListener();
		
	}

	private void setListener() {
		setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				Intent intent = new Intent(mContext,
						UploadPhotoActivity.class);
				Bundle b = new Bundle();			
				b.putString("photo_path", m_adapter.getItem(position));
				b.putString("caption", "");
				intent.putExtra("path", b);
				mContext.startActivity(intent);
				
			}
		});
		
	}

	public void setAdapter(ListAdapter adapter){
		this.m_adapter = (FriendAdapter) adapter;
		super.setAdapter(adapter);
	}

	Handler notify = new Handler() {

		public void handleMessage(Message msg) {
			m_adapter.notifyDataSetChanged();
		}

	};

	private void $$$$$$() {
		notify.removeMessages(0);
		notify.sendEmptyMessage(0);
	}

	public void zero(String uri){
		insert(uri, 0);
	}

	public void append(String uri){
		if(m_adapter==null) return;
		m_adapter.add(uri);
		$$$$$$();
	}

	public void insert(String uri, int pos){
		if(m_adapter==null) return;
		m_adapter.insert(uri, pos);
		$$$$$$();
	}

	public void remove(String uri){
		if(m_adapter==null) return;
		m_adapter.remove(uri);
		$$$$$$();
	}
	
	public void clear() {
		if(m_adapter==null) return;
		m_adapter.clear();
		$$$$$$();
	}
	
	public int itemheight() {
		return m_adapter.glo.height;
	}

	public class FriendAdapter extends ArrayAdapter<String> implements ListAdapter {

		GridView.LayoutParams glo;
		
		private ImageView create() {
			ImageView v;
			v = new ImageView(this.getContext());
			layout(v);
            v.setScaleType(ImageView.ScaleType.CENTER_CROP);
            v.setPadding(10, 10, 10, 10);
            return v;
		}
		
		private ImageView layout(ImageView v) {
			if(glo == null) {
				int w = (int)(ImageGridView.this.getWidth() / 3.5);
				if(w > 0) {
					glo = new GridView.LayoutParams(w, w);
					v.setLayoutParams(glo);
				}
			}else{
				v.setLayoutParams(glo);
			}
			return v;
		}
		
		public FriendAdapter(Context context, int textViewResourceId, ArrayList<String> items) {
			super(context, textViewResourceId, items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String uri = getItem(position);
			ImageView v;
			if(convertView == null){
				v = create();
			}else{
				v = (ImageView) convertView;
				layout(v);
				Object obj = v.getTag();
				if(obj instanceof ImageSetter) {
					ImageSetter s = ((ImageSetter) obj);
					if(uri.equals(s.image().tag())){
						return v;
					}else{
						s.cancel();
					}
				}
			}
			new AdvancedImageConnector(mgr, v, uri);
			return v;
		}
		
	}

}
