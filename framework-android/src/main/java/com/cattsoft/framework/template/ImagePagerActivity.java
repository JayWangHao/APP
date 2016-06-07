package com.cattsoft.framework.template;

/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

import java.util.Arrays;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.R;
import com.cattsoft.framework.base.BaseActivity;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.view.TitleBarView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * ViewPager页面显示图片，用来预览图片
 * @author xueweiwei
 */
public class ImagePagerActivity extends BaseActivity {

	private static final String STATE_POSITION = "STATE_POSITION";

	DisplayImageOptions options;

	ViewPager pager;
	private String imageDesc;
	private List<String> list;
	
	private TitleBarView title;
	private int pagerPosition;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_pager);
		title =  (TitleBarView) findViewById(R.id.title1);
		
		Bundle bundle = getIntent().getExtras();
		String[] imageUrls = bundle.getStringArray("imageUrls");
		// 当前显示View的位置
		pagerPosition = bundle.getInt("position", 0);
		imageDesc = bundle.getString("imageDesc");
		
		list = Arrays.asList(imageUrls);//将数组转换成list
		title.setTitleBar((pagerPosition+1)+"/"+imageUrls.length, View.GONE,
				View.VISIBLE, View.VISIBLE, false);
		
		// 如果之前有保存用户数据
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error)
			.resetViewBeforeLoading(true)
			.cacheOnDisc(true)
			.imageScaleType(ImageScaleType.EXACTLY)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.displayer(new FadeInBitmapDisplayer(300))
			.build();

		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new ImagePagerAdapter(list));
		pager.setCurrentItem(pagerPosition);	// 显示当前位置的View
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// 保存用户数据
		outState.putInt(STATE_POSITION, pager.getCurrentItem());
	}

	private class ImagePagerAdapter extends PagerAdapter {

		private List<String> images;
		private LayoutInflater inflater;

		ImagePagerAdapter(List<String> images) {
			this.images = images;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			pagerPosition = position;
			View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
			
			ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
			TextView imageDesc = (TextView) imageLayout.findViewById(R.id.imageDesc);
			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
            title.setTitleText(position+"/"+images.size());
            
			imageLoader.displayImage(images.get(position), imageView, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					spinner.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					String message = null;
					switch (failReason.getType()) {		// 获取图片失败类型
						case IO_ERROR:				// 文件I/O错误
							message = "Input/Output error";
							break;
						case DECODING_ERROR:		// 解码错误
							message = "Image can't be decoded";
							break;
						case NETWORK_DENIED:		// 网络延迟
							message = "Downloads are denied";
							break;
						case OUT_OF_MEMORY:		    // 内存不足
							message = "Out Of Memory error";
							break;
						case UNKNOWN:				// 原因不明
							message = "Unknown error";
							break;
					}
					Toast.makeText(ImagePagerActivity.this, message, Toast.LENGTH_SHORT).show();

					spinner.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					spinner.setVisibility(View.GONE);		// 不显示圆形进度条
				}
			});

			((ViewPager) view).addView(imageLayout, 0);		// 将图片增加到ViewPager
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View container) {
		}
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void registerListener() {
		
		//删除图片
		title.getTitleRightButton().setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
                JSONObject parameter = new JSONObject();
				Communication communication = new Communication("",parameter,"afterDelete",ImagePagerActivity.this);
				communication.getPostHttpContent();
			}
			
		});
		
	}
	private void afterDelete(){
		
		list.remove(pagerPosition);
		pager.setAdapter(new ImagePagerAdapter(list));
		pager.setCurrentItem(pagerPosition);	// 显示当前位置的View
	}
}