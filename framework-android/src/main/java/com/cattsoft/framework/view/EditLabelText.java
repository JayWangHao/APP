package com.cattsoft.framework.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cattsoft.framework.R;
import com.cattsoft.framework.util.ViewUtil;

public class EditLabelText extends LinearLayout {

	private String label=""; //标签
	private String value="";//属性值
	private String hint = "";// 提示信息
	private Drawable backgroundImg;

    private boolean isShowColon;//是否显示冒号，默认显示
    private boolean valueRightGravity;//值左对齐还是右对齐，默认左对齐
    private boolean isShowEdit;//是否显示编辑图片按钮，默认不现实
    private float valueTextSize;
    private float labelTextSize;
    private int valueTextColor;
    private int labelTextColor;
    private int labelPaddingTop;
    private int labelPaddingBottom;
    private int valuePaddingTop;
    private int valuePaddingBottom;
    private int viewHeight;


    //设置标签和属性值的比重
    private float labelWeight;
    private float valueWeight;


	private View view;
	private TableRow row;
	private TextView tv_label;
	private EditText et_value;
	private TextView colon;
    private ImageView editImg;

	public EditLabelText(Context context) {
		this(context,null);
	}

	public EditLabelText(Context context, AttributeSet attrs) {
		super(context, attrs);
		int labelResouceId = -1;
		int valueResouceId = -1;
		int hintResourceId = -1;
		int backgroundResourceId = -1;

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		LayoutInflater inflater2 = LayoutInflater.from(context);
//		LayoutInflater inflater3 = getLayoutInflator();// 这个是在Activity里获取的，以上三种获取 inflater 的方式本质是一样的。
		if (isInEditMode()) { return; }

		view = inflater.inflate(R.layout.edit_label_text, null);
		row = (TableRow) view.findViewById(R.id.row);
		tv_label = (TextView) view.findViewById(R.id.label);
		et_value = (EditText) view.findViewById(R.id.value);
		colon = (TextView) view.findViewById(R.id.colon);
        editImg = (ImageView)view.findViewById(R.id.edit_img);
		
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
//		
//		hintResourceId = attrs.getAttributeResourceValue(null, "hint", 0);
//		if (hintResourceId > 0) {
//			hint = context.getResources().getText(hintResourceId).toString();
//        } else {
//        	hint = "";
//        }
		
		
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.edit_labeltext);
		backgroundImg = a.getDrawable(R.styleable.edit_labeltext_backgroundImg);
		label = a.getString(R.styleable.edit_labeltext_label);
		value = a.getString(R.styleable.edit_labeltext_value);
		hint = a.getString(R.styleable.edit_labeltext_hint_value);
        isShowColon = a.getBoolean(R.styleable.edit_labeltext_is_show_colon,true);
        valueRightGravity = a.getBoolean(R.styleable.edit_labeltext_value_right_gravity,false);
        isShowEdit = a.getBoolean(R.styleable.edit_labeltext_is_show_edit,false);
        valueTextSize = a.getDimensionPixelSize(R.styleable.edit_labeltext_value_text_size,ViewUtil.sp2px(getContext(),12));
        labelTextSize = a.getDimensionPixelSize(R.styleable.edit_labeltext_label_text_size,ViewUtil.sp2px(getContext(),12));
        valueTextColor = a.getColor(R.styleable.edit_labeltext_value_text_color,R.color.black);
        labelTextColor = a.getColor(R.styleable.edit_labeltext_label_text_color,R.color.black);

        labelPaddingTop = (int)a.getDimension(R.styleable.edit_labeltext_label_padding_top,ViewUtil.dip2px(getContext(),11));
        labelPaddingBottom = (int)a.getDimension(R.styleable.edit_labeltext_label_padding_bottom,ViewUtil.dip2px(getContext(),11));
        valuePaddingTop = (int)a.getDimension(R.styleable.edit_labeltext_value_padding_top,ViewUtil.dip2px(getContext(),11));
        valuePaddingBottom = (int)a.getDimension(R.styleable.edit_labeltext_value_padding_bottom,ViewUtil.dip2px(getContext(),11));

        viewHeight = (int) a.getDimension(R.styleable.edit_labeltext_view_height,0);

		a.recycle();

        tv_label.setText(label);
        et_value.setText(value);
        et_value.setHint(hint);

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
     * 设置标签和值的比重
     *
     */
    public void setLabelAndValueWeight(float labelWeight,float valueWeight){

        setLabelWeight(labelWeight);
        setValueWeight(valueWeight);
    }

    /**
     * 设置标签比重
     * @param labelWeight
     */
    public void setLabelWeight(float labelWeight){

        TableRow.LayoutParams lp = new TableRow.LayoutParams(0,LayoutParams.WRAP_CONTENT);

        lp.weight = labelWeight;

        tv_label.setLayoutParams(lp);
    }

    /**
     * 设置值的比重
     * @param valueWeight
     */
    public void setValueWeight(float valueWeight){

        TableRow.LayoutParams lp = new TableRow.LayoutParams(0,LayoutParams.WRAP_CONTENT);

        lp.weight = valueWeight;

        et_value.setLayoutParams(lp);
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
        isShowEdit = true;
        valueRightGravity = true;

        backgroundImg = getResources().getDrawable(R.drawable.edit_label_text_bg);

        view.setBackgroundDrawable(backgroundImg);

        setViewStyle();



    }
    /**
     * 设置组合控件的样式
     */
    private void setViewStyle(){


        tv_label.setTextColor(labelTextColor);
        tv_label.setTextSize(ViewUtil.px2sp(getContext(),labelTextSize));
        tv_label.setPadding(ViewUtil.dip2px(getContext(),5),labelPaddingTop,0,labelPaddingBottom);


        et_value.setTextColor(valueTextColor);
        et_value.setTextSize(ViewUtil.px2sp(getContext(),valueTextSize));
        et_value.setPadding(0,valuePaddingTop,ViewUtil.dip2px(getContext(),5),valuePaddingBottom);



        if(isShowColon){

            colon.setVisibility(View.GONE);
//            colon.setVisibility(View.VISIBLE);
        }else{
            colon.setVisibility(View.GONE);
        }
        if(valueRightGravity){
            et_value.setGravity(Gravity.RIGHT);
        }else{
            et_value.setGravity(Gravity.LEFT);
        }


        if(isShowEdit){

            editImg.setVisibility(View.VISIBLE);
          //  et_value.setEnabled(false);


        }else{
            editImg.setVisibility(View.GONE);
         //   et_value.setEnabled(true);
        }

        editImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

             //   et_value.setEnabled(true);

                et_value.setSelection(et_value.getText().length());

            }
        });
    }

    public boolean isShowColon() {
        return isShowColon;
    }

    /**
     * 设置是否显示冒号
     * @param isShowColon
     */
    public void setShowColon(boolean isShowColon) {
        this.isShowColon = isShowColon;
    }

    /**
     * 设置带有图片的hint
     * @param hintStr
     * @param hintDraw
     */
    public void setHintWithImg(String hintStr,Drawable hintDraw){

        SpannableString sp = new SpannableString(hintStr);
        Drawable d = getResources().getDrawable(R.drawable.edit_label_text_right_img);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        sp.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        this.setValueTextHint(sp);
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
	 * @see ： px
	 */
	public void setMargin(int left,int top,int right,int bottom){
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(ViewUtil.dip2px(getContext(),left), ViewUtil.dip2px(getContext(),top), ViewUtil.dip2px(getContext(),right), ViewUtil.dip2px(getContext(),bottom));
		view.setLayoutParams(lp);
	}
	
	/**
	 * 设置标签名
	 * @param alabel 
	 */
	public void setLabel(CharSequence alabel) {
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
	public void setValue(CharSequence aValue) {
		et_value.setText(aValue);
	}
	
	/**
	 * 获取value值
	 * @return
	 */
	public String getValue() {
		return et_value.getText().toString();
	}

    /**
     * 获取标签值
     * @return
     */
    public String getLabel() {

        return tv_label.getText().toString();
    }

    /**
	 * 设置标签属性是否可点击
	 * @param enabled
	 */
	public void setValueEnabled(boolean enabled) {
		et_value.setEnabled(enabled);
	}

	/**
	 * 设置标签属性值的颜色
	 * @param color
	 */
	public void setValueTextColor(int color){
		et_value.setTextColor(color);
	}
	
	/**
	 * 设置标签属性值的字体大小
	 * @param size
	 */
	public void setValueTextSize(int size){
		et_value.setTextSize(size);
	}
	
	/**
	 * 设置标签属性值的提示信息
	 * @param hintId
	 */
	public void setValueTextHint(int hintId){
		et_value.setHint(hintId);
	}
	
	/**
	 * 设置标签属性值的提示信息
	 * @param hintText
	 */
	public void setValueTextHint(CharSequence hintText){
		et_value.setHint(hintText);
	}
	
	/**
	 * 设置标签属性值的输入类型（InputType）
	 * @param inputType
	 */
	public void setValueInputType(int inputType){
		et_value.setInputType(inputType);
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


    public void setViewHeight(int height){

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,ViewUtil.dip2px(getContext(),height));
        lp.setMargins(ViewUtil.dip2px(getContext(),10),0,ViewUtil.dip2px(getContext(),10),0);
        view.setLayoutParams(lp);// 不设置这个，R.layout.search_edittext的 layout_width,layout_height会失效

    }



    public TextView getTv_label() {
        return tv_label;
    }

    public void setTv_label(TextView tv_label) {
        this.tv_label = tv_label;
    }

    public EditText getEt_value() {
        return et_value;
    }

    public void setEt_value(EditText et_value) {
        this.et_value = et_value;
    }
}
