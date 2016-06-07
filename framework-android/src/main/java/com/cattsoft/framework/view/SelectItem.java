package com.cattsoft.framework.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cattsoft.framework.R;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.framework.util.ViewUtil;

/**
 * ImageLeft,Label,Value,ImageRight(>)
 */
public class SelectItem extends LinearLayout {

	// SelectItem的 自定义属性
	private Drawable leftImage,rightImage,backgroundImg;
	private String label,value;
	
	private View view;
	private RelativeLayout background;
	private ImageView drawableLeft;// 图片大小最好是 30 × 30
	private TextView tv_label;
	private TextView tv_value;
	private ImageView drawableRight;
	private TextView hint;
	RelativeLayout relativeLayout;

    private float leftTextSize;
    private float rightTextSize;
    private int leftTextColor;
    private int rightTextColor;
    private int leftPaddingTop;
    private int leftPaddingBottom;
    private int rightPaddingTop;
    private int rightPaddingBottom;

	public SelectItem(Context context) {
		this(context, null);
	}

	public SelectItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		int labelResouceId = -1;
		int valueResouceId=-1;
		
		// 以下三种获取 inflater 的方式本质是一样的。
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		LayoutInflater inflater2 = LayoutInflater.from(context);
//		LayoutInflater inflater3 = getLayoutInflater();// 这个是在Activity里获取的。
		view = inflater.inflate(R.layout.select_item, null);
		background = (RelativeLayout) view.findViewById(R.id.background);
		drawableLeft = (ImageView) view.findViewById(R.id.icon);
		tv_label = (TextView) view.findViewById(R.id.label);
		tv_value = (TextView) view.findViewById(R.id.value);
		drawableRight = (ImageView) view.findViewById(R.id.arrow);
		hint = (TextView) view.findViewById(R.id.hint);

//		labelResouceId = attrs.getAttributeResourceValue(null, "label", 0);
//		if (labelResouceId > 0) {
//            label = context.getResources().getText(labelResouceId).toString();
//        } else {
//            label = "";
//        }
//
//		valueResouceId = attrs.getAttributeResourceValue(null, "value", 0);
//		if (valueResouceId > 0) {
//            value = context.getResources().getText(valueResouceId).toString();
//        } else {
//            value = "";
//        }
		

		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.select_item);
		leftImage = a.getDrawable(R.styleable.select_item_drawable_left);
		rightImage = a.getDrawable(R.styleable.select_item_drawable_right);
        backgroundImg = a.getDrawable(R.styleable.select_item_bgImg);

        leftTextSize = a.getDimension(R.styleable.select_item_left_label_text_size,ViewUtil.sp2px(getContext(),12));
        rightTextSize = a.getDimension(R.styleable.select_item_right_label_text_size,ViewUtil.sp2px(getContext(),12));
        leftTextColor = a.getColor(R.styleable.select_item_left_label_text_color,R.color.black);
        rightTextColor = a.getColor(R.styleable.select_item_right_label_text_color,R.color.black);

        leftPaddingTop = (int)a.getDimension(R.styleable.select_item_left_label_padding_top,ViewUtil.dip2px(getContext(),11));
        leftPaddingBottom = (int)a.getDimension(R.styleable.select_item_left_label_padding_bottom,ViewUtil.dip2px(getContext(),11));
        rightPaddingTop = (int)a.getDimension(R.styleable.select_item_right_label_padding_top,ViewUtil.dip2px(getContext(),11));
        rightPaddingBottom = (int)a.getDimension(R.styleable.select_item_right_label_padding_bottom,ViewUtil.dip2px(getContext(),11));
        label = a.getString(R.styleable.select_item_select_label);
        value = a.getString(R.styleable.select_item_select_value);
		a.recycle();

        tv_label.setText(label);
        tv_value.setText(value);


        setViewStyle();


		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(ViewUtil.dip2px(getContext(),10),0,ViewUtil.dip2px(getContext(),10),0);
        view.setLayoutParams(lp);// 不设置这个，R.layout.select_item 的 layout_width,layout_height会失效

        view.setBackgroundDrawable(backgroundImg);
        addView(view);
	}

    /**
     * 设置默认新样式，动态生成控件时使用
     */
    public void setDefaultStyle(){


        leftTextSize = ViewUtil.sp2px(getContext(),12);
        leftTextColor = getResources().getColor(R.color.label_color);

        rightTextSize = ViewUtil.sp2px(getContext(),12);
        rightTextColor = getResources().getColor(R.color.value_color);

        rightPaddingTop = ViewUtil.dip2px(getContext(),22) ;
        rightPaddingBottom = ViewUtil.dip2px(getContext(),22) ;

        leftPaddingTop = ViewUtil.dip2px(getContext(),22) ;
        leftPaddingBottom = ViewUtil.dip2px(getContext(),22) ;

        drawableLeft.setVisibility(View.GONE);

        backgroundImg = getResources().getDrawable(R.drawable.edit_label_text_bg);

        rightImage = getResources().getDrawable(R.drawable.select_item_detail);

        view.setBackgroundDrawable(backgroundImg);

        setViewStyle();

    }

    /**
     * 设置组合控件的样式
     */
    private void setViewStyle(){


        tv_label.setPadding(ViewUtil.dip2px(getContext(),5),leftPaddingTop,0,leftPaddingBottom);
        tv_value.setPadding(0,rightPaddingTop,ViewUtil.dip2px(getContext(),5),rightPaddingBottom);

        tv_label.setTextColor(leftTextColor);
        tv_label.setTextSize(ViewUtil.px2sp(getContext(),leftTextSize));

        tv_value.setTextColor(rightTextColor);
        tv_value.setTextSize(ViewUtil.px2sp(getContext(),rightTextSize));


        drawableLeft.setImageDrawable(leftImage);
        drawableRight.setImageDrawable(rightImage);

    }

    public ImageView getDrawableLeft() {
        return drawableLeft;
    }

    public void setDrawableLeft(ImageView drawableLeft) {
        this.drawableLeft = drawableLeft;
    }

    public ImageView getDrawableRight() {
        return drawableRight;
    }

    public void setDrawableRight(ImageView drawableRight) {
        this.drawableRight = drawableRight;
    }

    /**
	 * 设置item的背景色
	 * 
	 * @param color
	 */
	public void setBackGroundColor(int color) {
		background.setBackgroundColor(color);
	}

	/**
	 * 设置左侧图标
	 * 
	 * @param drawableLeftId
	 */
	public void setDrawableLeft(int drawableLeftId) {
		drawableLeft.setImageResource(drawableLeftId);
	}

	/**
	 * 设置右侧图标
	 * 
	 * @param drawableRightId
	 */
	public void setDrawableRight(int drawableRightId) {
		drawableRight.setImageResource(drawableRightId);
	}

	/**
	 * 设置label值
	 * 
	 * @param sLabel
	 */
	public void setLabel(String sLabel) {
		tv_label.setText(sLabel);
	}

	/**
	 * 设置label值
	 * 
	 * @param resId
	 */
	public void setLabel(int resId) {
		tv_label.setText(resId);
	}

	/**
	 * 设置label字体颜色
	 * 
	 * @param color
	 */
	public void setLabelTextColor(int color) {
		tv_label.setTextColor(color);
	}

	/**
	 * 设置label字体大小
	 * 
	 * @param size
	 */
	public void setLabelTextSize(int size) {

        tv_label.setTextSize(size);
	}

	/**
	 * 设置value值
	 * 
	 * @param sValue
	 */
	public void setValue(String sValue) {
		tv_value.setText(sValue);
	}

	/**
	 * 设置value值
	 * 
	 * @param resId
	 */
	public void setValue(int resId) {
		tv_value.setText(resId);
	}

	/**
	 * 设置value字体颜色
	 * 
	 * @param color
	 */
	public void setValueTextColor(int color) {
		tv_value.setTextColor(color);
	}

	/**
	 * 设置value字体大小
	 * 
	 * @param size
	 */
	public void setValueTextSize(int size) {

        tv_value.setTextSize(size);
	}

	/**
	 * 设置hint值
	 * 
	 * @param sHint
	 */
	public void setHint(String sHint) {
		if (StringUtil.isBlank(sHint) || "".equals(sHint)) {
			hint.setVisibility(View.GONE);
		} else {
			hint.setVisibility(View.VISIBLE);
		}
		hint.getText();
		hint.setText(sHint);
	}

	/**
	 * 设置hint值
	 * 
	 * @param resId
	 */
	public void setHint(int resId) {
		hint.setVisibility(View.VISIBLE);
		hint.setText(resId);
	}

	/**
	 * 设置hint字体颜色
	 * 
	 * @param color
	 */
	public void setHintTextColor(int color) {
		hint.setTextColor(color);
	}

	/**
	 * 设置hint字体大小
	 * 
	 * @param size
	 */
	public void setHintTextSize(int size) {

        hint.setTextSize(size);
	}

	/**
	 * 设置label的padding


     * * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void setLabelPaddings(int left, int top, int right, int bottom){
		tv_label.setPadding(ViewUtil.dip2px(getContext(),left), ViewUtil.dip2px(getContext(),top), ViewUtil.dip2px(getContext(),right), ViewUtil.dip2px(getContext(),bottom));
	}
	
	/**
	 * 设置value的padding

	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void setValuePaddings(int left, int top, int right, int bottom){
		tv_value.setPadding(ViewUtil.dip2px(getContext(),left), ViewUtil.dip2px(getContext(),top), ViewUtil.dip2px(getContext(),right), ViewUtil.dip2px(getContext(),bottom));;
	}
	
	/**
	 * 设置边距
	 * 
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void setMargin(int left, int top, int right, int bottom) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(ViewUtil.dip2px(getContext(),left), ViewUtil.dip2px(getContext(),top), ViewUtil.dip2px(getContext(),right), ViewUtil.dip2px(getContext(),bottom));
		view.setLayoutParams(lp);
	}

    public String getValue() {
        return tv_value.getText().toString();
    }

    public String getLabel() {
        return tv_label.getText().toString();
    }
}
