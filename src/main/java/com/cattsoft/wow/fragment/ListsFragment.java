package com.cattsoft.wow.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.framework.view.ProgressDialog;
import com.cattsoft.framework.view.pullableview.PullToRefreshLayout;
import com.cattsoft.framework.view.pullableview.PullableListView;
import com.cattsoft.wow.R;
import com.cattsoft.wow.activity.MySubscriptionActivity;
import com.cattsoft.wow.adapter.ListFragment_ListView_Adapter;
import com.cattsoft.wow.mudels.ListFragmentJavaBean;
import com.cattsoft.wow.view.ActionItem;
import com.cattsoft.wow.view.TitlePopup;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 列表fragment
 */
public class ListsFragment extends Fragment {


    private ImageView user_name_img;
    private ImageView my_take;

    private TextView areaName;
    //    private TextView noBrokenStationNumber;
    private TextView workingNumber;
    private TextView brokenStationNumber;

    //用于切换正序和倒序
    private String sortMethodNoBrokenStationNumber = "desc";
    private String sortMethodWorkingNumber = "desc";
    private String sortMethodBrokenStationNumber = "desc";

    private String sortMethodWaringNumber = "desc";
    private String sortMethodAreaCode = "desc";
    private String sortMethodAreaName = "desc";

    private PullToRefreshLayout list_fragment_RefreshLayout;
    private PullableListView list_fragment_listview;

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
    private Boolean isClearWoList;

    private SharedPreferences sharedPreferences;

    private TitlePopup titlePopup;

    private String cross;//标示是否是最后一页；

    ProgressDialog progressDialog;//加载进度条

    private FragmentManager fm;


    private String levels = "1,2,3";    //告警级别筛选
    private int DayRange = 1;
    private Boolean isClearList = false;
    private Boolean isShowProgressDialog = false;
    private static String mType;
    private int flag_type = 1;
    private String flag_sc = "desc";
    private Boolean isLiShi = false;
    private List<ListFragmentJavaBean> dataList = new ArrayList<ListFragmentJavaBean>();
    private ListFragment_ListView_Adapter listFragment_listView_adapter;
    private Spinner spinner;
    private RadioGroup rg_content;
    private String[] types;
    private ImageView iv_jiantou;
    private Boolean isUp = false;
    private String[] level;
    private int mPosition = -1;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_list, null);
        initView(view);
        resterlistener();
        return view;
    }

    public void initView(View view) {
        fm = getFragmentManager();
        user_name_img = (ImageView) view.findViewById(R.id.user_name_img);
        my_take = (ImageView) view.findViewById(R.id.my_take);
        areaName = (TextView) view.findViewById(R.id.areaName);
        workingNumber = (TextView) view.findViewById(R.id.workingNumber);
        brokenStationNumber = (TextView) view.findViewById(R.id.brokenStationNumber);
        list_fragment_RefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.list_fragment_refresh_view);
        list_fragment_listview = (PullableListView) view.findViewById(R.id.list_fragment_listview);
        iv_jiantou = (ImageView) view.findViewById(R.id.list_jiantou);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListsFragment.super.getActivity(), R.layout.simple_spinner_item);
        level = getResources().getStringArray(R.array.spingarr);
        for (int i = 0; i < level.length; i++) {
            adapter.add(level[i]);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        rg_content = (RadioGroup) view.findViewById(R.id.rg_content);
        rg_content.check(R.id.rb_day);
        types = getResources().getStringArray(R.array.spingarr);
    }

    public void resterlistener() {
        //        <item>告警数</item>
//        <item>区域名</item>
//        <item>区域编码</item>
//        <item>断站数</item>
//        <item>非断站数</item>
//        <item>工单数</item>
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ListsFragment.super.getActivity(), level[position], Toast.LENGTH_SHORT).show();
                mPosition = position + 1;
                switch (position) {
                    case 0:


                        flag_type = 6;

                        if (sortMethodWorkingNumber.equals("asc")) {
                            initDataThread(true, 6, "asc", levels);
                        } else {
                            initDataThread(true, 6, "desc", levels);
                        }
                        break;
                    case 1:
                        flag_type = 4;

                        if (sortMethodBrokenStationNumber.equals("asc")) {
                            initDataThread(true, 4, "asc", levels);
                        } else {
                            initDataThread(true, 4, "desc", levels);
                        }

                        break;
                    case 2:
                        flag_type = 3;

                        if (sortMethodAreaCode.equals("asc")) {
                            initDataThread(true, 3, "asc", levels);
                        } else {
                            initDataThread(true, 3, "desc", levels);
                        }
                        break;
                    case 3:
                        flag_type = 2;

                        if (sortMethodAreaName.equals("asc")) {
                            initDataThread(true, 2, "asc", levels);
                        } else {
                            initDataThread(true, 2, "desc", levels);
                        }
                        break;
                    case 4:
                        flag_type = 5;

                        if (sortMethodNoBrokenStationNumber.equals("asc")) {
                            initDataThread(true, 5, "asc", levels);
                        } else {
                            initDataThread(true, 5, "desc", levels);
                        }
                        break;
                    case 5:
                        flag_type = 1;
                        if (sortMethodWaringNumber.equals("asc")) {
                            initDataThread(true, 1, "asc", levels);
                        } else {
                            initDataThread(true, 1, "desc", levels);
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //选择年月日
        rg_content.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_day:
                        DayRange = 1;
                        isLiShi = false;

                        initDataThread(true, flag_type, flag_sc, levels);
                        break;
                    case R.id.rb_month:
                        DayRange = 30;
                        isLiShi = false;

                        initDataThread(true, flag_type, flag_sc, levels);

                        break;
                    case R.id.rb_year:
                        isLiShi = true;
                        initDataThread(true, flag_type, flag_sc, levels);

                        break;
                }
            }
        });


        iv_jiantou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUp) {
                    RotateAnimation animation;
                    animation = new RotateAnimation(-180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(500);//设置动画持续时间
                    animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
                    iv_jiantou.startAnimation(animation);
//                    if (mPosition > 0) {
                        flag_sc = "desc";
                        initDataThread(true, flag_type, "desc", levels);
//                    }
                    isUp = false;

                } else {
                    RotateAnimation animation;
                    animation = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(500);//设置动画持续时间
                    animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
                    iv_jiantou.startAnimation(animation);
//                    if (mPosition != -1) {
                        flag_sc = "asc";
                        initDataThread(true,flag_type, "asc", levels);
//                    }
                    isUp = true;

                }
            }
        });

        user_name_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof SlidingActivityBase) {
                    ((SlidingActivityBase) getActivity()).toggle();
                }
            }
        });
        my_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MySubscriptionActivity.class);
                startActivityForResult(intent, 1);


            }
        });

        list_fragment_RefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                isClearWoList = true;
                //下拉刷新
                mRefreshMode = PULL_DOWN_TO_REFRESH;
                message = 1;
                pageNo = 0;
                list_fragment_RefreshLayout.setCanLoadMore(true);
                initDataThread(true, 1, "asc", levels);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //上拉加载
                mRefreshMode = PULL_UP_TO_LOAD_MORE;
                message = 2;
                pageNo = pageNo + 5;
                isClearWoList = false;
                initDataThread(true, 1, "asc", levels);
            }
        });
    }

    private void initDataThread(boolean isprogress, final int sortway, final String mode, final String companyLevel) {
        if (isprogress) {
            progressDialog = new ProgressDialog(ListsFragment.super.getActivity());
            progressDialog.showProcessDialog();
        }
        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                initData(sortway, mode, companyLevel, DayRange);
            }
        });
        mThread.start();
    }

    private String result;

    public void initData(int sortway, String mode, String companyLevel, int DayRange) {

        JSONObject requestJson = new JSONObject();

        requestJson.put("SortWay", "" + sortway);// 按不同条件进行排序
        requestJson.put("level", companyLevel);//分公司级别
        requestJson.put("mode", mode); //正序、倒序
        if (!isLiShi) {
            requestJson.put("DayRange", DayRange); //正序、倒序
        }

        String url = "tz/TZDeviceAction.do?method=DataStatistic";
        Message m = new Message();
        try {//
            result = Communication.getPostResponseForNetManagement(url, requestJson.toString());
//            Log.d("TAG_3", "数据" + result);
            if (StringUtil.isBlank(result)) {
                m.what = 1;
                listFragmentHandler.sendMessage(m);
                return;
            }
            getData(result);
            m.what = 3;
            listFragmentHandler.sendMessage(m);
            return;
        } catch (IOException e) {
            e.printStackTrace();
            m.what = 2;
            listFragmentHandler.sendMessage(m);
            return;
        }
    }


    public void getData(String result) {

        JSONObject returnJson = JSON.parseObject(result);
//        if (isClearWoList) {
//            dataList.clear();
//        }

        JSONArray returnArray = returnJson.getJSONArray("allist");

        dataList.clear();

        for (int i = 0; i < returnArray.size(); i++) {
            JSONObject obj = returnArray.getJSONObject(i);
            ListFragmentJavaBean listFragmentJavaBean = ListFragmentJavaBean.parse(obj);


            dataList.add(listFragmentJavaBean);
        }
    }


    public Handler listFragmentHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    Toast.makeText(ListsFragment.super.getActivity(), "服务端返回为空！", Toast.LENGTH_SHORT).show();
                    progressDialog.closeProcessDialog();
                    list_fragment_RefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    break;
                }
                case 2: {
                    Toast.makeText(ListsFragment.super.getActivity(), "请求服务端异常！", Toast.LENGTH_SHORT).show();
                    progressDialog.closeProcessDialog();
                    list_fragment_RefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);

                    break;
                }
                case 3: {
                    if (mRefreshMode == PULL_DOWN_TO_REFRESH) {
                        listFragment_listView_adapter = new ListFragment_ListView_Adapter(ListsFragment.super.getActivity(), dataList, fm);
                        list_fragment_RefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);

                        //去掉分割线
                        list_fragment_listview.setDividerHeight(1);
                        list_fragment_listview.setDivider(new ColorDrawable(Color.GRAY));

                        list_fragment_listview.setAdapter(listFragment_listView_adapter);
                    } else {
                        list_fragment_RefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }


                    listFragment_listView_adapter.notifyDataSetChanged();
                    progressDialog.closeProcessDialog();
//
                    break;
                }
                case 4: {
                    Toast.makeText(ListsFragment.super.getActivity(), "登录调用服务端异常！", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 5: {
                    Toast.makeText(ListsFragment.super.getActivity(), "result", Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
            }
            progressDialog.closeProcessDialog();
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == -1) {
            if (requestCode == 1) {
                if (data != null) {
                    //ALARMLEVEL公司
                    levels = data.getStringExtra("ALARMLEVEL");

                    pageNo = 1;
                    isClearList = true;
                    isShowProgressDialog = true;

                    initDataThread(isShowProgressDialog, 1, "asc", levels);

                }
            }
        }


    }

}
