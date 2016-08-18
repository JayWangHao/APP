package com.cattsoft.framework.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cattsoft.framework.R;


public class ToastCommom {

    private static ToastCommom toastCommom;

    private Toast toast;

    public ToastCommom(){
    }

    public static ToastCommom createToastConfig(){
        if (toastCommom==null) {
            toastCommom = new ToastCommom();
        }
        return toastCommom;
    }

    /**
     * 显示Toast
     * @param context
     * @param root
     * @param tvString
     */

    public void ToastShow(Context context,ViewGroup root,String tvString){
        View layout = LayoutInflater.from(context).inflate(R.layout.toast,root);
        TextView text = (TextView) layout.findViewById(R.id.text);
        ImageView mImageView = (ImageView) layout.findViewById(R.id.iv);
//        mImageView.setBackgroundResource();
        text.setText(tvString);
        text.setTextColor(Color.WHITE);
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

}
