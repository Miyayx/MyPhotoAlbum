package my.photoalbum.sinaweibo;

import java.util.List;

import org.json.JSONObject;

import my.photoalbum.datamanager.DataHelper;
import my.photoalbum.datamanager.UserInfo;
import android.content.Context;

public abstract class WeiboManager {

	public static final String WEIBO_TYPE_SINA = "sina";
	public static final String WEIBO_TYPE_Q = "qweibo";

	protected Context mContext;
	protected String weiboType;

	public WeiboManager(Context context, String weiboType) {
		mContext = context;
		this.weiboType = weiboType;
	}

	protected boolean haveSavedUser() {
		Boolean b = false;
		DataHelper dbHelper = new DataHelper(mContext);
		b = dbHelper.HaveSuchKindUser(weiboType);
		dbHelper.Close();
		return b;
	}

	protected void saveUser(UserInfo user) {
		DataHelper dataHelper = new DataHelper(mContext);
		dataHelper.SaveUserInfo(user);
		dataHelper.Close();
	}

	protected UserInfo getUserInfoFromJSon(JSONObject json) {
		return new UserInfo();
	}

	protected UserInfo getSimpleUserInfoFromDB() {
		DataHelper dataHelper = new DataHelper(mContext);
		List<UserInfo> userList = dataHelper.GetUserList(weiboType, true);
		dataHelper.Close();
		if (userList.isEmpty()) {// 如果为空说明第一次使用跳到 AuthorizeActivity页面进行
			return null;
		} else {
			return userList.get(0);
		}

	}
}
