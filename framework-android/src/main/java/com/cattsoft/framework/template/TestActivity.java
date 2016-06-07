package com.cattsoft.framework.template;


import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.LinearLayout;

import com.cattsoft.framework.R;
import com.cattsoft.framework.base.BaseActivity;
import com.cattsoft.framework.view.CustomDialog;
import com.cattsoft.framework.view.EditLabelText;
import com.cattsoft.framework.view.LabelText;
import com.cattsoft.framework.view.SearchEditText;
import com.cattsoft.framework.view.SelectItem;
import com.cattsoft.framework.view.TitleBarView;

import java.util.ArrayList;
import java.util.HashMap;


public class TestActivity extends BaseActivity{

	private LabelText lt;
	private LinearLayout ll;
	private EditLabelText loginname,password;
	private SelectItem item,item2;
	private SearchEditText searchEditText;
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
//		TitleBarView title =  (TitleBarView) findViewById(R.id.title1);
//		title.setTitleBar("测试页面", View.GONE,View.GONE, View.GONE, false);
		LinearLayout l = (LinearLayout) findViewById(R.id.ll);
		TitleBarView title = new TitleBarView(this);
	//	title.setTitleLeftButtonVisibility(View.VISIBLE);
		title.setTitleBar("测试", View.VISIBLE, View.VISIBLE, View.GONE, false);

		l.addView(title);
		
		initView();
		registerListener();
		
	}
	@Override
	protected void initView() {
		ll = (LinearLayout)findViewById(R.id.ll);
		
		// EditLabelText
		// 在布局文件里写好后的用法
		loginname = (EditLabelText)findViewById(R.id.loginname);
	//	loginname.setBackground(Color.WHITE);
		loginname.setMargin(20,20,20,20);
//		loginname.setLabel("用户名");
		loginname.setValueInputType(InputType.TYPE_CLASS_TEXT);
//		loginname.setValueTextHint("请输入用户名");

        password = new EditLabelText(this);
	//	password = (EditLabelText)getLayoutInflater().inflate(R.layout.edit_label_text_layout, null);
		password.setLabel("密码");
		password.setValue("8888");
		password.setValueTextHint("请输入密码");
		password.setMargin(20,10,20,0);
	//	password.setBackground(Color.WHITE);
     //   password.setBackground(R.drawable.edit_label_text_bg);
        password.setDefaultStyle();
		ll.addView(password,4);
		
		// LabelText 
		// 动态生成的写法
		lt = (LabelText)getLayoutInflater().inflate(R.layout.label_text_layout, null);
	//	lt.setBackground(R.drawable.bg);

		lt.setMargin(20,20,20,0);
		lt.setLabel("工单号");
		lt.setLabelTextColor(getResources().getColor(R.color.black));
		lt.setLabelTextSize(20);
		lt.setValue("299800076");
		lt.setValueTextSize(20);
		lt.setValueInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        lt.setDefaultStyle();
		ll.addView(lt,6);// 第0个子元素（firstChild）后边插入当前view
		
		// SelectItem
		item = (SelectItem) findViewById(R.id.item);
//		item.setDrawableLeft(R.drawable.icon_test);
//		item.setDrawableRight(R.drawable.arrow);
//		item.setLabel("工作地区");// 默认是黑色字体
//		item.setValue("北京");// 默认是黑色字体
		//item.setValueTextColor(Color.GRAY);
		item.setMargin(20,0,20,0);

		
		item2 = (SelectItem)getLayoutInflater().inflate(R.layout.select_item_layout, null);

        item2.setDefaultStyle();
//		item2.setBackGroundColor(Color.WHITE);
//		item2.setDrawableRight(R.drawable.arrow);
		item2.setLabel("用户反馈");
        item2.setValue("34234");
//	//	item2.setHint("反馈您遇到的问题及宝贵意见");
		item2.setMargin(20,20,20,0);

        final ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

        HashMap<String,String> map = new HashMap<String, String>();
        map.put("id","1");
        map.put("name","京东");

        HashMap<String,String> map1 = new HashMap<String, String>();
        map1.put("id","1");
        map1.put("name","京东");

        list.add(map);
        list.add(map1);



        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new CustomDialog(TestActivity.this,list,"请选择厂商","name",new CustomDialog.OnCustomDialogListener(){

                    @Override
                    public void back(HashMap<String, String> value) {

                        item2.setValue(value.get("name"));
                    }
                }).showDialog();
            }
        });
		ll.addView(item2,8);
		
	}
	@Override
	protected void registerListener() {
		
	}
}
