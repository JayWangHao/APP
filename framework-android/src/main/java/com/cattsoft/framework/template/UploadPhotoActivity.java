package com.cattsoft.framework.template;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.R;
import com.cattsoft.framework.base.BaseActivity;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.connect.HttpMultipartPost;
import com.cattsoft.framework.pub.Constant;
import com.cattsoft.framework.util.BitMapUtil;
import com.cattsoft.framework.view.TitleBarView;
/**
 * 拍照之后用户用来预览刚拍完的照片
 * @author xueweiwei
 *
 */
public class UploadPhotoActivity extends BaseActivity{

	ImageView image;
	private Bitmap myBitmap;
    byte[] mContent;
    private String returnXxxurl;
    private JSONObject toOkJsonObject = new JSONObject();
    private String path;
    private Button sendToBtn,cancleBtn;
    private EditText remarkText;
    private String woNbr,localNetId,soNbr,soAttachId;
    byte[] bitmapBytes;
    private String imagePath;
    private HashMap<String,Object> map;
    private HttpMultipartPost post;
    
	public void onCreate(Bundle savedInstanceState) {
	//	this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置标题栏为用户自定义标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		TitleBarView title =  (TitleBarView) findViewById(R.id.title1);
		title.setTitleBar(getString(R.string.uploadPhotoActivityTitle), View.GONE,
				View.GONE, View.GONE, false);
	
		image = (ImageView)findViewById(R.id.show_image);
		sendToBtn = (Button)findViewById(R.id.send_to_btn);
		remarkText = (EditText) findViewById(R.id.describe_text);
		Intent intent=getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle.getString("imagePath")!=null){
			imagePath = bundle.getString("imagePath");
			List<HashMap<String,Object>> list = (List<HashMap<String, Object>>) bundle.getSerializable("map");
			map = list.get(0);
		}else{
			List<HashMap<String,Object>> list = (List<HashMap<String, Object>>) bundle.getSerializable("map");
			map = list.get(0);
			Uri uri = (Uri) map.get("uri");
			imagePath = BitMapUtil.uriToImagePath(uri, UploadPhotoActivity.this);
			
		}
	//	bitmapBytes=intent.getByteArrayExtra("BitImageBytes");
		
		myBitmap=BitMapUtil.convertToBitmap(imagePath);
		image.setImageBitmap(myBitmap);
		sendToBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				File file = new File(imagePath);
				if (file.exists()) {
//					post = new HttpMultipartPost(UploadPhotoActivity.this, imagePath, Constant.crashURL, true);
//					post.execute();
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("userName", "130000000");
					map.put("password", "111111");
					map.put("isCiphertext", false);
			    	JSONObject parameter = new JSONObject(map);
					Communication communication = new Communication(Constant.crashURL,parameter,imagePath,"afterSend",UploadPhotoActivity.this);
					communication.getPostHttpContent();

				} else {
					Toast.makeText(UploadPhotoActivity.this, "file not exists", Toast.LENGTH_LONG).show();
				}
				
			}
        	
        });
		 
	}
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void registerListener() {
		// TODO Auto-generated method stub
		
	}
	 public void afterSend(String json){
	    	
	    	
	    	Intent intent = new Intent();
			intent.putExtra("BitImageBytes",bitmapBytes);
			intent.putExtra("desc", remarkText.getText().toString());
			intent.putExtra("soAttachId", soAttachId);
			setResult(RESULT_OK,intent);
			finish();
	    	
//	    	try {
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
	    }
}
