package com.cattsoft.wow.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.framework.view.ProgressDialog;
import com.cattsoft.wow.R;
import com.cattsoft.wow.activity.TalkActivity;
import com.cattsoft.wow.activity.TrendAllActivity;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase;

import java.io.IOException;


/**
 * 走势fragment
 */
public class TrendFragment extends Fragment {
    private ImageView leftImg;
    private BridgeWebView webViewLine;
    private BridgeWebView webViewBar;
    private BridgeWebView webViewPie;

    private Button bt_all;
    private boolean isYear = false;
    private RadioGroup rg_content;
    private Button bt_gaojing;
    private Button bt_gongdan;
    private int flag = 1;  //0位不拍序，1按工单排序，2按告警排序
    private JsonObject jsonObjectBar;
    private JsonObject jsonObjectLine;
    private ProgressDialog progressDialog1;
    private ProgressDialog progressDialog;

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
        webViewBar.setLayerType(View.LAYER_TYPE_SOFTWARE, null);


        webViewBar.getSettings().setDomStorageEnabled(true);
        webViewBar.getSettings().setAppCacheMaxSize(1024 * 1024 * 10);//设置缓冲大小，10M
        String appCacheDir = getActivity().getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        webViewBar.getSettings().setAppCachePath(appCacheDir);
        webViewBar.getSettings().setAllowFileAccess(true);
        webViewBar.getSettings().setAppCacheEnabled(true);
        webViewBar.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        webViewBar.loadUrl("file:///android_asset/echarts/trendBarSmall.html");

        webViewLine = (BridgeWebView) view.findViewById(R.id.webViewLine);
        webViewLine.setDefaultHandler(new DefaultHandler());
        webViewLine.setLayerType(View.LAYER_TYPE_SOFTWARE, null);


        webViewLine.getSettings().setDomStorageEnabled(true);
        webViewLine.getSettings().setAppCacheMaxSize(1024 * 1024 * 10);//设置缓冲大小，10M
        String appCacheDirTwo = getActivity().getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        webViewLine.getSettings().setAppCachePath(appCacheDirTwo);
        webViewLine.getSettings().setAllowFileAccess(true);
        webViewLine.getSettings().setAppCacheEnabled(true);
        webViewLine.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        webViewLine.loadUrl("file:///android_asset/echarts/trendLine.html");

        webViewPie = (BridgeWebView) view.findViewById(R.id.webViewPie);
        webViewPie.setDefaultHandler(new DefaultHandler());
        webViewPie.setLayerType(View.LAYER_TYPE_SOFTWARE, null);


        webViewPie.getSettings().setDomStorageEnabled(true);
        webViewPie.getSettings().setAppCacheMaxSize(1024 * 1024 * 10);//设置缓冲大小，10M
        String appCacheDirThree = getActivity().getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        webViewPie.getSettings().setAppCachePath(appCacheDirThree);
        webViewPie.getSettings().setAllowFileAccess(true);
        webViewPie.getSettings().setAppCacheEnabled(true);
        webViewPie.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        webViewPie.loadUrl("file:///android_asset/echarts/trendPie.html");

        bt_all = (Button) view.findViewById(R.id.bt_trend_all);
        bt_gaojing = (Button) view.findViewById(R.id.bt_trend_gaojing);
        bt_gongdan = (Button) view.findViewById(R.id.bt_trend_gongdan);
        rg_content = (RadioGroup) view.findViewById(R.id.rg_content);
        rg_content.check(R.id.rb_week_trend);
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

        bt_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TrendAllActivity.class);
                getActivity().startActivity(intent);
            }
        });

        bt_gaojing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 2;
                getBarData();

            }
        });

        bt_gongdan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                getBarData();

            }
        });

        rg_content.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_week_trend:
                        isYear = false;
                        getLineData();
                        break;
                    case R.id.rb_month_trend:
                        isYear = true;
                        getLineData();
                        break;
                }
            }
        });
    }

    /**
     * 请求获取折线数据
     */
    private void getLineData() {
        progressDialog = new ProgressDialog(TrendFragment.super.getActivity());
        progressDialog.showProcessDialog();
        new lineThead().start();
    }

    /**
     * 请求获取柱形图数据
     */
    private void getBarData() {
        progressDialog1 = new ProgressDialog(TrendFragment.super.getActivity());
        progressDialog1.showProcessDialog();
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
        JSONObject requestJson = new JSONObject();
        if (isYear) {
            requestJson.put("level", "年");
        } else {
            requestJson.put("level", 0);
        }
        try {
            String result = Communication.getPostResponseForNetManagement(url, requestJson.toString());

            jsonObjectLine = (JsonObject) new JsonParser().parse(result);
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
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("alarm", "1");
            if (flag == 1) {
//                Log.d("TAG_3","1");
                jsonObject.put("date", "once");
            } else if (flag == 2) {
//                Log.d("TAG_3","2");
                jsonObject.put("date", "alarm");
            }
            String result = Communication.getPostResponseForNetManagement(url, jsonObject.toString());
//            Log.d("TAG_3","服务器："+result);

            if (StringUtil.isBlank(result)) {
                msg.what = 1;
                handler.sendMessage(msg);
                return;
            } else {
                jsonObjectBar = (JsonObject) new JsonParser().parse(result);
//                Log.d("TAG_3","我的："+jsonObjectBar);
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
                    progressDialog.closeProcessDialog();

                    break;
                }
                case 3: {


                    jiaohuBar();
                    progressDialog1.closeProcessDialog();

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

            webViewLine.callHandler("LINE", jsonObjectLine.toString(), new CallBackFunction() {

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
            webViewBar.callHandler("BAR1", jsonObjectBar.toString(), new CallBackFunction() {

                @Override
                public void onCallBack(String data) {
//                    Log.d("TAG_3", "jiaohuBar:" + data);

                }

            });
        } catch (NullPointerException e) {
            e.printStackTrace();
            getBarData();
        }

    }
}
