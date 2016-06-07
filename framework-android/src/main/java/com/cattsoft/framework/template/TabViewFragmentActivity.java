package com.cattsoft.framework.template;

import java.util.ArrayList;
import java.util.List;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.cattsoft.framework.R;
import com.cattsoft.framework.base.BaseActivity;
import com.cattsoft.framework.util.SerializableList;
import com.cattsoft.framework.view.TabViewFragment;
import com.cattsoft.framework.view.TitleBarView;
/**
 * viewpager+button示例
 * @author wei
 *
 */
public class TabViewFragmentActivity extends BaseActivity{

	private TabViewFragment tabFragment;
	private ViewPager mPager;// 页卡控件
	private List<View> mosGuidePageList;
	
	public void onCreate(Bundle savedInstanceState) {

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_view_activity);
		// 导航栏初始化
		TitleBarView title = (TitleBarView) findViewById(R.id.title1);
		title.setTitleBar(getString(R.string.app_name), View.VISIBLE,
				View.VISIBLE, View.GONE, false);

		title.getTitleLeftButton().setOnClickListener(
				new Button.OnClickListener() {

					@Override
					public void onClick(View arg0) {

						onBackPressed();

					}

				});
		title.getTitleRightButton().setBackgroundResource(
				R.drawable.titlebar_img_btn_reload);
		
		title.setVisibility(View.GONE);
		initView();
		registerListener();
	}
	
	@Override
	protected void initView() {
		
		LayoutInflater mInflater = getLayoutInflater();
		//内容页
		mosGuidePageList = new ArrayList<View>();
		mosGuidePageList.add(mInflater.inflate(R.layout.mos_guide_page_1, null));
		mosGuidePageList.add(mInflater.inflate(R.layout.mos_guide_page_2, null));
		mosGuidePageList.add(mInflater.inflate(R.layout.mos_guide_page_3, null));
		mosGuidePageList.add(mInflater.inflate(R.layout.mos_guide_page_4, null));
		mosGuidePageList.add(mInflater.inflate(R.layout.mos_guide_page_4, null));
		
		//标签标题
		List list = new ArrayList();
		list.add("工单信息");
		list.add("定单信息");
		list.add("资源信息");
		list.add("照片");
		list.add("材料信息");
		
		SerializableList sList = new SerializableList();
		sList.setList(list);
		//加载viewpager+button页面
		tabFragment = new TabViewFragment();
		//向fragment页面传送数据
		Bundle bundle = new Bundle();
		bundle.putBoolean("isHasTabBtn", true);//是否有标签按钮
		bundle.putInt("tabBtnCount", 5);//标签按钮的个数
		bundle.putSerializable("titleList", sList);//标签按钮的标题
		
		
		FragmentManager fm = getFragmentManager();  
		
		tabFragment.setArguments(bundle);
		
		
		
		FragmentTransaction tx = fm.beginTransaction();  
        tx.add(R.id.content, tabFragment, "main");  
        
        tx.commit();  


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
			//每个页面初始化数据部分
			if(arg1==0){
				
			}else if(arg1==1) {
				
			}else if(arg1 ==2 ){
				
			}else if (arg1 == 3){
				
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
	
		
	@Override
	protected void registerListener() {
		
      
		
	}
	@Override
	public void onResume() {
		super.onResume();
		//获取fragment对象中的控件需要在该方法中获取，如果写在oncreate方法里，会造成fragment还没有初始化完成
		mPager = tabFragment.getmPager();
	      
	    mPager.setAdapter(new MyPagerAdapter(mosGuidePageList));

	    mPager.setCurrentItem(0);

			
		
	}
	

}
