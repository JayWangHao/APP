package com.cattsoft.wow.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.activity.UpdateManager;
import com.cattsoft.framework.base.BaseActivity;
import com.cattsoft.framework.cache.CacheProcess;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.framework.view.ProgressDialog;
import com.cattsoft.framework.view.TitleBarView;
import com.cattsoft.wow.R;

import java.io.IOException;

/*
登陆界面
 */
public class MainActivity extends BaseActivity {

    private EditText userNameEdittext;
    private EditText pwdEditText;
    private CheckBox rememberPassword;
    private CheckBox autoLogin;
    private Button loginBtn;
    private String userName = "";
    private String pwd = "";
    private boolean isRememberPassword = false;
    private SharedPreferences sharedPreferences;
    private boolean isAutoLogin = false;//自动登录
    private String  result;
    private ProgressDialog progressDialog;
    private boolean isCiphertext = false;
    private String errorMessage = "";
    private String isLogout = "N";

    private JSONObject reponseJson = null;

    private UpdateManager manager;
    private boolean closePres = true;

    private String userType;//判断是什么账号；3是包机，2是维护，1是监管

    private String nid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TitleBarView title =  (TitleBarView) findViewById(R.id.title1);
        title.setTitleBar("登录", View.GONE, View.GONE, View.GONE, false);
        title.setTitleBackgroundColor(getResources().getColor(R.color.small_title_text_color));
        initView();
        Intent intent = this.getIntent();
        isLogout = intent.getStringExtra("isLogout");
        if("Y".equals(isLogout)){//注销清楚缓存
             cleanLoginInfo();
        }
        loadLoginInfo();
        if(isAutoLogin){
            loginThread();
        }
        registerListener();
    }

    @Override
    protected void initView() {
        userNameEdittext = (EditText)findViewById(R.id.login_edit_username);
        pwdEditText = (EditText)findViewById(R.id.login_edit_password);
        loginBtn = (Button)findViewById(R.id.login_btn_login);
        rememberPassword = (CheckBox)findViewById(R.id.login_remember_password);
        autoLogin = (CheckBox)findViewById(R.id.login_auto_login);

    }



    @Override
    protected void registerListener() {
        //登录
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginThread();
            }
        });
        //记住密码
        rememberPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isRememberPassword = true;
                }else{
                    isRememberPassword = false;
                    autoLogin.setChecked(false);
                }
            }
        });

        autoLogin.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    isAutoLogin = true;
                    rememberPassword.setChecked(true);
                } else {
                    isAutoLogin = false;
                }
            }
        });
        pwdEditText.setOnClickListener(new EditText.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isCiphertext) {
                    isCiphertext = false;
                    pwdEditText.setText("");
                } else {
                    pwdEditText.selectAll();
                }
            }
        });

        pwdEditText.setOnTouchListener(new EditText.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pwdEditText.requestFocus();
                return false;
            }
        });

        userNameEdittext.setOnClickListener(new EditText.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isCiphertext) {
                    isCiphertext = false;
                    userNameEdittext.setText("");
                    userNameEdittext.setText("");
                } else {
                    userNameEdittext.selectAll();
                }
            }
        });

        userNameEdittext.setOnTouchListener(new EditText.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                userNameEdittext.requestFocus();
                return false;
            }
        });


    }

    public void login(boolean isShowProcessDialog){
        if(!isCiphertext) {
            pwd = pwdEditText.getText().toString();
            userName = userNameEdittext.getText().toString();
        }
        Message m = new Message();
        if(StringUtil.isBlank(userName)){
            m.what = 1;
            loginHandler.sendMessage(m);
            return;
        }
        if(StringUtil.isBlank(pwd)){
            m.what = 2;
            loginHandler.sendMessage(m);
            return;
        }
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("isCiphertext",isCiphertext);
            requestJson.put("password", pwd);
            requestJson.put("userName",userName);
            String url = "tz/TZDeviceAction.do?method=login";
            result =  Communication.getPostResponseForNetManagement(url, requestJson.toString());
            reponseJson = JSON.parseObject(result);
            nid=reponseJson.getString("NID");
            if(StringUtil.isBlank(result)){
                m.what = 3;
                loginHandler.sendMessage(m);
                return;
            }
            if(reponseJson.containsKey("error")){
                errorMessage = reponseJson.getString("error");
                m.what = 5;
                loginHandler.sendMessage(m);
                return;
            }else{
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
               // startActivity(new Intent(MainActivity.this, HomeActivity.class));
              if (isRememberPassword) {
                    saveLoginInfo();
                } else {
                    cleanLoginInfo();
                }
                String userId = reponseJson.getString("userId");
                userType=reponseJson.getString("userType");
                String geoId = "";
                JSONArray geoArray = reponseJson.getJSONArray("geolist");
                if(geoArray !=null && geoArray.size()>0){
                    JSONObject json = (JSONObject)geoArray.get(0);
                    geoId = json.getString("value");
                }
                SharedPreferences.Editor editor =  sharedPreferences.edit();
                editor.putString("userId",userId);
                editor.putString("geoId",geoId);
                editor.commit();
               // finish();
            }
            m.what = 6;
            loginHandler.sendMessage(m);

        } catch (IOException e) {
            e.printStackTrace();
            m.what = 4;
            loginHandler.sendMessage(m);
            return;
        }

    }

    private void loginThread(){
        boolean isShowProcessDialog = true;
        if(isShowProcessDialog){
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.showProcessDialog();
        }
        Thread mThread = new Thread(new Runnable() {

            @Override
            public void run() {
                login(true);
            }
        });
        mThread.start();
    }



    /**
     * 保存用户名密码等登录信息
     */
    public void saveLoginInfo() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isRememberPassword", isRememberPassword);
        editor.putBoolean("isAutoLogin", isAutoLogin);
        editor.putString("password", pwd);
        editor.putString("userName", userName);

        editor.putString("nid",nid);
        editor.commit();
    }

    /**
     * 加载用户名密码等登录信息
     */
    public void loadLoginInfo() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userName = sharedPreferences.getString("userName", "");
        pwd = sharedPreferences.getString("password", "");
        isRememberPassword = sharedPreferences.getBoolean("isRememberPassword", false);
        isAutoLogin = sharedPreferences.getBoolean("isAutoLogin", false);
       // isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        if(isRememberPassword) {
            userNameEdittext.setText(userName);
            if(!StringUtil.isBlank(pwd)) {
                isCiphertext = true;
                pwdEditText.setText("*******");
            }
        }else {
            userNameEdittext.setText("");

            pwdEditText.setText("");

        }

        rememberPassword.setChecked(isRememberPassword);
        autoLogin.setChecked(isAutoLogin);
    }


    public void cleanLoginInfo() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("userName", "");
		editor.putString("password", "");
        editor.putBoolean("isRememberPassword", false);
        editor.putBoolean("isAutoLogin", false);
        editor.putString("userId","");
        editor.putString("geoId","");
        //editor.putBoolean("isFirstRun", isFirstRun);
        editor.commit();
    }

    public Handler loginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    Toast.makeText(MainActivity.this,"请输入用户名！",Toast.LENGTH_SHORT).show();
                    break;
                }
                case 2: {
                    Toast.makeText(MainActivity.this,"请输入密码！",Toast.LENGTH_SHORT).show();
                    break;
                }
                case 3: {
                    Toast.makeText(MainActivity.this,"登录失败,服务端返回为空！",Toast.LENGTH_SHORT).show();
                    break;
                }
                case 4: {
                    Toast.makeText(MainActivity.this,"登录调用服务端异常！",Toast.LENGTH_SHORT).show();
                    break;
                }
                case 5: {
                    Toast.makeText(MainActivity.this,errorMessage,Toast.LENGTH_SHORT).show();
                    break;
                }
                case 6: {
                    closePres=false;
                    //检测版本
                    checkNewVersion(reponseJson);
                    break;
                }
            }
            if(closePres){
                progressDialog.closeProcessDialog();
            }else{
                closePres=true;
            }


        }
    };



    //检查软件更新
    private void checkNewVersion(JSONObject info){
        JSONArray versonArray=info.getJSONArray("androidlist");
        JSONObject androidVersion=null;
        if(versonArray!=null && versonArray.size()>0){
            for(int i=0;i<versonArray.size();i++){
                JSONObject version=versonArray.getJSONObject(i);
                String remarks = version.getString("REMARKS");
                if("android".equals(remarks)){
                    androidVersion = version;
                    break;
                }
            }
        }

        int versonService = Integer.valueOf(androidVersion.getString("VERSION_NUM"));
        String pathUrl =  androidVersion.getString("PUBLISH_PATH");
        String desc = androidVersion.getString("VERSION_DESC");
        String isForce = androidVersion.getString("IS_FORCE");
        int versionLocal = 0;
        try {
            // 获取软件版本号
            versionLocal = MainActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if(versonService > versionLocal){
            manager = new UpdateManager(false,MainActivity.this);
            if("Y".equals(isForce)){
                manager.setIsForced(true);
            }
            manager.setPublishUrl(pathUrl);
            showNoticeDialog(desc,isForce);
        }else{
            Intent intent=new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("userType",userType);

            startActivity(intent);
            finish();
        }
    }


    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog(String versionDesc, final String isForced) {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(com.cattsoft.framework.R.string.soft_update_title);
        builder.setMessage(versionDesc);
        // 更新
        builder.setPositiveButton(com.cattsoft.framework.R.string.soft_update_updatebtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 显示下载对话框
                manager.showDownloadDialog();
            }
        });
        // 稍后更新
        builder.setNegativeButton(com.cattsoft.framework.R.string.soft_update_later, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ("Y".equals(isForced)) {
                    CacheProcess cacheProcess = new CacheProcess();
                    cacheProcess.clearCache(mosApp);
                    finish();
                } else {
                    dialog.dismiss();
                    Intent intent=new Intent(MainActivity.this, HomeActivity.class);
                    intent.putExtra("userType", userType);

                    startActivity(intent);
                    finish();

                }

            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }




}
