package com.cattsoft.framework.connect;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.cache.MosApp;
import com.cattsoft.framework.pub.Constant;
import com.cattsoft.framework.util.DeviceUtil;

/**
 * 请求数据的公共信息头
 * 
 * @author xueweiwei
 * 
 */
public class RequestHeadData {

	public static JSONObject getRequestHeadData() throws JSONException {

		JSONObject requestJSONObject = new JSONObject();
		JSONObject mobileHeadJSONObject = new JSONObject();
		JSONObject equipmentJSONObject = new JSONObject();// 设备信息
		JSONObject clientJSONObject = new JSONObject(); // 客户端信息
		JSONObject personalJSONObject = new JSONObject();// 登录用户信息
		JSONObject otherJSONObject = new JSONObject();// 其他信息

		TelephonyManager tm = (TelephonyManager) MosApp.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = tm.getDeviceId();// 手机的唯一标识
		String phoneNum = tm.getLine1Number();//手机号码
		String name = android.os.Build.MODEL;// 手机型号
		String version = android.os.Build.VERSION.RELEASE;// 手机版本号
		String osVersion = android.os.Build.VERSION.SDK_INT + "";// 手机Android系统版本号
		String osName = Constant.OPERATING_SYSTEM;// 操作系统名称
		
		if (deviceId == null || "".equals(deviceId)) {// 如果获取不到手机唯一标识，就随机生成一串数字，并存放到本地文件中
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MosApp.getInstance().getApplicationContext());
			deviceId = sharedPreferences.getString("deviceId", "");
			if ("".equals(deviceId)) {
				deviceId = java.util.UUID.randomUUID().toString();
				sharedPreferences.edit().putString("deviceId", deviceId).commit();
			}
		}

		equipmentJSONObject.put("deviceId", deviceId);
		equipmentJSONObject.put("phoneNum", phoneNum);
		equipmentJSONObject.put("name", name);
		equipmentJSONObject.put("version", version);
		equipmentJSONObject.put("osVersion", osVersion);
		equipmentJSONObject.put("osName", osName);
		mobileHeadJSONObject.put("equipment", equipmentJSONObject);

		ConnectivityManager connManager = (ConnectivityManager) MosApp.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = connManager.getActiveNetworkInfo();
		String networkType = ""; // 网络类型
		if (networkinfo != null) {
			networkType = networkinfo.getTypeName();
		}
		int versionCode = 0;// app版本号
		String versionName = "";// app版本名称
        ApplicationInfo appInfo = MosApp.getInstance().getApplicationInfo();
		try {
			versionCode = MosApp.getInstance().getApplicationContext().getPackageManager().getPackageInfo(appInfo.packageName, 0).versionCode;
			versionName = MosApp.getInstance().getApplicationContext().getPackageManager().getPackageInfo(appInfo.packageName, 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		clientJSONObject.put("applicationId", appInfo.packageName);
		clientJSONObject.put("netType", networkType);
		clientJSONObject.put("versionCode", versionCode);
		clientJSONObject.put("versionName", versionName);
		mobileHeadJSONObject.put("client", clientJSONObject);

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MosApp.getInstance().getApplicationContext());
		personalJSONObject.put("userId", sharedPreferences.getString("userId", ""));
		mobileHeadJSONObject.put("personal", personalJSONObject);
		String timezone = DeviceUtil.TIME_ZONE;// 时区
		String country = DeviceUtil.COUNTRY; // 取得运营商所在国家
		String lan = DeviceUtil.LANGUAGE; // 所使用语言

		otherJSONObject.put("timeZone", timezone);
		otherJSONObject.put("country", country);
		otherJSONObject.put("lan", lan);
		mobileHeadJSONObject.put("other", otherJSONObject);

		requestJSONObject.put("mobileHead", mobileHeadJSONObject);
		return requestJSONObject;
	}

}