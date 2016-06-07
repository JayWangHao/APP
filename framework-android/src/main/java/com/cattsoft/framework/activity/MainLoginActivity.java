package com.cattsoft.framework.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.R;
import com.cattsoft.framework.base.BaseActivity;
import com.cattsoft.framework.connect.RequestListener;
import com.cattsoft.framework.cache.CacheProcess;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.util.LogUtil;
import com.cattsoft.framework.util.Md5Builder;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.framework.view.TitleBarView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainLoginActivity extends BaseActivity {

    private static final String TAG = "MainLoginActivity";

    private EditText mEtUsername;
    private EditText mEtPassword;
    private Button mBtnLogin;

    private String md5Pwd;
    private String username = "";
    private String password = "";
    private String loginUserId;

    private String type = "";
    private int subType = 0;
    private boolean passwordFlag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_login_activity);
        TitleBarView title =  (TitleBarView) findViewById(R.id.title1);
        title.setTitleBar(getString(R.string.title_activity_main), View.GONE,View.GONE, View.GONE, false);
        LogUtil.isEnableLog = true;
        initView();
        registerListener();
        Drawable drawable= getResources().getDrawable(R.drawable.login_user_name_img);
        LogUtil.i(TAG, drawable.getMinimumHeight() + " " + drawable.getMinimumWidth());
        drawable.setBounds(0, 0, 35, 35);
        mEtUsername.setCompoundDrawables(drawable,null,null,null);

        drawable= getResources().getDrawable(R.drawable.login_user_password_img);
        LogUtil.i(TAG, drawable.getMinimumHeight() + " " + drawable.getMinimumWidth());
        drawable.setBounds(0, 0, 35, 35);
        mEtPassword.setCompoundDrawables(drawable,null,null,null);
    }

    @Override
    protected void initView() {
        mEtUsername = (EditText) findViewById(R.id.user_name_txt);
        mEtPassword = (EditText) findViewById(R.id.password_txt);
        mBtnLogin = (Button) findViewById(R.id.login_btn);

        SharedPreferences  sharedPreferences = getSharedPreferences("username", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", username);
        SharedPreferences  sharedPreferences1 = getSharedPreferences("password", Context.MODE_PRIVATE);
        password = sharedPreferences1.getString("password", password);

        if(!username.equals("")){
            mEtUsername.setText(username);
        }
        if(!password.equals("")){
            passwordFlag = true;
            mEtPassword.setText("******");
        }
    }

    @Override
    protected void registerListener() {

        mEtUsername.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (passwordFlag) {
                    passwordFlag = false;
                } else {
                    mEtPassword.selectAll();
                }
                return false;
            }
        });

        mEtPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (passwordFlag) {
                    passwordFlag = false;
                    mEtPassword.setText("");
                } else {
                    mEtPassword.selectAll();
                }
                return false;
            }
        });


        mBtnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                checkInternetInfo();	//检查网络连接问题

                if(type.equals("")){
                    Toast.makeText(getApplicationContext(), "网络连接异常，请检查接入点等网络配置!", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    login();
                }

            }
        });
    }

    private void checkInternetInfo(){

        type = "";
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            type = "";
        } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            type = "wifi";
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            subType = info.getSubtype();
            if (subType == TelephonyManager.NETWORK_TYPE_CDMA || subType == TelephonyManager.NETWORK_TYPE_GPRS
                    || subType == TelephonyManager.NETWORK_TYPE_EDGE) {
                type = "2g";
            } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS || subType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_A || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
                type = "3g";
            } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准
                type = "4g";
            }else{
                type = subType+"";
            }
        }

    }


    public void login(){
        String url = "mobileService/login";
        if(!passwordFlag){
            username = mEtUsername.getText().toString();
            password = mEtPassword.getText().toString();
        }

        if(StringUtil.isBlank(username)){

            Toast.makeText(MainLoginActivity.this,"请输入用户名",Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtil.isBlank(password)){

            Toast.makeText(MainLoginActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
            return;
        }

        Date nowtDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        String textDateContentStr = sdf.format(nowtDate);
        String reportDate = textDateContentStr.replace("年","");
        reportDate = reportDate.replace("月","");

        md5Pwd = Md5Builder.getMd5(password);
        JSONObject reqJson = new JSONObject();
        reqJson.put("username", username);
        reqJson.put("password", md5Pwd);
        reqJson.put("reportType", "M");
        reqJson.put("reportDate", reportDate);

        Communication communication = new Communication(url, reqJson, new RequestListener() {
            @Override
            public void onComplete(String result) {
                afterLogin(result);

            }
        }, MainLoginActivity.this);
        communication.getPostHttpContent();
    }


    public void afterLogin(String result){
        LogUtil.i(TAG, result);
        JSONObject json = JSONObject.parseObject(result);
        if (json.get("sysuser")!=null) {//获得响应结果，登录成功                                      .

            if(json.getJSONObject("reportList") != null){
                JSONObject jsonObject = json.getJSONObject("reportList");
                if(jsonObject.getString("loginUserId") != null){
                    loginUserId = jsonObject.getString("loginUserId");
                }
            }

            //存储服务端返回的信息
            CacheProcess.initCacheInSharedPreferences(this, json);
            CacheProcess.setCacheValueInSharedPreferences(this, "username", username);
            CacheProcess.setCacheValueInSharedPreferences(this,"password",md5Pwd);
            CacheProcess.setCacheValueInSharedPreferences(this,"loginUserId",loginUserId);
            String theme = CacheProcess.getCacheValueInSharedPreferences(this, "theme");
            LogUtil.i(TAG, "theme="+theme );
            Class homeActivityClass = null;
            SharedPreferences sharedPreferences=getSharedPreferences("isLoginSuccess",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("isSuccess", true);
            editor.commit();

            SharedPreferences sharedPreferences2 = getSharedPreferences("username",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor2 = sharedPreferences2.edit();
            editor2.putString("username", username);
            editor2.commit();

            SharedPreferences sharedPreferences3 = getSharedPreferences("password",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor3 = sharedPreferences3.edit();
            editor3.putString("password", password);
            editor3.commit();


            try {
                homeActivityClass = Class.forName(theme);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "初始化HomeActivity异常,未找到theme="+theme, Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(MainLoginActivity.this, homeActivityClass);
            startActivity(intent);
            finish();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
