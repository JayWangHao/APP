package com.cattsoft.framework.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cattsoft.framework.R;
import com.cattsoft.framework.cache.MosApp;

/**
 * 自定义带有消息数字的图标
 * @author xueweiwei
 *
 */
public class ImageWithTips extends RelativeLayout {
	
	private String tips="..."; 
	
	private ImageView iv=null;
	private TextView tvTips=null;
	final int imageId=10000000;
	
	public ImageWithTips(Context context) {
		super(context,null);
		

	}

    public ImageWithTips(Context context, AttributeSet attrs){

        super(context,attrs);
        LinearLayout.LayoutParams layoutpare=
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutpare);

        initView(context);

    }
	
	
	public void initView(Context context) {

        LinearLayout.LayoutParams layoutpare=
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		iv=new ImageView(context);
        iv.setLayoutParams(layoutpare);
	//	iv.setId(imageId);
		
		tvTips=new TextView(context);
		tvTips.setBackgroundResource(R.drawable.func_nav_tips_bg);
		tvTips.setTextColor(0xFFFFFFFF);
		tvTips.setGravity(Gravity.CENTER);
		tvTips.setText(tips);
		RelativeLayout.LayoutParams llp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
		llp.setMargins(0, 10, 15, 0);
		llp.addRule(RelativeLayout.ALIGN_TOP, imageId);
		llp.addRule(RelativeLayout.ALIGN_RIGHT,imageId);
		tvTips.setLayoutParams(llp);
		tvTips.setVisibility(View.GONE);
		
		this.addView(iv);
		this.addView(tvTips);
		
	}
	
	public void setImageResource(int resource){
		iv.setImageResource(resource);
	}
	
	/**
	 * 通过图片名称设置首页图片
	 * @param imageName
	 */
	public void setImageResourceStr(String imageName){
		
		  Resources res=getResources();
		  iv.setImageResource(res.getIdentifier(imageName,"drawable",MosApp.getInstance().getPackageName())); 
	}
	
	public TextView getTextView() {
		return tvTips;
	}
	//是否显示数字
	public void setTipVisible(int visible){
		
		tvTips.setVisibility(visible);
	}
	
	//设置数字背景图
	public void setTipBackground(int resource){
		
		tvTips.setBackgroundResource(resource);
	}

 

	
}
