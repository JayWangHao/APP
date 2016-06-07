package com.cattsoft.wow.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.wow.R;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 走势fragment
 */
public class TrendFragment extends Fragment {
    private ImageView leftImg;
    private BridgeWebView webViewLine;
    private BridgeWebView webViewBar;
    private BridgeWebView webViewPie;

    private com.alibaba.fastjson.JSONObject respsoneJsonLine;//折线
    private com.alibaba.fastjson.JSONObject respsoneJsonBar;//折线

    public TrendFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trend, container, false);

        initView(view);
        resterlistener();

        //getLineData();//请求获取折线数据
        //getBarData();//请求获取柱形图数据
        jiaohuLine();//折线数据进行JS与java交互
        jiaohuBar();//柱状图数据进行JS与java交互
        return view;
    }

    private void initView(View view) {
        leftImg = (ImageView) view.findViewById(R.id.user_name_img);

        webViewBar = (BridgeWebView) view.findViewById(R.id.webViewBar);
        webViewBar.setDefaultHandler(new DefaultHandler());

        webViewBar.getSettings().setDomStorageEnabled(true);
        webViewBar.getSettings().setAppCacheMaxSize(1024*1024*10);//设置缓冲大小，10M
        String appCacheDir = getActivity().getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        webViewBar.getSettings().setAppCachePath(appCacheDir);
        webViewBar.getSettings().setAllowFileAccess(true);
        webViewBar.getSettings().setAppCacheEnabled(true);
        webViewBar.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        webViewBar.loadUrl("file:///android_asset/echarts/trendBar.html");

        webViewLine = (BridgeWebView) view.findViewById(R.id.webViewLine);
        webViewLine.setDefaultHandler(new DefaultHandler());

        webViewLine.getSettings().setDomStorageEnabled(true);
        webViewLine.getSettings().setAppCacheMaxSize(1024*1024*10);//设置缓冲大小，10M
        String appCacheDirTwo = getActivity().getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        webViewLine.getSettings().setAppCachePath(appCacheDirTwo);
        webViewLine.getSettings().setAllowFileAccess(true);
        webViewLine.getSettings().setAppCacheEnabled(true);
        webViewLine.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        webViewLine.loadUrl("file:///android_asset/echarts/trendLine.html");

        webViewPie = (BridgeWebView) view.findViewById(R.id.webViewPie);
        webViewPie.setDefaultHandler(new DefaultHandler());

        webViewPie.getSettings().setDomStorageEnabled(true);
        webViewPie.getSettings().setAppCacheMaxSize(1024*1024*10);//设置缓冲大小，10M
        String appCacheDirThree = getActivity().getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        webViewPie.getSettings().setAppCachePath(appCacheDirThree);
        webViewPie.getSettings().setAllowFileAccess(true);
        webViewPie.getSettings().setAppCacheEnabled(true);
        webViewPie.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        webViewPie.loadUrl("file:///android_asset/echarts/trendPie.html");
    }

    private void resterlistener() {
        leftImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof SlidingActivityBase) {
                    ((SlidingActivityBase) getActivity()).toggle();
                }
            }
        });
    }

    /**
     * 请求获取折线数据
     */
    private void getLineData() {
        new lineThead().start();
    }

    /**
     * 请求获取柱形图数据
     */
    private void getBarData() {
        new barThead().start();
    }


    /**
     * 启动获取折线图数据的线程
     */
    private class lineThead extends Thread {
        @Override
        public void run() {
            getLine();
        }
    }

    private void getLine() {
        String url = "tz/TZDeviceAction.do?method=ZXInfo";
        Message msg = new Message();
        try {

            String result = Communication.getPostResponseForNetManagement(url, "");

            respsoneJsonLine = JSON.parseObject(result);


            if (StringUtil.isBlank(result)) {
                msg.what = 1;
                handler.sendMessage(msg);
                return;
            } else {
                msg.what = 2;
                handler.sendMessage(msg);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动线程请求柱状图数据
     */
    private class barThead extends Thread {
        @Override
        public void run() {
            getBar();
        }
    }

    private void getBar() {
        String url = "tz/TZDeviceAction.do?method=MapInfo";
        Message msg = new Message();

        try {
            JSONObject jsonObject =new JSONObject();
            jsonObject.put("alarm","1");

            String result = Communication.getPostResponseForNetManagement(url,jsonObject.toString());

            if (StringUtil.isBlank(result)) {
                msg.what = 1;
                handler.sendMessage(msg);
                return;
            }else{

            respsoneJsonBar=JSON.parseObject(result);


                msg.what = 3;
                handler.sendMessage(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    Toast.makeText(getActivity(), "服务端返回为空！", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 2: {


                   jiaohuLine();

                    break;
                }
                case 3: {


                    jiaohuBar();

                    break;
                }
            }
        }
    };


    /**
     * 折线数据进行JS与java交互
     */
    private void jiaohuLine() {


        try {

            webViewLine.callHandler("LINE", respsoneJsonLine.toString(), new CallBackFunction() {

                @Override
                public void onCallBack(String data) {

                }

            });
        } catch (NullPointerException e) {
            e.printStackTrace();
            getLineData();
        }

    }


    /**
     * 柱形图数据进行JS与java交互
     */
    private void jiaohuBar() {

        try {
            webViewBar.callHandler("BAR", respsoneJsonBar.toString(), new CallBackFunction() {

                @Override
                public void onCallBack(String data) {

                }

            });
        } catch (NullPointerException e) {
            e.printStackTrace();
            getBarData();
        }

    }
}
