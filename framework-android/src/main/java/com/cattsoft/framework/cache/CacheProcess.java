package com.cattsoft.framework.cache;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.util.JsonUtil;
/**
 * 数据缓存到本地文件中
 * @author xueweiwei
 *
 */
public class CacheProcess{

    private static SharedPreferences.Editor saveEditor;
    private static SharedPreferences saveInfo;
    private static CacheProcess spUtil = new CacheProcess();
    private static Context mContext;


    public static synchronized CacheProcess getInstance(Context context) {
        mContext = context;
        if ((saveInfo == null) && (mContext != null)) {
            saveInfo = PreferenceManager.getDefaultSharedPreferences(context);
            saveEditor = saveInfo.edit();
        }
        return spUtil;
    }
	
	/**
	 * add by xyc 20130521 缓存在mosApp中的数据可能由于Android操作系统的某种机制被回收，经常造成系统获取缓存对象为空，因此在获得服务器端传来的数据后，放到本地文件中
	 * @param context
	 * @param
	 * @throws com.alibaba.fastjson.JSONException
	 */
	public static void initCacheInSharedPreferences(Context context,JSONObject jsonObject) throws JSONException{
		SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
		Editor edit = sharedPreferences.edit();
		JSONObject sysUserJson=JsonUtil.getJSONObject(jsonObject,"sysuser");
		
//		JSONArray menusArr=JsonUtil.getJSONArray(jsonObject,"menus");
		
		String createDate = JsonUtil.getString(sysUserJson, "createDate");
		String initPwdFlag = JsonUtil.getString(sysUserJson, "initPwdFlag");
		String lastPwd = JsonUtil.getString(sysUserJson, "lastPwd");
		String loginName = JsonUtil.getString(sysUserJson, "loginName");
		String mailBox = JsonUtil.getString(sysUserJson, "mailBox");
		String name = JsonUtil.getString(sysUserJson, "name");
		String orgId = JsonUtil.getString(sysUserJson, "orgId");
		String orgName = JsonUtil.getString(sysUserJson, "orgName");
	//	String password = JsonUtil.getString(sysUserJson, "password");
		String pwdInactiveTime = JsonUtil.getString(sysUserJson, "pwdInactiveTime");
		String pwdSetTime = JsonUtil.getString(sysUserJson, "pwdSetTime");
		String remarks = JsonUtil.getString(sysUserJson, "remarks");
		String roleId = JsonUtil.getString(sysUserJson, "roleId");
		String roleName = JsonUtil.getString(sysUserJson, "roleName");
		String staffId = JsonUtil.getString(sysUserJson, "staffId");
		String staffName = JsonUtil.getString(sysUserJson, "staffName");
		String staffSts = JsonUtil.getString(sysUserJson, "staffSts");
		String sts = JsonUtil.getString(sysUserJson, "sts");
		String stsDate = JsonUtil.getString(sysUserJson, "stsDate");
		String sysUserId = JsonUtil.getString(sysUserJson, "sysUserId");
		String telNbr = JsonUtil.getString(sysUserJson, "telNbr");
        String mobile = JsonUtil.getString(sysUserJson, "mobile");
		
		edit.putString("createDate", createDate);
		edit.putString("initPwdFlag", initPwdFlag);
		edit.putString("lastPwd", lastPwd);
		edit.putString("loginName", loginName);
		edit.putString("mailBox", mailBox);
		edit.putString("name", name);
		edit.putString("orgId", orgId);
		edit.putString("orgName", orgName);
	//	edit.putString("password", password);
		edit.putString("pwdInactiveTime", pwdInactiveTime);
		edit.putString("pwdSetTime", pwdSetTime);
		edit.putString("remarks", remarks);
		edit.putString("roleId", roleId);
		edit.putString("roleName", roleName);
		edit.putString("staffId", staffId);
		edit.putString("staffName", staffName);
		edit.putString("staffSts", staffSts);
		edit.putString("sts", sts);
		edit.putString("stsDate", stsDate);
		edit.putString("sysUserId", sysUserId);
		edit.putString("telNbr", telNbr);
        edit.putString("mobile", mobile);

//		edit.putString("menus", menusArr.toString());

		String theme = JsonUtil.getString(jsonObject, "theme");
		edit.putString("theme", theme);

		//缓存数据
		JSONObject cacheObj = JsonUtil.getJSONObject(jsonObject, "cachedata");
		String province = JsonUtil.getString(cacheObj, "province");
		edit.putString("province", province); //区域省份信息

        String areaId = JsonUtil.getString(cacheObj, "areaId");
        edit.putString("areaId", areaId); //服务区

        String localNetId = JsonUtil.getString(cacheObj, "localNetId");
        edit.putString("localNetId", localNetId); //本地网

        //功能对应的操作
        JSONObject operationsObj = JsonUtil.getJSONObject(jsonObject, "operations");
        edit.putString("operations", operationsObj == null ? "" : operationsObj.toJSONString());

        //目录菜单
        JSONArray functionsObj = JsonUtil.getJSONArray(jsonObject, "functions");
        edit.putString("functions", functionsObj == null ? "" : functionsObj.toJSONString());

        //报表
        JSONObject reportsObj = JsonUtil.getJSONObject(jsonObject, "reportList");
        edit.putString("reportList", reportsObj == null ? "" : reportsObj.toJSONString());

        //定制信息
        JSONArray customsObj = JsonUtil.getJSONArray(jsonObject, "custom");
        edit.putString("custom", customsObj == null ? "" : customsObj.toJSONString());
		edit.commit();

        //其他系统缓存信息
        JSONObject cacheJson = JsonUtil.getJSONObject(jsonObject,"cache");
        edit.putString("cache", customsObj == null ? "" : cacheJson.toJSONString());
        edit.commit();

        // loginUserId信息
        JSONObject loginUserIdJson = JsonUtil.getJSONObject(jsonObject,"loginUserId");
        edit.putString("loginUserId", loginUserIdJson == null ? "" : loginUserIdJson.toJSONString());
        edit.commit();


	}

    public static void setCacheValueInSharedPreferences(Context context,String key,String value){

        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
        Editor edit = sharedPreferences.edit();

        edit.putString(key,value);

        edit.commit();

    }
	
	/**
	 * 根据键获取缓存在SharedPreference的值
	 * @param context
	 * @param key
	 */
	public static String getCacheValueInSharedPreferences(Context context,String key){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		String val=sharedPreferences.getString(key, "");
		return val;
	}
	/**
	 * 根据键清空缓存在SharedPreference的值
	 * @param context
	 * @param key
	 */
	public static void clearCacheValueInSharedPreferences(Context context,String key){
		SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
		Editor edit = sharedPreferences.edit();
		edit.putString(key, "");
		edit.commit();
	}

	/**
	 * 根据custom获取SharedPreference的值
	 * @param context
	 * @param key
	 */
	public static String getCustomCacheValueInSharedPreferences(Context context, String key) {
		String custom = CacheProcess.getCacheValueInSharedPreferences(context, "custom");

		JSONArray array = JSONArray.parseArray(custom);
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = (JSONObject) array.get(i);
			if (obj.containsKey(key)) {
				custom = obj.toJSONString();
				break;
			}
		}
		return custom;
	}


    /**
     *获取本地文件boolean类型的值
     * @param context
     * @param key
     * @param defalut
     * @return
     */
    public static boolean getBoolean(Context context,String key,boolean defalut){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getBoolean(key, defalut);
    }

    /**
     * 设置本地文件boolean类型的值
     * @param context
     * @param key
     * @param value
     */
    public static void setBoolean(Context context,String key,boolean value){

        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
        Editor edit = sharedPreferences.edit();

        edit.putBoolean(key, value);
        edit.commit();
    }


	public void clearCache(MosApp mosApp) {
		mosApp.clearAll();
	}


}
