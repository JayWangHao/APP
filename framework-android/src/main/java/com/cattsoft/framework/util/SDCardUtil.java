package com.cattsoft.framework.util;

import android.os.Environment;

public class SDCardUtil {

	/**
	 * 判断SD卡 是否存在
	 * */
	public static Boolean IsSDCardExist(){
		String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)){
			return true;
		}else{
			return false;
		}
	}
	
	 /**
	 * 读取SD卡路径
	 * @return
	 */
	public static String ReadSDPath(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			return Environment.getExternalStorageDirectory().toString();
		}else{
			return null;
		}
		
	}
}
