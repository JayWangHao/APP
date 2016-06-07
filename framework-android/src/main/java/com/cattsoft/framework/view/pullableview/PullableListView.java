package com.cattsoft.framework.view.pullableview;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.cattsoft.framework.view.pullableview.swipemenu.SwipeMenu;
import com.cattsoft.framework.view.pullableview.swipemenu.SwipeMenuCreator;
import com.cattsoft.framework.view.pullableview.swipemenu.SwipeMenuLayout;


public class PullableListView extends ListView implements Pullable {

	private static final int TOUCH_STATE_NONE = 0;
	private static final int TOUCH_STATE_X = 1;
	private static final int TOUCH_STATE_Y = 2;

	private int MAX_Y = 5;
	private int MAX_X = 3;
	private float mDownX;
	private float mDownY;
	private int mTouchState;
	private int mTouchPosition;
	private SwipeMenuLayout mTouchView;
	private OnSwipeListener mOnSwipeListener;

	private SwipeMenuCreator mMenuCreator;
	private OnMenuItemClickListener mOnMenuItemClickListener;
	private Interpolator mCloseInterpolator;
	private Interpolator mOpenInterpolator;

	// isSwipe == false 才能下拉刷新或者上拉加载
	public static boolean isSwipe = false;

	public PullableListView(Context context) {
		super(context);
		init();
	}

	public PullableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PullableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@Override
	public boolean canPullDown() {
		if (getCount() == 0) {
			// 没有item的时候也可以下拉刷新
			return true;
		} else if (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= 0 && !isSwipe) {
			// 滑到ListView的顶部了
			return true;
		} else
			return false;
	}

	@Override
	public boolean canPullUp() {
		if (getCount() == 0) {
			// 没有item的时候不可以上拉加载
			return false;
		} else if (getLastVisiblePosition() == (getCount() - 1)) {
			// 滑到底部了
			if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
					&& getChildAt(
					getLastVisiblePosition()
							- getFirstVisiblePosition()).getBottom() <= getMeasuredHeight()
					&& !isSwipe)
				return true;
		}
		return false;
	}

	// ================  SwipeMenuList ======================================================
	private void init() {
		MAX_X = dp2px(MAX_X);
		MAX_Y = dp2px(MAX_Y);
		mTouchState = TOUCH_STATE_NONE;
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getContext().getResources().getDisplayMetrics());
	}

	@Override
	public void setAdapter(ListAdapter adapter) {

		super.setAdapter(adapter);
//		super.setAdapter(new SwipeMenuAdapter(getContext(), adapter) {
//			@Override
//			public void createMenu(SwipeMenu menu) {
//				System.out.println("size = "+menu.getMenuItems().size());
//				if (mMenuCreator != null) {
//					mMenuCreator.create(menu);
//				}
//			}
//
//			@Override
//			public void onItemClick(SwipeMenuView view, SwipeMenu menu,
//					int index) {
//				boolean flag = false;
//				if (mOnMenuItemClickListener != null) {
//					flag = mOnMenuItemClickListener.onMenuItemClick(
//							view.getPosition(), menu, index);
//				}
//				if (mTouchView != null && !flag) {
//					mTouchView.smoothCloseMenu();
//				}
//			}
//		});
	}

	public void setCloseInterpolator(Interpolator interpolator) {
		mCloseInterpolator = interpolator;
	}

	public void setOpenInterpolator(Interpolator interpolator) {
		mOpenInterpolator = interpolator;
	}

	public Interpolator getOpenInterpolator() {
		return mOpenInterpolator;
	}

	public Interpolator getCloseInterpolator() {
		return mCloseInterpolator;
	}

//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		return false;
//	}






	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

//		if (ev.getAction() != MotionEvent.ACTION_DOWN && mTouchView == null){
//			return true;
//		}

		int action = MotionEventCompat.getActionMasked(ev);
		action = ev.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				int oldPos = mTouchPosition;
				mDownX = ev.getX();
				mDownY = ev.getY();
				mTouchState = TOUCH_STATE_NONE;

				mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());

				if (mTouchPosition == oldPos && mTouchView != null
						&& mTouchView.isOpen() && mTouchView.bCanSwipe == true) {
					mTouchState = TOUCH_STATE_X;
					mTouchView.onSwipe(ev);
					return true;
				}

				View view = getChildAt(mTouchPosition - getFirstVisiblePosition());

				if (mTouchView != null && mTouchView.isOpen()&& mTouchView.bCanSwipe == true) {
					mTouchView.smoothCloseMenu();
					mTouchView = null;
//				isSwipe = false;
					// 不在这里设置isSwipe = false，在action_up里设置。
//				return super.onTouchEvent(ev);
					return true;
				}
				if (view instanceof SwipeMenuLayout) {
					mTouchView = (SwipeMenuLayout) view;
				}
				if (mTouchView != null&& mTouchView.bCanSwipe == true) {
					mTouchView.onSwipe(ev);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				float dy = Math.abs((ev.getY() - mDownY));
				float dx = Math.abs((ev.getX() - mDownX));

				if (mTouchState == TOUCH_STATE_X) {
					if (mTouchView != null&& mTouchView.bCanSwipe == true) {
						mTouchView.onSwipe(ev);
					}
					getSelector().setState(new int[] { 0 });
					ev.setAction(MotionEvent.ACTION_CANCEL);
					super.onTouchEvent(ev);
					return true;
				} else if (mTouchState == TOUCH_STATE_NONE) {
					if (Math.abs(dy) > MAX_Y) {
						mTouchState = TOUCH_STATE_Y;
//					isSwipe = false;//
					} else if (dx > MAX_X) {
						mTouchState = TOUCH_STATE_X;
						if (mOnSwipeListener != null) {
							mOnSwipeListener.onSwipeStart(mTouchPosition);
							if(mTouchView != null){
								isSwipe = true;
							} else {
								isSwipe = false;
							}
						}
					}
				}
				break;
			case MotionEvent.ACTION_UP:

				if (mTouchState == TOUCH_STATE_X) {

					if (mTouchView != null&& mTouchView.bCanSwipe == true) {
						mTouchView.onSwipe(ev);
						if (!mTouchView.isOpen()) {
							mTouchPosition = -1;
							mTouchView = null;
							isSwipe = false;
						}
					}
					if (mOnSwipeListener != null) {
						mOnSwipeListener.onSwipeEnd(mTouchPosition);
//					isSwipe = false;  这里不能写isSwipe
					}
					ev.setAction(MotionEvent.ACTION_CANCEL);
					super.onTouchEvent(ev);
					return true;
				} else if(mTouchState == TOUCH_STATE_NONE){
					mTouchView = null;
					isSwipe = false;
				} else if(mTouchState == TOUCH_STATE_Y){
					mTouchView = null;
					isSwipe = false;
				}
				break;

//		case MotionEvent.ACTION_CANCEL:
//
//			mTouchView = null;
//			isSwipe = true;
//
//			break;
		}
		return super.onTouchEvent(ev);
	}

	public void smoothOpenMenu(int position) {
		if (position >= getFirstVisiblePosition()
				&& position <= getLastVisiblePosition()) {
			View view = getChildAt(position - getFirstVisiblePosition());
			if (view instanceof SwipeMenuLayout) {
				mTouchPosition = position;
				if (mTouchView != null && mTouchView.isOpen()) {
					mTouchView.smoothCloseMenu();
				}
				mTouchView = (SwipeMenuLayout) view;
				mTouchView.smoothOpenMenu();
			}
		}
	}

	public void setMenuCreator(SwipeMenuCreator menuCreator) {
		this.mMenuCreator = menuCreator;
	}

	public void setOnMenuItemClickListener(
			OnMenuItemClickListener onMenuItemClickListener) {
		this.mOnMenuItemClickListener = onMenuItemClickListener;
	}

	public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
		this.mOnSwipeListener = onSwipeListener;
	}

	public static interface OnMenuItemClickListener {
		boolean onMenuItemClick(int position, SwipeMenu menu, int index);
	}

	public static interface OnSwipeListener {
		void onSwipeStart(int position);

		void onSwipeEnd(int position);
	}
}
