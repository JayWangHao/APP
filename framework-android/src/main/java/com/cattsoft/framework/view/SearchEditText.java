package com.cattsoft.framework.view;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.cattsoft.framework.R;


public class SearchEditText extends EditText implements OnFocusChangeListener, TextWatcher {
	
	private FrameLayout relativeLayout;
	
	/**
	 * 删除按钮的引用
	 */
	private Drawable mClearDrawable;
	
	/**
	 * 左侧搜索图标
	 */
	private Drawable mSearchDrawable;
	
	/**
	 * 控件是否有焦点
	 */
	private boolean hasFocus;

	public SearchEditText(Context context) {
		this(context, null);
	}

	public SearchEditText(Context context, AttributeSet attrs) {
		// 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
		this(context, attrs, android.R.attr.editTextStyle);
	}

	public SearchEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
//		setMargin(20,20,20,20);
		
//		relativeLayout = new RelativeLayout(context);
//		MarginLayoutParams mp = new MarginLayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);  //item的宽高
//		this.setMargin(10, 10, 10, 10);
//	    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mp);
//	    relativeLayout.setLayoutParams(lp);
		init();
	}

	private void init() {
		mSearchDrawable = getCompoundDrawables()[0];// 左，上，右，下
		if (mSearchDrawable == null) {
			setDrawableLeft(R.drawable.search);
		}
		setCompoundDrawables(mSearchDrawable, null, null, null);
		// 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
		mClearDrawable = getCompoundDrawables()[2]; // 左，上，右，下
		if (mClearDrawable == null) {
			setDrawablRight(R.drawable.delete);
		}
		
		// 不设置下边两行代码，就不会显示图标
		mSearchDrawable.setBounds(0, 0, mSearchDrawable.getIntrinsicWidth(), mSearchDrawable.getIntrinsicHeight());
		mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
		
		// 默认设置隐藏图标
		setClearIconVisible(false);
		// 设置焦点改变的监听
		setOnFocusChangeListener(this);
		// 设置输入框里面内容发生改变的监听
		addTextChangedListener(this);
		
		this.setBackgroundResource(R.drawable.edittext_bg);
		this.setHint("请输入关键字");
		this.setHintTextColor(Color.GRAY);
		this.setTextColor(Color.BLACK);
		this.setPadding(20,20,20,20);
		this.setSingleLine(true);
	}
	
	/**
	 * 设置左侧搜索图标
	 * @param drawableLeftId
	 */
	public void setDrawableLeft(int drawableLeftId){
		mSearchDrawable = getResources().getDrawable(drawableLeftId);
	}
	
	/**
	 * 设置右侧删除图标
	 * @param drawableLeftId
	 */
	public void setDrawablRight(int drawableRightId){
		mClearDrawable = getResources().getDrawable(drawableRightId);
	}
	
	
//	public void setMargin(int left,int top,int right,int bottom){
//		MarginLayoutParams mp = new MarginLayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);  //item的宽高
//	    mp.setMargins(left, top, right, bottom);
//	    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mp);
//	    ViewParent vvvv =  this.getParent();
//	    ((View) vvvv).setLayoutParams(lp);
//	}
	
	/**
	 * 因为不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
	 * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (getCompoundDrawables()[2] != null) {

				boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight()) && (event.getX() < ((getWidth() - getPaddingRight())));

				if (touchable) {
					this.setText("");
				}
			}
		}

		return super.onTouchEvent(event);
	}

	/**
	 * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		this.hasFocus = hasFocus;
		if (hasFocus) {
			setClearIconVisible(getText().length() > 0);
		} else {
			setClearIconVisible(false);
		}
	}

	/**
	 * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
	 * 
	 * @param visible
	 */
	protected void setClearIconVisible(boolean visible) {
		Drawable right = visible ? mClearDrawable : null;
		setCompoundDrawables(mSearchDrawable, null, right, null);
	}

	/**
	 * 当输入框里面内容发生变化的时候回调的方法
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		if (hasFocus) {
			setClearIconVisible(s.length() > 0);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	/**
	 * 设置晃动动画
	 */
	public void setShakeAnimation() {
		this.setAnimation(shakeAnimation(5));
	}

	/**
	 * 晃动动画
	 * 
	 * @param counts
	 *            1秒钟晃动多少下
	 * @return
	 */
	public static Animation shakeAnimation(int counts) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
		translateAnimation.setInterpolator(new CycleInterpolator(counts));
		translateAnimation.setDuration(1000);
		return translateAnimation;
	}

}
