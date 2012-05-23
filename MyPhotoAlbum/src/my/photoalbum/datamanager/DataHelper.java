package my.photoalbum.datamanager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class DataHelper {

	// 数据库名称
	private static String DB_NAME = "myweibo";
	// 数据库版本
	private static int DB_VERSION = 2;
	private SQLiteDatabase db;
	private SqliteHelper dbHelper;

	public DataHelper(Context context) {
		dbHelper = new SqliteHelper(context, DB_NAME, null, DB_VERSION);
		db = dbHelper.getWritableDatabase();
	}

	public void Close() {
		db.close();
		dbHelper.close();
	}

	// 获取users 表中的UserID、Access Token、Access Secret的记录
	public List<UserInfo> GetUserList(String kind, Boolean isSimple) {
		List<UserInfo> userList = new ArrayList<UserInfo>();
		Cursor cursor = db.query(SqliteHelper.TABLE_NAME, null, UserInfo.Kind
				+ "=" + "\"" + kind + "\"", null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			UserInfo user = new UserInfo();
			user.setUserID(cursor.getLong(0));
			user.setScreenName(cursor.getString(1));
			user.setToken(cursor.getString(3));
			user.setTokenSecret(cursor.getString(4));
			user.setExpiresIn(cursor.getLong(6));
			if (!isSimple) {
				ByteArrayInputStream stream = new ByteArrayInputStream(
						cursor.getBlob(5));
				Drawable icon = Drawable.createFromStream(stream, "image");
				user.setUserIcon(icon);
			}
			userList.add(user);
			cursor.moveToNext();
		}
		cursor.close();
		return userList;
	}

	// 判断users 表中的是否包含某个 UserID 的记录
	public Boolean HaveUserInfo(String UserID) {
		Boolean b = false;
		Cursor cursor = db.query(SqliteHelper.TABLE_NAME, null, UserInfo.USERID
				+ "=" + UserID, null, null, null, null);
		b = cursor.moveToFirst();
		Log.e("HaveUserInfo", b.toString());
		cursor.close();
		return b;
	}

	public Boolean HaveSuchKindUser(String kind) {
		Boolean b = false;
		Cursor cursor = db.query(SqliteHelper.TABLE_NAME, null, UserInfo.Kind
				+ "=" + "\"" + kind + "\"", null, null, null, null);
		b = cursor.moveToFirst();
		Log.e("HaveSuchKindUser", b.toString());
		cursor.close();
		return b;
	}

	// 更新users 表的记录，根据 UserId更新用户昵称和用户图标
	public int UpdateUserInfo(String userId, Bitmap userIcon) {
		ContentValues values = new ContentValues();
		values.put(UserInfo.USERID, userId);
		// BLOB 类型
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		// 将 Bitmap 压缩成PNG编码，质量为 100%存储
		userIcon.compress(Bitmap.CompressFormat.PNG, 100, os);
		// 构造 SQLite的 Content 对象，这里也可以使用 raw
		values.put(UserInfo.USERICON, os.toByteArray());
		int id = db.update(SqliteHelper.TABLE_NAME, values, UserInfo.USERID
				+ "=" + userId, null);
		Log.e("UpdateUserInfo2", id + "");
		return id;
	}

	// 更新users 表的记录
	public int UpdateUserInfo(UserInfo user) {
		ContentValues values = new ContentValues();
		values.put(UserInfo.USERID, user.getUserID());
		values.put(UserInfo.TOKEN, user.getToken());
		values.put(UserInfo.TOKENSECRET, user.getTokenSecret());
		int id = db.update(SqliteHelper.TABLE_NAME, values, UserInfo.USERID
				+ "=" + user.getUserID(), null);
		Log.e("UpdateUserInfo", id + "");
		return id;
	}

	// 添加users 表的记录
	public long SaveUserInfo(UserInfo user) {

		ContentValues values = new ContentValues();
		values.put(UserInfo.USERID, user.getUserID());
		values.put(UserInfo.TOKEN, user.getToken());
		values.put(UserInfo.TOKENSECRET, user.getTokenSecret());
		values.put(UserInfo.Kind, user.getKind());
		values.put(UserInfo.EXPIRESIN, user.getExpiresIn());
		values.put(UserInfo.SCREENNAME, user.getScreenName());

		String kind = user.getKind();
		long id;
		if (HaveSuchKindUser(kind)) {
			id = db.update(SqliteHelper.TABLE_NAME, values, UserInfo.Kind + "="
					+ "\"" + kind + "\"", null);
			Log.e("UpdateUserInfo", id + "");
		} else {
			id = db.insert(SqliteHelper.TABLE_NAME, null, values);
			Log.e("SaveUserInfo", id + "");
		}
		return id;
	}

	// 删除users 表的记录
	public int DelUserInfo(String userId) {
		int id = db.delete(SqliteHelper.TABLE_NAME, UserInfo.USERID + "="
				+ userId, null);
		Log.e("DelUserInfo", id + "");
		return id;
	}

}
