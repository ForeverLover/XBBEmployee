package com.xbb.la.modellibrary.net;

/**
 * 请求接口回调
 */
public interface XRequestCallBack {

	/**
	 * 准备操作
	 *
	 * @param taskId
	 */
	void onPrepare(int taskId);

	/**
	 * 刷新界面的回调方法
	 *
	 * @param taskId
	 * @param params
	 */
	void onSuccess(int taskId, Object... params);

	/**
	 * 带参数的刷新界面的回调方法
	 *
	 * @param taskId
	 * @param params
	 */
	void onSuccess(int taskId, String flag, Object... params);

	/**
	 * 请求结束
	 *
	 * @param taskId
	 * @Title: onEnd
	 */
	void onEnd(int taskId);

	/**
	 * 请求失败
	 *
	 * @param taskId
	 * @param errorCode
	 * @param errorMsg
	 */
	void onFailed(int taskId, int errorCode, String errorMsg);

	void onFailed(int taskId, String errorMsg);

	/**
	 * 上传/下载文件进度
	 */
	void onLoading(int taskId, long count, long current);

	/**
	 * 是否需要返回数据
	 *
	 * @return true 返回数据, false 不返回数据（用户判断Activity、fragment 活动状态）
	 * @Title: isCallBack
	 */
	boolean isCallBack();

}
