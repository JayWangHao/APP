package com.cattsoft.wow.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.framework.view.ProgressDialog;
import com.cattsoft.wow.R;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * 概览fragment
 */
public class OverviewFragment extends Fragment {
    private View view;
    private ImageView leftImg;
    private BridgeWebView wanzhouwebView;
    private BridgeWebView fulinwebView;
    private BridgeWebView jinjiaowebView;
    private BridgeWebView qianjiangwebView;
    private BridgeWebView zhuchengwebView;
    private TextView wanZhou;
    private TextView fuLin;
    private TextView jinJiao;
    private TextView qianJiang;
    private TextView zhuCheng;
    private RelativeLayout dingRelLay;
    private ViewPager viewPager;

    private com.alibaba.fastjson.JSONObject respsoneJson;

    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();

    String result;

    public OverviewFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_overview, container, false);

        initView();

        initDataThread();

        resterlistener();

        return view;

    }

    private void initDataThread() {

        new OverviewThead().start();
    }

    private void initView() {
        leftImg = (ImageView) view.findViewById(R.id.user_name_img);

        wanzhouwebView = (BridgeWebView) view.findViewById(R.id.wanzhouWebView);
        fulinwebView = (BridgeWebView) view.findViewById(R.id.fulinWebView);
        jinjiaowebView = (BridgeWebView) view.findViewById(R.id.jinjiaoWebView);
        qianjiangwebView = (BridgeWebView) view.findViewById(R.id.qianjiangWebView);
        zhuchengwebView = (BridgeWebView) view.findViewById(R.id.zhuchengWebView);

        wanzhouwebView.setDefaultHandler(new DefaultHandler());
        wanzhouwebView.loadUrl("file:///android_asset/echarts/wanzhoubar.html");

        fulinwebView.setDefaultHandler(new DefaultHandler());
        fulinwebView.loadUrl("file:///android_asset/echarts/fulinbar.html");

        jinjiaowebView.setDefaultHandler(new DefaultHandler());
        jinjiaowebView.loadUrl("file:///android_asset/echarts/jinjiaobar.html");

        qianjiangwebView.setDefaultHandler(new DefaultHandler());
        qianjiangwebView.loadUrl("file:///android_asset/echarts/qianjiangbar.html");

        zhuchengwebView.setDefaultHandler(new DefaultHandler());
        zhuchengwebView.loadUrl("file:///android_asset/echarts/zhuchengbar.html");


        wanZhou = (TextView) view.findViewById(R.id.wanTextView);
        fuLin = (TextView) view.findViewById(R.id.fuTextView);
        jinJiao = (TextView) view.findViewById(R.id.jinTextView);
        qianJiang = (TextView) view.findViewById(R.id.qianTextView);
        zhuCheng = (TextView) view.findViewById(R.id.zhuTextView);
        dingRelLay = (RelativeLayout) view.findViewById(R.id.relLay);
        dingRelLay.setVisibility(View.GONE);

        viewPager = (ViewPager) view.findViewById(R.id.viewPage);

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

        wanZhou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageSelected(1);
                viewPager.setCurrentItem(1);
            }
        });
        fuLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageSelected(2);
                viewPager.setCurrentItem(2);
            }
        });
        jinJiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageSelected(3);
                viewPager.setCurrentItem(3);
            }
        });
        qianJiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageSelected(4);
                viewPager.setCurrentItem(4);
            }
        });
        zhuCheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageSelected(5);
                viewPager.setCurrentItem(5);
            }
        });


    }

    private void pageSelected(int position) {
        if (position == 0) {
            dingRelLay.setVisibility(View.GONE);
        } else if (position == 1) {
            dingRelLay.setVisibility(View.VISIBLE);
            wanzhouwebView.setVisibility(View.VISIBLE);
            fulinwebView.setVisibility(View.GONE);
            jinjiaowebView.setVisibility(View.GONE);
            qianjiangwebView.setVisibility(View.GONE);
            zhuchengwebView.setVisibility(View.GONE);

            wanZhou.setTextColor(Color.GREEN);
            fuLin.setTextColor(Color.WHITE);
            jinJiao.setTextColor(Color.WHITE);
            qianJiang.setTextColor(Color.WHITE);
            zhuCheng.setTextColor(Color.WHITE);

            wanZhouBar();

        } else if (position == 2) {
            dingRelLay.setVisibility(View.VISIBLE);
            wanzhouwebView.setVisibility(View.GONE);
            fulinwebView.setVisibility(View.VISIBLE);
            jinjiaowebView.setVisibility(View.GONE);
            qianjiangwebView.setVisibility(View.GONE);
            zhuchengwebView.setVisibility(View.GONE);

            wanZhou.setTextColor(Color.WHITE);
            fuLin.setTextColor(Color.GREEN);
            jinJiao.setTextColor(Color.WHITE);
            qianJiang.setTextColor(Color.WHITE);
            zhuCheng.setTextColor(Color.WHITE);

            fulinBar();

        } else if (position == 3) {
            dingRelLay.setVisibility(View.VISIBLE);
            wanzhouwebView.setVisibility(View.GONE);
            fulinwebView.setVisibility(View.GONE);
            jinjiaowebView.setVisibility(View.VISIBLE);
            qianjiangwebView.setVisibility(View.GONE);
            zhuchengwebView.setVisibility(View.GONE);

            wanZhou.setTextColor(Color.WHITE);
            fuLin.setTextColor(Color.WHITE);
            jinJiao.setTextColor(Color.GREEN);
            qianJiang.setTextColor(Color.WHITE);
            zhuCheng.setTextColor(Color.WHITE);

            jinjiaoBar();

        } else if (position == 4) {
            dingRelLay.setVisibility(View.VISIBLE);
            wanzhouwebView.setVisibility(View.GONE);
            fulinwebView.setVisibility(View.GONE);
            jinjiaowebView.setVisibility(View.GONE);
            qianjiangwebView.setVisibility(View.VISIBLE);
            zhuchengwebView.setVisibility(View.GONE);


            wanZhou.setTextColor(Color.WHITE);
            fuLin.setTextColor(Color.WHITE);
            jinJiao.setTextColor(Color.WHITE);
            qianJiang.setTextColor(Color.GREEN);
            zhuCheng.setTextColor(Color.WHITE);

            qianjiangBar();
        } else if (position == 5) {
            dingRelLay.setVisibility(View.VISIBLE);
            wanzhouwebView.setVisibility(View.GONE);
            fulinwebView.setVisibility(View.GONE);
            jinjiaowebView.setVisibility(View.GONE);
            qianjiangwebView.setVisibility(View.GONE);
            zhuchengwebView.setVisibility(View.VISIBLE);

            wanZhou.setTextColor(Color.WHITE);
            fuLin.setTextColor(Color.WHITE);
            jinJiao.setTextColor(Color.WHITE);
            qianJiang.setTextColor(Color.WHITE);
            zhuCheng.setTextColor(Color.GREEN);

            zhuchengBar();
        }
    }



    private class OverviewThead extends Thread {
        @Override
        public void run() {
            getData();
        }
    }

    public void getData() {
        String url = "tz/TZDeviceAction.do?method=MapInfo";
        Message msg = new Message();

        try {
           result = Communication.getPostResponseForNetManagement(url, "");
            respsoneJson = JSON.parseObject(result);

            if (StringUtil.isBlank(result)) {
                msg.what = 1;
                handler.sendMessage(msg);
                return;
            } else {


                msg.what = 3;
                handler.sendMessage(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
            msg.what = 2;
            handler.sendMessage(msg);
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

                    Toast.makeText(getActivity(), "服务异常！", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 3: {

//在这里把数据传到下一个页面
                    setFragmentList(result);

                    viewPager.setAdapter(new FrgAdapter(getChildFragmentManager()));
                    viewPager.setCurrentItem(0);
                    viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            pageSelected(position);
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });

                    break;
                }
            }
        }

    };

    private void setFragmentList(String result) {
        if (fragmentList.size() == 0) {

            MapFragment mapFragment=new MapFragment();

            AreaChartWanFragment wanFragment=new AreaChartWanFragment();
            AreaChartPeiFragment peiFragment=new AreaChartPeiFragment();
            AreaChartJinFragment jinFragment=new AreaChartJinFragment();
            AreaChartQianFragment qianFragment=new AreaChartQianFragment();
            AreaChartZhuFragment zhuFragment=new AreaChartZhuFragment();

            Bundle data = new Bundle();
            data.putString("result", result);

            mapFragment.setArguments(data);
            wanFragment.setArguments(data);
            peiFragment.setArguments(data);
            jinFragment.setArguments(data);
            qianFragment.setArguments(data);
            zhuFragment.setArguments(data);

            fragmentList.add(mapFragment);

            fragmentList.add(wanFragment);
            fragmentList.add(peiFragment);
            fragmentList.add(jinFragment);
            fragmentList.add(qianFragment);
            fragmentList.add(zhuFragment);
        }


    }
    class FrgAdapter extends FragmentPagerAdapter {
        public FrgAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * 返回的Fragment相当于ViewPager中的一个item
         */
        @Override
        public Fragment getItem(int pos) {
            return fragmentList.get(pos);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

    }


    /**
     * 万州数据交互
     */
    private void wanZhouBar() {
        try {
            wanzhouwebView.callHandler("WanBAR", respsoneJson.toString(), new CallBackFunction() {

                @Override
                public void onCallBack(String data) {

                }

            });
        } catch (NullPointerException e) {
            e.printStackTrace();
            getData();
        }
    }


    /**
     * fulin数据交互
     */
    private void fulinBar() {
        try {
            fulinwebView.callHandler("fuBAR", respsoneJson.toString(), new CallBackFunction() {

                @Override
                public void onCallBack(String data) {

                }

            });
        } catch (NullPointerException e) {
            e.printStackTrace();
            getData();
        }
    }

    /**
     *近郊数据交互
     */
    private void jinjiaoBar() {
        try {
            jinjiaowebView.callHandler("jinBAR", respsoneJson.toString(), new CallBackFunction() {

                @Override
                public void onCallBack(String data) {

                }

            });
        } catch (NullPointerException e) {
            e.printStackTrace();
            getData();
        }
    }


    /**
     * 黔江数据交互
     */
    private void qianjiangBar() {
        try {
            qianjiangwebView.callHandler("qianBAR", respsoneJson.toString(), new CallBackFunction() {

                @Override
                public void onCallBack(String data) {

                }

            });
        } catch (NullPointerException e) {
            e.printStackTrace();
            getData();
        }
    }








    /**
     * 主城数据交互
     */
    private void zhuchengBar() {
        try {
            zhuchengwebView.callHandler("zhuBAR", respsoneJson.toString(), new CallBackFunction() {

                @Override
                public void onCallBack(String data) {

                }

            });
        } catch (NullPointerException e) {
            e.printStackTrace();
            getData();
        }
    }

}
