package com.cattsoft.framework.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cattsoft.framework.R;
import com.cattsoft.framework.util.ViewUtil;

public class LabelText extends LinearLayout {

	private String label=""; //标签
	private String value="";//属性值

    private boolean isShowColon;//是否显示冒号，默认显示
    private boolean valueRightGravity;//值左对齐还是右对齐，默认左对齐
    private float valueTextSize;
    private float labelTextSize;
    private int valueTextColor;
    private int labelTextColor;
    private int labelPaddingTop;
    private int labelPaddingBottom;
    private int valuePaddingTop;
    private int valuePaddingBottom;
	
	private View view;
	private TableRow row;
	private TextView tv_label;
	private TextView tv_value;
	private TextView colon;
	private Drawable backgroundImg;

    private int viewHeight;
	
	public LabelText(Context context) {
		this(context,null);
	}

	public LabelText(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		LayoutInflater inflater2 = LayoutInflater.from(context);
//		LayoutInflater inflater3 = getLayoutInflator();// 这个是在Activity里获取的，以上三种获取 inflater 的方式本质是一样的。
		view = inflater.inflate(R.layout.label_text, null);
		row = (TableRow) view.findViewById(R.id.row);
		tv_label = (TextView) view.findViewById(R.id.label);
		tv_value = (TextView) view.findViewById(R.id.value);
		colon = (TextView) view.findViewById(R.id.colon);
		
		
		//该注释部分只适合该自定义控件写在xml中，AttributeSet用来在xml中找自定义控件的自定义属性
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
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.edit_labeltext);
		backgroundImg = a.getDrawable(R.styleable.edit_labeltext_backgroundImg);
		label = a.getString(R.styleable.edit_labeltext_label);
		value = a.getString(R.styleable.edit_labeltext_value);
        isShowColon = a.getBoolean(R.styleable.edit_labeltext_is_show_colon,true);
        valueRightGravity = a.getBoolean(R.styleable.edit_labeltext_value_right_gravity,false);
        valueTextSize = a.getDimension(R.styleable.edit_labeltext_value_text_size, ViewUtil.sp2px(getContext(),12));
        labelTextSize = a.getDimension(R.styleable.edit_labeltext_label_text_size,ViewUtil.sp2px(getContext(),12));
        valueTextColor = a.getColor(R.styleable.edit_labeltext_value_text_color,R.color.black);
        labelTextColor = a.getColor(R.styleable.edit_labeltext_label_text_color,R.color.black);

        labelPaddingTop = (int)a.getDimension(R.styleable.edit_labeltext_label_padding_top,ViewUtil.dip2px(getContext(),11));
        labelPaddingBottom = (int)a.getDimension(R.styleable.edit_labeltext_label_padding_bottom,ViewUtil.dip2px(getContext(),11));
        valuePaddingTop = (int)a.getDimension(R.styleable.edit_labeltext_value_padding_top,ViewUtil.dip2px(getContext(),11));
        valuePaddingBottom = (int)a.getDimension(R.styleable.edit_labeltext_value_padding_bottom,ViewUtil.dip2px(getContext(),11));

        viewHeight = (int) a.getDimension(R.styleable.edit_labeltext_view_height,0);
		
		a.recycle();
		
		tv_label.setText(label);
		tv_value.setText(value);



        setViewStyle();

        if(viewHeight==0){
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(ViewUtil.dip2px(getContext(),10),0,ViewUtil.dip2px(getContext(),10),0);
            view.setLayoutParams(lp);// 不设置这个，R.layout.search_edittext的 layout_width,layout_height会失效
        }else{

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,viewHeight);
            lp.setMargins(ViewUtil.dip2px(getContext(),10),0,ViewUtil.dip2px(getContext(),10),0);
            view.setLayoutParams(lp);// 不设置这个，R.layout.search_edittext的 layout_width,layout_height会失效
        }
		view.setBackgroundDrawable(backgroundImg);
		addView(view);
	}

    /**
     * 设置省略号的位置
     * @param truncateAt
     */
    public void setEllipsize(String truncateAt){

        tv_value.setSingleLine(true);
        tv_value.setMaxEms(13);

        if(("START").equalsIgnoreCase(truncateAt)){

            tv_value.setEllipsize(TextUtils.TruncateAt.valueOf("START"));

        }else  if(("MIDDLE").equalsIgnoreCase(truncateAt)){

            tv_value.setEllipsize(TextUtils.TruncateAt.valueOf("MIDDLE"));

        }else  if(("END").equalsIgnoreCase(truncateAt)){

            tv_value.setEllipsize(TextUtils.TruncateAt.valueOf("END"));

        }else  if(("MARQUEE").equalsIgnoreCase(truncateAt)){

            tv_value.setEllipsize(TextUtils.TruncateAt.valueOf("MARQUEE"));

            tv_value.setSelected(true);
        }
    }

    /**
     * 设置默认新样式，动态生成控件时使用
     */
    public void setDefaultStyle(){


        labelTextSize = ViewUtil.sp2px(getContext(),12);
        labelTextColor = getResources().getColor(R.color.label_color);

        valueTextSize = ViewUtil.sp2px(getContext(),12);
        valueTextColor = getResources().getColor(R.color.value_color);

        labelPaddingTop = ViewUtil.dip2px(getContext(),22);
        labelPaddingBottom = ViewUtil.dip2px(getContext(),22);

        valuePaddingTop = ViewUtil.dip2px(getContext(),22);
        valuePaddingBottom = ViewUtil.dip2px(getContext(),22);

        isShowColon = false;

        valueRightGravity = true;

        backgroundImg = getResources().getDrawable(R.drawable.edit_label_text_bg);

        view.setBackgroundDrawable(backgroundImg);

        setViewStyle();


    }

    /**
     * 设置组合控件的样式
     */
    private void setViewStyle(){


        tv_label.setPadding(ViewUtil.dip2px(getContext(),5),labelPaddingTop,0,labelPaddingBottom);
        tv_value.setPadding(0,valuePaddingTop,ViewUtil.dip2px(getContext(),5),valuePaddingBottom);

        tv_label.setTextColor(labelTextColor);
        tv_label.setTextSize(ViewUtil.px2sp(getContext(),labelTextSize));

        tv_value.setTextColor(valueTextColor);
        tv_value.setTextSize(ViewUtil.px2sp(getContext(),valueTextSize));

        if(isShowColon){

            colon.setVisibility(View.VISIBLE);
        }else{
            colon.setVisibility(View.GONE);
        }
        if(valueRightGravity){
            tv_value.setGravity(Gravity.RIGHT);
        }else{
            tv_value.setGravity(Gravity.LEFT);
        }

    }

	/**
	 * 设置背景图片
	 * @param resource
	 */
	public void setBackground(int resource){
		row.setBackgroundColor(resource);
	}
	
	/**
	 * 
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @see  ： px
	 */
	public void setMargin(int left,int top,int right,int bottom){
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(ViewUtil.dip2px(getContext(),left),ViewUtil.dip2px(getContext(),top),ViewUtil.dip2px(getContext(),right),ViewUtil.dip2px(getContext(),bottom));
		view.setLayoutParams(lp);
	}
	
	/**
	 * 设置标签名
	 * @param alabel 
	 */
	public void setLabel(String alabel) {
		tv_label.setText(alabel);
	}
	
	/**
	 * 设置标签值颜色
	 * @param color
	 */
	public void setLabelTextColor(int color){
		tv_label.setTextColor(color);
	}
	
	/**
	 * 设置标签值字体大小
	 * @param size
	 */
	public void setLabelTextSize(int size){
		tv_label.setTextSize(size);
	}
	
	
	/**
	 * 设置标签属性值
	 * @param aValue
	 */
	public void setValue(String aValue) {
		tv_value.setText(aValue);
	}
	
	/**
	 * 设置标签属性值的颜色
	 * @param color
	 */
	public void setValueTextColor(int color){
		tv_value.setTextColor(color);
	}
	
	/**
	 * 设置标签属性值的字体大小
	 * @param size
	 */
	public void setValueTextSize(int size){
		tv_value.setTextSize(size);
	}
	
	/**
	 * 设置标签属性值的提示信息
	 * @param
	 */
	public void setValueTextHint(int hintId){
		tv_value.setHint(hintId);
	}
	
	/**
	 * 设置标签属性值的提示信息
	 * @param
	 */
	public void setValueTextHint(String hintText){
		tv_value.setHint(hintText);
	}
	
	/**
	 * 设置标签属性值的输入类型（InputType）
	 * @param
	 */
	public void setValueInputType(int inputType){
		tv_value.setInputType(inputType);

	}
	
	/**
	 * 设置冒号的颜色
	 * @param color
	 */
	public void setColonColor(int color){
		colon.setTextColor(color);
	}
	
	/**
	 * 设置冒号的大小
	 * @param size
	 */
	public void setColonSize(int size){
		colon.setTextSize(size);
	}


    /**
     * 设置值的背景颜色
     * @param color
     */
    public void setValueBackgroundColor(int color){
        tv_value.setBackgroundColor(color);
    }

    /**
     * 设置值的边距
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setValueMargins(int left,int top,int right,int bottom){

        TableRow.LayoutParams  lp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp.setMargins(left, top, right, bottom);

        tv_value.setLayoutParams(lp);

    }

    /**
     * 设置标签的边距
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setLabelMargins(int left,int top,int right,int bottom){

        TableRow.LayoutParams  lp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp.setMargins(left, top, right, bottom);

        tv_label.setLayoutParams(lp);
    }

    /**
     * 设置值的比重
     * @param ValueWeight
     */
    public void setValueWeight(float ValueWeight){

        TableRow.LayoutParams  lp = new TableRow.LayoutParams(0,LayoutParams.WRAP_CONTENT);

        lp.weight = ValueWeight;

        tv_value.setLayoutParams(lp);
    }

    /**
     * 设置冒号的比重
     * @param colonWeight
     */
    public void setColonWeight(float colonWeight){

        TableRow.LayoutParams  lp = new TableRow.LayoutParams(0,LayoutParams.WRAP_CONTENT);

        lp.weight = colonWeight;

        colon.setLayoutParams(lp);
    }

    /**
     * 设置标签的比重
     * @param labelWeight
     */
    public void setLabelWeight(float labelWeight){

        TableRow.LayoutParams  lp = new TableRow.LayoutParams(0,LayoutParams.WRAP_CONTENT);

        lp.weight = labelWeight;

        tv_label.setLayoutParams(lp);
    }

    /**
     * 设置标签、冒号和值的比重
     * @param labelWeight
     * @param valueWeight
     * @param colonWeight
     */
    public void setLabelValueAndColonWeight(float labelWeight,float valueWeight,float colonWeight){


        setLabelValueWeight(labelWeight,valueWeight);

        setColonWeight(colonWeight);

    }

    /**
     * 设置标签和值的比重
     * @param labelWeight
     * @param valueWeight
     */
    public void setLabelValueWeight(float labelWeight,float valueWeight){

        setLabelWeight(labelWeight);

        setValueWeight(valueWeight);
    }

    /**
     * 设置标签的相对位置
     * @param gravity
     */
    public void setLabelGravity(int gravity){
        tv_label.setGravity(gravity);
        colon.setGravity(gravity);
    }

    /**
     * 设置值的相对位置
     * @param gravity
     */
    public void setValueGravity(int gravity){
        tv_value.setGravity(gravity);
    }

    /**
     * 获取值
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置值的选择复制功能
     * @param enable
     */
    public void setTextIsSelectable(boolean enable){
        tv_value.setTextIsSelectable(enable);
    }


    /**
     * 设置tv_value可以换行
     * @param enable
     */
    public void setLableSingleLine(boolean enable) {

        tv_value.setSingleLine(enable);
    }

}
