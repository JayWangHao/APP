package com.cattsoft.framework.util;

import java.sql.Date;
import java.sql.Timestamp;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
/**
 * Json处理
 * @author maxun
 *
 */
public class JsonUtil {

	private static final String TAG = "JsonUtil";

	/**
	 * 获取JSONobject中的JSONobject
	 * 
	 * @param jsonObject
	 *            待取值的JSONobject
	 * @param name
	 *            目标在JSONobject中的键值
	 * @return 当键值存在且对应内容非空时返回获取到的JSONobject，否则返回null
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static JSONObject getJSONObject(JSONObject jsonObject,String name){
		if(jsonObject.containsKey(name)){
			try {
				return jsonObject.getJSONObject(name);
			} catch (JSONException e) {
				Log.e(TAG,  "getJSONObject解析json异常");
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取JSONobject中的JSONArray
	 * 
	 * @param jsonObject
	 *            待取值的JSONObject
	 * @param name
	 *            目标在JSONObject中的键值
	 * @return 当键值存在且对应内容非空时返回获取到的JSONArray，否则返回null
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static JSONArray getJSONArray(JSONObject jsonObject,String name){
		if(!(jsonObject == null)){

			if(jsonObject.containsKey(name)){
				try {
					return jsonObject.getJSONArray(name);
				} catch (JSONException e) {
					Log.e(TAG, "getJSONArray解析json异常");
					e.printStackTrace();
				}
			}

			return null;
		}
		return null;
	}


	/**
	 * 获取JSONobject中的boolean
	 * 
	 * @param jsonObject
	 *            待取值的JSONobject
	 * @param name
	 *            目标在JSONobject中的键值
	 * @return 当键值存在且对应内容非空时返回获取到的boolean，否则返回false
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static boolean getBoolean(JSONObject jsonObject,String name){
		if(jsonObject.containsKey(name)){
			try {
				return jsonObject.getBoolean(name);
			} catch (JSONException e) {
				Log.e(TAG, "getBoolean解析json异常");
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 获取JSONobject中的int
	 * 
	 * @param jsonObject
	 *            待取值的JSONobject
	 * @param name
	 *            目标在JSONobject中的键值
	 * @return 当键值存在且对应内容非空时返回获取到的int，否则返回0
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static int getInt(JSONObject jsonObject,String name){
		if(jsonObject.containsKey(name)){
			try {
				return jsonObject.getIntValue(name);
			} catch (JSONException e) {
				Log.e(TAG, "getInt解析json异常");
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	/**
	 * 获取JSONobject中的long
	 * 
	 * @param jsonObject
	 *            待取值的JSONobject
	 * @param name
	 *            目标在JSONobject中的键值
	 * @return 当键值存在且对应内容非空时返回获取到的long，否则返回0
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static Long getLong(JSONObject jsonObject,String name){
		if(jsonObject.containsKey(name)){
			try {
				return jsonObject.getLongValue(name);
			} catch (JSONException e) {
				Log.e(TAG, "getLong解析json异常");
				e.printStackTrace();
			}
		}
		return 0l;
	}
	
	/**
	 * 获取JSONobject中的String
	 * 
	 * @param jsonObject
	 *            待取值的JSONobject
	 * @param name
	 *            目标在JSONobject中的键值
	 * @return 当键值存在且对应内容非空时返回获取到的String，否则返回null
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static String getString(JSONObject jsonObject,String name){
		if(jsonObject.containsKey(name)){
			try {
				return jsonObject.getString(name);
			} catch (JSONException e) {
				Log.e(TAG, "getString解析json异常");
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取JSONobject中去空格后的String
	 * 
	 * @param jsonObject
	 *            待取值的JSONobject
	 * @param name
	 *            目标在JSONobject中的键值
	 * @return 当键值存在且对应内容非空时返回获取到的String，否则返回null
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static String getTrimString(JSONObject jsonObject,String name){
		if(jsonObject.containsKey(name)){
			try {
				return jsonObject.getString(name).trim();
			} catch (JSONException e) {
				Log.e(TAG, "getTrimString解析json异常");
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 获取JSONobject中的Timestamp
	 * 
	 * @param jsonObject
	 *            待取值的JSONobject
	 * @param name
	 *            目标在JSONobject中的键值
	 * @return 当键值存在且对应内容非空时返回获取到的Timestamp，否则返回null
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static Timestamp getTimestamp(JSONObject jsonObject,String name){
		if(jsonObject.containsKey(name)){
			try {
				if(!jsonObject.getJSONObject(name).containsKey("timestamp")) {
					return new Timestamp(jsonObject.getJSONObject(name).getJSONObject("timestamp").getLong("time"));
				}else if(!jsonObject.getJSONObject(name).containsKey("time")) {
					return new Timestamp(jsonObject.getJSONObject(name).getLong("time"));
				}
			} catch (JSONException e) {
				Log.e(TAG, "getTimestamp解析json异常");
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取JSONobject中的Date
	 * 
	 * @param jsonObject
	 *            待取值的JSONobject
	 * @param name
	 *            目标在JSONobject中的键值
	 * @return 当键值存在且对应内容非空时返回获取到的Date，否则返回null
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static Date getDate(JSONObject jsonObject,String name){
		if(jsonObject.containsKey(name)){
			try {
				return new Date(jsonObject.getJSONObject(name).getLong("time"));
			} catch (JSONException e) {
				Log.e(TAG, "getDate解析json异常");
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 获取JSONobject中的Date
	 * 其中Date是以Long型传递过来
	 * @param jsonObject
	 *            待取值的JSONobject
	 * @param name
	 *            目标在JSONobject中的键值
	 * @return 当键值存在且对应内容非空时返回获取到的Date，否则返回null
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static Date getDateByLong(JSONObject jsonObject,String name){
		if(jsonObject.containsKey(name)){
			try {
				return new Date(jsonObject.getLong(name));
			} catch (JSONException e) {
				Log.e(TAG, "getDateByLong解析json异常");
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 获取JSONobject中的Date,类型为util.Date
	 * 其中Date是以Long型传递过来
	 * @param jsonObject
	 *            待取值的JSONobject
	 * @param name
	 *            目标在JSONobject中的键值
	 * @return 当键值存在且对应内容非空时返回获取到的Date，否则返回null
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static java.util.Date getUtilDateByLong(JSONObject jsonObject,String name){
		if(jsonObject.containsKey(name)){
			try {
				return new java.util.Date(jsonObject.getLong(name));
			} catch (JSONException e) {
				Log.e(TAG, "getUtilDateByLong解析json异常");
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static boolean[] jsonArrayToBooleanArray(JSONArray jsonArray) {
		if(jsonArray==null) {
			return null;
		}
		boolean[] booleanArray = new boolean[jsonArray.size()];
		if (jsonArray != null && jsonArray.size() > 0) {
			try {
				for (int i = 0; i < jsonArray.size(); i++) {
					booleanArray[i] = jsonArray.getBoolean(i);
				}
			} catch (JSONException e) {
				Log.e(TAG, "jsonArrayToBooleanArray获取JSONArray的元素错误");
			}
		}
		return booleanArray;
	}
	
	/**
	 * 获取JSONobject中的Timestamp格式化输出字符串
	 * 
	 * @param jsonObject
	 *            待取值的JSONobject
	 * @param name
	 *            目标在JSONobject中的键值
	 * @return 当键值存在且对应内容非空时返回获取到的Timestamp的yyyy-MM-dd HH:mm:ss格式字符串，否则返回null
	 * @throws JSONException
	 *             JSON解析异常
	 * @author maxun
	 */
	public static String getTimestampString(JSONObject jsonObject,String name){
		if(jsonObject.containsKey(name)){
			try {
				if(!jsonObject.getJSONObject(name).containsKey("timestamp")) {
					String timestampString = new String();
					timestampString = (jsonObject.getJSONObject(name).getJSONObject("timestamp").getIntValue("year")+1900)+"-";
					timestampString += (jsonObject.getJSONObject(name).getJSONObject("timestamp").getIntValue("month")+1)+"-";
					timestampString += jsonObject.getJSONObject(name).getJSONObject("timestamp").getIntValue("date")+" ";
					timestampString += jsonObject.getJSONObject(name).getJSONObject("timestamp").getIntValue("hours")+":";
					timestampString += jsonObject.getJSONObject(name).getJSONObject("timestamp").getIntValue("minutes")+":";
					timestampString += jsonObject.getJSONObject(name).getJSONObject("timestamp").getIntValue("seconds");
					return timestampString;
				}else if(!jsonObject.getJSONObject(name).containsKey("time")) {
					String timestampString = new String();
					timestampString = (jsonObject.getJSONObject(name).getIntValue("year")+1900)+"-";
					timestampString += (jsonObject.getJSONObject(name).getIntValue("month")+1)+"-";
					timestampString += jsonObject.getJSONObject(name).getIntValue("date")+" ";
					timestampString += jsonObject.getJSONObject(name).getIntValue("hours")+":";
					timestampString += jsonObject.getJSONObject(name).getIntValue("minutes")+":";
					timestampString += jsonObject.getJSONObject(name).getIntValue("seconds");
					return timestampString;
				}
			} catch (JSONException e){
				Log.e(TAG, "getTimestamp解析json异常");
				e.printStackTrace();
			}
		}
		return null;
	}
}
