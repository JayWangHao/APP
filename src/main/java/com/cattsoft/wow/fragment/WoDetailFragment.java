package com.cattsoft.wow.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.listener.OnFragmentInteractionListener;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.framework.view.ProgressDialog;
import com.cattsoft.framework.view.pullableview.PullableListView;
import com.cattsoft.wow.R;
import com.cattsoft.wow.adapter.WoListViewAdapter;
import com.cattsoft.wow.mudels.Wo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.cattsoft.wow.fragment.WoDetailFragment
 * @author: LQY
 * @date: 2016-03-09 10:44
 */
public class WoDetailFragment extends Fragment {
    private View view;

    private String flowId;
    private String geoId;
    private int needpage;
    private int presentpage;
    private String status;
    private String  userId;
    private List<Wo> woList = new ArrayList<Wo>();

    private ProgressDialog progressDialog;   //进度条

    private WoListViewAdapter myWoListAdapter;
    private PullableListView mlistview;

    public WoDetailFragment() {}

    private OnFragmentInteractionListener onFragmentInteractionListener;

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle mBundle = getArguments();
        flowId = mBundle.getString("flowId");
        geoId = mBundle.getString("geoId");
        userId = mBundle.getString("userId");
        view = inflater.inflate(R.layout.fragment_wo_detail, container, false);
        mlistview = (PullableListView) view.findViewById(R.id.wo_detail_list_view);

        initDetailDataThread(true);

        return view;
    }
    private void initDetailDataThread(boolean flag){
        if(flag){
            progressDialog = new ProgressDialog(WoDetailFragment.super.getActivity());
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

    public void initData(boolean flag){
        Message msg = new Message();
        String url = "tz/TZDeviceAction.do?method=QuerytFaultlist";
        JSONObject jsonParmter = new JSONObject();
        jsonParmter.put("FLOWID", flowId);
        jsonParmter.put("USERID", userId);
        jsonParmter.put("geoId", geoId);
        jsonParmter.put("needpage", 5);
        jsonParmter.put("presentpage", 0);
        jsonParmter.put("status", "");

        String ss = jsonParmter.toString();
        try {
           String  result =  Communication.getPostResponseForNetManagement(url, ss);
            if(StringUtil.isBlank(result)) {
                msg.what = 1;
                handler.sendMessage(msg);
                return;
            }

            getData(result);
            msg.what = 3;
            handler.sendMessage(msg);
            return;

        } catch (IOException e) {
            e.printStackTrace();
            msg.what = 2;
            handler.sendMessage(msg);
            return;
        }
    }

    public void getData(String result){
        JSONObject returnJson = JSON.parseObject(result);
        JSONArray returnArray = returnJson.getJSONArray("allist");
        for(int i=0;i<returnArray.size();i++){
            JSONObject obj = returnArray.getJSONObject(i);
            Wo wo =Wo.parse(obj);
            woList.add(wo);
        }
    }

    // 用于接收线程发送的消息
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message m) {
            switch (m.what) {
                case 1: {
                    Toast.makeText(WoDetailFragment.super.getActivity(), "服务端返回为空！", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 2:{
                    Toast.makeText(WoDetailFragment.super.getActivity(),"请求服务端异常！",Toast.LENGTH_SHORT).show();
                    break;
                }
                case 3:{
                    fillUI();
                    break;
                }

            }
            progressDialog.closeProcessDialog();
        }
    };

    public void fillUI(){
        myWoListAdapter = new WoListViewAdapter(WoDetailFragment.super.getActivity(),woList);
        mlistview.setAdapter(myWoListAdapter);
        notifiAcivity();
    }

    /**
     *通知依附的activity
     */
    private void  notifiAcivity(){
        onFragmentInteractionListener.onFragmentInteraction("Y");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onFragmentInteractionListener = (OnFragmentInteractionListener) activity;
    }




}
