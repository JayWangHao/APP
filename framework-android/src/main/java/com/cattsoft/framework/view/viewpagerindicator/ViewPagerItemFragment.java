package com.cattsoft.framework.view.viewpagerindicator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cattsoft.framework.R;

/**
 * tab页面的fragment      http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2014/1021/1813.html
 * 主要解决当切换到这个fragment的时候，才去加载数据
 * A simple {@link Fragment} subclass.
 */
public abstract class ViewPagerItemFragment extends Fragment {


    protected boolean isVisible;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }
    protected void onVisible(){
        loadData();
    }
    protected abstract void loadData();
    protected void onInvisible(){}


}
