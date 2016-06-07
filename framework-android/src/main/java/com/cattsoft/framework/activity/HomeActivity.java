package com.cattsoft.framework.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cattsoft.framework.R;
import com.cattsoft.framework.base.BaseActivity;
import com.cattsoft.framework.base.BaseFragmentActivity;
import com.cattsoft.framework.cache.CacheProcess;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.framework.view.TitleBarView;

public class HomeActivity extends BaseFragmentActivity {
	
	private Fragment homeFragment;
	private static boolean isExit = false;// 程序退出标志位
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		
		//导航栏初始化
		TitleBarView title =  (TitleBarView) findViewById(R.id.title1);
		title.setTitleBar(getString(R.string.func_nav_title), View.GONE,View.GONE, View.GONE, false);
		
		title.getTitleLeftButton().setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				onBackPressed();
				
			}
			
		});
		
		
		initView();
		registerListener();
	}

	@Override
	protected void initView() {
		
		String menusListJson =CacheProcess
				.getCacheValueInSharedPreferences(this, "menus");
		
		String homeTheme = CacheProcess.getCacheValueInSharedPreferences(this, "theme");
		
		try {
			if (!StringUtil.isBlank(homeTheme)) {
				homeFragment = (Fragment) Class.forName(homeTheme)
						.newInstance();
			} else {
				homeFragment = new HomeFragment();
			}
		}catch (ClassNotFoundException e)   {
			homeFragment = new HomeFragment();
		} catch (InstantiationException e) {
			homeFragment = new HomeFragment();
		} catch (IllegalAccessException e) {
			homeFragment = new HomeFragment();
		}
		   
		
		
		Bundle bundle = new Bundle();
		bundle.putString("menus", menusListJson);
		
        FragmentManager fm = getSupportFragmentManager();
		
        homeFragment.setArguments(bundle);
		
		
		
		FragmentTransaction tx = fm.beginTransaction();
        tx.add(R.id.content, homeFragment, "main");  
        
        tx.commit();
		
	}

	@Override
	protected void registerListener() {
		// TODO Auto-generated method stub
		
	}

	// 点击两次返回键退出程序
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				exit();
				return false;
			} else {
				return super.onKeyDown(keyCode, event);
			}
		}

		public void exit() {
			if (!isExit) {
				isExit = true;
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mHandler.sendEmptyMessageDelayed(0, 2000);

			} else {

				new Handler().postDelayed(new Runnable() {
					public void run() {
						finish();
						System.exit(0);
					}
				}, 700);

			}
		}

		Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				isExit = false;

			}

		};
}
