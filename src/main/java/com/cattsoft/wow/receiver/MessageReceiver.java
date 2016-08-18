package com.cattsoft.wow.receiver;

import android.content.ContentValues;
import android.content.Context;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

/**
 * Created by sjzb on 2016/7/27.
 */
public class MessageReceiver extends XGPushBaseReceiver {
    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {

    }

    @Override
    public void onUnregisterResult(Context context, int i) {

    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {

    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {

    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {

    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {

    }
    //在该方法中实现对于通知内容的获取
    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
        if(context == null||xgPushShowedResult == null){
            return;
        }

        ContentValues values = new ContentValues();
        values.put("title", xgPushShowedResult.getTitle());
        values.put("content", xgPushShowedResult.getContent());
//        String customContent = xgPushShowedResult.getCustomContent();
//
//        if (customContent !=""||customContent.length()!=0){
//            try {
//                JSONObject js = new JSONObject(customContent);
//                String time = js.getString("date");
//                String nid = js.getString("nid");
//                values.put("time", time);
//                values.put("nid", nid);
//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
    }
}
