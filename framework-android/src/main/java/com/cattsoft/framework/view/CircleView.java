package com.cattsoft.framework.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.cattsoft.framework.R;
import com.cattsoft.framework.util.ViewUtil;

/**
 * Created by xueweiwei on 2015/1/20.
 * 自定义圆环
 */
public class CircleView extends View{

    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 内圆环的颜色
     */
    private int innerRoundColor;

    /**
     * 外圆环的颜色
     */
    private int outerRoundColor;

    /**
     * 中间数字颜色
     */
    private int numTextColor;

    /**
     * 中间数字字体
     */
    private float numTextSize;

    /**
     * 中间文字颜色
     */
    private int remarkTextColor;

    /**
     * 中间文字字体
     */
    private float remarkTextSize;
    /**
     * 中间显示文字
     */
    private String remark;

    /**
     * 内圆环的宽度
     */
    private float innerRoundWidth;

    /**
     * 外圆环的宽度
     */
    private float outerRoundWidth;

    /**
     * 内圆环所代表的数量
     */
    private int max;

    /**
     * 外圆环所代表的数量
     */
    private int progress;
    /**
     * 是否显示中间的百分比
     */
    private boolean textIsDisplayable;

    /**
     * 环实心或者空心
     */
    private int style;

    public static final int STROKE = 0;
    public static final int FILL = 1;

    private Context context;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.context = context;
        paint = new Paint();


        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.CircleView);

        //获取自定义属性和默认值
        innerRoundColor = mTypedArray.getColor(R.styleable.CircleView_innerRoundColor, R.color.inner_round_default_color);
        outerRoundColor = mTypedArray.getColor(R.styleable.CircleView_outerRoundColor, R.color.outer_round_default_color);
        numTextColor = mTypedArray.getColor(R.styleable.CircleView_numTextColor, R.color.num_text_default_color);
        numTextSize = mTypedArray.getDimensionPixelSize(R.styleable.CircleView_numTextSize, ViewUtil.sp2px(context,28));//单位是px
        remarkTextSize = mTypedArray.getDimensionPixelSize(R.styleable.CircleView_remarkTextSize,ViewUtil.sp2px(context,11));//单位是px
        remarkTextColor = mTypedArray.getColor(R.styleable.CircleView_remarkTextColor,R.color.remark_text_default_color);
        innerRoundWidth = mTypedArray.getDimension(R.styleable.CircleView_innerRoundWidth, ViewUtil.dip2px(context,23));
        outerRoundWidth = mTypedArray.getDimension(R.styleable.CircleView_outerRoundWidth, ViewUtil.dip2px(context,28));

        remark = mTypedArray.getString(R.styleable.CircleView_remark);

        if(innerRoundWidth>outerRoundWidth){
            float temp = innerRoundWidth;
            innerRoundWidth = outerRoundWidth;
            outerRoundWidth = temp;

        }
        max = mTypedArray.getInteger(R.styleable.CircleView_max, 100);
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.CircleView_textIsDisplayable, true);
        style = mTypedArray.getInt(R.styleable.CircleView_style, 0);

        mTypedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 画内圆环
         */
        int centre = getWidth()/2; //获取圆心的x坐标,单位是dip
       // int radius = ViewUtil.px2dip(context,centre-outerRoundWidth + innerRoundWidth/2); //圆环的半径
        int radius = (int)(centre-outerRoundWidth + innerRoundWidth/2); //圆环的半径
        paint.setColor(innerRoundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(innerRoundWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centre, centre, radius, paint); //画出圆环

        Log.e("log", centre + "");

        /**
         * 画进度百分比
         */
        paint.setStrokeWidth(0);
        paint.setColor(numTextColor);
        paint.setTextSize(numTextSize);
     //   paint.setTextSize(numTextSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
        int percent = progress;  //中间的进度百分比，先转换成float在进行除法运算，不然都为0
        float textWidth = paint.measureText(percent+"");   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间

        if(textIsDisplayable && percent >= 0 && style == STROKE){
            canvas.drawText(percent+"", centre - textWidth / 2, centre + numTextSize/2, paint); //画出进度百分比

            paint.setTextSize(remarkTextSize);
            paint.setColor(remarkTextColor);
            float textWidth2 = paint.measureText(remark);
            canvas.drawText(remark, centre - textWidth2 / 2, centre +remarkTextSize/2+  numTextSize/2+ViewUtil.dip2px(context,10), paint); //画出进度百分比

        }





        /**
         * 画圆弧 ，画外圆环的进度
         */

        //设置进度是实心还是空心
        paint.setStrokeWidth(outerRoundWidth); //设置圆环的宽度
        paint.setColor(outerRoundColor);  //设置进度的颜色
       // radius = ViewUtil.px2dip(context,centre - outerRoundWidth/2); //圆环的半径
        radius = (int)(centre - outerRoundWidth/2); //圆环的半径
        RectF oval = new RectF(centre - radius+1, centre - radius+1, centre
                + radius-1, centre + radius-1);  //用于定义的圆弧的形状和大小的界限


        switch (style) {
            case STROKE:{
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, -90, max == 0 ? 0 : 360 * progress / max, false, paint);  //根据进度画圆弧
                break;
            }
            case FILL:{
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if(progress !=0)
                    canvas.drawArc(oval, -90, max == 0 ? 0 : 360 * progress / max, true, paint);  //根据进度画圆弧
                break;
            }
        }

    }


    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     * @param max
     */
    public synchronized void setMax(int max) {
        if(max < 0){
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if(progress < 0){
            throw new IllegalArgumentException("progress not less than 0");
        }
        if(progress > max){
            progress = max;
        }
        if(progress <= max){
            this.progress = progress;
            postInvalidate();
        }

    }


    public int getInnerRoundColor(){

        return innerRoundColor;
    }
    public void setInnerRoundColor(int innerRoundColor){

        this.innerRoundColor = innerRoundColor;
    }

    public int getOuterRoundColor(){
        return outerRoundColor;
    }
    public void setOuterRoundColor(int outerRoundColor){

        this.outerRoundColor = outerRoundColor;

    }


    public int getNumTextColor() {
        return numTextColor;
    }

    public void setNumTextColor(int textColor) {
        this.numTextColor = textColor;
    }

    public float getNumTextSize() {
        return numTextSize;
    }

    public void setNumTextSize(float textSize) {
        this.numTextSize = ViewUtil.sp2px(context,textSize);
    }

    public int getRemarkTextColor(){
        return remarkTextColor;
    }

    public void setRemarkTextColor(int remarkTextColor){
        this.remarkTextColor = remarkTextColor;
    }

    public float getRemarkTextSize(){
        return remarkTextSize;
    }

    public void setRemarkTextSize(int remarkTextSize){

        this.remarkTextSize = ViewUtil.sp2px(context,remarkTextSize);
    }

    public float getInnerRoundWidth() {
        return innerRoundWidth;
    }

    public void setInnerRoundWidth(float innerRoundWidth) {
        this.innerRoundWidth = ViewUtil.dip2px(context,innerRoundWidth);
    }

    public float getOuterRoundWidth() {

        return outerRoundWidth;
    }

    public void setOuterRoundWidth(float outerRoundWidth) {
        this.outerRoundWidth = ViewUtil.dip2px(context,outerRoundWidth);
    }

    public String getRemark(){
        return remark;
    }

    public void setRemark(String remark){
        this.remark = remark;
    }


}
