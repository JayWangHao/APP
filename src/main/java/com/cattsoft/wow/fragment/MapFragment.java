package com.cattsoft.wow.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.wow.R;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;

/**
 * 重庆地图
 */
public class MapFragment extends Fragment {
    private BridgeWebView webView;

    private String mParam;
    private RelativeLayout over_biaoge;
    private TextView tv_over_da;
    private TextView tv_over_2x;
    private TextView tv_over_3x;
    private TextView tv_over_4x;
//    private ProgressBar mProgressBar;
private SpannableString ss;
    private SpannableString ss2;
    private SpannableString ss3;
    private SpannableString ss4;
    private JSONObject date;

    public MapFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        over_biaoge = (RelativeLayout) view.findViewById(R.id.over_biaoge);
        tv_over_da = (TextView) view.findViewById(R.id.tv_over_da);
        tv_over_2x = (TextView) view.findViewById(R.id.tv_over_2x);
        tv_over_3x = (TextView) view.findViewById(R.id.tv_over_3x);
        tv_over_4x = (TextView) view.findViewById(R.id.tv_over_4x);

        if (getArguments() != null) {
            mParam = getArguments().getString("result");
            setDate(JSON.parseObject(mParam));
//            Log.d("TAG_3","地图"+mParam);
        }


        webView = (BridgeWebView) view.findViewById(R.id.webView);
//        mProgressBar = (ProgressBar) view.findViewById(R.id.processBar);


//        webView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                mProgressBar.setMax(100);
//                if (newProgress < 100) {
//                    if (mProgressBar.getVisibility() == View.INVISIBLE) {
//                        mProgressBar.setVisibility(View.VISIBLE);
//                    }
//                    mProgressBar.setProgress(newProgress);
//                } else {
//                    mProgressBar.setProgress(100);
//                    mProgressBar.setVisibility(View.INVISIBLE);
//                }
//                super.onProgressChanged(view, newProgress);
//            }
//
//        });

//        webView.reload();

        webView.setDefaultHandler(new DefaultHandler());
//
//        webView.getSettings().setDomStorageEnabled(true);
//        webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 10);//设置缓冲大小，10M
//        String appCacheDirTwo = getActivity().getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
//        webView.getSettings().setAppCachePath(appCacheDirTwo);
//        webView.getSettings().setAllowFileAccess(true);
//        webView.getSettings().setAppCacheEnabled(true);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        webView.loadUrl("file:///android_asset/echarts/map.html");
////支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
//// 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
// 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
//扩大比例的缩放
//        webView.getSettings().setUseWideViewPort(true);
////自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.setInitialScale(100);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        jiaohuMap();//jsjava交互

        return view;
    }

        private static final String[] cityLists = {"巴南", "北新", "北碚", "大渡口", "江北", "九龙坡", "南岸", "沙坪坝", "渝北", "渝中", "大足", "合川", "永川", "綦江", "铜梁", "长寿", "璧山", "江津", "潼南", "万盛", "荣昌", "涪陵", "垫江", "丰都", "武隆", "南川", "黔江", "彭水", "石柱", "秀山", "酉阳", "万州", "奉节", "开县", "梁平", "云阳", "忠县", "城口", "巫山", "巫溪"
    };

    public void setDate(JSONObject date1) {
        this.date = date1;
        int AllDuan = 0;
        int AllUnDuan = 0;
        int AllDuan2G = 0;
        int AllUnDuan2G = 0;
        int AllDuan3G = 0;
        int AllUnDuan3G = 0;
        int AllDuan4G = 0;
        int AllUnDuan4G = 0;

        for (int i = 0; i < cityLists.length; i++) {
            JSONObject cityDate = date.getJSONObject(cityLists[i]);
            if (cityDate != null) {
                int Duan = cityDate.getInteger("ONCEALARM");
                int unDuan = cityDate.getInteger("ALARMUNFIN");
                int Duan2G = cityDate.getInteger("ONCEALARM2G");
                int unDuan2G = cityDate.getInteger("ALARMUNFIN2G");
                int Duan3G = cityDate.getInteger("ONCEALARM3G");
                int unDuan3G = cityDate.getInteger("ALARMUNFIN3G");
                int Duan4G = cityDate.getInteger("ONCEALARM4G");
                int unDuan4G = cityDate.getInteger("ALARMUNFIN4G");
                AllDuan += Duan;
                AllUnDuan += unDuan;
                AllDuan2G += Duan2G;
                AllUnDuan2G += unDuan2G;
                AllDuan3G += Duan3G;
                AllUnDuan3G += unDuan3G;
                AllDuan4G += Duan4G;
                AllUnDuan4G += unDuan4G;
            }
        }

        String a = "" + AllUnDuan;
        String b = "" + AllDuan;
        ss = new SpannableString(a + "/" + b);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#DC143C")), 0, a.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.LTGRAY), a.length(), a.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#4876FF")), a.length() + 1, a.length() + 1 + b.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_over_da.setText(ss);

        String a2 = "" + AllUnDuan2G;
        String b2 = "" + AllDuan2G;
        ss2 = new SpannableString(a2 + "/" + b2);
        ss2.setSpan(new ForegroundColorSpan(Color.parseColor("#DC143C")), 0, a2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss2.setSpan(new ForegroundColorSpan(Color.LTGRAY), a2.length(), a2.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss2.setSpan(new ForegroundColorSpan(Color.parseColor("#4876FF")), a2.length() + 1, a2.length() + 1 + b2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_over_2x.setText(ss2);

        String a3 = "" + AllUnDuan3G;
        String b3 = "" + AllDuan3G;
        ss3 = new SpannableString(a3 + "/" + b3);
        ss3.setSpan(new ForegroundColorSpan(Color.parseColor("#DC143C")), 0, a3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss3.setSpan(new ForegroundColorSpan(Color.LTGRAY), a3.length(), a3.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss3.setSpan(new ForegroundColorSpan(Color.parseColor("#4876FF")), a3.length() + 1, a3.length() + 1 + b3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_over_3x.setText(ss3);

        String a4 = "" + AllUnDuan4G;
        String b4 = "" + AllDuan4G;
        ss4 = new SpannableString(a4 + "/" + b4);
        ss4.setSpan(new ForegroundColorSpan(Color.parseColor("#DC143C")), 0, a4.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss4.setSpan(new ForegroundColorSpan(Color.LTGRAY), a4.length(), a4.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss4.setSpan(new ForegroundColorSpan(Color.parseColor("#4876FF")), a4.length() + 1, a4.length() + 1 + b4.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_over_4x.setText(ss4);
    }

    /**
     * jsjava交互
     */
    private void jiaohuMap() {
        webView.callHandler("MAP", mParam, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {

            }
        });
    }

    @Override
    public void onStop() {
        //// 设置可以支持缩放
//        webView.getSettings().setSupportZoom(false);
// 设置出现缩放工具
//        webView.getSettings().setBuiltInZoomControls(false);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        //// 设置可以支持缩放
//        webView.getSettings().setSupportZoom(false);
// 设置出现缩放工具
//        webView.getSettings().setBuiltInZoomControls(false);
        super.onDestroyView();
    }
}
