package com.cattsoft.framework.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cattsoft.framework.R;
import com.cattsoft.framework.base.BaseActivity;
import com.cattsoft.framework.view.TitleBarView;

import java.util.ArrayList;
import java.util.HashMap;


public class AboutUsActivity extends BaseActivity{
	private TitleBarView title;
	TextView versionName;
	ListView aboutLst;
	private ArrayList<HashMap<String,String>> listData = new ArrayList<HashMap<String,String>>();
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}
	@Override
	protected void initView() {
		title = (TitleBarView) findViewById(R.id.title1);
		title.setTitleBar("关于", View.VISIBLE,
				View.GONE, View.GONE, false);
		title.getTitleLeftButton().setImageDrawable(getResources().
				getDrawable(R.drawable.title_left_btn_back));
		title.getTitleLeftButton().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		versionName = (TextView) findViewById(R.id.about_version);
	}
	@Override
	protected void registerListener() {

	}
}
