package com.cattsoft.framework.base;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cattsoft.framework.R;
import com.cattsoft.framework.cache.CacheProcess;
import com.cattsoft.framework.cache.MosApp;
import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class BaseListActivity extends ListActivity {
	private String encryptUsable;
	private String isScreen;//是否点击筛选按钮
	private SharedPreferences sharedPreferences;
	private ImageButton titleLeftButton;// 标题栏左边按钮，默认图标为返回，背景为透明
	private TextView titleTextView;// 标题栏居中标题
	private ImageView titleDownArrow;// 标题右侧小箭头，默认不可见
	private RelativeLayout titleMiddleButton;// 中部可点击区域，包括标题以及小箭头，默认不可点击
	private ImageButton titleRightButton;// 标题栏右边按钮，默认图标为刷新，背景为透明
	public MosApp mosApp;//缓存
	public Dialog mProgressDialog;//进度条
	public boolean isCancel=false;//判断是按了返回键
	public String exceptionDesc=null;
	public  CacheProcess cache;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	public ImageButton getTitleLeftButton() {
		return titleLeftButton;
	}

	public TextView getTitleTextView() {
		return titleTextView;
	}

	public ImageView getTitleDownArrow() {
		return titleDownArrow;
	}

	public RelativeLayout getTitleMiddleButton() {
		return titleMiddleButton;
	}

	public ImageButton getTitleRightButton() {
		return titleRightButton;
	}

	/**
	 * 设置标题栏
	 * 
	 * @param titleText
	 *            标题文字
	 */
	public void setTitleBar(String titleText) {
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);// 设置标题栏布局文件为title_model
		titleLeftButton = (ImageButton) findViewById(R.id.titlebar_img_btn_left);
		titleTextView = (TextView) findViewById(R.id.titlebar_text);
		titleDownArrow = (ImageView) findViewById(R.id.titlebar_down_arrow);
		titleMiddleButton = (RelativeLayout) findViewById(R.id.titlebar_title);	
		titleRightButton = (ImageButton) findViewById(R.id.titlebar_img_btn_right);
		setTitleText(titleText);
		leftButtonOnClick();
		middleButtonOnClick();
		rightButtonOnClick();
	}

	/**
	 * 设置标题栏
	 * 
	 * @param titleText
	 *            标题文字
	 * @param leftButtonVisibility
	 *            左侧按钮可见状态
	 * @param rightButtonVisibility
	 *            右侧按钮可见状态
	 * @param downArrowVisibility
	 *            小箭头可见状态
	 * @param middleButtonClickable
	 *            中部标题区域可点击状态
	 */
	public void setTitleBar(String titleText, int leftButtonVisibility,
			int rightButtonVisibility, int downArrowVisibility,
			boolean middleButtonClickable) {
					
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);// 设置标题栏布局文件为title_model
		
		titleLeftButton = (ImageButton) findViewById(R.id.titlebar_img_btn_left);
		titleTextView = (TextView) findViewById(R.id.titlebar_text);
		titleDownArrow = (ImageView) findViewById(R.id.titlebar_down_arrow);
		titleMiddleButton = (RelativeLayout) findViewById(R.id.titlebar_title);
		titleRightButton = (ImageButton) findViewById(R.id.titlebar_img_btn_right);
		setTitleText(titleText);
		setTitleLeftButtonVisibility(leftButtonVisibility);
		setTitleRightButtonVisibility(rightButtonVisibility);
		setTitleDownArrowVisibility(downArrowVisibility);
		leftButtonOnClick();
		middleButtonOnClick();
		rightButtonOnClick();
		setTitleMiddleButtonClickable(middleButtonClickable);
	}

	/**
	 * 设置左边按钮是否可见
	 * 
	 * @param visibility
	 *            可见状态
	 */
	public void setTitleLeftButtonVisibility(int visibility) {
		titleLeftButton.setVisibility(visibility);
	}

	/**
	 * 设置右边按钮是否可见
	 * 
	 * @param visibility
	 *            可见状态
	 */
	public void setTitleRightButtonVisibility(int visibility) {
		titleRightButton.setVisibility(visibility);
	}

	/**
	 * 设置小箭头是否可见
	 * 
	 * @param visibility
	 *            可见状态
	 */
	public void setTitleDownArrowVisibility(int visibility) {
		titleDownArrow.setVisibility(visibility);
	}

	/**
	 * 设置左侧按钮图标
	 * 
	 * @param leftButtonImg
	 *            图标资源id
	 */
	public void setTitleLeftButtonImg(int leftButtonImg) {
		titleLeftButton.setImageResource(leftButtonImg);
	}

	/**
	 * 设置右侧按钮图标
	 * 
	 * @param rightButtonImg
	 *            图标资源id
	 */
	public void setTitleRightButtonImg(int rightButtonImg) {
		titleRightButton.setImageResource(rightButtonImg);
	}

	/**
	 * 设置标题文本
	 * 
	 * @param titleText
	 *            标题文本
	 */
	public void setTitleText(String titleText) {
		titleTextView.setText(titleText);
	}

	/**
	 * 设置中部区域可点击状态
	 * 
	 * @param clickable
	 *            可点击状态
	 */
	public void setTitleMiddleButtonClickable(boolean clickable) {
		titleMiddleButton.setClickable(clickable);
	}

	/**
	 * 左侧按钮点击事件监听 默认返回主界面
	 */
	public void leftButtonOnClick() {
		titleLeftButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onBackPressed();
			}
		});
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
	 * 中部标题区域点击事件监听 默认切换工区
	 */
	public void middleButtonOnClick() {
		titleMiddleButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent("com.cattsoft.mos.CHANGE_WORK_AREA");// CHANGE_WORK_AREA为自定义action，在Manifest中配置
				startActivity(intent);
			}
		});
	}

	/**
	 * 右侧按钮点击事件监听
	 */
	public void rightButtonOnClick() {
		titleRightButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mosApp=(MosApp) getApplication();
		if(cache==null) {
			cache=new  CacheProcess();
		}

		mosApp.addActivity(this);

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(mosApp);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean("isCall",false);//是否点击呼叫
        edit.commit();
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
	 * 重写onResume()方法，获得application
	 */
	@Override
	public void onResume() {
		super.onResume();
		
	}

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
	
	@Override
	public void onBackPressed(){
		/*ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		ComponentName componentName = activityManager.getRunningTasks(1).get(0).topActivity;
		String preActivity=componentName.getClassName().substring(26);
		if(preActivity.equals("AllTaskListActivity")||preActivity.equals("WoQueryActivity")||preActivity.equals("ProcessQueryActivity")||preActivity.equals("StatisticalReportActivity")||preActivity.equals("KnowledgeBaseMainActivity")||preActivity.equals("BulletinListActivity")||preActivity.equals("SetUpActivity")) {
			Intent intent = new Intent("com.cattsoft.framework.HOME");// HOME为自定义action，在Manifest中配置
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 清除栈中FuncNavActivity之上的所有Activity。FuncNavActivity也同样会finish(),并重新创建
			startActivity(intent);
		}else {*/
			super.onBackPressed();
		/*}*/
	}
	
	public  CacheProcess getCacheProcess() {
		if(cache==null) {
			cache=new  CacheProcess();
		}
		return cache;
	}
	
	public void setIsScreen(String str){
		isScreen=str;
	}
}