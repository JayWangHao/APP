package com.cattsoft.framework.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.R;
import com.cattsoft.framework.view.ImageWithTips;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

	private LinearLayout linearLayout;
	private List menuList;
	private List<View> funcNavPageList;
	private LinearLayout pageSwitchLayout; //用于界面底部界面切换
	private ViewPager funcNavPager;
	private View view;
	private String menusListJson;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.func_nav_activity, container, false);
		
        getMenuList();
		
		initPageViewList();
		initViewPageSwitcher();
		
		initViewPager();

        return view;
		
	}
	
	/**
	 * 获取功能点列表
	 */
	private void getMenuList() {
		
		menuList = new ArrayList();
		
		Bundle bundle = this.getArguments();
		
		if(bundle!=null) {
			menusListJson = bundle.getString("menus");
		}
		
		com.alibaba.fastjson.JSONArray menuArray=com.alibaba.fastjson.JSONArray.parseArray(menusListJson);
		
		for(int i=0;i<menuArray.size();i++){
			
			JSONObject menuJson = menuArray.getJSONObject(i);
			
			String menuCatalogId = menuJson.getString("menuCatalogId");
			String menuCode = menuJson.getString("menuCode");
			String menuDesc = menuJson.getString("menuDesc");
			String menuId = menuJson.getString("menuId");
			String menuName = menuJson.getString("menuName");
			String menuUrl = menuJson.getString("menuUrl");
			
			ImageWithTips imageView = new ImageWithTips(getActivity());
			imageView.setImageResource(R.drawable.non_color);
			if("system_setting".equals(menuCode)){
				imageView.setImageResource(R.drawable.func_nav_setting);
			}
			
			menuList.add(imageView);
		
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					
					startActivity(new Intent(getActivity(),SetUpActivity.class));
				}
				
			});
		}
		
		if(menuList.size()%2!=0) {//如果功能点为单数，则处理成双数，增加一个完全透明的图片，避免问题的复杂性
			ImageView imv=new ImageView(getActivity());
			imv.setImageResource(R.drawable.non_color);
			menuList.add(imv);
		}
		
	}
	
	//根据功能点组装页面视图，并放入集合中
		private void  initPageViewList() {
			if(funcNavPageList==null) funcNavPageList=new ArrayList();
			//计算需要多少页
			
			double n=Math.ceil((menuList.size()/8.0d));//一页放8个，不足8个也放入一页
			
			for(int i=1;i<=n;i++) {
				//往视图中放图片
				android.view.View v=initView(i,menuList);
				v.setBackgroundColor(Color.parseColor("#FFF0F0F0"));
				funcNavPageList.add(v); 
			}
		}
		
		//初始化每一页的功能图片
		private android.view.View initView(double pageNo,List funcViewList){ 
			List subList=null;
			if(pageNo==Math.ceil((menuList.size()/8.0d))) { //如果是最后一页
				subList=funcViewList.subList(((int)(pageNo-1))*8, funcViewList.size());
			}else {
				subList=funcViewList.subList(((int)(pageNo-1))*8, (int)(pageNo*8));
			}
			//每一页纵向来讲是一个相对布局,每一行也是一个相对布局
			LinearLayout layout=new LinearLayout(getActivity());
			LinearLayout.LayoutParams layoutpare=  
		            new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			layout.setLayoutParams(layoutpare);
			layout.setOrientation(LinearLayout.VERTICAL); 
			
			
			//每行2个，计算有多少行,并把每一行作为一个布局添加到总体布局中
			double rows=Math.ceil(subList.size()/2.0d);
			
			if(subList.size()==1) {
				
			}else {
				
			}
			
			for(int i=1;i<=rows;i++) {
				List rowList=null;
				LinearLayout rl=new LinearLayout(getActivity());
				LinearLayout.LayoutParams lp=  
			            new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
				
				rl.setLayoutParams(lp);
				//rl.setOrientation(LinearLayout.HORIZONTAL); 
				float scale = this.getResources().getDisplayMetrics().density; 
				
				if(i==1) { //如果是第一行
					int hight= (int)(40 * scale + 0.5f);
					lp.setMargins(0, hight, 0, 0);
					if(subList.size()==1) {//避免第一行出现只有一个出现空指针异常
						rowList=subList.subList((i-1)*2, i*2-1); 
					}else {
						rowList=subList.subList((i-1)*2, i*2);//获取每一行
					}
				}else if(i==rows) {//最后一行
					int hight= (int)(20 * scale + 0.5f);
					lp.setMargins(0, hight, 0, 0);
					rowList=subList.subList((i-1)*2, subList.size());//获取每一行
				}else {
					int hight= (int)(20 * scale + 0.5f);
					lp.setMargins(0, hight, 0, 0); 
					rowList=subList.subList((i-1)*2, i*2);//获取每一行
				}
				for(int j=0;j<rowList.size();j++) {
					View iv=(View)rowList.get(j);
					if(j%2!=0) {
						LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
						int hight= (int)(20 * scale + 0.5f); 
						llp.setMargins(hight, 0, 0, 0);
						iv.setLayoutParams(llp);
					} 
					rl.addView(iv);;
				}
				layout.addView(rl);
			}
			
			//为了结局线性布局无法水平居中的问题，故在外层创建一个相对布局，把线性布局放入其中
			RelativeLayout rl=new RelativeLayout(getActivity());
			RelativeLayout.LayoutParams laypare=  
		            new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
			rl.setLayoutParams(laypare);
			rl.setGravity(Gravity.CENTER_HORIZONTAL);
			
			rl.addView(layout);
			 
			return rl;
		}
		
		//初始化界面底部switchBar
		private void initViewPageSwitcher() {
			
			//只有页数超过一页的时候出现switchbar才有意义
			if(funcNavPageList!=null && funcNavPageList.size()>1) {
				pageSwitchLayout=(LinearLayout)view.findViewById(R.id.func_nav_view_page_switcher);
				
				int n=funcNavPageList.size();
				for(int i=1;i<=n;i++) {//有几页就出现几个图片点
					
					ImageView iv=new ImageView(getActivity());
					LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
					iv.setLayoutParams(llp);
					
					if(i==1) {//第一页默认为选中页，图片为红点
						iv.setImageResource(R.drawable.func_nav_switch_bar_red);
					}else {
						iv.setImageResource(R.drawable.func_nav_switch_bar_gray);
						LinearLayout.LayoutParams llps = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
						llps.setMargins(5, 0, 0, 0);
						iv.setLayoutParams(llps);
					}
					pageSwitchLayout.addView(iv);
				}
			}
			
			
		}
		
		private void initViewPager() {
			funcNavPager = (ViewPager) view.findViewById(R.id.func_nav_view_pager);

			LayoutInflater mInflater = getActivity().getLayoutInflater();
	 
			
			funcNavPager.setAdapter(new MyPagerAdapter(funcNavPageList));
			
			funcNavPager.setCurrentItem(0);
			
			funcNavPager.setOnPageChangeListener(new MyOnPageChangeListener());
		}
		
		// 用于页面切换时可能触发的事件
		public class MyOnPageChangeListener implements OnPageChangeListener {
			@Override
			public void onPageSelected(int arg0) {
				int childCount=pageSwitchLayout.getChildCount();
				for(int i=0;i<childCount;i++) {
					ImageView iv=(ImageView)pageSwitchLayout.getChildAt(i);
					if(i==arg0) {//把当前页的图片改成红点，其他全部是黑点
						iv.setImageResource(R.drawable.func_nav_switch_bar_red);
					}else {
						iv.setImageResource(R.drawable.func_nav_switch_bar_gray);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
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
}
