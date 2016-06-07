package com.cattsoft.framework.view;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cattsoft.framework.R;
import com.cattsoft.framework.util.DeviceUtil;
import com.cattsoft.framework.util.SerializableList;

import java.util.List;
/**
 * viewpager+button控件
 * @author wei
 *
 */
public class TabViewFragment extends Fragment{

	private ViewPager mPager;// 页卡控件
	private LinearLayout tabBtnLayout;// 添加标签按钮的父布局
	private ImageView navigationImg;// 导航条
	private ImageView leftMoreImg;// 左侧显示图标
	private ImageView rightMoreImg;// 右侧显示图标
	private int tabBtnCount = 4;// 标签页数，默认四个页签
//	private PagerAdapter viewPagerAdapter;// 滑动页面的适配器
//	private OnPageChangeListener viewOnPageChangeListener;
	private int tabBtnWith;
	private boolean isHasTabBtn = true;// 是否有标签按钮，默认有
	private List titleList;//标签按钮标题
	private int currentIndex=0;//当前页签
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.tab_view_main_view, container, false);
		
		tabBtnLayout = (LinearLayout) view
				.findViewById(R.id.detail_title_layout);
		mPager = (ViewPager) view.findViewById(R.id.vPager);
		navigationImg = (ImageView) view.findViewById(R.id.cursor);
		leftMoreImg = (ImageView) view.findViewById(R.id.left_more);
		rightMoreImg = (ImageView) view.findViewById(R.id.right_more);
		
		Bundle bundle = this.getArguments();
		if(bundle!=null) {
			tabBtnCount = bundle.getInt("tabBtnCount")==0?4:bundle.getInt("tabBtnCount");
			isHasTabBtn = bundle.getBoolean("isHasTabBtn");
			
			SerializableList sList = (SerializableList) bundle.getSerializable("titleList");
			titleList = sList.getList();
			
		}
		
		if (isHasTabBtn) {

			// 初始化标签按钮部分
			if (tabBtnCount <= 4) {
				tabBtnWith = DeviceUtil.getPhoneScreenWidth()
						/ tabBtnCount;
				leftMoreImg.setVisibility(View.GONE);
				rightMoreImg.setVisibility(View.GONE);

			} else {
				tabBtnWith = DeviceUtil.getPhoneScreenWidth() / 4;
				leftMoreImg.setVisibility(View.GONE);
				rightMoreImg.setVisibility(View.VISIBLE);

			}
			// 初始化页卡标题下面的红色条
			Bitmap bmp = Bitmap.createBitmap(tabBtnWith, 5,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bmp);
			canvas.drawColor(0xFFBC1E28);
			navigationImg.setImageBitmap(bmp);
			navigationImg.setVisibility(View.VISIBLE);

			for (int i = 0; i < tabBtnCount; i++) {

				 LinearLayout.LayoutParams btn_params = new LinearLayout.LayoutParams(
						 LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

				 btn_params.width =  tabBtnWith;  
				
				Button button = new Button(this.getActivity());
				button.setLayoutParams(btn_params);
				button.setBackgroundResource(R.drawable.detail_title_cell);
				button.setOnClickListener(new MyOnClickListener(i));
				button.setText(titleList.get(i).toString());
				if(i==0) {
					button.setTextColor(Color.parseColor("#ca202b"));
				}else {
					button.setTextColor(Color.parseColor("#000000"));
				}
				
				button.setTextSize(14);

				button.setId(i);
				tabBtnLayout.addView(button);
			}
		} else {

			tabBtnLayout.setVisibility(View.GONE);
		}
		
		
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 内容页之间的切换
		mPager.setCurrentItem(currentIndex);
		
		return view;
		
	}
	
	public ViewPager getmPager() {
		return mPager;
	}

	public void setmPager(ViewPager mPager) {
		this.mPager = mPager;
	}

	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};
	// 用于页面切换时可能触发的事件
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			
			for(int i=0;i<tabBtnCount;i++) {
				
				
				if(arg0==i) {
					
					if(tabBtnCount>4) {
						if(i==0) {
							leftMoreImg.setVisibility(View.GONE);
							rightMoreImg.setVisibility(View.VISIBLE);
						}
						if(i==tabBtnCount-1) {
							rightMoreImg.setVisibility(View.GONE);
							leftMoreImg.setVisibility(View.VISIBLE);
						}
						if(i==4&&currentIndex==3) {//从第四页面到第五页面
							leftMoreImg.setVisibility(View.VISIBLE);
						}
						if(i==currentIndex-1&&currentIndex==tabBtnCount-3-1) {
							rightMoreImg.setVisibility(View.VISIBLE);
						}
					}
					
					
					if(currentIndex>arg0) {//向右滑
						
						if(tabBtnCount-currentIndex>=4&&tabBtnLayout.getChildAt(0).getLeft()<0) {
							
							navigationImg.setPadding(0,
									navigationImg.getPaddingTop(), navigationImg.getPaddingRight(),
									navigationImg.getPaddingBottom());
							LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) tabBtnLayout.getChildAt(0).getLayoutParams();
							params1.leftMargin = tabBtnLayout.getChildAt(0).getLeft()+tabBtnWith;
							tabBtnLayout.getChildAt(0).setLayoutParams(params1);
							
						}else if(tabBtnCount-currentIndex<4){
							navigationImg.setPadding(navigationImg.getPaddingLeft() - (currentIndex-arg0)*tabBtnWith,
									navigationImg.getPaddingTop(), navigationImg.getPaddingRight(),
									navigationImg.getPaddingBottom());
							
						}else {
							
							
							navigationImg.setPadding(0,
									navigationImg.getPaddingTop(), navigationImg.getPaddingRight(),
									navigationImg.getPaddingBottom());
						}
						
						
					}else if(currentIndex<arg0) {//向左滑
						
						if(currentIndex+1>=4&&tabBtnLayout.getChildAt(0).getLeft()==0) {
							navigationImg.setPadding(tabBtnWith*3,
									navigationImg.getPaddingTop(), navigationImg.getPaddingRight(),
									navigationImg.getPaddingBottom());
							LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) tabBtnLayout.getChildAt(0).getLayoutParams();
							params1.leftMargin = tabBtnLayout.getChildAt(0).getLeft()-tabBtnWith;
							tabBtnLayout.getChildAt(0).setLayoutParams(params1);
							
							
						}else if(currentIndex+1<4){
							
							navigationImg.setPadding(navigationImg.getPaddingLeft() - (currentIndex-arg0)*tabBtnWith,
									navigationImg.getPaddingTop(), navigationImg.getPaddingRight(),
									navigationImg.getPaddingBottom());
							
						}else {
							navigationImg.setPadding(tabBtnWith*3,
									navigationImg.getPaddingTop(), navigationImg.getPaddingRight(),
									navigationImg.getPaddingBottom());
							
						}
					}
					
					
					Button currentBtn = (Button) tabBtnLayout.getChildAt(currentIndex);
					Button nextBtn = (Button) tabBtnLayout.getChildAt(arg0);
					nextBtn.setTextColor(0xffca202b);
					currentBtn.setTextColor(Color.BLACK);
					
					currentIndex = arg0;
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
}
