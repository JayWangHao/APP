package com.cattsoft.wow.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.connect.Communication;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 心跳服务，每30秒请求一次服务
 */
public class PalpitateService extends Service {

    private String nid;
    private String online;//online:1  是登录状态；2是离线状态
    private final Timer timer=new Timer();
    private TimerTask timerTask;

    private SharedPreferences sharedPreferences;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            new XinThread().start();//执行请求服务
            super.handleMessage(msg);
        }
    };

    public PalpitateService() {

    }

    @Override
    public void onCreate() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        nid=sharedPreferences.getString("nid","");
        online="1";

        timerTask = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };

        timer.schedule(timerTask, 30000, 30000);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        nid=intent.getStringExtra("nid");
//        online=intent.getStringExtra("Online");
//        timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        };
//
//        timer.schedule(timerTask, 30000, 30000);


        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 启动线程请求服务
     */
    private class XinThread extends Thread{
        @Override
        public void run() {
            String url="tz/TZDeviceAction.do?method=Heartbeat";

            JSONObject jsonObject=new JSONObject();
            jsonObject.put("Nid",nid);
            jsonObject.put("Online",online);

            try {
                String result= Communication.getPostResponseForNetManagement(url,jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        online="2";
        Message message = new Message();
        message.what = 1;
        handler.sendMessage(message);

        timer.cancel();
    }
}
