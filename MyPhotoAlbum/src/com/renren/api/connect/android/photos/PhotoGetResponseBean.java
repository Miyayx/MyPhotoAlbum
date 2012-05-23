package com.renren.api.connect.android.photos;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.common.ResponseBean;

import android.os.Parcel;
import android.os.Parcelable;

public class PhotoGetResponseBean extends ResponseBean implements Parcelable{

	
	private List<PhotoBean> photos = new ArrayList<PhotoBean>();

	public PhotoGetResponseBean() {
		super(null);
	}

	public PhotoGetResponseBean(String response) {
		this(response, "json");
	}

	/**
	 * 构造函数，通过请求返回的字符串构造返回对象
	 * 
	 * @param response
	 * @param format
	 */
	public PhotoGetResponseBean(String response, String format) {
		super(response);

		if (response == null) {
			return;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

		if (format.toLowerCase().endsWith("json")) {
			try {
				JSONArray array = new JSONArray(response);
				int size = array.length();
				JSONObject object = null;
				for (int i = 0; i < size; i++) {
					object = array.optJSONObject(i);
					if (object != null) {
						PhotoBean photo = new PhotoBean();
						photo.setAid(object.optLong("aid"));
						photo.setCaption(object.optString("caption"));
						photo.setCommentCount(object.optInt("comment_count"));
						photo.setUid(object.optLong("uid"));
						photo.setCreateTime(sdf.parse(object
									.optString("time")));
						photo.setPid(object.optLong("pid"));
						photo.setUrlHead(object.optString("url_head"));
						photo.setUrlTiny(object.optString("url_tiny"));
						photo.setUrlLarge(object.optString("url_large"));
						photo.setViewCount(object.optInt("view_count"));
						
						photos.add(photo);
					}
				}
			} catch (JSONException e) {
				Util.logger("exception in parsing json data: " + e.getMessage());
			} catch (ParseException e) {
				Util.logger("exception in parsing json data: " + e.getMessage());
			}
		}

	}

	public List<PhotoBean> getPhotos() {
		return photos;
	}

	public void setPhotos(List<PhotoBean> photos) {
		if (photos != null) {
			this.photos = photos;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (PhotoBean photo : photos) {
			sb.append("\n").append(photo.toString()).append("\r\n");
		}

		return sb.toString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(photos);

	}

	public static final Parcelable.Creator<PhotoGetResponseBean> CREATOR = new Parcelable.Creator<PhotoGetResponseBean>() {
		public PhotoGetResponseBean createFromParcel(Parcel in) {
			return new PhotoGetResponseBean(in);
		}

		public PhotoGetResponseBean[] newArray(int size) {
			return new PhotoGetResponseBean[size];
		}
	};
	
	/**
	 * 序列化构造函数
	 * 
	 * @param in
	 */
	public PhotoGetResponseBean(Parcel in) {
		super(null);

		in.readTypedList(photos, PhotoBean.CREATOR);
	}

}
