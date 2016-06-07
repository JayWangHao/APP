package com.cattsoft.framework.template;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.R;
import com.cattsoft.framework.base.BaseActivity;
import com.cattsoft.framework.cache.CacheProcess;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.pub.Constant;
import com.cattsoft.framework.template.TestActivity;
import com.cattsoft.framework.util.CameraUtil;
import com.cattsoft.framework.view.TitleBarView;

public class MainActivity extends BaseActivity {

	private Button loginBtn;
	private EditText etUserName;
	private EditText etPassword;
	private String url;
	private JSONObject parameter;
	private CacheProcess cacheProcess;
	
	private EditText loginName;
	private EditText password;
	
	private TextView resultTextView;
	
	private CameraUtil cameraUtil;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TitleBarView title =  (TitleBarView) findViewById(R.id.title1);
		title.setTitleBar(getString(R.string.title_activity_main), View.GONE,View.GONE, View.GONE, false);
		
   //     super.setTitleBar(getString(R.string.title_activity_main), View.GONE,View.GONE, View.GONE, false);
		cacheProcess = new CacheProcess();
		
		initView();
		initParameter();
		registerListener();
		
    }
    /**
	 * 调用头像的的onActivityResult
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	//	cameraUtil.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Constant.SCANNIN_GREQUEST_CODE:
				Bundle bundle = data.getExtras();
				String scanResult = bundle.getString("result");
				resultTextView.setText(scanResult);
			break;
		}
	}
 }
    private void initParameter(){
    	url = "loginAction.do?method=spslogin4MOS";
    	HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userName", "130000000");
		map.put("password", "111111");
		map.put("isCiphertext", false);
    	parameter = new JSONObject(map);
    }
    
    private void afterLogin(JSONObject json){
    	
    	JSONObject result = json.getJSONObject("mobileBody");
    	cacheProcess.initCacheInSharedPreferences(this, result);
    //	Intent intent = new Intent(MainActivity.this,AboutUsActivity.class);
    //	startActivity(intent);
    	
//    	try {
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
    }

	@Override
	protected void initView() {
		
		resultTextView = (TextView) findViewById(R.id.result_text);
		loginBtn = (Button) findViewById(R.id.login_btn_login);
		loginName = (EditText) findViewById(R.id.login_edit_username);
		password = (EditText) findViewById(R.id.login_edit_password);
		
		// 缺少一个值，是否加密？
	}

	@Override
	protected void registerListener() {
		// TODO Auto-generated method stub
		loginBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
//				Communication communication = new Communication(url,parameter,"afterLogin",MainActivity.this);
//				communication.getPostHttpContent();
				
//				startActivity(new Intent(MainActivity.this,TestActivity.class));
				
				
				//测试通信类
				Communication communication = new Communication(url,parameter,"afterLogin",MainActivity.this);
				communication.getPostHttpContent();
				
				//测试拍照上传功能
//				HashMap<String,Object> map = new HashMap<String,Object>();
//				cameraUtil = new CameraUtil(MainActivity.this,map,new CameraCallBack() {
//					@Override
//					public void cropResult(Context context, Intent data) {
//						
//					}
//
//					@Override
//					public void upLoadImageResult(Context context, Intent data) {
//						
//					}
//
//				});
//				cameraUtil.showUplaodImageDialog();
				
			//	测试图片异步加载
//				Intent intent = new Intent(MainActivity.this,ImageGridActivity.class);
//				startActivity(intent);
				
				//测试扫描二维码功能
//				Intent openCameraIntent = new Intent(MainActivity.this,CaptureActivity.class);
//				startActivityForResult(openCameraIntent,Constant.SCANNIN_GREQUEST_CODE);
				
//				测试日历控件
//				Intent intent = new Intent(MainActivity.this,CalendarDialogActivity.class);
//				startActivity(intent);
			}

		});
	}


    
}
