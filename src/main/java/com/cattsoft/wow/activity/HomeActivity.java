package com.cattsoft.wow.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.wow.R;
import com.cattsoft.wow.base.BaseSlidingActivity;
import com.cattsoft.wow.fragment.LeftSlidingFragment;
import com.cattsoft.wow.fragment.ListItemFragment;
import com.cattsoft.wow.fragment.ListsFragment;
import com.cattsoft.wow.fragment.MapWoFragment;
import com.cattsoft.wow.fragment.OverviewFragment;
import com.cattsoft.wow.fragment.ReportFragment;
import com.cattsoft.wow.fragment.TrendFragment;
import com.cattsoft.wow.fragment.WarningFragment;
import com.cattsoft.wow.fragment.WoFragment;
import com.cattsoft.wow.service.PalpitateService;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.io.IOException;
/*
包机主页面
 */

public class HomeActivity extends BaseSlidingActivity {

    private FragmentManager fm;
    private FragmentTransaction ft;
    private Fragment overviewFragment, reprotFragment, warningFragment, woFragment, leftSlidingFragment, listsFragment, trendFragment;
    private FrameLayout FLayout;
    private RadioGroup rgp;
    private SlidingMenu sm;
    private SharedPreferences sharedPreferences;

    private String userType;//判断是什么账号；3是包机，2是维护，1是监管
    private String nid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        setBehindContentView(R.layout.left_menu_layou);
        sm = getSlidingMenu();    //滑动菜单
        sm.setShadowWidth(15); // 阴影宽度
        sm.setBehindOffset(150); // 菜单与边框的距离
        sm.setShadowDrawable(R.drawable.shadow); // 滑动菜单渐变
        sm.setFadeDegree(0.35f);                //色度
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN); // 边缘滑动菜单

        Intent intent = getIntent();
        userType = intent.getStringExtra("userType");

        startSerice();
        initData();
        initView();
        setWarningOrderInfo();

    }

    private void startSerice() {
        Intent intent = new Intent(HomeActivity.this, PalpitateService.class);
        startService(intent);
    }

    protected void initView() {
        FLayout = (FrameLayout) findViewById(R.id.layout);
        rgp = (RadioGroup) findViewById(R.id.radioGroup);
        rgp.check(R.id.radioBut_wo);
        final RadioButton radioBut_wo = (RadioButton) findViewById(R.id.radioBut_wo);
        radioBut_wo.setTextColor(getResources().getColor(R.color.text_checked));
        final RadioButton radioBut_warning = (RadioButton) findViewById(R.id.radioBut_warning);
        final RadioButton radioBut_report = (RadioButton) findViewById(R.id.radioBut_report);
        FragmentTransaction transaction1 = fm.beginTransaction();
        if ("1".equals(userType)) {
            transaction1.replace(R.id.layout, overviewFragment);
        } else if ("3".equals(userType)) {
            transaction1.replace(R.id.layout, woFragment);
        }

        transaction1.replace(R.id.menu_frame_id, leftSlidingFragment);
        transaction1.commit();

        if ("1".equals(userType)) {
            radioBut_wo.setText("概览");
            radioBut_warning.setText("列表");
            radioBut_report.setText("走势");

            rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch (i) {
                        case R.id.radioBut_wo:
                            radioBut_wo.setTextColor(getResources().getColor(R.color.text_checked));
                            radioBut_warning.setTextColor(getResources().getColor(R.color.text_nochecked));
                            radioBut_report.setTextColor(getResources().getColor(R.color.text_nochecked));
                            FragmentTransaction transaction4 = fm.beginTransaction();
                            transaction4.replace(R.id.layout, overviewFragment);
                            transaction4.commit();
                            break;
                        case R.id.radioBut_warning:
                            radioBut_wo.setTextColor(getResources().getColor(R.color.text_nochecked));
                            radioBut_warning.setTextColor(getResources().getColor(R.color.text_checked));
                            radioBut_report.setTextColor(getResources().getColor(R.color.text_nochecked));
                            FragmentTransaction transaction5 = fm.beginTransaction();
                            transaction5.replace(R.id.layout, listsFragment);
                            transaction5.commit();
                            break;
                        case R.id.radioBut_report:
                            radioBut_wo.setTextColor(getResources().getColor(R.color.text_nochecked));
                            radioBut_warning.setTextColor(getResources().getColor(R.color.text_nochecked));
                            radioBut_report.setTextColor(getResources().getColor(R.color.text_checked));
                            FragmentTransaction transaction6 = fm.beginTransaction();
                            transaction6.replace(R.id.layout, trendFragment);
                            transaction6.commit();
                            break;
                    }
                }
            });

        } else if ("3".equals(userType)) {
            rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch (i) {
                        case R.id.radioBut_wo:
                            radioBut_wo.setTextColor(getResources().getColor(R.color.text_checked));
                            radioBut_warning.setTextColor(getResources().getColor(R.color.text_nochecked));
                            radioBut_report.setTextColor(getResources().getColor(R.color.text_nochecked));
                            FragmentTransaction transaction1 = fm.beginTransaction();
                            transaction1.replace(R.id.layout, woFragment);
                            transaction1.commit();
                            break;
                        case R.id.radioBut_warning:
                            radioBut_wo.setTextColor(getResources().getColor(R.color.text_nochecked));
                            radioBut_warning.setTextColor(getResources().getColor(R.color.text_checked));
                            radioBut_report.setTextColor(getResources().getColor(R.color.text_nochecked));
                            FragmentTransaction transaction2 = fm.beginTransaction();
                            transaction2.replace(R.id.layout, warningFragment);
                            transaction2.commit();
                            break;
                        case R.id.radioBut_report:
                            radioBut_wo.setTextColor(getResources().getColor(R.color.text_nochecked));
                            radioBut_warning.setTextColor(getResources().getColor(R.color.text_nochecked));
                            radioBut_report.setTextColor(getResources().getColor(R.color.text_checked));
                            FragmentTransaction transaction3 = fm.beginTransaction();
                            transaction3.replace(R.id.layout, reprotFragment);
                            transaction3.commit();
                            break;
                    }
                }
            });

        } else {
            Toast.makeText(HomeActivity.this, "userType 服务传来有误", Toast.LENGTH_LONG).show();
        }

    }

    public void initData() {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        reprotFragment = new ReportFragment();
        warningFragment = new WarningFragment();
        woFragment = new WoFragment();
        leftSlidingFragment = new LeftSlidingFragment();
        overviewFragment = new OverviewFragment();
        listsFragment = new ListsFragment();
        trendFragment = new TrendFragment();

    }

    public void setWarningOrderInfo() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("first_level_checked", false);
        editor.putBoolean("second_level_checked", false);
        editor.putBoolean("third_level_checked", false);
        editor.putBoolean("fourth_level_checked", false);
        editor.putBoolean("flash_checked", false);
        editor.putBoolean("avtivity_state_checked", true);
        editor.putBoolean("recover_state_checked", false);
        editor.putBoolean("all_state_checked", false);
        editor.putString("Break_Value", "1");
        editor.putString("Data_Value", "1");
        editor.commit();
    }

    @Override
    public void onBackPressed() {

        Fragment fragment =
                fm.findFragmentById(R.id.layout);

        if (fragment instanceof ListItemFragment) {

            FragmentTransaction transaction = fm.beginTransaction();
            ListsFragment listsFragment = new ListsFragment();
            transaction.replace(R.id.layout, listsFragment);
            transaction.commit();

        } else if(fragment instanceof MapWoFragment){

            FragmentTransaction transaction=fm.beginTransaction();
            transaction.replace(R.id.layout,overviewFragment);
            transaction.commit();
        }else {

            new AlertDialog.Builder(this).setTitle("退出登录确认")
                    .setMessage("提示：退出后，请重新再登录！")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new zhuThread().start();
                            finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();

        }

    }

    /**
     * 启动线程请求服务
     */
    private class zhuThread extends Thread {
        @Override
        public void run() {
            Message msg = new Message();
            String url = "tz/TZDeviceAction.do?method=Heartbeat";

            JSONObject jsonObject = new JSONObject();
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
            nid = sharedPreferences.getString("nid", "");
            jsonObject.put("Nid", nid);
            jsonObject.put("Online", "2");

            try {
                String result = Communication.getPostResponseForNetManagement(url, jsonObject.toString());
                msg.what = 1;
                cancelHandler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Handler cancelHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    finish();
                    break;
            }
        }
    };
}
