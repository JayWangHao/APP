package com.cattsoft.framework.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;

import com.cattsoft.framework.R;
import com.cattsoft.framework.cache.CacheProcess;
import com.cattsoft.framework.cache.MosApp;
import com.cattsoft.framework.view.qrcode.decoding.Intents;
import com.nostra13.universalimageloader.core.ImageLoader;
/**
 * Activity的基类，所有的Activity都继承该类
 * @author xueweiwei
 *
 */
public abstract class BaseActivity extends Activity{
	
	public MosApp mosApp;
	public CacheProcess cache;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

    public Dialog mProgressDialog;//进度条
    public boolean isCancel=false;//判断是按了返回键
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mosApp=(MosApp) getApplication();
		if(cache==null) {
			cache=new  CacheProcess();
		}
		
		mosApp.addActivity(this);
//		initView();
//		registerListener();

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
     * 显示进度条
     * @param finishActivity
     */
    public void showProcessDialog(final boolean finishActivity) {
        mProgressDialog = new Dialog(this,
                R.style.process_dialog);//创建自定义进度条
        mProgressDialog.setContentView(R.layout.progress_dialog);//自定义进度条的内容
        mProgressDialog.setCancelable(true);
        mProgressDialog
                .setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        isCancel=true;
                        if(finishActivity)finish();
                    }
                });
        mProgressDialog.show();//显示进度条

    }

    /**
     * 关闭进度条
     */
    protected void closeProcessDialog() {
        if(mProgressDialog!=null) {
            mProgressDialog.dismiss();
        }
    }

	
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


		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		ComponentName componentName = activityManager.getRunningTasks(1).get(0).topActivity;
		String preActivity=componentName.getClassName().substring(26);
		if(preActivity.equals(".activity.WoHandleListActivity")||preActivity.equals(".activity.WoHandleFetchListActivity")
				) {
//			Intent intent = new Intent("com.cattsoft.mos.HOME");// HOME为自定义action，在Manifest中配置
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 清除栈中FuncNavActivity之上的所有Activity。FuncNavActivity也同样会finish(),并重新创建
//          startActivity(intent);
//			Intent intent = new Intent();
//			intent.putExtra("refreshHome", true);
//			setResult(RESULT_OK, intent);
//		}else {

            CacheProcess.setBoolean(this,"refreshHome",true);


		}else{

            CacheProcess.setBoolean(this,"refreshHome",false);
        }
		super.onBackPressed();
	}



}
