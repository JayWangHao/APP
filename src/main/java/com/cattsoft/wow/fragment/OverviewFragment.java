package com.cattsoft.wow.fragment;


import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.cache.MosApp;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.framework.view.ProgressDialog;
import com.cattsoft.wow.R;
import com.cattsoft.wow.bean.MapBean;
import com.cattsoft.wow.bean.NetEvent;
import com.cattsoft.wow.receiver.NetReceiver;
import com.cattsoft.wow.utils.NetUtils;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

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
    private BridgeWebView webView;

    private TextView wanZhou;
    private TextView fuLin;
    private TextView jinJiao;
    private TextView qianJiang;
    private TextView zhuCheng;
    private RelativeLayout dingRelLay;
    private ViewPager viewPager;
    private RadioGroup rg_content;

    private com.alibaba.fastjson.JSONObject respsoneJson;

    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();

    String result;


    private int flag_item = 0;
    private int flag = 30;//1,7,30
    private ProgressDialog progressDialog;


    private MapBean mapLists;
    private ArrayList<MapBean.CityBean> mapLists1;
    private Boolean flag_updateFm = false;
    private RelativeLayout no_network_rl;
    private NetReceiver mReceiver;
    private boolean isNet = true;

    public OverviewFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_overview, container, false);
        flag_item = 0;
        initView();
//        initReceive();
        initDataThread();

        resterlistener();
        EventBus.getDefault().register(this);
        return view;

    }

    private void initDataThread() {
        progressDialog = new ProgressDialog(OverviewFragment.super.getActivity());
        progressDialog.showProcessDialog();
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
        wanzhouwebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        wanzhouwebView.loadUrl("file:///android_asset/echarts/wanzhoubar.html");

        fulinwebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        fulinwebView.setDefaultHandler(new DefaultHandler());
        fulinwebView.loadUrl("file:///android_asset/echarts/fulinbar.html");

        jinjiaowebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        jinjiaowebView.setDefaultHandler(new DefaultHandler());
        jinjiaowebView.loadUrl("file:///android_asset/echarts/jinjiaobar.html");

        qianjiangwebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        qianjiangwebView.setDefaultHandler(new DefaultHandler());
        qianjiangwebView.loadUrl("file:///android_asset/echarts/qianjiangbar.html");

        zhuchengwebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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


        rg_content = (RadioGroup) view.findViewById(R.id.rg_content_map);
        rg_content.check(R.id.rb_month_map);



        no_network_rl = (RelativeLayout)view.findViewById(R.id.net_view_rl);
        no_network_rl.setVisibility(isNet ? View.GONE : View.VISIBLE);
    }
    public void onEventMainThread(NetEvent event) {

        setNetState(event.isNet());
    }

    public void setNetState(boolean netState) {
        if (netState){
//            Toast.makeText(WoFragment2.super.getActivity(),"网络以连接",Toast.LENGTH_SHORT).show();
            isNet = true;

        }else {
//            Toast.makeText(WoFragment2.super.getActivity(),"网络未连接",Toast.LENGTH_SHORT).show();
            isNet = false;
        }
        if (no_network_rl != null) {
            no_network_rl.setVisibility(netState ? View.GONE : View.VISIBLE);
            no_network_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    NetUtils.startToSettings(OverviewFragment.super.getActivity());
                }
            });
        }
    }



    private void resterlistener() {

        //选择年月日
        rg_content.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_day_map:
                        flag = 1;
                        flag_updateFm = true;
                        initDataThread();
                        break;
                    case R.id.rb_week_map:
                        flag = 7;
                        flag_updateFm = true;
                        initDataThread();
                        break;
                    case R.id.rb_month_map:
                        flag = 30;
                        flag_updateFm = true;
                        initDataThread();
                        break;
                }
            }
        });

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
                flag_item = 1;
            }
        });
        fuLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageSelected(2);
                viewPager.setCurrentItem(2);
                flag_item = 2;

            }
        });
        jinJiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageSelected(3);
                viewPager.setCurrentItem(3);
                flag_item = 3;

            }
        });
        qianJiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageSelected(4);
                viewPager.setCurrentItem(4);
                flag_item = 4;

            }
        });
        zhuCheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageSelected(5);
                viewPager.setCurrentItem(5);
                flag_item = 5;

            }
        });


    }

    private void pageSelected(int position) {
        if (position == 0) {
            dingRelLay.setVisibility(View.GONE);
//            over_biaoge.setVisibility(View.VISIBLE);
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
//            over_biaoge.setVisibility(View.GONE);

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
//            over_biaoge.setVisibility(View.GONE);

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
//            over_biaoge.setVisibility(View.GONE);

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
//            over_biaoge.setVisibility(View.GONE);

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
//            over_biaoge.setVisibility(View.GONE);

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
        JSONObject requestJson = new JSONObject();
        requestJson.put("date", flag);
        try {
            result = Communication.getPostResponseForNetManagement(url, requestJson.toString());
            respsoneJson = JSON.parseObject(result);
//            Log.d("TAG_1","flag:"+flag+"||"+respsoneJson);
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
                    Toast.makeText(OverviewFragment.super.getActivity(), "服务端返回为空！", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 2: {

                    Toast.makeText(OverviewFragment.super.getActivity(), "请求服务端异常！", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 3: {

//在这里把数据传到下一个页面
                    setFragmentList(result);
                    viewPager.setAdapter(new FrgAdapter(getChildFragmentManager()));
                    viewPager.setCurrentItem(flag_item);
                    viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            pageSelected(position);
                            flag_item = position;

                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                    progressDialog.closeProcessDialog();

                    break;
                }
            }

            progressDialog.closeProcessDialog();
            if (flag_updateFm) {
                flag_updateFm = false;
            }
        }

    };

    private void setFragmentList(String result) {
        fragmentList.clear();
        MapFragment mapFragment = new MapFragment();
        AreaChartWanFragment wanFragment = new AreaChartWanFragment();
        AreaChartPeiFragment peiFragment = new AreaChartPeiFragment();
        AreaChartJinFragment jinFragment = new AreaChartJinFragment();
        AreaChartQianFragment qianFragment = new AreaChartQianFragment();
        AreaChartZhuFragment zhuFragment = new AreaChartZhuFragment();

        Bundle data = new Bundle();
        data.putString("result", result);
//        if (fragmentList.size() == 0) {

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
//        }
    }

    class FrgAdapter extends FragmentPagerAdapter {
        FragmentManager fm;

        public FrgAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
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
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            if (flag_updateFm) {
                //得到缓存的fragment
                Fragment fragment = (Fragment) super.instantiateItem(container, position);
//得到tag，这点很重要
                String fragmentTag = fragment.getTag();
//如果这个fragment需要更新
                FragmentTransaction ft = fm.beginTransaction();
//移除旧的fragment
                ft.remove(fragment);
//换成新的fragment
                fragment = fragmentList.get(position);
//添加新fragment时必须用前面获得的tag，这点很重要
                ft.add(container.getId(), fragment, fragmentTag);
                ft.attach(fragment);
                ft.commit();

                return fragment;
            } else {
                return super.instantiateItem(container, position);
            }
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
     * 近郊数据交互
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

    @Override
    public void onDestroyView() {
//        OverviewFragment.super.getActivity().unregisterReceiver(mReceiver);
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
