package my.photoalbum.sinaweibo;

import java.util.ArrayList;
import java.util.HashMap;

import my.photoalbum.R;
import my.photoalbum.photo.SharedPhoto;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class WeiboChooseDailog extends Dialog {

	private Context mContext;
	private SimpleAdapter adapter;
	public ListView weiboList;
	private ArrayList<HashMap<String, Object>> listItems; // 存放文字、图片信息
	private SharedPhoto sharedPhoto;

	public WeiboChooseDailog(Context context, int theme,SharedPhoto sp) {
		super(context,theme);
		mContext = context;
		sharedPhoto = sp;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		weiboList = new ListView(mContext);
		initAdapter();
		weiboList.setAdapter(adapter);
		this.setContentView(weiboList);

		weiboList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				WeiboChooseDailog.this.hide();
				int type = (Integer) listItems.get(position).get("weiboType");
				switch (type) {
				case WeiboParam.SINA_WEIBO:
					SinaWeiboManager weiboManager = new SinaWeiboManager(mContext);
					weiboManager.uploadLocalPhotoToWeibo(sharedPhoto);
					break;

				case WeiboParam.Q_WEIBO:

					break;

				default:
					break;
				}
				WeiboChooseDailog.this.dismiss();
			}
		});
	}

	private void initAdapter() {
		listItems = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("weiboType", WeiboParam.SINA_WEIBO);
		map.put("weiboImage", R.drawable.sina_weibo_publish); // 图片
		listItems.add(map);

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("weiboType", WeiboParam.Q_WEIBO);
		map2.put("weiboImage", R.drawable.q_weibo_publish); // 图片
		listItems.add(map2);

		// 生成适配器的Item和动态数组对应的元素
		adapter = new SimpleAdapter(mContext, listItems, // listItems数据源
				R.layout.weibo_item, // ListItem的XML布局实现
				new String[] { "weiboImage" }, // 动态数组与ImageItem对应的子项
				new int[] { R.id.weibo_icon } // list_item.xml布局文件里面的一个ImageView的ID,一个TextView
												// 的ID
		);
	}

}
