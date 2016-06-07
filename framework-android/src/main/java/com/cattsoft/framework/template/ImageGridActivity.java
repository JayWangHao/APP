package com.cattsoft.framework.template;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.cattsoft.framework.R;
import com.cattsoft.framework.base.BaseActivity;
import com.cattsoft.framework.pub.Constant;
import com.cattsoft.framework.view.TitleBarView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * 网格视图显示Activity
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImageGridActivity extends BaseActivity {

	private String[] imageUrls;					// 图片Url

	private DisplayImageOptions options;		// 显示图片的设置
	private GridView listView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_grid);
		TitleBarView title =  (TitleBarView) findViewById(R.id.title1);
		title.setTitleBar(getString(R.string.title_activity_main), View.GONE,
				View.GONE, View.GONE, false);
//		Bundle bundle = getIntent().getExtras();
		imageUrls = Constant.IMAGES;
         
		initView();
		registerListener();
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	private void startImagePagerActivity(int position) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		intent.putExtra("imageUrls", imageUrls);//可以直接传送list
		intent.putExtra("position", position);
		intent.putExtra("imageDesc", "图片描述");
		startActivity(intent);
	}

	public class ImageAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return imageUrls.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ImageView imageView;
			if (convertView == null) {
				imageView = (ImageView) getLayoutInflater().inflate(R.layout.item_grid_image, parent, false);
			} else {
				imageView = (ImageView) convertView;
			}

			// 将图片显示任务增加到执行池，图片将被显示到ImageView当轮到此ImageView
			imageLoader.displayImage(imageUrls[position], imageView, options);

			return imageView;
		}
	}

	@Override
	protected void initView() {
		
		initImageLoader(getApplicationContext());
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_stub)//设置图片在下载期间显示的图片
		.showImageForEmptyUri(R.drawable.ic_empty)//设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.ic_error) //设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true)//设置下载的图片是否缓存在内存中
		.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
		.bitmapConfig(Bitmap.Config.RGB_565)	 //设置图片的解码类型
		.build();

	listView = (GridView) findViewById(R.id.gridview);
	((GridView) listView).setAdapter(new ImageAdapter());			// 填充数据
	
		
	}

	@Override
	protected void registerListener() {
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startImagePagerActivity(position);
			}
		});
		
	}
}