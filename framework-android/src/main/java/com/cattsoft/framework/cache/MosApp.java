package com.cattsoft.framework.cache;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.cattsoft.framework.exception.CrashHandler;

/**
 * 用于存放全局变量
 * 
 * @author xueweiwei
 * 
 */
public class MosApp extends Application {

	/**
	 * 退出状态，用于整个程序的退出
	 */
	private boolean isExit = false;

	private boolean isReLogin = false;

	private static MosApp instance;
	private static int i = 0;

	private List<Activity> activityList = new LinkedList<Activity>();

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		// 捕获程序崩溃日志，并将日志发送到服务端
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		 crashHandler.init(getApplicationContext());

	}

	public boolean isExit() {
		return isExit;
	}

	public void setExit(boolean isExit) {
		this.isExit = isExit;
	}

	public boolean isReLogin() {
		return isReLogin;
	}

	public void setReLogin(boolean isReLogin) {
		this.isReLogin = isReLogin;
	}

	public static MosApp getInstance() {
		return instance;
	}

	public static void setInstance(MosApp instance) {
		MosApp.instance = instance;
	}

	/**
	 * 清空缓存
	 */
	public void clearAll() {

		this.isExit = false;
		this.isReLogin = false;

	}

	// /**
	// * 退出程序
	// */
	// public void exit() {
	// this.isExit = true;
	//
	// clearAll();
	// System.gc();
	// }

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity并finish

	public void exit() {

		for (Activity activity : activityList) {
			activity.finish();
		}

		System.exit(0);

	}

//	@Override
//	public void onTerminate() {
//		super.onTerminate();
//		if(isServiceWork(this,"PalpitateService")){
////			Intent intent=new Intent(this,PalpitateService.class);
////			stopService(intent);
//		}
//	}
//
//	/**
//	 * 判断某个服务是否正在运行的方法
//	 *
//	 * @param mContext
//	 * @param serviceName
//	 *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
//	 * @return true代表正在运行，false代表服务没有正在运行
//	 */
//	public boolean isServiceWork(Context mContext, String serviceName) {
//		boolean isWork = false;
//		ActivityManager myAM = (ActivityManager) mContext
//				.getSystemService(Context.ACTIVITY_SERVICE);
//		List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
//		if (myList.size() <= 0) {
//			return false;
//		}
//		for (int i = 0; i < myList.size(); i++) {
//			String mName = myList.get(i).service.getClassName().toString();
//			if (mName.equals(serviceName)) {
//				isWork = true;
//				break;
//			}
//		}
//		return isWork;
//	}
}
