package com.cattsoft.wow.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.wow.R;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;

/**
 *重庆地图
 */
public class MapFragment extends Fragment {
    private BridgeWebView webView;

    private String mParam;

    private ProgressBar mProgressBar;

    public MapFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_map, container, false);

        if (getArguments() != null) {
            mParam = getArguments().getString("result");

        }

        webView=(BridgeWebView)view.findViewById(R.id.webView);
        mProgressBar = (ProgressBar) view.findViewById(R.id.processBar);


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setMax(100);
                if (newProgress < 100) {
                    if (mProgressBar.getVisibility() == View.INVISIBLE) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setProgress(newProgress);
                } else {
                    mProgressBar.setProgress(100);
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
                super.onProgressChanged(view, newProgress);
            }

        });

        webView.reload();

        webView.setDefaultHandler(new DefaultHandler());

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheMaxSize(1024*1024*10);//设置缓冲大小，10M
        String appCacheDirTwo = getActivity().getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        webView.getSettings().setAppCachePath(appCacheDirTwo);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        webView.loadUrl("file:///android_asset/echarts/map.html");

        jiaohuMap();//jsjava交互

        return view;
    }

    /**
     * jsjava交互
     */
    private void jiaohuMap(){
        webView.callHandler("MAP", mParam, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {

            }
        });
    }

}
