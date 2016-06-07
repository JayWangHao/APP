package com.cattsoft.framework.service;

import android.content.Context;
import android.content.Intent;

/***
 * 
 * CameraUtil、HttpMultipartPost 回调函数 接口
 * */
public interface CameraCallBack {
	/**
	 * CameraUtil剪切结果 回调 函数
	 * */
	public void cropResult(Context context, Intent data);

	/***
	 * 
	 * CameraUtil上传图片回调 函数
	 * */
	public void upLoadImageResult(Context context, Intent data);

}
