package com.cattsoft.framework.template;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.cattsoft.framework.R;
import com.cattsoft.framework.view.TitleBarView;
import com.cattsoft.framework.view.viewpagerindicator.TabPageIndicator;

/**
 * 测试tab页面
 */

public class ViewPagerActivity extends FragmentActivity {
    /**
     * Tab标题
     */
    private static final String[] TITLE = new String[] { "基本信息", "详细信息", "资源信息", "女人",
            "财经", "数码", "情感", "科技" };

    TabPageIndicator indicator;

    Drawable drawable;

    private String titleStr = "    ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_activity);
        // 导航栏初始化
        TitleBarView title = (TitleBarView) findViewById(R.id.title1);
        title.setTitleBar(getString(R.string.app_name), View.VISIBLE,
                View.GONE, View.GONE, false);

        initTabTitleThread();


        //ViewPager的adapter
        FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

        //实例化TabPageIndicator然后设置ViewPager与之关联
        indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);

        //如果我们要对ViewPager设置监听，用indicator设置就行了
        indicator.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

                titleStr = arg0+"nihao";
                Toast.makeText(getApplicationContext(), titleStr, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });


    }

    private void initTabTitleThread(){


    }


    /**
     * ViewPager适配器
     * @author len
     *
     */
    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //新建一个Fragment来展示ViewPager item的内容，并传递参数
            Fragment fragment = new ItemFragment();
            Bundle args = new Bundle();
            args.putString("arg", titleStr);
            fragment.setArguments(args);


            return fragment;
        }

 //       @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//
//            //新建一个Fragment来展示ViewPager item的内容，并传递参数
////            Fragment fragment = new ItemFragment();
////            Bundle args = new Bundle();
////            args.putString("arg", titleStr);
////            fragment.setArguments(args);
////
////
////            return fragment;
//        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLE[position % TITLE.length];
        }

        @Override
        public int getCount() {
            return TITLE.length;
        }
    }

}
