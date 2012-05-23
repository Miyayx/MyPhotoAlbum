package my.photoalbum;

import java.util.ArrayList;

import my.photoalbum.album.AlbumInfo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbumListAdapter extends BaseAdapter{
	
	private final String TAG = AlbumListAdapter.class.getSimpleName();

	private LayoutInflater inflater;
	private ArrayList<AlbumInfo> albumItems;
	
	public AlbumListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        albumItems = new ArrayList<AlbumInfo>();
    }
	
	public void clear() {
		albumItems.clear();
        notifyDataSetChanged();
    }
	
    public void add(AlbumInfo bean) {
    	albumItems.add(bean);
    }

	@Override
	public int getCount() {
		return albumItems.size();
	}

	@Override
	public AlbumInfo getItem(int position) {
		return albumItems.get(position);
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
        	convertView = inflater.inflate(R.layout.album_item, parent, false);
        	
        	holder = new ViewHolder();
        	holder.albumImage = (ImageView) convertView.findViewById(R.id.album_image);
        	holder.albumName = (TextView) convertView.findViewById(R.id.album_name);
        	holder.photoNum = (TextView)convertView.findViewById(R.id.photo_num);
        	convertView.setTag(holder);
        }else{
        	holder = (ViewHolder) convertView.getTag();
        }
        
        AlbumInfo bean = albumItems.get(position);
        holder.albumName.setText(bean.getAlbumName());
        holder.photoNum.setText(String.valueOf(bean.getPhotoNum()));
        holder.albumImage.setImageBitmap(bean.getAlbumBitmap());
        
		return convertView;
	}
	
	static class ViewHolder{
		ImageView albumImage;//图片
    	TextView albumName;//名称
    	TextView photoNum;
    }

}
