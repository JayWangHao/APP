package com.cattsoft.wow.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.SDKInitializer;
import com.cattsoft.framework.activity.UpdateManager;
import com.cattsoft.framework.base.BaseActivity;
import com.cattsoft.framework.cache.CacheProcess;
import com.cattsoft.framework.cache.MosApp;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.framework.view.ProgressDialog;
import com.cattsoft.framework.view.TitleBarView;
import com.cattsoft.framework.view.ToastCommom;
import com.cattsoft.wow.R;
import com.cattsoft.wow.bean.NetEvent;
import com.cattsoft.wow.receiver.NetReceiver;
import com.cattsoft.wow.utils.NetUtils;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import java.io.IOException;

import de.greenrobot.event.EventBus;


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
    private String result;
    private ProgressDialog progressDialog;
    private boolean isCiphertext = false;
    private String errorMessage = "";
    private String isLogout = "N";

    private JSONObject reponseJson = null;

    private UpdateManager manager;
    private boolean closePres = true;

    private String userType;//判断是什么账号；3是包机，2是维护，1是监管

    private String nid;
    private SharedPreferences.Editor editor;
    private String userId;
    private Boolean isAlsoLogin;
    private String geoId;
    private RelativeLayout no_network_rl;
    private NetReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        TitleBarView title = (TitleBarView) findViewById(R.id.title1);
        title.setTitleBar("登录", View.GONE, View.GONE, View.GONE, false);
        title.setTitleBackgroundColor(getResources().getColor(R.color.small_title_text_color));
        initView();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Intent intent = this.getIntent();
        isLogout = intent.getStringExtra("isLogout");
//        Log.d("TAG","isLogout:"+isLogout);
        if ("Y".equals(isLogout)) {//注销清楚缓存
            cleanLoginInfo();
        }
        loadLoginInfo();


        if (isAlsoLogin && !TextUtils.isEmpty(nid) && !TextUtils.isEmpty(userType) && !TextUtils.isEmpty(geoId) && !TextUtils.isEmpty(userId)) {
//            Log.d("TAG_2", "geoid:" + geoId + "userId:"+userId +"userType"+userType);
            Intent intent1 = new Intent(MainActivity.this, HomeActivity.class);
            intent1.putExtra("userType", userType);

            startActivity(intent1);
            finish();
//            loginThread();
        } else if (isAutoLogin) {
            loginThread();
        }

        initReceive();
        EventBus.getDefault().register(this);//eventBus是不同类之间通信的第三方包
        registerListener();
    }

    @Override
    protected void initView() {
        userNameEdittext = (EditText) findViewById(R.id.login_edit_username);
        pwdEditText = (EditText) findViewById(R.id.login_edit_password);
        loginBtn = (Button) findViewById(R.id.login_btn_login);
        rememberPassword = (CheckBox) findViewById(R.id.login_remember_password);
        autoLogin = (CheckBox) findViewById(R.id.login_auto_login);
        no_network_rl = (RelativeLayout)findViewById(R.id.net_view_rl);
    }
    public void onEventMainThread(NetEvent event) { //eventBus的处理数据的方法

        setNetState(event.isNet());
    }

    public void setNetState(boolean netState) {

        if (no_network_rl != null) {
            no_network_rl.setVisibility(netState ? View.GONE : View.VISIBLE);
            no_network_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    NetUtils.startToSettings(MainActivity.this);
                }
            });
        }
    }

    private void initReceive() {
        mReceiver = new NetReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    private void StartXG(String id) {
        //开启信鸽
        // 开启logcat输出，方便debug，发布时请关闭

        Context context = getApplicationContext();
        XGPushManager.registerPush(context);

// 2.36（不包括）之前的版本需要调用以下2行代码
//        Intent service = new Intent(context, XGPushService.class);
//        context.startService(service);

        XGPushConfig.enableDebug(this, true);
// 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(), XGIOperateCallback)带callback版本
        XGPushManager.registerPush(context, id, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                Log.d("TAG", "信鸽成功" + o);
            }

            @Override
            public void onFail(Object o, int i, String s) {
                Log.d("TAG", "信鸽失败");

            }
        });
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
                if (b) {
                    isRememberPassword = true;
                } else {
                    isRememberPassword = false;
                    autoLogin.setChecked(false);
                }
            }
        });

        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

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

    public static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) MosApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    public void login(boolean isShowProcessDialog) {
        Message m = new Message();
        if (!isCiphertext) {
            pwd = pwdEditText.getText().toString();
            userName = userNameEdittext.getText().toString();
        }

        if (StringUtil.isBlank(userName)) {
            m.what = 1;
            loginHandler.sendMessage(m);
            return;
        }
        if (StringUtil.isBlank(pwd)) {
            m.what = 2;
            loginHandler.sendMessage(m);
            return;
        }
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("isCiphertext", isCiphertext);
            requestJson.put("password", pwd);
            requestJson.put("userName", userName);
            String url = "tz/TZDeviceAction.do?method=login";
            result = Communication.getPostResponseForNetManagement(url, requestJson.toString());
            reponseJson = JSON.parseObject(result);

//            Log.d("TAG_1"," login reponseJson:"+reponseJson);
            if (StringUtil.isBlank(result)) {
                m.what = 3;
                loginHandler.sendMessage(m);
                return;
            }
            if (reponseJson.containsKey("error")) {
                errorMessage = reponseJson.getString("error");
                m.what = 5;
                loginHandler.sendMessage(m);
                return;
            } else {
                nid = reponseJson.getString("NID");

                if (isRememberPassword) {
                    saveLoginInfo();
                } else {
                    cleanLoginInfo();
                }
                userId = reponseJson.getString("userId");
                if (!TextUtils.isEmpty(userId)) {
                    StartXG(userId);
                }
                userType = reponseJson.getString("userType");
//                Log.d("TAG_1"," login NID:"+nid+" userId:"+userId+" userType:"+userType);
                String geoId = "";
                JSONArray geoArray = reponseJson.getJSONArray("geolist");
                if (geoArray != null && geoArray.size() > 0) {
                    JSONObject json = (JSONObject) geoArray.get(0);
                    geoId = json.getString("value");
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userId", userId);
                editor.putString("userType", userType);
                editor.putString("geoId", geoId);
                editor.putString("nid", nid);
                editor.commit();
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


    private void loginThread() {
        boolean isShowProcessDialog = true;
        if (isShowProcessDialog) {
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
        editor = sharedPreferences.edit();
        editor.putBoolean("isRememberPassword", isRememberPassword);
        editor.putBoolean("isAutoLogin", isAutoLogin);
        editor.putString("password", pwd);
        editor.putString("userName", userName);


        editor.commit();
        editor.clear();
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
        nid = sharedPreferences.getString("nid", "");
        userType = sharedPreferences.getString("userType", "");
        isAlsoLogin = sharedPreferences.getBoolean("isAlsoLogin", false);
        userId = sharedPreferences.getString("userId", "");
        geoId = sharedPreferences.getString("geoId", "");
        if (isRememberPassword) {
            userNameEdittext.setText(userName);
            if (!StringUtil.isBlank(pwd)) {
                isCiphertext = true;
                pwdEditText.setText("*******");
            }
        } else {
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
        editor.putString("userId", "");
        editor.putString("geoId", "");
        editor.putString("nid", "");
        editor.putString("userType", "");
        //editor.putBoolean("isFirstRun", isFirstRun);
        editor.commit();
    }

    public Handler loginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    Toast.makeText(MainActivity.this, "请输入用户名！", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 2: {
                    Toast.makeText(MainActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 3: {
                    Toast.makeText(MainActivity.this, "登录失败,服务端返回为空！", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 4: {
                    Toast.makeText(MainActivity.this, "登录调用服务端异常！", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 5: {
//                    Log.d("TAG_1", "case 5");
//                    if (errorMessage.contains("该用户已登录")) {
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
//                    }else if (errorMessage.contains("密码错误")){
//                        Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
//                    }
                    break;
                }
                case 6: {
//                    Log.d("TAG_1", "case 6");
                    closePres = false;
                    //检测版本
                    checkNewVersion(reponseJson);
                    break;
                }

            }
            if (closePres) {
                progressDialog.closeProcessDialog();
            } else {
                closePres = true;
            }


        }
    };


    //检查软件更新
    private void checkNewVersion(JSONObject info) {
        JSONArray versonArray = info.getJSONArray("androidlist");
        JSONObject androidVersion = null;
        if (versonArray != null && versonArray.size() > 0) {
            for (int i = 0; i < versonArray.size(); i++) {
                JSONObject version = versonArray.getJSONObject(i);
                String remarks = version.getString("REMARKS");
                if ("android".equals(remarks)) {
                    androidVersion = version;
                    break;
                }
            }
        }

        int versonService = Integer.valueOf(androidVersion.getString("VERSION_NUM"));
        String pathUrl = androidVersion.getString("PUBLISH_PATH");
        String desc = androidVersion.getString("VERSION_DESC");
        String isForce = androidVersion.getString("IS_FORCE");

//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("wh_versonService", versonService);
//        editor.putString("wh_pathUrl", pathUrl);
//        editor.putString("wh_desc", desc);
//        editor.putString("wh_isForce", isForce);
//        editor.commit();
//        editor.clear();

        int versionLocal = 0;
        try {
            // 获取软件版本号
            versionLocal = MainActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (versonService > versionLocal) {
            manager = new UpdateManager(false, MainActivity.this);
            if ("Y".equals(isForce)) {
                manager.setIsForced(true);
            }
            manager.setPublishUrl(pathUrl);
            showNoticeDialog(desc, isForce);
        } else {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("userType", userType);

            startActivity(intent);
            finish();
        }
    }

    //检查软件更新
    private void checkNewVersionAlso() {

        int versonService = sharedPreferences.getInt("wh_versonService", 0);
        String pathUrl = sharedPreferences.getString("wh_pathUrl", "");
        String desc = sharedPreferences.getString("wh_desc", "");
        String isForce = sharedPreferences.getString("wh_isForce", "");
        int versionLocal = 0;
        try {
            // 获取软件版本号
            versionLocal = MainActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (versonService > versionLocal) {
            manager = new UpdateManager(false, MainActivity.this);
            if ("Y".equals(isForce)) {
                manager.setIsForced(true);
            }
            manager.setPublishUrl(pathUrl);
            showNoticeDialog(desc, isForce);
        } else {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("userType", userType);

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
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.putExtra("userType", userType);

                    startActivity(intent);
                    finish();

                }

            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 必须要调用这句
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
