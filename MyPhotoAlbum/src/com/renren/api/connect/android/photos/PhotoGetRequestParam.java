package com.renren.api.connect.android.photos;

import android.os.Bundle;
import android.text.TextUtils;

import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.common.RequestParam;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.exception.RenrenException;

public class PhotoGetRequestParam extends RequestParam {

	/**
	 * 调用获取相册API传入的method参数，必须参数
	 */
	private static final String METHOD = "photos.get";
	/**
	 * 获取的相册的所有者用户uid，必须参数
	 */
	private Long uid;

	/**
	 * 分页的页数，默认值为1
	 */
	private int page = 1;
	/**
	 * 分页后每页的个数，默认值为10
	 */
	private int count = 200;

	private long aid;

	private String pids;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getAids() {
		return aid;
	}

	public void setAids(long aid) {
		this.aid = aid;
	}

	public String getPids() {
		return pids;
	}

	public void setPids(String pids) {
		this.pids = pids;
	}

	@Override
	public Bundle getParams() throws RenrenException {
		Bundle params = new Bundle();
		params.putString("method", METHOD);
		// 默认请求返回参数为json格式，不提供设置此值的接口给用户
		params.putString("format", Renren.RESPONSE_FORMAT_JSON);
		params.putString("uid", String.valueOf(this.uid));
		params.putString("aid", String.valueOf(this.aid));

		// pids的长度如果大于20，系统会抛出错误信息“pids的长度必须小于20”
		if (this.pids != null && !"".equals(this.pids)) {
			// 判断是否合法的照片
			String[] pid = this.pids.split(",");
			int length = pid.length;

			if (length >= 20) {
				Util.logger("exception in getting albums: the length of aids should less than 10!");
				throw new RenrenException(
						RenrenError.ERROR_CODE_PARAMETER_EXTENDS_LIMIT,
						"同时获取的照片数不能大于10个", "同时获取的照片数不能大于10个");
			}

			for (int i = 0; i < length; i++) {
				if (!TextUtils.isDigitsOnly(pid[i].trim())) {
					continue;
				}

				Util.logger("exception in getting albums: invalid aids!");
				throw new RenrenException(
						RenrenError.ERROR_CODE_ILLEGAL_PARAMETER, "不合法的照片pid",
						"不合法的照片pid");
			}

			params.putString("pids", this.pids);
		}

		params.putString("page", String.valueOf(this.page));
		params.putString("count", String.valueOf(this.count));

		return params;
	}

}
