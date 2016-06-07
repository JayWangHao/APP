package com.cattsoft.framework.connect;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.cattsoft.framework.cache.CacheProcess;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by xueweiwei on 15/6/2.
 * 异步获取天气信息
 */
public class GetWeatherAsyncTask extends AsyncTask<Object, Integer, String>{

    private Handler handler;
    private Context context;

    private String weatherInfo = "";

    private String city;

    public GetWeatherAsyncTask(Handler handler,Context context) {

        this.handler = handler;
        this.context = context;



    }
    //TestAsyncTask被后台线程执行后，被UI线程被调用，一般用于初始化界面控件，如进度条
    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Object[] objects) {

        city = CacheProcess.getCacheValueInSharedPreferences(context,"city");


        return city;

    }

    @Override
    protected void onPostExecute(String result) {
         super.onPostExecute(result);

        Message message = new Message();
        message.what = 1;
        Bundle bundle = new Bundle();
        bundle.putString("result",weatherInfo);
        message.setData(bundle);

        handler.sendMessage(message);
    }
}
