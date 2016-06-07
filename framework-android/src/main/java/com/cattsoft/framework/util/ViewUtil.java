package com.cattsoft.framework.util;

import android.content.Context;

/**
 * 
 * @author 姜勇男
 * 
 */
public class ViewUtil {

	/**
	 * dip转换px
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * px转换dip
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

    /**
     * px转换sp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {

       final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
       return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * sp转换px
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue){

        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);

    }

}
