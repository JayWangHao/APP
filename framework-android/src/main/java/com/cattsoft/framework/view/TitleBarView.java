package com.cattsoft.framework.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cattsoft.framework.R;
import com.cattsoft.framework.util.ViewUtil;

public class TitleBarView extends RelativeLayout{
	
	private ImageButton titleLeftButton;// 标题栏左边按钮，默认图标为返回，背景为透明
	private TextView titleTextView;// 标题栏居中标题
	private ImageView titleDownArrow;// 标题右侧小箭头，默认不可见
	private SearchEditText searchEditText;//搜索输入框，默认不可见
	private TextView cancel;//搜索输入框取消按钮
	private RelativeLayout titleMiddleButton;// 中部可点击区域，包括标题以及小箭头，默认不可点击
	private ImageButton titleRightButton;// 标题栏右边按钮，默认图标为刷新，背景为透明
    private TextView titleRightTextView;//标题栏右边显示文字
	private View view;
	private Drawable backgroundImg,leftImg,rightImg,arrowImg;
	private int backgroundColor;
	private int titleTextColor;
	private float titleTextSize;
    private int titleRightTextColor;
    private float titleRightTextSize;

    private int titleHeight;
	
	public TitleBarView(Context context) {
		this(context,null);
	}

	public TitleBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		view = LayoutInflater.from(context).inflate(R.layout.title, null);
		titleLeftButton = (ImageButton) view.findViewById(R.id.titlebar_img_btn_left);
		titleTextView = (TextView) view.findViewById(R.id.titlebar_text);
		titleDownArrow = (ImageView) view.findViewById(R.id.titlebar_down_arrow);
		searchEditText=(SearchEditText)view.findViewById(R.id.search_edit);
		cancel=(TextView)view.findViewById(R.id.cancel);
		titleMiddleButton = (RelativeLayout) view.findViewById(R.id.titlebar_title);
		titleRightButton = (ImageButton) view.findViewById(R.id.titlebar_img_btn_right);
        titleRightTextView = (TextView) view.findViewById(R.id.titlebar_textview_right);


		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.titlebar);
		backgroundImg = a.getDrawable(R.styleable.titlebar_title_backgroundImg);
		backgroundColor = a.getColor(R.styleable.titlebar_title_backgroundColor, R.color.red);
		leftImg = a.getDrawable(R.styleable.titlebar_left_img);
		rightImg = a.getDrawable(R.styleable.titlebar_right_img);
		arrowImg = a.getDrawable(R.styleable.titlebar_arrow_img);
		titleTextColor = a.getColor(R.styleable.titlebar_title_text_color, R.color.white);
		titleTextSize = a.getDimension(R.styleable.titlebar_title_text_size, ViewUtil.sp2px(getContext(),17));
        titleHeight = (int)a.getDimension(R.styleable.titlebar_title_height,0);
        titleRightTextColor = a.getColor(R.styleable.titlebar_title_right_text_color,R.color.title_right_color);
        titleRightTextSize = a.getDimension(R.styleable.titlebar_title_right_text_size,ViewUtil.sp2px(getContext(),16));

		
		a.recycle();
		if(leftImg!=null) {
			titleLeftButton.setImageDrawable(leftImg);
		}else{
			titleLeftButton.setImageDrawable(getResources().getDrawable(R.drawable.titlebar_img_btn_back));
		}
		if(rightImg!=null) {
			titleRightButton.setImageDrawable(rightImg);
		}
		if(arrowImg!=null) {
			titleDownArrow.setImageDrawable(arrowImg);
		}
		if(titleTextColor!=R.color.white) {
			titleTextView.setTextColor(titleTextColor);
		}
		if(titleTextSize!=ViewUtil.sp2px(getContext(),17)) {
			titleTextView.setTextSize(ViewUtil.px2sp(getContext(),titleTextSize));
		}

        titleRightTextView.setTextColor(titleRightTextColor);

        if(titleRightTextSize!=ViewUtil.sp2px(getContext(),16)){
            titleRightTextView.setTextSize(ViewUtil.px2sp(getContext(),titleRightTextSize));
        }
		
		MarginLayoutParams mp = new MarginLayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);  //item的宽高
	    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mp);
	    this.setLayoutParams(lp);
	    view.setLayoutParams(lp);
	    
	    if(backgroundColor!=R.color.red) {
	    	
	    	view.setBackgroundColor(backgroundColor);
	    }else {
	    	if(backgroundImg!=null) {
		    	
		    	view.setBackgroundDrawable(backgroundImg);
		    }else {
		    	
		    	view.setBackgroundResource(R.drawable.titlebar_bg);
		    }
	    }
        if(titleHeight==0){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

            view.setLayoutParams(layoutParams);
        }else{

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,titleHeight);

            view.setLayoutParams(layoutParams);
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,ViewUtil.dip2px(getContext(),48));

        view.setLayoutParams(layoutParams);
	    
		addView(view);
		
	}

	/**
	 * 设置标题栏
	 * 
	 * @param titleText
	 *            标题文字
	 */
	public void setTitleBar(String titleText) {
		titleLeftButton = (ImageButton) findViewById(R.id.titlebar_img_btn_left);
		titleTextView = (TextView) findViewById(R.id.titlebar_text);
		titleDownArrow = (ImageView) findViewById(R.id.titlebar_down_arrow);
		titleMiddleButton = (RelativeLayout) findViewById(R.id.titlebar_title);
		titleRightButton = (ImageButton) findViewById(R.id.titlebar_img_btn_right);
		setTitleText(titleText);
		leftButtonOnClick();
		middleButtonOnClick();
		rightButtonOnClick();
	}

	/**
	 * 设置标题栏
	 * 
	 * @param titleText
	 *            标题文字
	 * @param leftButtonVisibility
	 *            左侧按钮可见状态
	 * @param rightButtonVisibility
	 *            右侧按钮可见状态
	 * @param downArrowVisibility
	 *            小箭头可见状态
	 * @param middleButtonClickable
	 *            中部标题区域可点击状态
	 */
	public void setTitleBar(String titleText, int leftButtonVisibility,
			int rightButtonVisibility, int downArrowVisibility,
			boolean middleButtonClickable) {
		titleLeftButton = (ImageButton) findViewById(R.id.titlebar_img_btn_left);
		titleTextView = (TextView) findViewById(R.id.titlebar_text);
		titleDownArrow = (ImageView) findViewById(R.id.titlebar_down_arrow);
		titleMiddleButton = (RelativeLayout) findViewById(R.id.titlebar_title);
		titleRightButton = (ImageButton) findViewById(R.id.titlebar_img_btn_right);
		setTitleText(titleText);
		setTitleLeftButtonVisibility(leftButtonVisibility);
		setTitleRightButtonVisibility(rightButtonVisibility);
		setTitleDownArrowVisibility(downArrowVisibility);
		leftButtonOnClick();
		middleButtonOnClick();
		rightButtonOnClick();
		setTitleMiddleButtonClickable(middleButtonClickable);
	}

    /**
     * 设置标题栏
     * @param titleText
     *           标题文字
     * @param titleRightText
     *           标题右侧文字
     * @param leftButtonVisibility
     *           左侧按钮是否可见
     * @param rightButtonVisibility
     *           右侧按钮是否可见
     * @param rightTextVisibility
     *            右侧文字是否可见
     * @param downArrowVisibility
     *             小箭头可见状态
     * @param middleButtonClickable
     *             中部标题区域可点击状态
     */
    public void setTitleBar(String titleText,String titleRightText, int leftButtonVisibility,
                            int rightButtonVisibility,int rightTextVisibility, int downArrowVisibility,
                            boolean middleButtonClickable){
        titleLeftButton = (ImageButton) findViewById(R.id.titlebar_img_btn_left);
        titleTextView = (TextView) findViewById(R.id.titlebar_text);
        titleDownArrow = (ImageView) findViewById(R.id.titlebar_down_arrow);
        titleMiddleButton = (RelativeLayout) findViewById(R.id.titlebar_title);
        titleRightButton = (ImageButton) findViewById(R.id.titlebar_img_btn_right);
        titleRightTextView = (TextView) findViewById(R.id.titlebar_textview_right);


        setTitleText(titleText);
        setTitleRightText(titleRightText);
        setTitleLeftButtonVisibility(leftButtonVisibility);
        setTitleRightButtonVisibility(rightButtonVisibility);
        setTitleRightTextViewVisibility(rightTextVisibility);
        setTitleDownArrowVisibility(downArrowVisibility);
        leftButtonOnClick();
        middleButtonOnClick();
        rightButtonOnClick();
        setTitleMiddleButtonClickable(middleButtonClickable);
    }

	/**
	 * 设置左边按钮是否可见
	 * 
	 * @param visibility
	 *            可见状态
	 */
	public void setTitleLeftButtonVisibility(int visibility) {
		titleLeftButton.setVisibility(visibility);
	}

	/**
	 * 设置右边按钮是否可见
	 * 
	 * @param visibility
	 *            可见状态
	 */
	public void setTitleRightButtonVisibility(int visibility) {
		titleRightButton.setVisibility(visibility);
	}

    /**
     * 设置右边文字是否可见
     * @param visibility
     */
    public void setTitleRightTextViewVisibility(int visibility){

        titleRightTextView.setVisibility(visibility);
    }

	/**
	 * 设置小箭头是否可见
	 * 
	 * @param visibility
	 *            可见状态
	 */
	public void setTitleDownArrowVisibility(int visibility) {
		titleDownArrow.setVisibility(visibility);
	}

	/**
	 * 设置左侧按钮图标
	 * 
	 * @param leftButtonImg
	 *            图标资源id
	 */
	public void setTitleLeftButtonImg(int leftButtonImg) {
		titleLeftButton.setImageResource(leftButtonImg);
	}

	/**
	 * 输入框的显隐
	 * @param visibility
     */
	public void setTitleSearchEditVisibility(int visibility) {
		searchEditText.setVisibility(visibility);
	}

	/**
	 * 取消按钮的显隐
	 * @param visibility
     */
	public void setTitleCancelVisibility(int visibility) {
		cancel.setVisibility(visibility);
	}

	/**
	 * 标题栏的显隐
	 * @param visibility
     */
	public void setTitleTextVisibility(int visibility) {
		titleTextView.setVisibility(visibility);
	}

	/**
	 * 设置右侧按钮图标
	 * 
	 * @param rightButtonImg
	 *            图标资源id
	 */
	public void setTitleRightButtonImg(int rightButtonImg) {
		titleRightButton.setImageResource(rightButtonImg);
	}

	/**
	 * 设置标题文本
	 * 
	 * @param titleText
	 *            标题文本
	 */
	public void setTitleText(String titleText) {
		titleTextView.setText(titleText);
	}

	/**
	 * 设置输入框的内容
	 * @param searchText
     */
	public void setSearchEditText(String searchText) {
		searchEditText.setText(searchText);
	}

    public void setTitleRightText(String titleRightText){

        titleRightTextView.setText(titleRightText);
    }

	/**
	 * 设置中部区域可点击状态
	 * 
	 * @param clickable
	 *            可点击状态
	 */
	public void setTitleMiddleButtonClickable(boolean clickable) {
		titleMiddleButton.setClickable(clickable);
	}

	/**
	 * 左侧按钮点击事件监听 默认返回主界面
	 */
	public void leftButtonOnClick() {
		titleLeftButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
	
			}
		});
	}

	/**
	 * 所搜框的事件监听
	 */
	public void searchEditTextOnClick(){

	}

	/**
	 * 所搜框的事件监听
	 */
	public void cancelOnClick(){
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});
	}

	/**
	 * 中部标题区域点击事件监听 默认切换工区
	 */
	public void middleButtonOnClick() {
		titleMiddleButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
			}
		});
	}

	/**
	 * 右侧按钮点击事件监听
	 */
	public void rightButtonOnClick() {
		titleRightButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

			}
		});
	}
	
	public ImageButton getTitleLeftButton() {
		return titleLeftButton;
	}

	public TextView getTitleTextView() {
		return titleTextView;
	}

	public SearchEditText getSearchEditText() {
		return searchEditText;
	}

	public TextView getCancel() {
		return cancel;
	}

	public ImageView getTitleDownArrow() {
		return titleDownArrow;
	}

	public RelativeLayout getTitleMiddleButton() {
		return titleMiddleButton;
	}

	public ImageButton getTitleRightButton() {
		return titleRightButton;
	}

    public TextView getTitleRightTextView() {
        return titleRightTextView;
    }

    public void setTitleBackground(int backgroundImg) {
		
		view.setBackgroundResource(backgroundImg);
	}

    public void setTitleBackgroundColor(int color){

        view.setBackgroundDrawable(null);
        view.setBackgroundColor(color);
    }

    public void setTitleHeight(int height){

        MarginLayoutParams mp = new MarginLayoutParams(LinearLayout.LayoutParams.FILL_PARENT,ViewUtil.dip2px(getContext(),height));  //item的宽高
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mp);

        view.setLayoutParams(lp);
    }

    public void setTitleTextColor(int color){
        titleTextView.setTextColor(color);
    }


}
