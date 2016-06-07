package com.cattsoft.framework.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cattsoft.framework.R;
import com.cattsoft.framework.listener.OnFragmentInteractionListener;
import com.cattsoft.framework.util.LogUtil;
import com.cattsoft.framework.view.CustomDialog;
import com.cattsoft.framework.view.SelectItem;
import com.cattsoft.framework.view.TitleBarView;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 系统通用设置功能
 * Created by Baixd on 2015/5/25 025.
 */
public class SettingsFragment extends Fragment {


    private static final String TAG = "SettingsFragment";

    private OnFragmentInteractionListener mInteractionListener;
    public static final int CHANGE_USER = 0;
    public static final int EXIT_APP = 1;
    public static final int CHECK_VERSION = 2;

    private UpdateManager mUpdateManager;

    //切换用户
    private SelectItem mSelectChangeUser;

    //修改密码
//    private SelectItem mSelectModifyPassword;

    //用户反馈
    private SelectItem mSelectFeedback;

    //检查更新
    private SelectItem mSelectCheckVersion;

    //关于
    private SelectItem mSelectAboutme;

    //接受推送消息
    private SelectItem mSelectReceiverPushMessage;

    private boolean isReceiverPushMessage = true;

    //是否推荐附近工单
    private SelectItem mSelectRecommendNearbyWork;


    //退出
   // private Button mButtonExit;
    private SelectItem mButtonExit;


    private View rootView;


    private Activity mActivity;


    private TitleBarView title;//导航栏


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.isEnableLog = true;
//        LogUtil.d(TAG, "进入设置功能点");

        rootView = inflater.inflate(R.layout.fragment_settings, container, false);


        mActivity = getActivity();
        initView();
        registListener();
        return rootView;
    }

    private void initView(){

       mSelectChangeUser = (SelectItem) rootView.findViewById(R.id.select_settings_change_user);
//        mSelectModifyPassword = (SelectItem) rootView.findViewById(R.id.select_settings_modify_password);
        mSelectFeedback = (SelectItem) rootView.findViewById(R.id.select_settings_feedback);
        mSelectCheckVersion = (SelectItem) rootView.findViewById(R.id.select_settings_check_version);
        mSelectAboutme = (SelectItem) rootView.findViewById(R.id.select_settings_about_me);

    //  mSelectReceiverPushMessage = (SelectItem) rootView.findViewById(R.id.receiver_push_message_item);

     //   mSelectRecommendNearbyWork = (SelectItem)rootView.findViewById(R.id.is_recommend_nearby_work);

        mButtonExit = (SelectItem) rootView.findViewById(R.id.btn_exit);



        PackageManager pm = mActivity.getPackageManager();
        String versionName = "";
        try {
            PackageInfo packageInfo = pm.getPackageInfo(mActivity.getPackageName() ,0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
      //  mSelectCheckVersion.setValue(versionName);
    }

    public void registListener() {
        //切换用户
        mSelectChangeUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mInteractionListener.onFragmentInteraction(CHANGE_USER);
            }
        });

        mSelectFeedback.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rootView.getContext(), FeedbackActivity.class);
                startActivity(intent);
            }
        });

        mSelectCheckVersion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mInteractionListener.onFragmentInteraction(CHECK_VERSION);
            }
        });
        //关于
        mSelectAboutme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), AboutActivity.class);
                startActivity(intent);
            }
        });

        //退出登录
        mButtonExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ArrayList<HashMap<String,String>> dataList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> map = new HashMap<String, String>();
               // map.put("name", "退出登录");
                map.put("name", "是");
                dataList.add(map);
                new CustomDialog(rootView.getContext(),dataList,"否","name",new CustomDialog.OnCustomDialogListener(){
                    @Override
                    public void back(HashMap<String, String> value) {
                        mInteractionListener.onFragmentInteraction(EXIT_APP);
                    }
                }).showDialog();

//                new CustomDialog(rootView.getContext(),dataList,"是否退出登录","name",new CustomDialog.OnCustomDialogListener(){
//                    @Override
//                    public void back(HashMap<String, String> value) {
//                        mInteractionListener.onFragmentInteraction(EXIT_APP);
//                    }
//                }).showDialog();
            }
        });

//        mSelectReceiverPushMessage.getDrawableRight().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(isReceiverPushMessage){
//
//                    mSelectReceiverPushMessage.setDrawableRight(R.drawable.setting_on);
//                    isReceiverPushMessage = false;
//
//                    // 注册接口
//                    XGPushManager.registerPush(getActivity().getApplicationContext(),
//                            new XGIOperateCallback() {
//                                @Override
//                                public void onSuccess(Object data, int flag) {
//                                    Log.e("信鸽推送",
//                                            "+++ register push sucess. token:" + data);
//
//                                }
//
//                                @Override
//                                public void onFail(Object data, int errCode, String msg) {
//                                    Log.e("信鸽推送",
//                                            "+++ register push fail. token:" + data
//                                                    + ", errCode:" + errCode + ",msg:"
//                                                    + msg);
//                                }
//                            });
//                }else{
//
//                    mSelectReceiverPushMessage.setDrawableRight(R.drawable.setting_off);
//                    isReceiverPushMessage = true;
//
//                    XGPushManager.unregisterPush(getActivity().getApplicationContext());
//                }
//
//            }
//        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mInteractionListener = (OnFragmentInteractionListener) activity;
    }
}
