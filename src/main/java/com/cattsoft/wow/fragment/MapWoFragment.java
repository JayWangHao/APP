package com.cattsoft.wow.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.pub.BaiduService;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.framework.view.ProgressDialog;
import com.cattsoft.framework.view.pullableview.PullToRefreshLayout;
import com.cattsoft.framework.view.pullableview.PullableListView;
import com.cattsoft.wow.R;
import com.cattsoft.wow.activity.RelevanceWarningActivity;
import com.cattsoft.wow.adapter.ActivityWoItemListViewAdapter;
import com.cattsoft.wow.mudels.Wo;
import com.cattsoft.wow.mudels.WoFragmentListViewItemMessageJavaBean;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *工单页面
 */
public class MapWoFragment extends Fragment {

    ViewHolder holder = null;

    private Boolean isInsertData = false;
    private Boolean isClearMessageData = false;
    private Boolean isSubmitMessageData = false;

    //    private StringBuffer returnedmessageData=new StringBuffer();
    String returnedmessageData;

    private TextView queryImgBtn;
    private TextView querytext;
    private String queryTextValue;
    private ImageView user_name;
    private MyWoListAdapter myWoListAdapter;
    private String result;
    private List<Wo> woList = new ArrayList<Wo>();
    private List<WoFragmentListViewItemMessageJavaBean> messageList = new ArrayList<WoFragmentListViewItemMessageJavaBean>();
    private String allCount;//今日总量
    private String finishCount;//已处理
    private String noFinishCount;//未处理
    private PullableListView mlistview;
    private View view;
    private TextView allCountTxt;
    private TextView finishCountTxt;
    private TextView noFinishCountTxt;
    private TextView processingText;
    private TextView pendingText;
    private TextView confirmText;
    private TextView people;
    private LinearLayout finshtimeLin;

    private AlertDialog alertDialog;

    private int currIndex = 0;// 当前页卡编号
    Animation animation = null;
    private int perSpacing = 0; // 每个动画图片间距
    private ImageView cursor;       // 动画图片
    private int screenW;
    private String state = "1";
    /**
     * 页卡头标
     */
    private ArrayList<TextView> pageTitles = new ArrayList<TextView>();
    private ProgressDialog progressDialog;
    private PullToRefreshLayout refreshLayout;
    private Wo itemWo;

    private String vcLowId;
    private String mLongitude;
    private String mLatitude;
    private String resultB;

    private int finallyAllCount = 0;
    private int finallyFinishCount = 0;

    /**
     * 下拉刷新
     */
    public static final int PULL_DOWN_TO_REFRESH = 0;
    /**
     * 上拉加载
     */
    public static final int PULL_UP_TO_LOAD_MORE = 1;
    private int mRefreshMode = PULL_DOWN_TO_REFRESH;
    private int message = 0;//用来标示下拉刷新请求数据、上拉刷新数据、第一次请求数据
    private int pageNo = 0;
    private SharedPreferences sharedPreferences;

    private String userId;
    private String geoId;
    private String vcemosid;

    //  VCEMOSID = 1392235;//这个是你要查看或插入留言的工单的EMOSID
    private String expendedVcemosid;//指要展开的工单


    private String remark;

    private String action;
    private BaiduService baiduService;
    private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
    private int oldPostion = -1;

    private static HashMap<Integer, Boolean> isExpand;//设置item展开或者收缩的初始值

    private ArrayList<String> opinionList = new ArrayList<String>();

    private boolean isClearWoList = false;
    private String cross;//标示是否是最后一页；

    public MapWoFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_wo, container, false);

        if(getArguments()!=null){
            geoId=getArguments().getString("geoId");
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        myLocation(getActivity());
        initView();
        initDataThread(true);
        register();
        return view;
    }

    public void register() {

        queryImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                woList.clear();
                queryTextValue = querytext.getText().toString();
                initDataThread(true);

            }
        });
        processingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = "1";
                processingText.setTextColor(getResources().getColor(R.color.small_title_text_color));
                pendingText.setTextColor(getResources().getColor(R.color.black));
                confirmText.setTextColor(getResources().getColor(R.color.black));
                pageNo = 0;
                if (currIndex == 1) {
                    moveOnetoLeft(perSpacing);      // 从2 到 1
                } else if (currIndex == 2) {
                    moveTwoToLeft(perSpacing);       // 从3 到 1
                }
                currIndex = 0;
                if (animation == null) {

                } else {
                    isClearWoList = true;
                    animation.setFillAfter(true);   // True:图片停在动画结束位置
                    animation.setDuration(300);
                    cursor.startAnimation(animation);
                    initDataThread(true);
                }
            }
        });
        pendingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.clear();
                state = "2";
                processingText.setTextColor(getResources().getColor(R.color.black));
                pendingText.setTextColor(getResources().getColor(R.color.small_title_text_color));
                confirmText.setTextColor(getResources().getColor(R.color.black));
                pageNo = 0;
                if (currIndex == 0) {
                    moveOneToRight(perSpacing);      // 从1 到 2
                } else if (currIndex == 2) {
                    moveOnetoLeft(perSpacing);       // 从 3到 2
                }
                currIndex = 1;
                animation.setFillAfter(true);   // True:图片停在动画结束位置
                animation.setDuration(300);
                cursor.startAnimation(animation);
                isClearWoList = true;
                initDataThread(true);
            }
        });
        confirmText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.clear();
                state = "3";
                processingText.setTextColor(getResources().getColor(R.color.black));
                pendingText.setTextColor(getResources().getColor(R.color.black));
                confirmText.setTextColor(getResources().getColor(R.color.small_title_text_color));
                pageNo = 0;
                if (currIndex == 0) {
                    moveTwoToRight(perSpacing);      // 从 1 到3
                } else if (currIndex == 1) {
                    moveOneToRight(perSpacing);      // 从 2 到3
                }
                currIndex = 2;
                animation.setFillAfter(true);   // True:图片停在动画结束位置
                animation.setDuration(300);
                cursor.startAnimation(animation);
                isClearWoList = true;
                initDataThread(true);
            }
        });


        refreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                isClearWoList = true;
                //下拉刷新
                mRefreshMode = PULL_DOWN_TO_REFRESH;
                message = 1;
                pageNo = 0;
                refreshLayout.setCanLoadMore(true);
                initDataThread(true);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //上拉加载
                mRefreshMode = PULL_UP_TO_LOAD_MORE;
                pageNo = pageNo + 5;
                message = 2;
                isClearWoList = false;
                initDataThread(false);
            }
        });
        user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof SlidingActivityBase) {
                    ((SlidingActivityBase) getActivity()).toggle();
                }
            }
        });


        /*mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                LinearLayout hideLayout = (LinearLayout) view.findViewById(R.id.hide_layout);
                ImageView downImg = (ImageView) view.findViewById(R.id.down_img);
                ImageView upImg = (ImageView) view.findViewById(R.id.up_img);
                TextView accept_wo = (TextView) view.findViewById(R.id.accept_wo);
                TextView accept_wo2 = (TextView) view.findViewById(R.id.accept_wo2);
                TextView warning = (TextView) view.findViewById(R.id.warning);
                TextView warning2 = (TextView) view.findViewById(R.id.warning2);
                TextView start = (TextView) view.findViewById(R.id.start);
                TextView start2 = (TextView) view.findViewById(R.id.start2);
                TextView warning3 = (TextView) view.findViewById(R.id.warning3);
                TextView warning4 = (TextView) view.findViewById(R.id.warning4);
                TextView finsh = (TextView) view.findViewById(R.id.finish);
                TextView finsh2 = (TextView) view.findViewById(R.id.finish2);
                TextView warning5 = (TextView) view.findViewById(R.id.warning5);
                TextView warning6 = (TextView) view.findViewById(R.id.warning6);

                map.put(position, true);
                if ("1".equals(state)) {
                    if (hideLayout.getVisibility() == View.GONE) {
                        hideLayout.setVisibility(View.VISIBLE);
                        downImg.setVisibility(View.GONE);
                        upImg.setVisibility(View.VISIBLE);
                        accept_wo.setVisibility(View.GONE);
                        accept_wo2.setVisibility(View.VISIBLE);
                        warning.setVisibility(View.GONE);
                        warning2.setVisibility(View.VISIBLE);

                    } else {
                        hideLayout.setVisibility(View.GONE);
                        downImg.setVisibility(View.VISIBLE);
                        upImg.setVisibility(View.GONE);
                        accept_wo2.setVisibility(View.GONE);
                        accept_wo.setVisibility(View.VISIBLE);
                        warning2.setVisibility(View.GONE);
                        warning.setVisibility(View.VISIBLE);
                    }
                } else if ("2".equals(state)) {
                    if (hideLayout.getVisibility() == View.GONE) {
                        hideLayout.setVisibility(View.VISIBLE);
                        downImg.setVisibility(View.GONE);
                        upImg.setVisibility(View.VISIBLE);
                        start.setVisibility(View.GONE);
                        start2.setVisibility(View.VISIBLE);
                        warning3.setVisibility(View.GONE);
                        warning4.setVisibility(View.VISIBLE);
                        finsh.setVisibility(View.GONE);
                        finsh2.setVisibility(View.VISIBLE);

                    } else {
                        hideLayout.setVisibility(View.GONE);
                        downImg.setVisibility(View.VISIBLE);
                        upImg.setVisibility(View.GONE);
                        start2.setVisibility(View.GONE);
                        start.setVisibility(View.VISIBLE);
                        warning4.setVisibility(View.GONE);
                        warning3.setVisibility(View.VISIBLE);
                        finsh2.setVisibility(View.GONE);
                        finsh.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (hideLayout.getVisibility() == View.GONE) {
                        hideLayout.setVisibility(View.VISIBLE);
                        downImg.setVisibility(View.GONE);
                        upImg.setVisibility(View.VISIBLE);
                        warning5.setVisibility(View.GONE);
                        warning6.setVisibility(View.VISIBLE);
                    } else {
                        hideLayout.setVisibility(View.GONE);
                        downImg.setVisibility(View.VISIBLE);
                        upImg.setVisibility(View.GONE);
                        warning5.setVisibility(View.VISIBLE);
                        warning6.setVisibility(View.GONE);
                    }
                }
                Wo wo = woList.get(position);
                //对itm点击后展开 或者收缩的处理 start
                if (oldPostion == position) {
                    if (wo.isExpend()) {
                        oldPostion = -1;
                    }
                    woList.get(position).setExpend(false);
                } else {
                    if (oldPostion != -1) {
                        woList.get(oldPostion).setExpend(false);
                    }
                    oldPostion = position;
                    woList.get(oldPostion).setExpend(true);
                }
                myWoListAdapter.notifyDataSetChanged();
                //end
            }
        });*/
    }

    /**
     * 去到定位数据
     * 数据格式
     * 字符串拼接“latitude,longtitude,addr”
     *
     * @param context
     */
    public void myLocation(final Context context) {
        final LocationClient mLocClient = new LocationClient(context);

        mLocClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (location == null) {
                    //Toast.makeText(context, "定位失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer sb = new StringBuffer(256);
                sb.append(location.getLatitude());
                sb.append(",");
                sb.append(location.getLongitude());
                sb.append(",");
                // 能否去到addr 要根据setIsNeedAddress(boolean)这个参数和定位方式决定
                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    //sb.append("");
                    sb.append(location.getAddrStr());
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    sb.append(location.getAddrStr());
                }

                // 事实上这里取不到address，取到的值为null，如果要用address，则需要反编码取到
                // 在导航时，如果起点的经纬度正确，而address为null，不影响正常导航，只是起点名显示为‘无名路’
                // 保存city是为了在‘百度在线建议查询’时 查询所在城市的信息
                // mApplication.locationCity=location.getCity();
                /**
                 * 定位当前位置的经纬度，和当前的地址
                 */
                Log.e(">>>>>>>>", location.getLatitude() + ":la:lo:" + location.getLongitude()
                        + ":add:" + location.getAddrStr());
                mLatitude = location.getLatitude() + "";
                mLongitude = location.getLongitude() + "";
                // 这里停掉,需要的时候再次开启
                mLocClient.stop();
            }
        });
        LocationClientOption option = new LocationClientOption();
        // 高精度定位模式：这种定位模式下，会同时使用网络定位和GPS定位，优先返回最高精度的定位结果；
        // 低功耗定位模式：这种定位模式下，不会使用GPS，只会使用网络定位（Wi-Fi和基站定位）
        // 仅用设备定位模式：这种定位模式下，不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setOpenGps(true);
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll");//设置返回值的坐标类型。
        //当不设此项，或者所设的整数值小于1000（ms）时，采用一次定位模式。每调用一次requestLocation( )，定位SDK会发起一次定位。请求定位与监听结果一一对应。
        //option.setScanSpan(1000 * 60 * 10);// 设置扫描周期 10分钟
        mLocClient.setLocOption(option);
        mLocClient.start();
        mLocClient.requestLocation();
    }

    public void initView() {

        mlistview = (PullableListView) view.findViewById(R.id.listview);
        allCountTxt = (TextView) view.findViewById(R.id.all_count);
        finishCountTxt = (TextView) view.findViewById(R.id.finish_count);
        noFinishCountTxt = (TextView) view.findViewById(R.id.nofinish_count);
        processingText = (TextView) view.findViewById(R.id.processing);
        processingText.setTextColor(getResources().getColor(R.color.small_title_text_color));
        pendingText = (TextView) view.findViewById(R.id.pending);
        confirmText = (TextView) view.findViewById(R.id.confirm);
        refreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
        user_name = (ImageView) view.findViewById(R.id.user_name_img);
        queryImgBtn = (TextView) view.findViewById(R.id.wo_sreach_btn);
        querytext = (TextView) view.findViewById(R.id.sreach_text);

        initPageTitles();

        initImageView();

    }


    private void initDataThread(boolean isprogress) {
        if (isprogress) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.showProcessDialog();
        }
        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                initData(false);
            }
        });
        mThread.start();
    }

    public void initData(boolean flag) {
        JSONObject requestJson = new JSONObject();
        userId = sharedPreferences.getString("userId", "");

        requestJson.put("USERID", userId);
        requestJson.put("geoId", geoId);
        requestJson.put("needpage", "5");
        requestJson.put("presentpage", pageNo);
        requestJson.put("status", state);
        if (queryTextValue != null && !"".equals(queryTextValue)) {
            requestJson.put("FLOWID", queryTextValue);
        }

        String url = "tz/TZDeviceAction.do?method=QuerytFaultlist";
        Message m = new Message();
        try {
            result = Communication.getPostResponseForNetManagement(url, requestJson.toString());
            Log.i("TAG", "数据：" + result);
            if (StringUtil.isBlank(result)) {
                m.what = 1;
                woListHandler.sendMessage(m);
                return;
            }
            getData(result);
            m.what = 3;
            woListHandler.sendMessage(m);
            return;
        } catch (IOException e) {
            e.printStackTrace();
            m.what = 2;
            woListHandler.sendMessage(m);
            return;
        }
    }


    public void getData(String result) {
        JSONObject returnJson = JSON.parseObject(result);
        allCount = returnJson.getString("s1count");
        finishCount = returnJson.getString("s2count");
        noFinishCount = returnJson.getString("s3count");
        cross = returnJson.getString("cross");

        finallyAllCount = Integer.parseInt(allCount) + Integer.parseInt(finishCount) + Integer.parseInt(noFinishCount);
        finallyFinishCount = Integer.parseInt(finishCount) + Integer.parseInt(noFinishCount);
        if (isClearWoList) {
            oldPostion = -1;
            woList.clear();
        }

        JSONArray returnArray = returnJson.getJSONArray("allist");
        for (int i = 0; i < returnArray.size(); i++) {
            JSONObject obj = returnArray.getJSONObject(i);
            Wo wo = Wo.parse(obj);
            woList.add(wo);
        }
    }

    public Handler woListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    Toast.makeText(getActivity(), "服务端返回为空！", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 2: {
                    Toast.makeText(getActivity(), "请求服务端异常！", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 3: {
                    if (mRefreshMode == PULL_DOWN_TO_REFRESH) {
                        myWoListAdapter = new MyWoListAdapter(getActivity(), woList);
                        refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        mlistview.setAdapter(myWoListAdapter);
                    } else {
                        refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }

                    mlistview.setVisibility(View.VISIBLE);
                    myWoListAdapter.notifyDataSetChanged();

                    allCountTxt.setText(String.valueOf(finallyAllCount));
                    finishCountTxt.setText(String.valueOf(finallyFinishCount));
                    noFinishCountTxt.setText(allCount);

                    if ("end".equals(cross)) {
                        refreshLayout.setCanLoadMore(false);
                    } else {
                        refreshLayout.setCanLoadMore(true);
                    }
                    break;
                }
                case 4: {
                    Toast.makeText(getActivity(), "登录调用服务端异常！", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 5: {
                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                    break;
                }


            }
            progressDialog.closeProcessDialog();


        }
    };


    // 初始化页卡(上方)的标题
    private void initPageTitles() {
        pageTitles.clear();
        pageTitles.add(processingText);
        pageTitles.add(pendingText);
        pageTitles.add(confirmText);
    }

    private void initImageView() {
        cursor = (ImageView) view.findViewById(R.id.cursor);
        getScreenWidth();
        Bitmap bmp = Bitmap.createBitmap(perSpacing, 5, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.drawColor(0xFFBC1E28);
        cursor.setImageBitmap(bmp);
        if ("1".equals(state)) {
            setCursorPosition();
        } else if ("2".equals(state)) {
            processingText.setTextColor(getResources().getColor(R.color.black));
            pendingText.setTextColor(getResources().getColor(R.color.small_title_text_color));
            confirmText.setTextColor(getResources().getColor(R.color.black));
            if (currIndex == 0) {
                moveOneToRight(perSpacing);      // 从1 到 2
            } else if (currIndex == 2) {
                moveOnetoLeft(perSpacing);       // 从 3到 2
            }
            currIndex = 1;
            animation.setFillAfter(true);   // True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        } else {
            processingText.setTextColor(getResources().getColor(R.color.black));
            pendingText.setTextColor(getResources().getColor(R.color.black));
            confirmText.setTextColor(getResources().getColor(R.color.small_title_text_color));
            if (currIndex == 0) {
                moveTwoToRight(perSpacing);      // 从 1 到3
            } else if (currIndex == 1) {
                moveOneToRight(perSpacing);      // 从 2 到3
            }
            currIndex = 2;
            animation.setFillAfter(true);   // True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }
    }

    public Object getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenW = dm.widthPixels;       // 获取屏幕宽度
        perSpacing = screenW / 3;
        return perSpacing;
    }

    //设置cursor初始位置
    private void setCursorPosition() {
        Matrix matrix = new Matrix();
        matrix.postTranslate(0, 1);
        cursor.setImageMatrix(matrix);      // 设置动画初始位置
    }

    //向左移动2个单位
    private void moveTwoToLeft(int one) {
        if (pageTitles.get(currIndex).getLeft() > one * 5 / 2) {
            animation = new TranslateAnimation(3 * one, one, 0, 0);
        } else if ((pageTitles.get(currIndex).getLeft() > one * 3 / 2)
                && (pageTitles.get(currIndex).getLeft() < one * 5 / 2)) {
            animation = new TranslateAnimation(2 * one, 0, 0, 0);
        }
    }

    //  向右移动1个单位
    private void moveOneToRight(int one) {
        if (pageTitles.get(currIndex).getLeft() > one * 5 / 2) {
            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) pageTitles
                    .get(0).getLayoutParams();
            params1.leftMargin = pageTitles.get(0).getLeft() - one;
            pageTitles.get(0).setLayoutParams(params1);
            animation = new TranslateAnimation(3 * one, 3 * one, 0, 0);
        } else if ((pageTitles.get(currIndex).getLeft() > one * 3 / 2)
                && (pageTitles.get(currIndex).getLeft() < one * 5 / 2)) {
            animation = new TranslateAnimation(2 * one, 3 * one, 0, 0);
        } else if ((pageTitles.get(currIndex).getLeft() > one / 2)
                && (pageTitles.get(currIndex).getLeft() < one * 3 / 2)) {
            animation = new TranslateAnimation(one, 2 * one, 0, 0);
        } else if (pageTitles.get(currIndex).getLeft() < one / 2) {
            animation = new TranslateAnimation(0, one, 0, 0);
        }
    }

    //向左移动一个单位
    private void moveOnetoLeft(int one) {
        if (pageTitles.get(currIndex).getLeft() > one * 5 / 2) {
            animation = new TranslateAnimation(3 * one, 2 * one, 0, 0);
        } else if ((pageTitles.get(currIndex).getLeft() > one * 3 / 2)
                && (pageTitles.get(currIndex).getLeft() < one * 5 / 2)) {
            animation = new TranslateAnimation(2 * one, one, 0, 0);
        } else if ((pageTitles.get(currIndex).getLeft() > one / 2)
                && (pageTitles.get(currIndex).getLeft() < one * 3 / 2)) {
            animation = new TranslateAnimation(one, 0, 0, 0);
        } else if (pageTitles.get(currIndex).getLeft() < one / 2) {
            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) pageTitles
                    .get(0).getLayoutParams();
            params1.leftMargin = pageTitles.get(0).getLeft() + one;
            pageTitles.get(0).setLayoutParams(params1);
            animation = new TranslateAnimation(0, 0, 0, 0);
        }
    }

    // 向右移2个单位
    private void moveTwoToRight(int one) {
        if (pageTitles.get(currIndex).getLeft() > one * 5 / 2) {
            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) pageTitles
                    .get(0).getLayoutParams();
            params1.leftMargin = pageTitles.get(0).getLeft() - one;
            pageTitles.get(0).setLayoutParams(params1);
            animation = new TranslateAnimation(3 * one, 3 * one, 0, 0);
        } else if ((pageTitles.get(currIndex).getLeft() > one * 3 / 2)
                && (pageTitles.get(currIndex).getLeft() < one * 5 / 2)) {
            animation = new TranslateAnimation(2 * one, 3 * one, 0, 0);
        } else if ((pageTitles.get(currIndex).getLeft() > one / 2)
                && (pageTitles.get(currIndex).getLeft() < one * 3 / 2)) {
            animation = new TranslateAnimation(one, 3 * one, 0, 0);
        } else if (pageTitles.get(currIndex).getLeft() < one / 2) {
            animation = new TranslateAnimation(0, 2 * one, 0, 0);
        }
    }


    public class MyWoListAdapter extends BaseAdapter {
        private Context mcontext = null;
        private EditText editText;
        private EditText editText2;
        public List<Wo> list;
        private boolean nSpreaded = true;//listview item 是否展开
        private int itemnum = -1;

        public MyWoListAdapter(Context context, List<Wo> list) {

            this.mcontext = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {


            if (view == null) {
                view = LayoutInflater.from(this.mcontext).inflate(R.layout.activity_wo_item, null);
                holder = new ViewHolder();
                holder.siteTxt = (TextView) view.findViewById(R.id.site_name);//站点名称
                holder.woTitleTxt = (TextView) view.findViewById(R.id.wo_title);//工单标题
                holder.woDurationTxt = (TextView) view.findViewById(R.id.wo_duration);//工单历时
                holder.distributePresonTxt = (TextView) view.findViewById(R.id.distribute_person);//派发人员
                holder.distributeTimeTxt = (TextView) view.findViewById(R.id.distribute_time);//派发时间
                holder.noticeTimeTxt = (TextView) view.findViewById(R.id.notic_time);//通知时间
                holder.woContentTxt = (TextView) view.findViewById(R.id.wo_content);//工单内容
                holder.emosStepTxt = (TextView) view.findViewById(R.id.emos_step);//EMOS环节
                holder.warningLevelTxt = (TextView) view.findViewById(R.id.warning_level);//工单等级

                holder.acceptPersonTxt = (TextView) view.findViewById(R.id.accept_person);//待受理人
                holder.emosid = (TextView) view.findViewById(R.id.emosid);//EmosId
                holder.overTimeTxt = (TextView) view.findViewById(R.id.isovertime);//是否超时
                holder.npromptTimeTex = (TextView) view.findViewById(R.id.nprompttime);//催单次数
                holder.acceptTanceTxt = (TextView) view.findViewById(R.id.acceptance);//受理时间
                holder.finishTimeTxt = (TextView) view.findViewById(R.id.finishtime);//完成时间
                holder.faultLevelTxt = (TextView) view.findViewById(R.id.faultlevel);// 故障单等级

                holder.vip = (ImageView) view.findViewById(R.id.vip);
                holder.alarm = (ImageView) view.findViewById(R.id.alarm);

                //留言的控件初始化
                holder.lv_message = (ListView) view.findViewById(R.id.lv_message);//指留言的listView列表
                holder.et_message = (EditText) view.findViewById(R.id.et_message);//
                //holder.et_message.setText("输入留言内容");
                holder.btn_message_submit = (Button) view.findViewById(R.id.btn_message_submit);//

                holder. hide_text=(LinearLayout) view.findViewById(R.id.hide_text);//
                holder. ll_down=(LinearLayout) view.findViewById(R.id.ll_down);//
                holder. rel_up=(LinearLayout) view.findViewById(R.id.rel_up);//

                holder.leftLinlay=(LinearLayout) view.findViewById(R.id.left_linlay);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            final ImageView downImg = (ImageView) view.findViewById(R.id.down_img);
            final ImageView upImg = (ImageView) view.findViewById(R.id.up_img);
            final TextView jiTextView = (TextView) view.findViewById(R.id.ji_text_view);
            final LinearLayout hideLayout = (LinearLayout) view.findViewById(R.id.hide_layout);
            people = (TextView) view.findViewById(R.id.people);
            finshtimeLin = (LinearLayout) view.findViewById(R.id.finishTimeLinlay);


            //待受理
            final RelativeLayout linearLayout1 = (RelativeLayout) view.findViewById(R.id.layout_01);
            final RelativeLayout linearLayout2 = (RelativeLayout) view.findViewById(R.id.layout_02);
            final TextView accept_wo = (TextView) view.findViewById(R.id.accept_wo);
            final TextView warning = (TextView) view.findViewById(R.id.warning);
            final TextView accept_wo2 = (TextView) view.findViewById(R.id.accept_wo2);
            final TextView warning2 = (TextView) view.findViewById(R.id.warning2);
            //处理中
            final RelativeLayout linearLayout3 = (RelativeLayout) view.findViewById(R.id.layout_11);
            final RelativeLayout linearLayout4 = (RelativeLayout) view.findViewById(R.id.layout_12);
            final TextView start = (TextView) view.findViewById(R.id.start);
            final TextView warning3 = (TextView) view.findViewById(R.id.warning3);
            final TextView finsh = (TextView) view.findViewById(R.id.finish);
            final TextView start2 = (TextView) view.findViewById(R.id.start2);
            final TextView warning4 = (TextView) view.findViewById(R.id.warning4);
            final TextView finsh2 = (TextView) view.findViewById(R.id.finish2);
            //待确认
            final RelativeLayout linearLayout5 = (RelativeLayout) view.findViewById(R.id.layout_21);
            final RelativeLayout linearLayout6 = (RelativeLayout) view.findViewById(R.id.layout_22);
            final TextView warning5 = (TextView) view.findViewById(R.id.warning5);
            final TextView warning6 = (TextView) view.findViewById(R.id.warning6);

            Wo wo = list.get(i);
            final String eMosId = wo.geteMosId();
            vcemosid = eMosId;
            holder.siteTxt.setText(wo.getVcLogids());
            holder.woTitleTxt.setText(wo.getVcTitle());
            holder.woDurationTxt.setText(wo.getcTime());
            holder.distributePresonTxt.setText(wo.getDisPathPerson());
            holder.distributeTimeTxt.setText(wo.getDsendTime());
            holder.noticeTimeTxt.setText(wo.getDnoiifyTime());
            holder.woContentTxt.setText(wo.getVcConnect());
            holder.emosStepTxt.setText(wo.getNcurrlink());
            holder.acceptPersonTxt.setText(wo.getVcAccperiPersonList());
            holder.emosid.setText(wo.geteMosId());
            holder.overTimeTxt.setText(wo.getNiSoutTime());
            holder.npromptTimeTex.setText(wo.getNpromptTimes());
            holder.acceptTanceTxt.setText(wo.getDacceptTime());
            holder.finishTimeTxt.setText(wo.getFinishTime());
            holder.faultLevelTxt.setText(wo.getNfaultLevel());

            //左侧工单等级
            if ("1".equals(wo.getNfaultLevel())) {
                holder.warningLevelTxt.setText("一级");
            } else if ("2".equals(wo.getNfaultLevel())) {
                holder.warningLevelTxt.setText("二级");
            } else {
                holder.warningLevelTxt.setText("三级");
            }

            if ("1".equals(wo.getVip())) {//判断是否是VIP
                holder.vip.setVisibility(View.VISIBLE);
                holder.vip.setImageResource(R.drawable.vip);
            }

            if ("1".equals(wo.getNeedAlarm())) {//判断是否超时
                holder.alarm.setVisibility(View.VISIBLE);
                holder.alarm.setImageResource(R.drawable.alam);
                //摇摆
                TranslateAnimation alphaAnimation2 = new TranslateAnimation(-4f, 2f, -2f, 2f);
                alphaAnimation2.setDuration(300);
                alphaAnimation2.setRepeatCount(Animation.INFINITE);
                alphaAnimation2.setRepeatMode(Animation.REVERSE);
                holder.alarm.setAnimation(alphaAnimation2);
                alphaAnimation2.start();
            }

//            if(map !=null && map.size()>0){
//
//                if(!map.containsKey(i) || !map.get(i)){
//                    hideLayout.setVisibility(View.GONE);
//                    downImg.setVisibility(View.VISIBLE);
//                    upImg.setVisibility(View.GONE);
//                    accept_wo2.setVisibility(View.GONE);
//                    accept_wo.setVisibility(View.VISIBLE);
//                    warning2.setVisibility(View.GONE);
//                    warning.setVisibility(View.VISIBLE);
//                }
//            }
//
            final boolean flag = wo.isExpend();
            if (flag) {
                hideLayout.setVisibility(View.VISIBLE);
                downImg.setVisibility(View.GONE);
                upImg.setVisibility(View.VISIBLE);
                accept_wo2.setVisibility(View.VISIBLE);
                accept_wo.setVisibility(View.GONE);
                warning2.setVisibility(View.VISIBLE);
                warning.setVisibility(View.GONE);

                Wo expendedWo = woList.get(i);
                String expendedEMosId = expendedWo.geteMosId();

                expendedVcemosid = expendedEMosId;

                //在这里请求留言的数据
                isClearMessageData = true;
                isInsertData = false;
                initMessageDataThread();


            } else {
                hideLayout.setVisibility(View.GONE);
                downImg.setVisibility(View.VISIBLE);
                upImg.setVisibility(View.GONE);
                accept_wo2.setVisibility(View.GONE);
                accept_wo.setVisibility(View.VISIBLE);
                warning2.setVisibility(View.GONE);
                warning.setVisibility(View.VISIBLE);
            }


            if ("1".equals(state)) {//待处理
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                linearLayout3.setVisibility(View.GONE);
                linearLayout4.setVisibility(View.GONE);
                finshtimeLin.setVisibility(View.GONE);
                action = "2";


            } else if ("2".equals(state)) {//处理中
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.GONE);
                linearLayout3.setVisibility(View.VISIBLE);
                linearLayout4.setVisibility(View.VISIBLE);
                downImg.setImageResource(R.drawable.xia);
                upImg.setImageResource(R.drawable.shang);
                finshtimeLin.setVisibility(View.GONE);
                people.setText("受理人：");
                jiTextView.setBackgroundResource(R.color.wo_button_blue);

                if ("1".equals(wo.getVcdeallink())) {//判断可是点了“开始”
                    start.setBackgroundResource(R.color.wo_button_blue_selected);
                    start2.setBackgroundResource(R.color.wo_button_blue_selected);
                    finsh.setBackgroundResource(R.color.wo_button_blue);
                    finsh2.setBackgroundResource(R.color.wo_button_blue);
                } else {
                }

//                view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        if (hideLayout.getVisibility()==View.GONE) {
//                            hideLayout.setVisibility(View.VISIBLE);
//                            downImg.setVisibility(View.GONE);
//                            upImg.setVisibility(View.VISIBLE);
//                            start.setVisibility(View.GONE);
//                            start2.setVisibility(View.VISIBLE);
//                            warning3.setVisibility(View.GONE);
//                            warning4.setVisibility(View.VISIBLE);
//                            finsh.setVisibility(View.GONE);
//                            finsh2.setVisibility(View.VISIBLE);
//
//                        } else {
//                            hideLayout.setVisibility(View.GONE);
//                            downImg.setVisibility(View.VISIBLE);
//                            upImg.setVisibility(View.GONE);
//                            start2.setVisibility(View.GONE);
//                            start.setVisibility(View.VISIBLE);
//                            warning4.setVisibility(View.GONE);
//                            warning3.setVisibility(View.VISIBLE);
//                            finsh2.setVisibility(View.GONE);
//                            finsh.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });

            } else {//待确认
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.GONE);
                linearLayout3.setVisibility(View.GONE);
                linearLayout4.setVisibility(View.GONE);
                linearLayout5.setVisibility(View.VISIBLE);
                linearLayout6.setVisibility(View.VISIBLE);
                people.setText("受理人：");
                jiTextView.setBackgroundResource(R.color.wo_text_green);
                downImg.setImageResource(R.drawable.greendown);
                upImg.setImageResource(R.drawable.greenup);

//                view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        if (hideLayout.getVisibility() == View.GONE) {
//                            hideLayout.setVisibility(View.VISIBLE);
//                            downImg.setVisibility(View.GONE);
//                            upImg.setVisibility(View.VISIBLE);
//                            warning5.setVisibility(View.GONE);
//                            warning6.setVisibility(View.VISIBLE);
//                        } else {
//                            hideLayout.setVisibility(View.GONE);
//                            downImg.setVisibility(View.VISIBLE);
//                            upImg.setVisibility(View.GONE);
//                            warning5.setVisibility(View.VISIBLE);
//                            warning6.setVisibility(View.GONE);
//                        }
//                    }
//                });
            }

            downImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideLayout.setVisibility(View.GONE);
                    downImg.setVisibility(View.GONE);
                    upImg.setVisibility(View.VISIBLE);
                    accept_wo.setVisibility(View.GONE);
                    accept_wo2.setVisibility(View.VISIBLE);
                    warning.setVisibility(View.GONE);
                    warning2.setVisibility(View.VISIBLE);
                }
            });
//小图标的收缩
            upImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideLayout.setVisibility(View.GONE);
                    downImg.setVisibility(View.VISIBLE);
                    upImg.setVisibility(View.GONE);
                    accept_wo2.setVisibility(View.GONE);
                    accept_wo.setVisibility(View.VISIBLE);
                    warning2.setVisibility(View.GONE);
                    warning.setVisibility(View.VISIBLE);
                }
            });
            accept_wo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemWo = list.get(i);
                    new AlertDialog.Builder(getActivity()).setTitle("受理接单确认")
                            .setMessage("提示：点击确认后其他人将无法接该工单，您确定接故障标题为\"" + list.get(i).getVcTitle() + "\"的故障工单吗？")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    vcLowId = itemWo.getVcFlowid();
                                    new initThread().start();   // 线程请求数据
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();

                }
            });
            accept_wo2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemWo = list.get(i);
                    new AlertDialog.Builder(getActivity()).setTitle("受理接单确认")
                            .setMessage("提示：点击确认后其他人将无法接该工单，您确定接故障标题为\"" + list.get(i).getVcTitle() + "\"的故障工单吗？")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    vcLowId = itemWo.getVcFlowid();
                                    new initThread().start();   // 线程请求数据
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                }
            });
            warning.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), RelevanceWarningActivity.class);
                    intent.putExtra("emosid", eMosId);
                    startActivity(intent);
                }
            });
            warning2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mcontext, RelevanceWarningActivity.class);
                    intent.putExtra("emosid", eMosId);
                    mcontext.startActivity(intent);
                }
            });
            warning3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mcontext, RelevanceWarningActivity.class);
                    intent.putExtra("emosid", eMosId);
                    mcontext.startActivity(intent);
                }
            });
            warning4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mcontext, RelevanceWarningActivity.class);
                    intent.putExtra("emosid", eMosId);
                    mcontext.startActivity(intent);
                }
            });
            warning5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mcontext, RelevanceWarningActivity.class);
                    intent.putExtra("emosid", eMosId);
                    mcontext.startActivity(intent);
                }
            });
            warning6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mcontext, RelevanceWarningActivity.class);
                    intent.putExtra("emosid", eMosId);
                    mcontext.startActivity(intent);
                }
            });
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemWo = list.get(i);
                    action = "2";
                    Drawable background = start.getBackground();
                    ColorDrawable colorDrawable = (ColorDrawable) background;
                    int color = colorDrawable.getColor();
                    if (color == -13324289) {
                        new AlertDialog.Builder(getActivity()).setTitle("开始处理确认")
                                .setMessage("提示：您已确定到故障发生位置了吗？\n点击\"确认\"确认故障发生位置！")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        vcLowId = itemWo.getVcFlowid();
                                        new initThread().start();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .show();
                    } else {

                    }
                }
            });
            start2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemWo = list.get(i);
                    action = "2";
                    Drawable background = start2.getBackground();
                    ColorDrawable colorDrawable = (ColorDrawable) background;
                    int color = colorDrawable.getColor();
                    Log.i("TAG", "开始颜色" + color);
                    if (color == -13324289) {
                        new AlertDialog.Builder(getActivity()).setTitle("开始处理确认")
                                .setMessage("提示：您已确定到故障发生位置了吗？\n点击\"确认\"确认故障发生位置！")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        /*start.setBackgroundResource(R.color.wo_button_blue_selected);
                                        start2.setBackgroundResource(R.color.wo_button_blue_selected);
                                        finsh.setBackgroundResource(R.color.wo_button_blue);
                                        finsh2.setBackgroundResource(R.color.wo_button_blue);*/
                                        vcLowId = itemWo.getVcFlowid();
                                        new initThread().start();

                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .show();
                    } else {

                    }


                }
            });
            finsh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemWo = list.get(i);
                    action = "3";
                    final View viewDia = LayoutInflater.from(getActivity()).inflate(R.layout.custom_dialog, null);
                    editText = (EditText) viewDia.findViewById(R.id.contentText);
                    editText.setText("故障已恢复，请确认！");

                    Drawable background = finsh.getBackground();
                    ColorDrawable colorDrawable = (ColorDrawable) background;
                    int color = colorDrawable.getColor();
                    if (color == -13324289) {
                        new AlertDialog.Builder(getActivity()).setTitle("工单完成确认")
                                .setMessage("提示：点击完成后故障单将等待派单人员确认故障恢复，这期间无法再操作工单，您确认工单完成吗？")
                                .setView(viewDia)
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        vcLowId = itemWo.getVcFlowid();
                                        remark = editText.getText().toString();
                                        new initThread().start();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .show();
                    } else {

                    }
                }
            });
            finsh2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemWo = list.get(i);
                    action = "3";
                    final View viewDia = LayoutInflater.from(getActivity()).inflate(R.layout.custom_dialog, null);
                    editText2 = (EditText) viewDia.findViewById(R.id.contentText);

                    editText2.setText("故障已恢复，请确认！");

                    Drawable background = finsh2.getBackground();
                    ColorDrawable colorDrawable = (ColorDrawable) background;
                    int color = colorDrawable.getColor();
                    Log.i("TAG", "颜色：" + color);
                    if (color == -13324289) {
                        new AlertDialog.Builder(getActivity()).setTitle("工单完成确认")
                                .setMessage("提示：点击完成后故障单将等待派单人员确认故障恢复，这期间无法再操作工单，您确认工单完成吗？")
                                .setView(viewDia)
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        vcLowId = itemWo.getVcFlowid();
                                        remark = editText2.getText().toString();
                                        new initThread().start();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .show();
                    } else {

                    }
                }

            });


            holder.leftLinlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    map.put(i, true);
                    if ("1".equals(state)) {
                        if (hideLayout.getVisibility() == View.GONE) {
                            hideLayout.setVisibility(View.VISIBLE);
                            downImg.setVisibility(View.GONE);
                            upImg.setVisibility(View.VISIBLE);
                            accept_wo.setVisibility(View.GONE);
                            accept_wo2.setVisibility(View.VISIBLE);
                            warning.setVisibility(View.GONE);
                            warning2.setVisibility(View.VISIBLE);

                        } else {
                            hideLayout.setVisibility(View.GONE);
                            downImg.setVisibility(View.VISIBLE);
                            upImg.setVisibility(View.GONE);
                            accept_wo2.setVisibility(View.GONE);
                            accept_wo.setVisibility(View.VISIBLE);
                            warning2.setVisibility(View.GONE);
                            warning.setVisibility(View.VISIBLE);
                        }
                    } else if ("2".equals(state)) {
                        if (hideLayout.getVisibility() == View.GONE) {
                            hideLayout.setVisibility(View.VISIBLE);
                            downImg.setVisibility(View.GONE);
                            upImg.setVisibility(View.VISIBLE);
                            start.setVisibility(View.GONE);
                            start2.setVisibility(View.VISIBLE);
                            warning3.setVisibility(View.GONE);
                            warning4.setVisibility(View.VISIBLE);
                            finsh.setVisibility(View.GONE);
                            finsh2.setVisibility(View.VISIBLE);

                        } else {
                            hideLayout.setVisibility(View.GONE);
                            downImg.setVisibility(View.VISIBLE);
                            upImg.setVisibility(View.GONE);
                            start2.setVisibility(View.GONE);
                            start.setVisibility(View.VISIBLE);
                            warning4.setVisibility(View.GONE);
                            warning3.setVisibility(View.VISIBLE);
                            finsh2.setVisibility(View.GONE);
                            finsh.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (hideLayout.getVisibility() == View.GONE) {
                            hideLayout.setVisibility(View.VISIBLE);
                            downImg.setVisibility(View.GONE);
                            upImg.setVisibility(View.VISIBLE);
                            warning5.setVisibility(View.GONE);
                            warning6.setVisibility(View.VISIBLE);
                        } else {
                            hideLayout.setVisibility(View.GONE);
                            downImg.setVisibility(View.VISIBLE);
                            upImg.setVisibility(View.GONE);
                            warning5.setVisibility(View.VISIBLE);
                            warning6.setVisibility(View.GONE);
                        }
                    }
                    Wo wo = woList.get(i);
                    //对itm点击后展开 或者收缩的处理 start
                    if (oldPostion == i) {
                        if (wo.isExpend()) {
                            oldPostion = -1;
                        }
                        woList.get(i).setExpend(false);
                    } else {
                        if (oldPostion != -1) {
                            woList.get(oldPostion).setExpend(false);
                        }
                        oldPostion = i;
                        woList.get(oldPostion).setExpend(true);
                    }
                    myWoListAdapter.notifyDataSetChanged();
                }
            });


            holder.rel_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    map.put(i, true);
                    if ("1".equals(state)) {
                        if (hideLayout.getVisibility() == View.GONE) {
                            hideLayout.setVisibility(View.VISIBLE);
                            downImg.setVisibility(View.GONE);
                            upImg.setVisibility(View.VISIBLE);
                            accept_wo.setVisibility(View.GONE);
                            accept_wo2.setVisibility(View.VISIBLE);
                            warning.setVisibility(View.GONE);
                            warning2.setVisibility(View.VISIBLE);

                        } else {
                            hideLayout.setVisibility(View.GONE);
                            downImg.setVisibility(View.VISIBLE);
                            upImg.setVisibility(View.GONE);
                            accept_wo2.setVisibility(View.GONE);
                            accept_wo.setVisibility(View.VISIBLE);
                            warning2.setVisibility(View.GONE);
                            warning.setVisibility(View.VISIBLE);
                        }
                    } else if ("2".equals(state)) {
                        if (hideLayout.getVisibility() == View.GONE) {
                            hideLayout.setVisibility(View.VISIBLE);
                            downImg.setVisibility(View.GONE);
                            upImg.setVisibility(View.VISIBLE);
                            start.setVisibility(View.GONE);
                            start2.setVisibility(View.VISIBLE);
                            warning3.setVisibility(View.GONE);
                            warning4.setVisibility(View.VISIBLE);
                            finsh.setVisibility(View.GONE);
                            finsh2.setVisibility(View.VISIBLE);

                        } else {
                            hideLayout.setVisibility(View.GONE);
                            downImg.setVisibility(View.VISIBLE);
                            upImg.setVisibility(View.GONE);
                            start2.setVisibility(View.GONE);
                            start.setVisibility(View.VISIBLE);
                            warning4.setVisibility(View.GONE);
                            warning3.setVisibility(View.VISIBLE);
                            finsh2.setVisibility(View.GONE);
                            finsh.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (hideLayout.getVisibility() == View.GONE) {
                            hideLayout.setVisibility(View.VISIBLE);
                            downImg.setVisibility(View.GONE);
                            upImg.setVisibility(View.VISIBLE);
                            warning5.setVisibility(View.GONE);
                            warning6.setVisibility(View.VISIBLE);
                        } else {
                            hideLayout.setVisibility(View.GONE);
                            downImg.setVisibility(View.VISIBLE);
                            upImg.setVisibility(View.GONE);
                            warning5.setVisibility(View.VISIBLE);
                            warning6.setVisibility(View.GONE);
                        }
                    }
                    Wo wo = woList.get(i);
                    //对itm点击后展开 或者收缩的处理 start
                    if (oldPostion == i) {
                        if (wo.isExpend()) {
                            oldPostion = -1;
                        }
                        woList.get(i).setExpend(false);
                    } else {
                        if (oldPostion != -1) {
                            woList.get(oldPostion).setExpend(false);
                        }
                        oldPostion = i;
                        woList.get(oldPostion).setExpend(true);
                    }
                    myWoListAdapter.notifyDataSetChanged();


                }
            });
//            holder.ll_down.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                    Boolean isScroll=false;
//                    switch (motionEvent.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//
//
//                            isScroll=false;
//
//                            break;
//                        case MotionEvent.ACTION_MOVE:
//                       isScroll=true;
//                            break;
//                        case MotionEvent.ACTION_UP:
//                            isScroll=false;
//                            break;
//                        default:
//                            break;
//                    }
//                    return isScroll;
//                }
//            });



            holder.ll_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//
                    map.put(i, true);
                    if ("1".equals(state)) {
                        if (hideLayout.getVisibility() == View.GONE) {
                            hideLayout.setVisibility(View.VISIBLE);
                            downImg.setVisibility(View.GONE);
                            upImg.setVisibility(View.VISIBLE);
                            accept_wo.setVisibility(View.GONE);
                            accept_wo2.setVisibility(View.VISIBLE);
                            warning.setVisibility(View.GONE);
                            warning2.setVisibility(View.VISIBLE);

                        } else {
                            hideLayout.setVisibility(View.GONE);
                            downImg.setVisibility(View.VISIBLE);
                            upImg.setVisibility(View.GONE);
                            accept_wo2.setVisibility(View.GONE);
                            accept_wo.setVisibility(View.VISIBLE);
                            warning2.setVisibility(View.GONE);
                            warning.setVisibility(View.VISIBLE);
                        }
                    } else if ("2".equals(state)) {
                        if (hideLayout.getVisibility() == View.GONE) {
                            hideLayout.setVisibility(View.VISIBLE);
                            downImg.setVisibility(View.GONE);
                            upImg.setVisibility(View.VISIBLE);
                            start.setVisibility(View.GONE);
                            start2.setVisibility(View.VISIBLE);
                            warning3.setVisibility(View.GONE);
                            warning4.setVisibility(View.VISIBLE);
                            finsh.setVisibility(View.GONE);
                            finsh2.setVisibility(View.VISIBLE);

                        } else {
                            hideLayout.setVisibility(View.GONE);
                            downImg.setVisibility(View.VISIBLE);
                            upImg.setVisibility(View.GONE);
                            start2.setVisibility(View.GONE);
                            start.setVisibility(View.VISIBLE);
                            warning4.setVisibility(View.GONE);
                            warning3.setVisibility(View.VISIBLE);
                            finsh2.setVisibility(View.GONE);
                            finsh.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (hideLayout.getVisibility() == View.GONE) {
                            hideLayout.setVisibility(View.VISIBLE);
                            downImg.setVisibility(View.GONE);
                            upImg.setVisibility(View.VISIBLE);
                            warning5.setVisibility(View.GONE);
                            warning6.setVisibility(View.VISIBLE);
                        } else {
                            hideLayout.setVisibility(View.GONE);
                            downImg.setVisibility(View.VISIBLE);
                            upImg.setVisibility(View.GONE);
                            warning5.setVisibility(View.VISIBLE);
                            warning6.setVisibility(View.GONE);
                        }
                    }
                    Wo wo = woList.get(i);
                    //对itm点击后展开 或者收缩的处理 start
                    if (oldPostion == i) {
                        if (wo.isExpend()) {
                            oldPostion = -1;
                        }
                        woList.get(i).setExpend(false);
                    } else {
                        if (oldPostion != -1) {
                            woList.get(oldPostion).setExpend(false);
                        }
                        oldPostion = i;
                        woList.get(oldPostion).setExpend(true);
                    }
                    myWoListAdapter.notifyDataSetChanged();
                    //end
                }



            });

            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    Log.i("TAG", "afterTextChanged：" + s);

                    returnedmessageData = new String(s.toString());

                }
            };

            holder.et_message.addTextChangedListener(textWatcher);

            holder.btn_message_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    isSubmitMessageData = true;
//
                    isClearMessageData = true;

                    isInsertData = true;

                    if ("".equals(returnedmessageData)||returnedmessageData==null) {
                        Toast.makeText(getActivity(), "留言内容不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }




                    initMessageDataThread();

                    //提交之后怎么清空EditeText


                }

            });

            holder.lv_message.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(motionEvent.getAction()==MotionEvent.ACTION_MOVE){
                        mlistview.requestDisallowInterceptTouchEvent(true);
                    }
                    return false;
                }
            });


            return view;//18616718850
        }


        private class initThread extends Thread {
            @Override
            public void run() {
                request();
            }
        }

        private void request() {

            String url = "tz/TZDeviceAction.do?method=QueryFaultdetailOperating";
            userId = sharedPreferences.getString("userId", "");
            String coor = mLatitude + "," + mLongitude;
            if (coor == null) {
                Toast.makeText(getActivity(), "定位失败，请检查网络或开启位置信息", Toast.LENGTH_SHORT).show();
            }

            // 把条件数据以JSON形式存储好
            Message msg = new Message();
            try {
                JSONObject jsonParmter = new JSONObject();

                jsonParmter.put("state", state);
                jsonParmter.put("action", action);
                jsonParmter.put("COOR", coor);
                jsonParmter.put("FID", vcLowId);     //工单
                jsonParmter.put("USERID", userId);
                jsonParmter.put("EMOSID", vcemosid);
                if (action.equals("3")) {
                    jsonParmter.put("REMARK", remark);
                }

                String parameter = jsonParmter.toString();
                resultB = Communication.getPostResponseForNetManagement(url, parameter);
                msg.what = 1;
                handler.sendMessage(msg);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 用于接收线程发送的消息
        private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1: {
                        handleResult(resultB);
                        break;
                    }
                }
            }
        };

        private void handleResult(String result) {
            //处理数据，解析数据
            JSONObject resultJson = JSON.parseObject(result);

            if ("成功".equals(resultJson.getString("result"))) {
                new AlertDialog.Builder(getActivity()).setTitle("处理信息")
                        .setMessage(resultJson.getString("reason"))
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
                //Toast.makeText(mcontext,""+ resultJson.getString("reason"), Toast.LENGTH_SHORT).show();

                queryTextValue = "";
                mRefreshMode = PULL_DOWN_TO_REFRESH;
                message = 1;
                //pageNo = 0;
                refreshLayout.setCanLoadMore(true);
                woList.clear();
                initDataThread(true);

            } else {
                new AlertDialog.Builder(getActivity()).setTitle("处理信息")
                        .setMessage("失败原因：" + resultJson.getString("reason"))
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
                /*Toast toast=Toast.makeText(mcontext, "失败原因：" + resultJson.getString("reason"), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();*/
                queryTextValue = "";
                mRefreshMode = PULL_DOWN_TO_REFRESH;
                message = 1;
                //pageNo = 0;
                refreshLayout.setCanLoadMore(true);
                woList.clear();
                initDataThread(true);
            }

        }
    }


    public class ViewHolder {
        TextView siteTxt;//站点名称
        TextView woTitleTxt;//工单标题
        TextView woDurationTxt;//工单历时
        TextView distributePresonTxt;//派发人员
        TextView distributeTimeTxt;//派发时间
        TextView noticeTimeTxt;//通知时间
        TextView woContentTxt;//工单内容
        TextView emosStepTxt;//EMOS环节
        TextView acceptPersonTxt;//待受理人
        TextView emosid;//EmosId
        TextView overTimeTxt;//是否超时
        TextView npromptTimeTex;//催单次数
        TextView acceptTanceTxt;//受理时间
        TextView finishTimeTxt;//完成时间
        TextView faultLevelTxt;//故障单等级
        ImageView vip;//VIP图标
        ImageView alarm;//闹钟图标
        TextView warningLevelTxt;//工单等级

        ListView lv_message;//
        EditText et_message;//
        Button btn_message_submit;//

        LinearLayout   hide_text;
        LinearLayout rel_up;
        LinearLayout ll_down;
        LinearLayout leftLinlay;
    }

    public static HashMap<Integer, Boolean> getIsExpand() {
        return isExpand;
    }


    private void initMessageDataThread() {
        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                initMessageData();
            }
        });
        mThread.start();
    }

    public void initMessageData() {

        JSONObject requestJson = new JSONObject();
        userId = sharedPreferences.getString("userId", "");

        if (isInsertData) {
            requestJson.put("INSERT", "1");//INSERT=0//为查看留言 INSERT=1//为插入留言
        } else {
            requestJson.put("INSERT", "0");
        }

        requestJson.put("ISNO", "1");//ISNO是是否审核,默认都传1
        requestJson.put("UserID", userId);

        if (StringUtil.isBlank(expendedVcemosid)) {
            return;
        } else {
            Log.i("TAG", "expendedVcemosid：" + expendedVcemosid);
            requestJson.put("VCEMOSID", expendedVcemosid);//这个是你要查看或插入留言的工单的EMOSID

        }

        if (isInsertData) {
            requestJson.put("VCMESSAGE", returnedmessageData);

            returnedmessageData = new String();
            isInsertData = false;
        } else {

        }

        String url = "tz/TZDeviceAction.do?method=MessageInfo";
        Message m = new Message();
        try {
            Log.i("TAG", "数据：" + requestJson.toString());
            result = Communication.getPostResponseForNetManagement(url, requestJson.toString());
            Log.i("TAG", "数据：" + result);

            if (StringUtil.isBlank(result)) {
                m.what = 1;
                messageListHandler.sendMessage(m);
                return;
            }
            getMessageData(result);
            m.what = 3;
            messageListHandler.sendMessage(m);
            return;
        } catch (IOException e) {
            e.printStackTrace();
            m.what = 2;
            messageListHandler.sendMessage(m);
            return;
        }
    }

    public void getMessageData(String result) {
        JSONObject returnJson = JSON.parseObject(result);


        JSONArray returnArray = returnJson.getJSONArray("allist");

        if (isClearMessageData) {
            messageList.clear();
            isClearMessageData = false;
        }
        for (int i = 0; i < returnArray.size(); i++) {
            JSONObject obj = returnArray.getJSONObject(i);
            WoFragmentListViewItemMessageJavaBean message = WoFragmentListViewItemMessageJavaBean.parse(obj);
            messageList.add(message);
        }
    }


    ActivityWoItemListViewAdapter messageListAdapter;


    Handler messageListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    Toast.makeText(getActivity(), "服务端返回为空！", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 2: {
                    Toast.makeText(getActivity(), "请求服务端异常！", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 3: {
                    if (messageListAdapter == null) {
                        messageListAdapter = new ActivityWoItemListViewAdapter(getActivity(), messageList);

                    }

                    holder.lv_message.setAdapter(messageListAdapter);
                    messageListAdapter.notifyDataSetChanged();

                    //清空输入框
                    if (oldPostion == -1) {
                        return;
                    } else {

                        int childId = oldPostion - mlistview.getFirstVisiblePosition();

                        if (childId >= 0) {
                            View viewItem = mlistview.getChildAt(childId);
                            if (viewItem != null) {

                                EditText et_message = (EditText) viewItem.findViewById(R.id.et_message);
                                if (et_message != null) {
                                    et_message.setText("");
                                }
                            }
                        }
                    }
                    break;
                }
                case 4: {
                    Toast.makeText(getActivity(), "登录调用服务端异常！", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 5: {
                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                    break;
                }


            }

        }
    };

}