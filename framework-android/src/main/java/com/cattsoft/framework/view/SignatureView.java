package com.cattsoft.framework.view;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.cattsoft.framework.cache.MosApp;
/**
 * 画板
 * @author xueweiwei
 *
 */
public class SignatureView extends View {

    
    
    private Bitmap  mBitmap;
    private Canvas  mCanvas;
    private Path    mPath;
    private Paint   mBitmapPaint;
    
    private Paint   mPaint;
    private boolean isSigned;
    private int width;//屏幕的宽度
    private int height;//屏幕的高度
    //获取屏幕宽度和高度
    public void getScreenHW(Context context){
    	WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
    	Display display = manager.getDefaultDisplay();
    	width =display.getWidth();
    	height=display.getHeight();
    	Log.d("width", String.valueOf(width));
    	Log.d("height", String.valueOf(height));        //第一种
    	
//    	DisplayMetrics dm=new DisplayMetrics();
//    	manager.getDefaultDisplay().getMetrics(dm);
//    	int width2=dm.widthPixels;
//    	int height2=dm.heightPixels;
//    	Log.d("width2", String.valueOf(width2));
//    	Log.d("height2", String.valueOf(height2));     //第二种
    }
    public SignatureView(Context c) {
        super(c);
        
        getScreenHW(MosApp.getInstance().getApplicationContext());
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5);
        
    }
    
    public SignatureView(Context context, AttributeSet attrs) {
    	super(context, attrs);
    	
    	getScreenHW(MosApp.getInstance().getApplicationContext());
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    	mCanvas = new Canvas(mBitmap);
    	mPath = new Path();
    	mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    	
    	mPaint = new Paint();
    	mPaint.setAntiAlias(true);
    	mPaint.setDither(true);
    	mPaint.setColor(Color.BLACK);
    	mPaint.setStyle(Paint.Style.STROKE);
    	mPaint.setStrokeJoin(Paint.Join.ROUND);
    	mPaint.setStrokeCap(Paint.Cap.ROUND);
    	mPaint.setStrokeWidth(5);
    	
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        
        canvas.drawPath(mPath, mPaint);
    }
    
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;
    
    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }
    private void touch_move(float x, float y) {
    	if(mX==x&&mY==y) {
    		this.isSigned=false;
    	}else {
    		this.isSigned=true;
    	}
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }
    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
        mPath.reset();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }
    
    public void clear() {
    	mPath.reset();
    	mCanvas.drawColor(0,PorterDuff.Mode.CLEAR);
    	invalidate();
    	this.isSigned=false;
    }

    public byte[] saveToBytes() {
    	ByteArrayOutputStream  bos=new ByteArrayOutputStream();
    	mBitmap.compress(CompressFormat.PNG, 100, bos);
    	byte[] bitmapBytes=bos.toByteArray();
    	try {
    		bos.flush();
			bos.close();
		} catch (IOException e) {
			Log.d("【IO】","SignatureView的saveToBytes()方法关闭流异常");
			e.printStackTrace();
		}
    	return bitmapBytes;
    }
    
	public void saveToFile(String filename) throws FileNotFoundException {
		File f = new File(Environment.getExternalStorageDirectory().getPath()+filename);
		if(f.exists())
			throw new RuntimeException("文件：" + filename + " 已存在！");
			
		FileOutputStream fos = new FileOutputStream(f);
		//将 bitmap 压缩成其他格式的图片数据
		mBitmap.compress(CompressFormat.PNG, 50, fos);
		try {
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isSigned() {
		return isSigned;
	}
}
