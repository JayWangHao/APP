package com.cattsoft.wow.fragment;


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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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
    private TextView noBrokenStationNumber;
    private TextView workingNumber;
    private TextView brokenStationNumber;

    //用于切换正序和倒序
    private String sortMethodNoBrokenStationNumber = "asc";
    private String sortMethodWorkingNumber = "asc";
    private String sortMethodBrokenStationNumber = "asc";

    private String sortMethodWaringNumber = "asc";
    private String sortMethodAreaCode = "asc";
    private String sortMethodAreaName = "asc";

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


    private String levels="1,2,3";    //告警级别筛选

    private Boolean isClearList = false;
    private Boolean isShowProgressDialog = false;


    private List<ListFragmentJavaBean> dataList = new ArrayList<ListFragmentJavaBean>();
    private ListFragment_ListView_Adapter listFragment_listView_adapter;

    public ListsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        initView(view);

        initDataThread(true, 1, "asc", "1,2,3");
        resterlistener();
        return view;
    }

    public void initView(View view) {

        fm = getFragmentManager();

        user_name_img = (ImageView) view.findViewById(R.id.user_name_img);
        my_take = (ImageView) view.findViewById(R.id.my_take);


        areaName = (TextView) view.findViewById(R.id.areaName);
        noBrokenStationNumber = (TextView) view.findViewById(R.id.noBrokenStationNumber);
        workingNumber = (TextView) view.findViewById(R.id.workingNumber);
        brokenStationNumber = (TextView) view.findViewById(R.id.brokenStationNumber);

        list_fragment_RefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.list_fragment_refresh_view);
        list_fragment_listview = (PullableListView) view.findViewById(R.id.list_fragment_listview);

        //popwindow
        titlePopup = new TitlePopup(getActivity(), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        titlePopup.addAction(new ActionItem(getActivity(), "按告警数排序", R.drawable.listsfragment_red_point));
        titlePopup.addAction(new ActionItem(getActivity(), "按区域码排序", R.drawable.listsfragment_red_point));
        titlePopup.addAction(new ActionItem(getActivity(), "按区域名排序", R.drawable.listsfragment_red_point));



    }

    public void resterlistener() {
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
        areaName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titlePopup.show(view);
                titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {

                    @Override
                    public void onItemClick(ActionItem item, int position) {
                        switch (position) {


                            case 0://告警数


                                if (sortMethodWaringNumber.equals("asc")) {
                                    sortMethodWaringNumber = "desc";
                                    initDataThread(true, 1, "desc", levels);
                                } else {
                                    sortMethodWaringNumber = "asc";
                                    initDataThread(true, 1, "asc", levels);
                                }
                                break;
                            case 1://区域码


                                if (sortMethodAreaCode.equals("asc")) {
                                    sortMethodAreaCode = "desc";
                                    initDataThread(true, 3, "desc", levels);
                                } else {
                                    sortMethodAreaCode = "asc";
                                    initDataThread(true, 3, "asc", levels);
                                }

                                break;
                            case 2://区域名


                                if (sortMethodAreaName.equals("asc")) {
                                    sortMethodAreaName = "desc";
                                    initDataThread(true, 2, "desc", levels);
                                } else {
                                    sortMethodAreaName = "asc";
                                    initDataThread(true, 2, "asc", levels);
                                }

                                break;


                        }
                    }
                });

            }
        });
//非断站数
        noBrokenStationNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (sortMethodNoBrokenStationNumber.equals("asc")) {
                    sortMethodNoBrokenStationNumber = "desc";
                    initDataThread(true, 5, "desc", levels);
                } else {
                    sortMethodNoBrokenStationNumber = "asc";
                    initDataThread(true, 5, "asc", levels);
                }


            }
        });

        //工单数
        workingNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (sortMethodWorkingNumber.equals("asc")) {
                    sortMethodWorkingNumber = "desc";
                    initDataThread(true, 6, "desc", levels);
                } else {
                    sortMethodWorkingNumber = "asc";
                    initDataThread(true, 6, "asc", levels);
                }
            }
        });
        //断站数
        brokenStationNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (sortMethodBrokenStationNumber.equals("asc")) {
                    sortMethodBrokenStationNumber = "desc";
                    initDataThread(true, 4, "desc", levels);
                } else {
                    sortMethodBrokenStationNumber = "asc";
                    initDataThread(true, 4, "asc", levels);
                }

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


        list_fragment_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                FragmentTransaction transaction4 = fm.beginTransaction();
                ListItemFragment listItemWoFragment = new ListItemFragment();

                Bundle bundle = new Bundle();
                bundle.putString("areacode", dataList.get(i).getNGEOID());
                bundle.putString("areaname", dataList.get(i).getNGEONAME());
                listItemWoFragment.setArguments(bundle);
                transaction4.replace(R.id.layout, listItemWoFragment);

                transaction4.commit();


            }
        });
    }

    private void initDataThread(boolean isprogress, final int sortway, final String mode, final String companyLevel) {
        if(isprogress){
            progressDialog = new ProgressDialog(ListsFragment.super.getActivity());
            progressDialog.showProcessDialog();
        }
        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                initData(sortway, mode, companyLevel);
            }
        });
        mThread.start();
    }

    private String result;

    public void initData(int sortway, String mode, String companyLevel) {

        JSONObject requestJson = new JSONObject();

        requestJson.put("SortWay", "" + sortway);// 按不同条件进行排序
        requestJson.put("level", companyLevel);//分公司级别
        requestJson.put("mode", mode); //正序、倒序

        String url = "tz/TZDeviceAction.do?method=DataStatistic";
        Message m = new Message();
        try {//
            result = Communication.getPostResponseForNetManagement(url, requestJson.toString());
            //Log.i("数据", "数据" + result);
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
                    break;
                }
                case 2: {
                    Toast.makeText(ListsFragment.super.getActivity(), "请求服务端异常！", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 3: {
                    if (mRefreshMode == PULL_DOWN_TO_REFRESH) {
                        listFragment_listView_adapter = new ListFragment_ListView_Adapter(ListsFragment.super.getActivity(), dataList);
                        list_fragment_RefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);

                        //去掉分割线
                        list_fragment_listview.setDividerHeight(1);
                        list_fragment_listview.setDivider(new ColorDrawable(Color.GRAY));

                        list_fragment_listview.setAdapter(listFragment_listView_adapter);
                    } else {
                        list_fragment_RefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }


                    listFragment_listView_adapter.notifyDataSetChanged();

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
