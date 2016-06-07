package com.cattsoft.framework.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.cattsoft.framework.cache.MosApp;
import com.cattsoft.framework.pub.Constant;

import java.util.Locale;
import java.util.TimeZone;

public class DeviceUtil {

    public static final int V2_2 = 8;
    public static final int V2_3 = 9;
    public static final int V2_3_3 = 10;
    public static final int V3_0 = 11;
    public static final int V3_1 = 12;
    public static final int V3_2 = 13;
    public static final int V4_0 = 14;
    public static final int V4_0_3 = 15;
    public static final int V4_1 = 16;
    public static final int V4_2 = 17;
    public static final int V4_3 = 18;
    public static final int V4_4 = 19;

	// 手机的唯一标识
	public static final String DEVICE_ID = ((TelephonyManager) MosApp.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	
	// 手机型号
	public static final String NAME = android.os.Build.MODEL;
	
	// 手机版本号
	public static final String VERSION = android.os.Build.VERSION.RELEASE;
	
	// 手机Android系统版本号
	public static final int OS_VERSION = android.os.Build.VERSION.SDK_INT ;
	
	// 操作系统名称
	public static final String OS_NAME = Constant.OPERATING_SYSTEM;
	
	//网络类型	
//	public static final String NETWORK_TYPE = ((ConnectivityManager) MosApp.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo().getTypeName();
	
	// 时区
	public static final String TIME_ZONE = TimeZone.getDefault().getDisplayName();
	
	// 取得运营商所在国家
	public static final String COUNTRY = ((TelephonyManager) MosApp.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getNetworkCountryIso(); 
	
	// 所使用语言
	public static final String LANGUAGE = Locale.getDefault().getLanguage(); 
	
	/**
	 * 获取手机号 获取不到则返回0
	 * */
	public static String getPhoneNumber() {
		TelephonyManager manager = (TelephonyManager) MosApp.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNum=manager.getLine1Number();
		return StringUtil.isBlank(phoneNum)?"0":phoneNum;
	}
	
	/***
	 * 获取手机屏幕宽度
	 * */
	public static int getPhoneScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;// 获取屏幕宽度
	}
	/***
	 * 获取手机屏幕高度
	 * */
	public static int getPhoneScreenHeght(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;// 获取屏幕高度
	}
	
	/***
	 * 获取手机屏幕宽度  无参数
	 * */
	public static int getPhoneScreenWidth() {
		WindowManager wm = (WindowManager)MosApp.getInstance().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;// 获取屏幕宽度
	}
	/***
	 * 获取手机屏幕高度 无参数
	 * */
	public static int getPhoneScreenHeght() {
		WindowManager wm = (WindowManager)MosApp.getInstance().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;// 获取屏幕高度
	}
	
}
