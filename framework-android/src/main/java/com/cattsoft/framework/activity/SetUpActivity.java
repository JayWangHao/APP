package com.cattsoft.framework.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cattsoft.framework.R;
import com.cattsoft.framework.base.BaseActivity;
import com.cattsoft.framework.cache.MosApp;
import com.cattsoft.framework.view.TitleBarView;

/**
 * 设置页面
 * @author wei
 *
 */
public class SetUpActivity extends BaseActivity  {

	private RelativeLayout relatup;
	private RelativeLayout relatmid;
	private RelativeLayout relatdown;
	private RelativeLayout changeUserRel,updatePwdRel;//更改用户 更新密码
	private ImageView exitMosIcon;//退出系统
	
	private UpdateManager updateManager;
	
	private MosApp mosApp;

	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_up);
		
		//导航栏初始化
		TitleBarView title =  (TitleBarView) findViewById(R.id.title1);
		title.setTitleBar("设置", View.VISIBLE,View.GONE, View.GONE, false);
		
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
		
		//用户反馈
		relatup = (RelativeLayout) findViewById(R.id.set_up_layout);
		//检测更新
		relatmid = (RelativeLayout) findViewById(R.id.set_up_layout_mid);
		//关于
		relatdown = (RelativeLayout) findViewById(R.id.set_up_layout_down);
		//退出系统按钮
		exitMosIcon = (ImageView)findViewById(R.id.btn_exit_mos);
		//切换用户按钮
		changeUserRel = (RelativeLayout) findViewById(R.id.set_up_chg_user);
		//修改密码按钮
		updatePwdRel = (RelativeLayout) findViewById(R.id.set_up_update_pwd);
		
		updateManager = new UpdateManager("mobileService/login1", true, "com.cattsoft.mainframework",SetUpActivity.this);
		
		mosApp=(MosApp) getApplication();
	}

	@Override
	protected void registerListener() {
		
		relatup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SetUpActivity.this, FeedbackActivity.class);
				SetUpActivity.this.startActivity(intent);
			}
		});
		//关于
		relatdown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent =new Intent();
				intent.setClass(SetUpActivity.this,AboutActivity.class);
				SetUpActivity.this.startActivity(intent);
			}
		});

		relatmid.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				updateManager.checkVersion();
			}
		});

		relatdown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SetUpActivity.this, AboutUsActivity.class);
				SetUpActivity.this.startActivity(intent);
			}
		});
		
		//退出系统
		exitMosIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mosApp.exit();

			}
		});
		
		//切换用户
		changeUserRel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentHome = new Intent("com.cattsoft.mainframework.LOGIN");
				
				intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				finish();
				startActivity(intentHome);
				
			}
		});
		
		//修改密码
		updatePwdRel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Toast.makeText(getApplicationContext(), "此功能暂未开放，如有需要请与开发者联系",
						Toast.LENGTH_SHORT).show();

				
			}
		});
		
	}
}
