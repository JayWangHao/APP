package com.cattsoft.framework.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.cattsoft.framework.R;
import com.cattsoft.framework.base.BaseActivity;
import com.cattsoft.framework.connect.Communication;

/**
 * 导航页面
 * @author xueweiwei
 *
 */
public class MosGuideActivity extends BaseActivity {

	private ViewPager mosGuidePager;
	private List<View> mosGuidePageList;
	private boolean isFirstRun = true;
	private SharedPreferences sharedPreferences;
	private SharedPreferences sharedPrefer;
	private String encryptUsable = "";

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mos_guide_activity);
		loadLoginInfo();
		initViewPager();
		if(isFirstRun){
			isFirstRun = false;
			saveFirstRunInfo();
		}
	}

	private void initViewPager() {
		mosGuidePager = (ViewPager) findViewById(R.id.mos_guide_view_pager);
		mosGuidePageList = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		mosGuidePageList.add(mInflater.inflate(R.layout.mos_guide_page_1, null));
		mosGuidePageList.add(mInflater.inflate(R.layout.mos_guide_page_2, null));
		mosGuidePageList.add(mInflater.inflate(R.layout.mos_guide_page_3, null));
		mosGuidePageList.add(mInflater.inflate(R.layout.mos_guide_page_4, null));
		mosGuidePager.setAdapter(new MyPagerAdapter(mosGuidePageList));
		mosGuidePager.setCurrentItem(0);
		mosGuidePager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * ViewPager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}
		@Override
		public void finishUpdate(View arg0) {
		}
		@Override
		public int getCount() {
			return mListViews.size();
		}
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			if(arg1==0){

			}else if(arg1==1) {

			}else if(arg1 ==2 ){

			}else if (arg1 == 3){
				initPage4(arg0);
			}
			return mListViews.get(arg1);
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}
		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}
		@Override
		public Parcelable saveState() {
			return null;
		}
		@Override
		public void startUpdate(View arg0) {
		}
	}
	//点击进入登陆界面
	private void initPage4(View funcNavPage4){
		Button experienceBtn = (Button) findViewById(R.id.guide_experience_btn);
		//点击马上体验，会去服务端获取加密方式
		experienceBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Communication communication = new Communication("loginAction.do?method=encryptConfig4MOS",null,"afterExperience",MosGuideActivity.this);
//				communication.getPostHttpContent();
				//	如果从导航页面进入登陆页面，即第一次使用该客户端，则需要用户下载各个模块
//				Bundle bundle = new Bundle();
//				bundle.putBoolean("isNeedDown", true);
//				intent.putExtras(bundle);
				Intent intent = new Intent(MosGuideActivity.this,MainLoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	private void afterExperience() {
		saveEncryptInfo();
		Intent intent = new Intent(MosGuideActivity.this,MainLoginActivity.class);
		//如果从导航页面进入登陆页面，即第一次使用该客户端，则需要用户下载各个模块
		Bundle bundle = new Bundle();
		bundle.putBoolean("isNeedDown", true);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	/**
	 * 页卡切换监听
	 */

	// 用于页面切换时可能触发的事件
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	private void saveFirstRunInfo(){
		sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("isFirstRun", isFirstRun);
		editor.commit();
	}

	private void saveEncryptInfo(){
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putString("encryptUsable", encryptUsable);
		editor.putString("calendarDate", dateFormat.format(now));
		editor.commit();
		//将第一次安装的当天日期也放到SharedPreferences中，供流量监控及后续其它功能使用
		sharedPrefer=this.getSharedPreferences("LIULIANG", Context.MODE_WORLD_WRITEABLE);
		Editor editor1 = sharedPrefer.edit();
		editor1.putString("calendarDate", dateFormat.format(now));
		editor1.commit();
	}

	private void loadLoginInfo(){
		sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
		isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}


	@Override
	protected void registerListener() {
		// TODO Auto-generated method stub

	}

}