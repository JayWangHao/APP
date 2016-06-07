package com.cattsoft.framework.base;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.cattsoft.framework.R;
import com.cattsoft.framework.cache.CacheProcess;
import com.cattsoft.framework.cache.MosApp;
import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class BaseFragmentActivity extends FragmentActivity {

    public MosApp mosApp;
    public CacheProcess cache;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mosApp=(MosApp) getApplication();
        if(cache==null) {
            cache=new CacheProcess();
        }

        mosApp.addActivity(this);

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(mosApp);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean("isCall",false);//是否点击呼叫
        edit.commit();
    }

    /**
     * 初始化组件。用于在Activity装载时初始化相关组件和变量，以减少oncreate方法的庞大和冗余。
     */
    protected abstract void initView();

    /**
     * 用于注册组件的监听事件。
     */
    protected abstract void registerListener();

    /**
     * 重写onRestart()方法，当isExit值为true时退出整个程序
     */
    @Override
    public void onRestart() {
        if (mosApp.isExit()) {
            super.onRestart();
            finish();
        } else {
            super.onRestart();
        }
    }

    /**
     * 重写onResume()方法
     */
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed(){
//		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//		ComponentName componentName = activityManager.getRunningTasks(1).get(0).topActivity;
//		String preActivity=componentName.getClassName().substring(26);
//		if(preActivity.equals("AllTaskListActivity")||preActivity.equals("WoQueryActivity")||preActivity.equals("ProcessQueryActivity")||preActivity.equals("StatisticalReportActivity")||preActivity.equals("KnowledgeBaseMainActivity")||preActivity.equals("BulletinListActivity")||preActivity.equals("SetUpActivity")) {
//			Intent intent = new Intent("com.cattsoft.mos.HOME");// HOME为自定义action，在Manifest中配置
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 清除栈中FuncNavActivity之上的所有Activity。FuncNavActivity也同样会finish(),并重新创建
//			startActivity(intent);
//		}else {
//			super.onBackPressed();
//		}
        super.onBackPressed();
    }



}
