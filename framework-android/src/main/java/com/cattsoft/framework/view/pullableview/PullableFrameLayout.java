package com.cattsoft.framework.view.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by admin_local on 15/9/25.
 */
public class PullableFrameLayout extends FrameLayout implements Pullable{
    public PullableFrameLayout(Context context)
    {
        super(context);
    }

    public PullableFrameLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public PullableFrameLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown()
    {

            return true;
    }

    @Override
    public boolean canPullUp()
    {

        return true;
    }



}
