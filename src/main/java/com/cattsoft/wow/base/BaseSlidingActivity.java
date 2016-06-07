package com.cattsoft.wow.base;

import android.os.Bundle;
import android.view.Window;

import com.cattsoft.framework.cache.MosApp;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

public class BaseSlidingActivity extends SlidingActivity {

    public MosApp mosApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mosApp = (MosApp) getApplication();
        mosApp.addActivity(this);
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
    public void onBackPressed() {
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
