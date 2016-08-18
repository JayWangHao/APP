package com.cattsoft.wow.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.framework.view.ProgressDialog;
import com.cattsoft.wow.R;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

public class TrendAllActivity extends Activity {
    private TextView back_text;
    private BridgeWebView webViewBar;
    private JsonObject jsonObjectBar;
    private RadioGroup rg_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend_all);
        back_text = (TextView)findViewById(R.id.back_text);//返回
        back_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        webViewBar = (BridgeWebView) findViewById(R.id.webViewBar_all);
        webViewBar.setDefaultHandler(new DefaultHandler());
        webViewBar.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        webViewBar.getSettings().setDomStorageEnabled(true);
        webViewBar.getSettings().setAppCacheMaxSize(1024 * 1024 * 10);//设置缓冲大小，10M
        String appCacheDir = getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        webViewBar.getSettings().setAppCachePath(appCacheDir);
        webViewBar.getSettings().setAllowFileAccess(true);
        webViewBar.getSettings().setAppCacheEnabled(true);
        webViewBar.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webViewBar.getSettings().setLoadWithOverviewMode(true);

        webViewBar.loadUrl("file:///android_asset/echarts/trendBar.html");
        jiaohuBar();//柱状图数据进行JS与java交互
        rg_content = (RadioGroup) findViewById(R.id.rg_content);
        rg_content.check(R.id.rb_once);
        rg_content.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_once:
                        flag = 1;
                        getBarData();
                        break;
                    case R.id.rb_alarm:
                        flag = 2;
                        getBarData();
                        break;
                }
            }
        });
    }
    private ProgressDialog progressDialog;

    /**
     * 请求获取柱形图数据
     */
    private void getBarData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.showProcessDialog();
        new barThead().start();
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
    private int flag = 1;  //0位不拍序，1按工单排序，2按告警排序

    private void getBar() {
        String url = "tz/TZDeviceAction.do?method=MapInfo";
        Message msg = new Message();

        try {
            JSONObject jsonObject =new JSONObject();
            jsonObject.put("alarm","1");
            if (flag == 1) {
//                Log.d("TAG_3","1");
                jsonObject.put("date", "once");
            } else if (flag == 2) {
//                Log.d("TAG_3","2");
                jsonObject.put("date", "alarm");
            }

            String result = Communication.getPostResponseForNetManagement(url, jsonObject.toString());

            if (StringUtil.isBlank(result)) {
                msg.what = 1;
                handler.sendMessage(msg);
                return;
            }else{

                jsonObjectBar = (JsonObject) new JsonParser().parse(result);


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
                    Toast.makeText(TrendAllActivity.this, "服务端返回为空！", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 3: {


                    jiaohuBar();
                    progressDialog.closeProcessDialog();
                    break;
                }
            }
        }
    };

    /**
     * 柱形图数据进行JS与java交互
     */
    private void jiaohuBar() {

        try {
            webViewBar.callHandler("BAR", jsonObjectBar.toString(), new CallBackFunction() {

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
