package com.cattsoft.wow.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.base.BaseActivity;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.framework.view.ProgressDialog;
import com.cattsoft.framework.view.pullableview.PullToRefreshLayout;
import com.cattsoft.framework.view.pullableview.PullableListView;
import com.cattsoft.wow.R;
import com.cattsoft.wow.fragment.ListsFragment;
import com.cattsoft.wow.mudels.Warning;
import com.cattsoft.wow.view.ToastCommom;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase;
import com.readystatesoftware.viewbadger.BadgeView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//告警
public class ListItemActivity extends BaseActivity {
    private ImageView user_name;
    private ImageView my_order;     //我的订阅
    private TextView Break;      //断站
    private TextView Not_Break;   //非断站
    private TextView today;         //今日
    private TextView week;          //本周
    private TextView month;         //本月
    private EditText sreach_text;    //搜索查询
    private TextView sreach_btn;      //搜索按钮
    private String sreach_text_context; //搜索内容
    private boolean sreach_start = false;   //搜索时调用接口查询

    private String Break_Value = "1";//1断站，2非断站
    private String Data_Value = "1";

    /** 页卡头标 */
    private ArrayList<TextView> pageTitles = new ArrayList<TextView>();
    private ImageView cursor;       // 动画图片
    private int screenW;
    private int perSpacing = 0; // 每个动画图片间距
    private int currIndex = 0;// 当前页卡编号
    Animation animation = null;

    private PullableListView mlistview;
    private List<Warning> mlist = new ArrayList<Warning>();
    private WarningListViewAdapter adapter;

    private ProgressDialog progressDialog;   //进度条
    private PullToRefreshLayout refreshLayout;

    public static final int PULL_DOWN_TO_REFRESH = 0;       //下拉刷新
    public static final int PULL_UP_TO_LOAD_MORE = 1;       //上拉加载
    private int mRefreshMode = PULL_DOWN_TO_REFRESH;
    private int message = 0;    //用来标示下拉刷新请求数据、上拉刷新数据、第一次请求数据
    private int pageNo = 1;     //页数
    private int limit = 6;      //每页显示的条数
    private String result;
    private boolean isShowProgressDialog = true;
    private boolean isClearList = true;
    private SharedPreferences sharedPreferences;
    private String userId;

    private boolean order_flag = false;     //订阅的标识
    private String levels;                     //告警级别筛选
    private String flashed;                     //闪烁告警
    private String state;                     //告警状态
    private boolean flash_flag = false;     //闪烁告警的标识
    private String error;

    private Map<Integer,Boolean> map = new HashMap<Integer,Boolean>();
    private int oldPostion = -1;
    private static HashMap<Integer, Boolean> isExpand;//设置item展开或者收缩的初始值
    private String areaCode;
    private String areaName;
    private TextView tv_city_name;
    private Button back_btn_btn;
    private int count_1;
    private int count_2;
    private int count_3;
    private BadgeView badge;
    private BadgeView badge2;
    private BadgeView badge3;
    private int width;
    private ListItemActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_warning_list);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        context = ListItemActivity.this;
        Intent intent = getIntent();
        areaCode = intent.getStringExtra("areacode");
        areaName = intent.getStringExtra("areaname");
        
        initView();
        getSharedPreferencesData();
        initDataWarningThread();
        resterlistener();
    }

    @Override
    protected void initView() {
        my_order = (ImageView)findViewById(R.id.my_order);
        Break = (TextView)findViewById(R.id.duanZhan);
        Not_Break = (TextView) findViewById(R.id.fei_duanZhan);
        today = (TextView) findViewById(R.id.today);
        today.setTextColor(getResources().getColor(R.color.small_title_text_color));
        week = (TextView) findViewById(R.id.week);
        month = (TextView) findViewById(R.id.month);
        sreach_text = (EditText) findViewById(R.id.sreach_text);
        sreach_btn = (TextView)findViewById(R.id.sreach_btn);

        initPageTitles();
        initImageView();        //初始化动画，生成cursor图片

        mlistview = (PullableListView) findViewById(R.id.warning_list_view);

        refreshLayout = (PullToRefreshLayout)findViewById(R.id.warning_refresh_view);
        toastCommom = ToastCommom.createToastConfig();
        tv_city_name = (TextView)findViewById(R.id.tv_city_name);
        tv_city_name.setText(areaName);//设置区域名
        back_btn_btn = (Button) findViewById(R.id.back_btn_btn);
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        width = wm.getDefaultDisplay().getWidth();
        badge = new BadgeView(context, today);
        badge.setTextSize(12);
        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badge2 = new BadgeView(context, week);
        badge2.setTextSize(12);
        badge2.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badge3 = new BadgeView(context, month);
        badge3.setTextSize(12);
        badge3.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
    }

    @Override
    protected void registerListener() {

    }

    private void getSharedPreferencesData(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ListItemActivity.this);
        Break_Value = sharedPreferences.getString("Break_Value", "");
        Data_Value = sharedPreferences.getString("Data_Value","");
        levels = sharedPreferences.getString("ALARMLEVEL","");
        state = sharedPreferences.getString("ALARMSTATE","");
        flashed = sharedPreferences.getString("flash","");
//        Log.d("TAG","order"+levels+state);
        if (!TextUtils.isEmpty(levels) || !TextUtils.isEmpty(state)){
//            Log.d("TAG","order true");
            order_flag = true;
        }

        if (!TextUtils.isEmpty(flashed)){
            flash_flag = true;
        }

        if(Break_Value.equals("2")){
            Break.setTextColor(getResources().getColor(R.color.black));
            Not_Break.setTextColor(getResources().getColor(R.color.small_title_text_color));
            Break.setBackgroundResource(R.drawable.title_middle_text_bg2);
            Not_Break.setBackgroundColor(getResources().getColor(R.color.white));
        }else{
            Break.setTextColor(getResources().getColor(R.color.small_title_text_color));
            Not_Break.setTextColor(getResources().getColor(R.color.black));
            Break.setBackgroundColor(getResources().getColor(R.color.white));
            Not_Break.setBackgroundResource(R.drawable.title_middle_text_bg);
        }

        if(Data_Value.equals("7")){
            today.setTextColor(getResources().getColor(R.color.black));
            week.setTextColor(getResources().getColor(R.color.small_title_text_color));
            month.setTextColor(getResources().getColor(R.color.black));
            moveOneToRight(perSpacing);      // 从1 到 2
            animation.setFillAfter(true);   // True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }else if(Data_Value.equals("30")){
            today.setTextColor(getResources().getColor(R.color.black));
            week.setTextColor(getResources().getColor(R.color.black));
            month.setTextColor(getResources().getColor(R.color.small_title_text_color));
            moveTwoToRight(perSpacing);      // 从 1 到3
            animation.setFillAfter(true);   // True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }
    }

    private void initDataWarningThread(){
        if(isShowProgressDialog){
            progressDialog = new ProgressDialog(ListItemActivity.this);
            progressDialog.showProcessDialog();
        }
        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                sreach_text_context = sreach_text.getText().toString();
                if (sreach_text_context.equals("") || sreach_text_context == null){
                    initData();
                }else {
                    sreach_start = true;
                    initDataSearch();
                }
            }
        });
        mThread.start();
    }

    private void initDataWarningThreadSearch() {

        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                initDataSearch();
            }
        });
        mThread.start();
    }

    public void initData(){

        Message msg = new Message();
        // 发起请求从后台获取数据后显示出来
        String url = "tz/TZDeviceAction.do?method=getQueryAlarmList";
        // 把条件数据以JSON形式存储好
        JSONObject jsonParmter = new JSONObject();
        userId = sharedPreferences.getString("userId","");
//        Log.d("TAG","order_flag:"+order_flag);
        if(order_flag == true ){
            jsonParmter.put("ALARMLEVEL", levels);
            jsonParmter.put("ALARMSTATE", state);
            if(flash_flag == true){
                jsonParmter.put("flash", 1);
            }
            jsonParmter.put("BREAK", Break_Value);                //1断站，2非断站
            jsonParmter.put("DAYLIMITVALUE", Data_Value);      //时间条件，1本日，7本周，30本月
            jsonParmter.put("GEOID", areaCode);           //用户ID
            jsonParmter.put("limit", limit);           //显示条数
            jsonParmter.put("page", pageNo);              //页码

        }else{
            jsonParmter.put("BREAK", Break_Value);                //1断站，2非断站
            jsonParmter.put("DAYLIMITVALUE", Data_Value);      //时间条件，1本日，7本周，30本月
            jsonParmter.put("GEOID", areaCode);           //用户ID
            jsonParmter.put("limit", 5);           //显示条数
            jsonParmter.put("page", pageNo);              //页码

            jsonParmter.put("ALARMSTATE", "1");
        }

        String ss = jsonParmter.toString();
        try {
            result =  Communication.getPostResponseForNetManagement(url, ss);

            if(StringUtil.isBlank(result)) {
                msg.what = 1;
                handler.sendMessage(msg);
                return;
            }

            JSONObject resultJson = JSON.parseObject(result);
            if(resultJson.containsKey("error")){
                error = resultJson.getString("error");
                msg.what = 2;
                handler.sendMessage(msg);
                return;
            }

            JSONArray alarmsArrayList = resultJson.getJSONArray("alarms");
//            Log.d("TAG_1", "告警默认返回结果：msg.what = 3;" + result);
            getData(alarmsArrayList);
            msg.what = 3;
            handler.sendMessage(msg);
            return;

        } catch (JSONException e){

        } catch (Exception e) {
            e.printStackTrace();
            msg.what = 4;
            handler.sendMessage(msg);
            return;
        }
    }

    //查询方法
    public void initDataSearch(){
//        Log.d("TAG_1", "进入查询方法");
        Message msg = new Message();
        // 发起请求从后台获取数据后显示出来
        String url = "tz/TZDeviceAction.do?method=getQueryAlarmList";
        // 把条件数据以JSON形式存储好
        JSONObject jsonParmter = new JSONObject();
        userId = sharedPreferences.getString("userId","");
        if(order_flag == true ){
            jsonParmter.put("ALARMLEVEL", levels);
            jsonParmter.put("ALARMSTATE", state);
            if(flash_flag == true){
                jsonParmter.put("flash", 1);
            }
            jsonParmter.put("BREAK", Break_Value);                //1断站，2非断站
            jsonParmter.put("DAYLIMITVALUE", Data_Value);      //时间条件，1本日，7本周，30本月
            jsonParmter.put("GEOID", areaCode);          //用户ID
            jsonParmter.put("limit", limit);           //显示条数
            jsonParmter.put("page", pageNo);              //页码
            jsonParmter.put("VCNECNAME", sreach_text_context);
        }else{
            jsonParmter.put("BREAK", Break_Value);                //1断站，2非断站
            jsonParmter.put("DAYLIMITVALUE", Data_Value);      //时间条件，1本日，7本周，30本月
            jsonParmter.put("GEOID", areaCode);            //用户ID
            jsonParmter.put("limit", 5);           //显示条数
            jsonParmter.put("page", pageNo);              //页码
            jsonParmter.put("VCNECNAME", sreach_text_context);
            jsonParmter.put("ALARMSTATE", "1");
        }

        String ss = jsonParmter.toString();
        try {
            result =  Communication.getPostResponseForNetManagement(url, ss);
            if (sreach_start){
                if (!result.contains("neCName")) {
                    toastCommom.ToastShow(ListItemActivity.this, (ViewGroup) ListItemActivity.this.findViewById(R.id.toast_layout_root), "服务器暂无该站点");
                    msg.what = 5;
                    handler.sendMessage(msg);
//                    Log.d("TAG_1", "告警搜索返回结果：msg.what = 5;" + result);
                    return;
                }
            }

            JSONObject resultJson = JSON.parseObject(result);
            if(resultJson.containsKey("error")){
                error = resultJson.getString("error");
                msg.what = 2;
                handler.sendMessage(msg);
                return;
            }

            JSONArray alarmsArrayList = resultJson.getJSONArray("alarms");

            getData(alarmsArrayList);
            msg.what = 3;
            handler.sendMessage(msg);
            return;

        } catch (JSONException e){

        } catch (Exception e) {
            e.printStackTrace();
            msg.what = 4;
//            Log.d("TAG_1", "告警搜索返回结果：msg.what = 4;" + result);
            handler.sendMessage(msg);
            return;
        }
    }

    // 用于接收线程发送的消息
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message m) {
            switch (m.what) {
                case 1: {
//                    Log.d("TAG_1", "case = 1;");
                    Toast.makeText(ListItemActivity.this, "服务端返回为空！", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 2:{
//                    Log.d("TAG_1", "case = 2;");
                    Toast.makeText(ListItemActivity.this, error, Toast.LENGTH_LONG).show();
                    break;
                }
                case 3:{

//                    Log.d("TAG_1", "case = 3;");
                    if (count_1 < 100) {
                        badge.setBadgeMargin((int)(width * 0.046), 0);
                        badge.setText("" + count_1);
                    } else {
                        badge.setBadgeMargin(15, 0);
                        badge.setText("99+");
                    }
                    if (count_2 < 100) {
                        badge2.setBadgeMargin((int)(width * 0.046), 0);
                        badge2.setText("" + count_2);
                    } else {
                        badge2.setBadgeMargin(15, 0);
                        badge2.setText("99+");
                    }
                    if (count_3 < 100) {
                        badge3.setBadgeMargin((int)(width * 0.046), 0);
                        badge3.setText("" + count_3);
                    } else {
                        badge3.setBadgeMargin(15, 0);
                        badge3.setText("99+");
                    }
                    badge.show();
                    badge2.show();
                    badge3.show();
                    adapter = new WarningListViewAdapter(ListItemActivity.this,mlist);
                    mlistview.setAdapter(adapter);
                    mlistview.setVisibility(View.VISIBLE);

                    if(pageNo != 1){
                        mlistview.setSelection((pageNo-1) * 5+1);
                    }

                    if(mlist.size() >= limit){
                        //设置最后一页时，提示没有数据
                        int len = mlist.size() % limit;
                        if( len != 0){
                            Toast.makeText(ListItemActivity.this,"已经是最后一页",Toast.LENGTH_SHORT).show();
                            refreshLayout.setCanLoadMore(false);

                        }
                    }

                    if (mlist.size() == 1){
                        refreshLayout.setCanLoadMore(false);
                    }

                    if (mRefreshMode == PULL_DOWN_TO_REFRESH) {
                        refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    } else {
                        refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                    break;
                }
                case 4: {
//                    Log.d("TAG_1", "case = 4;");
                    if(isClearList){
                        mlist.clear();
                    }
//                    Toast.makeText(ListItemActivity.this,"请求服务端异常aaa！",Toast.LENGTH_SHORT).show();
                    if (sreach_start){
                        toastCommom.ToastShow(ListItemActivity.this, (ViewGroup) ListItemActivity.this.findViewById(R.id.toast_layout_root), "没有查询到符合条件的告警数据");
                    }
                    if (mRefreshMode == PULL_DOWN_TO_REFRESH) {
                        refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    } else {
                        refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                    break;
                }
                case 5:{
//                    Log.d("TAG_1", "case = 5;");
                    if(isClearList){
                        mlist.clear();
                    }
                    if (sreach_start){
                        toastCommom.ToastShow(ListItemActivity.this, (ViewGroup) ListItemActivity.this.findViewById(R.id.toast_layout_root), "没有查询到符合条件的告警数据");
                    }
                    if (mRefreshMode == PULL_DOWN_TO_REFRESH) {
                        refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    } else {
                        refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
//                    Toast.makeText(ListItemActivity.this,"请求服务端异常！",Toast.LENGTH_SHORT).show();
                    break;
                }
            }

            if (sreach_start ){
                sreach_start = false;
            }
            progressDialog.closeProcessDialog();
        }
    };


    // 初始化页卡(上方)的标题
    private void initPageTitles() {
        pageTitles.clear();
        pageTitles.add((TextView) findViewById(R.id.today));
        pageTitles.add((TextView) findViewById(R.id.week));
        pageTitles.add((TextView) findViewById(R.id.month));
    }

    private void initImageView() {
        cursor = (ImageView) findViewById(R.id.cursor);
        getScreenWidth();
        Bitmap bmp = Bitmap.createBitmap(perSpacing, 5, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.drawColor(0xFFBC1E28);
        cursor.setImageBitmap(bmp);
        setCursorPosition();
    }

    public Object getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        ListItemActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
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

    private void resterlistener() {
        Not_Break.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Break.setTextColor(getResources().getColor(R.color.black));
                Not_Break.setTextColor(getResources().getColor(R.color.small_title_text_color));
                Break.setBackgroundResource(R.drawable.title_middle_text_bg2);
                Not_Break.setBackgroundColor(getResources().getColor(R.color.white));
                Break_Value = "2";
                pageNo = 1;
                isShowProgressDialog = true;
                isClearList = true;
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ListItemActivity.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Break_Value", "2");
                editor.commit();
                initDataWarningThread();
            }
        });
        Break.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Break.setTextColor(getResources().getColor(R.color.small_title_text_color));
                Not_Break.setTextColor(getResources().getColor(R.color.black));
                Break.setBackgroundColor(getResources().getColor(R.color.white));
                Not_Break.setBackgroundResource(R.drawable.title_middle_text_bg);
                Break_Value = "1";
                pageNo = 1;
                isShowProgressDialog = true;
                isClearList = true;
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ListItemActivity.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Break_Value", "1");
                editor.commit();
                initDataWarningThread();
            }
        });
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.clear();
                today.setTextColor(getResources().getColor(R.color.small_title_text_color));
                week.setTextColor(getResources().getColor(R.color.black));
                month.setTextColor(getResources().getColor(R.color.black));
                if (currIndex == 1) {
                    moveOnetoLeft(perSpacing);      // 从2 到 1
                } else if (currIndex == 2) {
                    moveTwoToLeft(perSpacing);       // 从3 到 1
                }
                currIndex = 0;
                if(animation==null){

                }else{
                    animation.setFillAfter(true);   // True:图片停在动画结束位置
                    animation.setDuration(300);
                    cursor.startAnimation(animation);
                    Data_Value = "1";
                    pageNo = 1;
                    isShowProgressDialog = true;
                    isClearList = true;
                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ListItemActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Data_Value", "1");
                    editor.commit();
                    initDataWarningThread();
                }
            }
        });
        week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.clear();
                today.setTextColor(getResources().getColor(R.color.black));
                week.setTextColor(getResources().getColor(R.color.small_title_text_color));
                month.setTextColor(getResources().getColor(R.color.black));
                if (currIndex == 0) {
                    moveOneToRight(perSpacing);      // 从1 到 2
                } else if (currIndex == 2) {
                    moveOnetoLeft(perSpacing);       // 从 3到 2
                }
                currIndex = 1;
                animation.setFillAfter(true);   // True:图片停在动画结束位置
                animation.setDuration(300);
                cursor.startAnimation(animation);
                Data_Value = "7";
                pageNo = 1;
                isShowProgressDialog = true;
                isClearList = true;
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ListItemActivity.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Data_Value", "7");
                editor.commit();
                initDataWarningThread();
            }
        });
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.clear();
                today.setTextColor(getResources().getColor(R.color.black));
                week.setTextColor(getResources().getColor(R.color.black));
                month.setTextColor(getResources().getColor(R.color.small_title_text_color));
                if (currIndex == 0) {
                    moveTwoToRight(perSpacing);      // 从 1 到3
                } else if (currIndex == 1) {
                    moveOneToRight(perSpacing);      // 从 2 到3
                }
                currIndex = 2;
                animation.setFillAfter(true);   // True:图片停在动画结束位置
                animation.setDuration(300);
                cursor.startAnimation(animation);
                Data_Value = "30";
                pageNo = 1;
                isShowProgressDialog = true;
                isClearList = true;
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ListItemActivity.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Data_Value", "30");
                editor.commit();
                initDataWarningThread();
            }
        });
        sreach_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageNo = 1;
                isShowProgressDialog = true;
                isClearList = true;
                sreach_text_context = sreach_text.getText().toString();
                if (TextUtils.isEmpty(sreach_text_context)){
                    Toast.makeText(ListItemActivity.this,"请输入站点",Toast.LENGTH_SHORT).show();
                }else {
                    sreach_start = true;
//                    initDataWarningThreadSearch();
                    initDataWarningThreadSearch();
                }
            }
        });
        my_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ListItemActivity.this, WarningOrderActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        refreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //下拉刷新
                mRefreshMode = PULL_DOWN_TO_REFRESH;
                message = 1;
                pageNo = 1;
                isShowProgressDialog = true;
                isClearList = true;
                refreshLayout.setCanLoadMore(true);
                sreach_text_context = sreach_text.getText().toString();
                if (sreach_text_context.equals("") || sreach_text_context == null) {

                    initDataWarningThread();
                } else {
                    sreach_start = true;
                    initDataWarningThreadSearch();
                }
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //上拉加载
                mRefreshMode = PULL_UP_TO_LOAD_MORE;
                pageNo++;
                message = 2;
                isShowProgressDialog = false;
                isClearList = false;
                initDataWarningThread();
            }
        });


        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if (mlist.size() > 1) {

                    final ImageView downImg = (ImageView) view.findViewById(R.id.down_img);
                    final ImageView upImg = (ImageView) view.findViewById(R.id.up_img);
                    final LinearLayout hideLayout = (LinearLayout) view.findViewById(R.id.hide_layout);
                    final TextView historyWarning = (TextView) view.findViewById(R.id.history_warning);
                    final TextView historyWarning2 = (TextView) view.findViewById(R.id.history_warning2);
                    final TextView flowWarning = (TextView) view.findViewById(R.id.flow_warning);
                    final TextView flowWarning2 = (TextView) view.findViewById(R.id.flow_warning2);

                    map.put(position, true);
                    if (hideLayout.getVisibility() == View.GONE) {
                        if (flowWarning.getVisibility() == View.VISIBLE) {
                            hideLayout.setVisibility(View.VISIBLE);
                            downImg.setVisibility(View.GONE);
                            upImg.setVisibility(View.VISIBLE);
                            historyWarning.setVisibility(View.GONE);
                            historyWarning2.setVisibility(View.VISIBLE);
                            flowWarning.setVisibility(View.GONE);
                            flowWarning2.setVisibility(View.VISIBLE);
                        } else {
                            hideLayout.setVisibility(View.VISIBLE);
                            downImg.setVisibility(View.GONE);
                            upImg.setVisibility(View.VISIBLE);
                            historyWarning.setVisibility(View.GONE);
                            historyWarning2.setVisibility(View.VISIBLE);
                            flowWarning.setVisibility(View.GONE);
                            flowWarning2.setVisibility(View.GONE);
                        }
                    } else {
                        if (flowWarning2.getVisibility() == View.VISIBLE) {
                            hideLayout.setVisibility(View.GONE);
                            downImg.setVisibility(View.VISIBLE);
                            upImg.setVisibility(View.GONE);
                            historyWarning.setVisibility(View.VISIBLE);
                            historyWarning2.setVisibility(View.GONE);
                            flowWarning.setVisibility(View.VISIBLE);
                            flowWarning2.setVisibility(View.GONE);
                        } else {
                            hideLayout.setVisibility(View.GONE);
                            downImg.setVisibility(View.VISIBLE);
                            upImg.setVisibility(View.GONE);
                            historyWarning.setVisibility(View.VISIBLE);
                            historyWarning2.setVisibility(View.GONE);
                            flowWarning.setVisibility(View.GONE);
                            flowWarning2.setVisibility(View.GONE);
                        }

                    }

                    //对item点击后展开 或者收缩的处理
                    Warning warning = mlist.get(position);
                    if (oldPostion == position) {
                        if (warning.isExpend()) {
                            oldPostion = -1;
                        }
                        mlist.get(position).setExpend(false);
                    } else {
                        if (oldPostion != -1) {
                            mlist.get(oldPostion).setExpend(false);
                        }
                        oldPostion = position;
                        mlist.get(oldPostion).setExpend(true);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        back_btn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//退出ff

                finish();
            }
        });
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

    private ToastCommom toastCommom;


    private void getData(JSONArray alarmsArrayList){
        Log.d("TAG_1", "isClearList" + isClearList);

        if(isClearList){
            mlist.clear();
        }
        for (int i = 0;i<alarmsArrayList.size();i++){
            JSONObject object = alarmsArrayList.getJSONObject(i);
            Warning warning = new Warning();
            warning.setWarningLevel(object.getString("almStdLevel")+"级");
            warning.setWarningAddress(object.getString("neCName"));
            warning.setWarningContext(object.getString("specificProblems"));
            warning.setBusinessType(object.getString("state"));
            warning.setWarningTime(object.getString("ctime1"));
            warning.setHappenTime(object.getString("firstEventTime"));
            warning.setWarningCode("未知");
            warning.setHandlePerson(object.getString("person"));
            warning.setNetLocation(object.getString("moiName"));
            warning.setLongitude(object.getString("vcendcoo"));
            warning.setAddText(object.getString("vcotherinfo"));
            warning.setTisyn(object.getString("tisyn"));
            warning.setGeoId(object.getString("geoId"));
            warning.setNeeName(object.getString("neEName"));
            mlist.add(warning);
        }

        Log.d("TAG_1", "isClearList1"+isClearList);

    }


    /**
     * 告警适配器
     * Created by lenovo on 2016/2/17.
     */
    public class WarningListViewAdapter extends BaseAdapter {

        private Context mcontext = null;
        public List<Warning> list;

        public WarningListViewAdapter(Context context, List<Warning> list){

            this.mcontext = context;
            this.list =list;
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

            int length = list.size();
            //list长度等于1，直接让其展开数据
//            if(length == 1) {
//                ViewHolder holder = null;
//                if (view == null) {
//                    view = LayoutInflater.from(this.mcontext).inflate(R.layout.fragment_warning_item_single, null);
//                    holder = new ViewHolder();
//
//                    holder.warningLevel = (TextView) view.findViewById(R.id.warning_level2);
//                    holder.warningAddress = (TextView) view.findViewById(R.id.warning_address2);
//                    holder.warningContext = (TextView) view.findViewById(R.id.warning_context2);
//                    holder.businessType = (TextView) view.findViewById(R.id.business_type2);
//                    holder.warningTime = (TextView) view.findViewById(R.id.warning_time2);
//                    holder.happenTime = (TextView) view.findViewById(R.id.happen_time2);
//                    holder.warningCode = (TextView) view.findViewById(R.id.warning_code2);
//                    holder.handlePerson = (TextView) view.findViewById(R.id.handle_person2);
//                    holder.netLocation = (TextView) view.findViewById(R.id.net_location2);
//                    holder.longitude = (TextView) view.findViewById(R.id.longitude2);
//                    holder.addText = (TextView) view.findViewById(R.id.add_text2);
//                    view.setTag(holder);
//                } else {
//                    holder = (ViewHolder) view.getTag();
//                }
//
//                final TextView historyWarning2 = (TextView) view.findViewById(R.id.history_warning22);
//                final TextView flowWarning2 = (TextView) view.findViewById(R.id.flow_warning_single);
//
//                final Warning warning = list.get(i);
//                holder.warningLevel.setText(warning.getWarningLevel());
//                holder.warningAddress.setText(warning.getWarningAddress());
//                holder.warningContext.setText(warning.getWarningContext());
//                holder.businessType.setText(warning.getBusinessType());
//                holder.warningTime.setText(warning.getWarningTime());
//                holder.happenTime.setText(warning.getHappenTime());
//                holder.warningCode.setText(warning.getWarningCode());
//                holder.handlePerson.setText(warning.getHandlePerson());
//                holder.netLocation.setText(warning.getNetLocation());
//                holder.longitude.setText(warning.getLongitude());
//                holder.addText.setText(warning.getAddText());
//
//                historyWarning2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(mcontext, WarningHistoryActivity.class);
//                        intent.putExtra("VCSITECODE", warning.getNeeName());
//                        mcontext.startActivity(intent);
//                    }
//                });
//                //判断工单按钮是否可以展示
//                final String tisyn = warning.getTisyn();
//                if (!StringUtil.isBlank(tisyn) && !"0".equals(tisyn)){
//                    flowWarning2.setBackgroundResource(R.drawable.button_wh_1);
//                }else {
//                    flowWarning2.setBackgroundResource(R.drawable.button_wh_4);
//                }
//                    flowWarning2.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (!StringUtil.isBlank(tisyn) && !"0".equals(tisyn)) {
//                            Warning warnvo = list.get(i);
//                            Intent intent = new Intent(mcontext, WoDetailActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("marnInfo", warnvo);
//                            intent.putExtras(bundle);
//                            mcontext.startActivity(intent);
//                            }else {
//                                Toast.makeText(ListItemActivity.this,"暂无工单信息",Toast.LENGTH_SHORT).show();
//
//                            }
//                        }
//                    });
//
//            }
//            //list长度大于1
//            else{

            ViewHolder viewHolder = null;
            if(view == null) {
                view = LayoutInflater.from(this.mcontext).inflate(R.layout.fragment_warning_item, null);

                viewHolder = new ViewHolder();
                viewHolder.warningLevel = (TextView) view.findViewById(R.id.warning_level);
                viewHolder.warningAddress = (TextView) view.findViewById(R.id.warning_address);
                viewHolder.warningContext = (TextView) view.findViewById(R.id.warning_context);
                viewHolder.businessType = (TextView) view.findViewById(R.id.business_type);
                viewHolder.warningTime = (TextView) view.findViewById(R.id.warning_time);
                viewHolder.happenTime = (TextView) view.findViewById(R.id.happen_time);
                viewHolder.warningCode = (TextView) view.findViewById(R.id.warning_code);
                viewHolder.handlePerson = (TextView) view.findViewById(R.id.handle_person);
                viewHolder.netLocation = (TextView) view.findViewById(R.id.net_location);
                viewHolder.longitude = (TextView) view.findViewById(R.id.longitude);
                viewHolder.addText = (TextView) view.findViewById(R.id.add_text);
                view.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) view.getTag();
            }
            final ImageView downImg = (ImageView) view.findViewById(R.id.down_img);
            final ImageView upImg = (ImageView) view.findViewById(R.id.up_img);
            final LinearLayout hideLayout = (LinearLayout) view.findViewById(R.id.hide_layout);
            final TextView historyWarning = (TextView) view.findViewById(R.id.history_warning);
            final TextView historyWarning2 = (TextView) view.findViewById(R.id.history_warning2);
//                final TextView flowWarning = (TextView) view.findViewById(R.id.flow_warning);
//                final TextView flowWarning2 = (TextView) view.findViewById(R.id.flow_warning2);
            final TextView flowWarning = (TextView) view.findViewById(R.id.flow_warning);
            final TextView flowWarning2 = (TextView) view.findViewById(R.id.flow_warning2);

            final Warning warning = list.get(i);
            viewHolder.warningLevel.setText(warning.getWarningLevel());
            viewHolder.warningAddress.setText(warning.getWarningAddress());
            viewHolder.warningContext.setText(warning.getWarningContext());
            viewHolder.businessType.setText(warning.getBusinessType());
            viewHolder.warningTime.setText(warning.getWarningTime());
            viewHolder.happenTime.setText(warning.getHappenTime());
            viewHolder.warningCode.setText(warning.getWarningCode());
            viewHolder.handlePerson.setText(warning.getHandlePerson());
            viewHolder.netLocation.setText(warning.getNetLocation());
            viewHolder.longitude.setText(warning.getLongitude());
            viewHolder.addText.setText(warning.getAddText());

            //判断工单按钮是否可以展示
            final  String tisyn = warning.getTisyn();
            if (!StringUtil.isBlank(tisyn) && !"0".equals(tisyn)){
                flowWarning.setBackgroundResource(R.drawable.button_wh_1);
                flowWarning2.setBackgroundResource(R.drawable.button_wh_1);
            }else {
                flowWarning.setBackgroundResource(R.drawable.button_wh_4);
                flowWarning2.setBackgroundResource(R.drawable.button_wh_4);
            }


            //工单按钮
            View.OnClickListener  flowOnClick=new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!StringUtil.isBlank(tisyn) && !"0".equals(tisyn)){
//                            Log.d("TAG","工单可按");
                        Warning warnvo = list.get(i);
                        Intent intent = new Intent(mcontext, WoDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("marnInfo", warnvo);
                        intent.putExtras(bundle);
                        mcontext.startActivity(intent);
                    }else {
                        Toast.makeText(ListItemActivity.this,"暂无工单信息",Toast.LENGTH_SHORT).show();
                    }

                }
            };
            flowWarning.setOnClickListener(flowOnClick);
            flowWarning2.setOnClickListener(flowOnClick);

            boolean flag = warning.isExpend();
            if (list.size() == 1){
                hideLayout.setVisibility(View.VISIBLE);
                downImg.setVisibility(View.GONE);
                upImg.setVisibility(View.VISIBLE);
                historyWarning.setVisibility(View.GONE);
                historyWarning2.setVisibility(View.VISIBLE);
//                    if(!StringUtil.isBlank(tisyn) && !"0".equals(tisyn)){
                flowWarning.setVisibility(View.GONE);
                flowWarning2.setVisibility(View.VISIBLE);
//                    }
            }else {
                if(flag) {
                    hideLayout.setVisibility(View.VISIBLE);
                    downImg.setVisibility(View.GONE);
                    upImg.setVisibility(View.VISIBLE);
                    historyWarning.setVisibility(View.GONE);
                    historyWarning2.setVisibility(View.VISIBLE);
//                    if(!StringUtil.isBlank(tisyn) && !"0".equals(tisyn)){
                    flowWarning.setVisibility(View.GONE);
                    flowWarning2.setVisibility(View.VISIBLE);
//                    }
                }else{
                    hideLayout.setVisibility(View.GONE);
                    downImg.setVisibility(View.VISIBLE);
                    upImg.setVisibility(View.GONE);
                    historyWarning.setVisibility(View.VISIBLE);
                    historyWarning2.setVisibility(View.GONE);
//                    if(!StringUtil.isBlank(tisyn) && !"0".equals(tisyn)){
                    flowWarning.setVisibility(View.VISIBLE);
                    flowWarning2.setVisibility(View.GONE);
//                    }
                }

            }

            historyWarning.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mcontext, WarningHistoryActivity.class);
                    intent.putExtra("VCSITECODE", warning.getNeeName());
                    mcontext.startActivity(intent);
                }
            });
            historyWarning2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mcontext, WarningHistoryActivity.class);
                    intent.putExtra("VCSITECODE", warning.getNeeName());
                    mcontext.startActivity(intent);
                }
            });
//            }
            return view;
        }
    }

    class ViewHolder {
        TextView warningLevel;
        TextView warningAddress;
        TextView warningContext;
        TextView businessType;
        TextView warningTime;
        TextView happenTime;
        TextView warningCode;
        TextView handlePerson;
        TextView netLocation;
        TextView longitude;
        TextView addText;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null){
            levels = data.getStringExtra("ALARMLEVEL");
            state = data.getStringExtra("ALARMSTATE");
            flashed = data.getStringExtra("flash");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("ALARMLEVEL",levels);
            editor.putString("ALARMSTATE",state);
            editor.putString("flash", flashed);
            editor.commit();
            order_flag = true;
            if(flashed != null){
                flash_flag = true;
            }else{
                flash_flag = false;
            }
            pageNo = 1;
            isClearList = true;
            isShowProgressDialog = true;
            initDataWarningThread();
        }

    }

}
