package com.cattsoft.framework.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cattsoft.framework.R;
import com.cattsoft.framework.util.ViewUtil;

/**
 * Created by admin_local on 15/5/21.
 * 菜单按钮的图标＋底部文字
 */
public class MenuImg extends RelativeLayout{

    private ImageView menuImgView;
    private TextView menuTextView;
    private TextView menuCodeText;

    private Drawable menuDraw;
    private int menuTextColor;
    private float menuTextSize;

    private float menuTextSizeDef;

    public MenuImg(Context context) {

        this(context,null);

    }

    public MenuImg(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(context).inflate(R.layout.menu_img, null);

        menuImgView = (ImageView) view.findViewById(R.id.menu_imgview);
        menuTextView = (TextView) view.findViewById(R.id.menu_text);
        menuCodeText = (TextView) view.findViewById(R.id.menu_code);

        menuTextSizeDef = ViewUtil.sp2px(context,10);

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.MenuImg);

        menuDraw = a.getDrawable(R.styleable.MenuImg_menuDrawable);
        menuTextSize = a.getDimension(R.styleable.MenuImg_menuTextSize,menuTextSizeDef);
        menuTextColor = a.getColor(R.styleable.MenuImg_menuDrawable,R.color.menu_text_color);

        a.recycle();

        if(menuDraw!=null){

            setMenuDrawValue(menuDraw);
        }

       // setMenuTextColor(menuTextColor);

     //   setMenuTextSize(ViewUtil.px2sp(context,menuTextSize));

        addView(view);

    }

    public ImageView getMenuImgView() {
        return menuImgView;
    }

    public void setMenuImgView(ImageView menuImgView) {
        this.menuImgView = menuImgView;
    }

    public TextView getMenuTextView() {
        return menuTextView;
    }

    public void setMenuTextView(TextView menuTextView) {
        this.menuTextView = menuTextView;
    }

    public TextView getMenuCodeText() {
        return menuCodeText;
    }

    public void setMenuCodeText(TextView menuCodeText) {
        this.menuCodeText = menuCodeText;
    }

    /**
     * 设置图片背景
     * @param img
     */
    public void setMenuImagValue(int img){

        this.menuImgView.setBackgroundResource(img);
    }

    /**
     * 设置图片背景
     * @param drawValue
     */
    public void setMenuDrawValue(Drawable drawValue){

        this.menuImgView.setBackground(drawValue);
    }

    /**
     * 设置文字颜色
     * @param color
     */
    public void setMenuTextColor(int color){

        this.menuTextView.setTextColor(color);
    }

    /**
     * 设置文字大小
     * @param size
     */
    public void setMenuTextSize(float size){

        this.menuTextView.setTextSize(size);
    }

    /**
     * 设置文本文字
     * @param value
     */
    public void setMenuTextValue(String value){

        this.menuTextView.setText(value);
    }

    public void setMenuCodeTextValue(String value) {
        this.menuCodeText.setText(value);
    }


}
