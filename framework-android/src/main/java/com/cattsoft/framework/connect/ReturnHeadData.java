package com.cattsoft.framework.connect;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 服务端返回的公共的头信息
 * @author xueweiwei
 *
 */
public class ReturnHeadData {

	
	public String returnCode;
	public String msg;

	/**
	 * 将服务端返回的结果解析成json格式，返回头信息
	 * @param result
	 * @return
	 */
	public static ReturnHeadData parserReturnHeadFromStr(String result) {
		ReturnHeadData returnHeadData = new ReturnHeadData();
		try {
			JSONObject resultJSONObject = JSONObject.parseObject(result);
			JSONObject mobileHeadJSONObject = resultJSONObject.getJSONObject("mobileHead");
			String code = mobileHeadJSONObject.getString("returnCode");
			returnHeadData.returnCode = code;	
			if(returnHeadData.returnCode.equals("1")){
				JSONObject mobileBodyJSONObject = resultJSONObject.getJSONObject("mobileBody");
				String msg = (String) mobileBodyJSONObject.get("result");
				returnHeadData.msg = msg;
			}else{
				
				JSONObject mobileBodyJSONObject = resultJSONObject.getJSONObject("mobileBody");
				String msg = mobileBodyJSONObject.get("result").toString();
				returnHeadData.msg = msg;
			}
		} catch (JSONException e) {
			returnHeadData.returnCode = "1";
			returnHeadData.msg = "返回头信息解析json出错";
		}
		return returnHeadData;
		
	}
}
