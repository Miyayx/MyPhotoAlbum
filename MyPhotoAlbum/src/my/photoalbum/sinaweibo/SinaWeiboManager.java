package my.photoalbum.sinaweibo;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.weibo.net.AccessToken;
import com.weibo.net.AsyncWeiboRunner;
import com.weibo.net.AsyncWeiboRunner.RequestListener;
import com.weibo.net.DialogError;
import com.weibo.net.Utility;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;
import com.weibo.net.WeiboParameters;

import my.photoalbum.R;
import my.photoalbum.datamanager.UserInfo;
import my.photoalbum.photo.SharedPhoto;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

public class SinaWeiboManager extends WeiboManager {

	private Context mContext;
	private Weibo weibo;
	private SharedPhoto sharedPhoto;
	
	public SinaWeiboManager(Context context) {
		super(context, WeiboManager.WEIBO_TYPE_SINA);
		mContext = context;
		weibo = Weibo.getInstance();
		weibo.setupConsumerConfig(WeiboParam.CONSUMER_KEY,
				WeiboParam.CONSUMER_SECRET);
		weibo.setRedirectUrl("http://www.baidu.com");		
		if (!haveSavedUser()) {		
				
		} else {
			UserInfo userinfo = getSimpleUserInfoFromDB();
			AccessToken accessToken = new AccessToken(userinfo.getToken(), userinfo.getTokenSecret());
			accessToken.setExpiresIn(userinfo.getExpiresIn());
			weibo.setAccessToken(accessToken);
			if(!weibo.isSessionValid())
				weibo.setAccessToken(null);
		}
	}

	@Override
	protected UserInfo getUserInfoFromJSon(JSONObject json) {
		UserInfo user = new UserInfo();
		try {
			String userScreenName = json.getString("screen_name");
			String user_id = json.getString("id");
			user.setUserID(Long.valueOf(user_id));
			user.setScreenName(userScreenName);
			user.setKind(WeiboManager.WEIBO_TYPE_SINA);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}
	

	public  void  uploadLocalPhotoToWeibo(SharedPhoto sp) {
		this.sharedPhoto = sp;
	
		if(weibo.getAccessToken() ==null){
			weibo.authorize((Activity) mContext, new AuthDialogListener());		
		} else
			try {
				upload(weibo, Weibo.getAppKey(), sharedPhoto.getPhotoPath(), sharedPhoto.getText(),"","");
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private String upload(Weibo weibo, String source, String file,
			String status, String lon, String lat) throws WeiboException {
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", source);
		bundle.add("pic", file);
		bundle.add("status", status);
		if (!TextUtils.isEmpty(lon)) {
			bundle.add("lon", lon);
		}
		if (!TextUtils.isEmpty(lat)) {
			bundle.add("lat", lat);
		}
		String rlt = "";
		String url = Weibo.SERVER + "statuses/upload.json";
		AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(weibo);
		weiboRunner.request(mContext, url, bundle, Utility.HTTPMETHOD_POST,
				new WeiboRequestListener());

		return rlt;
	}

	class WeiboRequestListener implements RequestListener {

		@Override
		public void onComplete(String response) {
			((Activity) mContext).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(mContext, R.string.send_sucess,
							Toast.LENGTH_LONG).show();
					File photofile = new File(sharedPhoto.getPhotoPath());
					photofile.delete();					
				}
			});
			((Activity) mContext).finish();
		}

		@Override
		public void onIOException(IOException e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onError(final WeiboException e) {
			((Activity) mContext).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(
							mContext,
							String.format(
									mContext.getString(R.string.send_failed)
											+ ":%s", e.getMessage()),
							Toast.LENGTH_LONG).show();
				}
			});

		}

	}

	class AuthDialogListener implements WeiboDialogListener {

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			AccessToken accessToken = new AccessToken(token,
					WeiboParam.CONSUMER_SECRET);
			accessToken.setExpiresIn(expires_in);
			Weibo myWeibo = Weibo.getInstance();
			myWeibo.setAccessToken(accessToken);

			String user_id;

			try {
				WeiboParameters weiboPara = new WeiboParameters();
				weiboPara.add("source", Weibo.getAppKey());
				weiboPara.add("access_token", token);
				String account_url = "https://api.weibo.com/2/account/get_uid.json";
				String user_id_json = myWeibo.request(mContext, account_url,
						weiboPara, "GET", accessToken);
				JSONObject user_id_data = new JSONObject(user_id_json);
				user_id = user_id_data.getString("uid");
				weiboPara.add("uid", user_id);
				String userUrl = "https://api.weibo.com/2/users/show.json";
				String userinfo_json = myWeibo.request(mContext, userUrl,
						weiboPara, "GET", accessToken);
				JSONObject userinfo_data = new JSONObject(userinfo_json);

				UserInfo user = getUserInfoFromJSon(userinfo_data);
				user.setToken(accessToken.getToken());
				user.setTokenSecret(accessToken.getSecret());
				user.setKind(WeiboManager.WEIBO_TYPE_SINA);
				user.setExpiresIn(expires_in);
				saveUser(user);
			
				upload(weibo, Weibo.getAppKey(), sharedPhoto.getPhotoPath(), sharedPhoto.getText(),"","");
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
		}

		@Override
		public void onError(DialogError e) {
			Toast.makeText(mContext, "Auth error : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(mContext, "Auth cancel", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(mContext, "Auth exception : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}

	}
	
	
}
