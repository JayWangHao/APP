package com.cattsoft.framework.activity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.R;
import com.cattsoft.framework.base.BaseActivity;
import com.cattsoft.framework.cache.CacheProcess;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.util.Md5Builder;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.framework.view.EditLabelText;
import com.cattsoft.framework.view.TitleBarView;

/**
 * 登陆页面
 * @author xueweiwei
 *
 */
public class LoginActivity extends BaseActivity {
	
	
	private SharedPreferences sharedPreferences;
	private Button loginBtn;
	private EditLabelText etUserName;
	private EditLabelText etPassword;
	private CheckBox rememberPassword;
	private CheckBox autoLogin;
	private boolean isRememberPassword;
	private boolean isAutoLogin;
	private boolean isFirstLoad = true;
	private String userName;
	private String password;
	private String passwordWithMD5;
	private boolean isCiphertext = false;
	private static boolean isExit = false;// 程序退出标志位
	
	private String url;//请求url
	private com.alibaba.fastjson.JSONObject parameter;//请求参数
	
	private boolean isNeedDown = false;//是否需要下载各个模块
	
	/* 下载保存路径 */
	private String mSavePath;
	private List<Map<String,String>> apkDownUrlList;
	/* 下载中 */
	private static final String DOWNLOAD = "1";
	/* 下载结束 */
	private static final String DOWNLOAD_FINISH = "2";
	/* SD卡不存在，下载失败*/
	private static final String DOWNLOAD_FAULT = "3";
	/* 网络连接失败，下载失败*/
	private static final String DOWNLOAD_CONNECT_FAIL = "4";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		
		//导航栏初始化
		TitleBarView title =  (TitleBarView) findViewById(R.id.title1);
		title.setTitleBar(getString(R.string.app_name), View.INVISIBLE,View.INVISIBLE, View.GONE, false);
		
		Bundle bundle = this.getIntent().getExtras();
		if(bundle!=null) {
			isNeedDown = bundle.getBoolean("isNeedDown");
		}
		
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		//初始化页面控件
		initView();
		
		//加载登陆信息
		loadLoginInfo();
		
		if (isAutoLogin) {
			loginThread();
		}
		registerListener();
	}

	

	/**
	 * 登录验证并处理缓存
	 */
	public void afterLogin(String jsonStr) {
		
		JSONObject json = JSONObject.parseObject(jsonStr);
		
			if (json.get("sysuser")!=null) {//获得响应结果，登录成功                                      .                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
				
			    //存储服务端返回的信息
				CacheProcess.initCacheInSharedPreferences(this, json);
				
//				if(isNeedDown){
//					
//					apkDownUrlList = new ArrayList<Map<String,String>>();
//					
//					JSONArray menuList = json.getJSONArray("menus");
//					
//					for(int i=0;i<menuList.size();i++){
//						
//						JSONObject menu = menuList.getJSONObject(i);
//						
//						Map<String,String> map = new HashMap<String,String>();
//						
//						map.put("apkDownUrl", menu.getString("menuUrl"));
//						map.put("apkInstall", menu.getString("menuCode"));
//						
//						apkDownUrlList.add(map);
//					}
//					new downloadApk().execute();
//					
//				}else{
					
					if (isRememberPassword) {
						saveLoginInfo();
					} else {
						cleanLoginInfo();
					}
					
					startActivity(new Intent(LoginActivity.this, HomeActivity.class));
					
					finish();
//				}
			}
	}
	/**
	 * 参数1：向后台任务的执行方法传递参数的类型
       参数2：在后台任务执行过程中，要求主UI线程处理中间状态，通常是一些UI处理中传递的参数类型
       参数3：后台任务执行完返回时的参数类型
	 * @author xueweiwei
	 *
	 */
	public class downloadApk extends AsyncTask<Void,String,Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			
			for(int i=0;i<apkDownUrlList.size();i++){

				try {
					// 判断SD卡是否存在，并且是否具有读写权限
					if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
						// 获得存储卡的路径
						String sdpath = Environment.getExternalStorageDirectory() + "/";
						mSavePath = sdpath + "Download";
					//	URL url = new URL("http://10.21.1.34:7001/web/mos.apk");
						URL url = new URL(apkDownUrlList.get(i).get("apkDownUrl"));
						// 创建连接
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.connect();
						// 获取文件大小
						int length = conn.getContentLength();
						// 创建输入流
						InputStream is = conn.getInputStream();

						File file = new File(mSavePath);
						// 判断文件目录是否存在
						if (!file.exists()) {
							file.mkdir();
						}
						File apkFile = new File(mSavePath,"mosmain");
						FileOutputStream fos = new FileOutputStream(apkFile);
						int count = 0;
						// 缓存
						byte buf[] = new byte[64];
						// 写入到文件中
						do {
							int numread = is.read(buf);
							count += numread;
							
							if (numread <= 0) {
								// 下载完成
								publishProgress(DOWNLOAD_FINISH);
								break;
							}
							// 写入文件
							fos.write(buf, 0, numread);
						} while (true);// 点击取消就停止下载.
						fos.close();
						is.close();
					}else {
						publishProgress(DOWNLOAD_FAULT);
					}
				} catch (MalformedURLException e) {
					publishProgress(DOWNLOAD_CONNECT_FAIL);
					e.printStackTrace();
				} catch (IOException e) {
					publishProgress(DOWNLOAD_CONNECT_FAIL);
					e.printStackTrace();
				}
			}
			return null;
		}
		
		protected void onProgressUpdate(String...value){
			
			if(DOWNLOAD_FINISH.equals(value[0])){//下载完成
				
				// 安装文件
				installApk();
				
				//Log.e("", "root = " + haveRoot("pm install -r " +mSavePath+"/mosmain.apk" ));
				
			}else if(DOWNLOAD_FAULT.equals(value[0])){
				
				Toast.makeText(LoginActivity.this, "您的SD卡已拔出，不能下载更新！",
						Toast.LENGTH_SHORT).show();
			}else if(DOWNLOAD_CONNECT_FAIL.equals(value[0])){
				
				Toast.makeText(LoginActivity.this, "网络出错，下载失败！",
						Toast.LENGTH_SHORT).show();
			}
		}
		
		protected void onPostExecute(Void result/*参数3*/) {
			
			if (isRememberPassword) {
				saveLoginInfo();
			} else {
				cleanLoginInfo();
			}
			
			startActivity(new Intent(LoginActivity.this, FuncNavActivity.class));
			
			finish();
		}
	}
	
	protected int execRootCmdSilent(String paramString) {
        try {
            Process localProcess = Runtime.getRuntime().exec("su");
            Object localObject = localProcess.getOutputStream();
            DataOutputStream localDataOutputStream = new DataOutputStream(
                    (OutputStream) localObject);
            String str = String.valueOf(paramString);
            localObject = str + "\n";
            localDataOutputStream.writeBytes((String) localObject);
            localDataOutputStream.flush();
            localDataOutputStream.writeBytes("exit\n");
            localDataOutputStream.flush();
            localProcess.waitFor();
            int result = localProcess.exitValue();
            return (Integer) result;
        } catch (Exception localException) {
            localException.printStackTrace();
            return -1;
        }
    }

    protected boolean haveRoot(String cmd) {
        int i = execRootCmdSilent(cmd);
        Log.e("", "result = " + i);
        if (i != -1) {
            return true;
        }
        return false;
    }

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(mSavePath,"mosmain");
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		startActivity(i);
	}

	/**
	 * 加载用户名密码等登录信息
	 */
	public void loadLoginInfo() {
		
		userName = sharedPreferences.getString("username", "");
		passwordWithMD5 = sharedPreferences.getString("password", "");
		
		isRememberPassword = sharedPreferences.getBoolean("isRememberPassword",	false);
		isAutoLogin = sharedPreferences.getBoolean("isAutoLogin", false);
		isFirstLoad = sharedPreferences.getBoolean("isFirstLoad", true);
		if(isRememberPassword) {
			etUserName.setValue(userName);
			if(!StringUtil.isBlank(passwordWithMD5)) {
				isCiphertext = true;
				etPassword.setValue("*******");
			}
		}else {
			etUserName.setValue("");
			
			etPassword.setValue("");
			
		}
		
		rememberPassword.setChecked(isRememberPassword);
		autoLogin.setChecked(isAutoLogin);
	}
	/**
	 * 保存用户名密码等登录信息
	 */
	public void saveLoginInfo() {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	 
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("isRememberPassword", isRememberPassword);
		editor.putBoolean("isAutoLogin", isAutoLogin);
        editor.putString("isFirstHomeActivity", "");

		editor.commit();
	}

	public void cleanLoginInfo() {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("isRememberPassword", false);
		editor.putBoolean("isAutoLogin", false);
		editor.putBoolean("isFirstLoad", isFirstLoad);
        editor.putString("isFirstHomeActivity", "");


		editor.commit();
	}

	@Override
	protected void initView() {
		
		loginBtn = (Button) findViewById(R.id.login_btn_login);
		etUserName = (EditLabelText) findViewById(R.id.login_edit_username);
		etPassword = (EditLabelText) findViewById(R.id.login_edit_password);
		rememberPassword = (CheckBox) findViewById(R.id.login_remember_password);
		autoLogin = (CheckBox) findViewById(R.id.login_auto_login);
		//设置输入框的输入类型为密码类型
		etPassword.setValueInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_PASSWORD);
	}

	@Override
	protected void registerListener() {
		loginBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				loginThread();
			}

		});

		rememberPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							isRememberPassword = true;
						} else {
							isRememberPassword = false;
							autoLogin.setChecked(false);
						}
					}
				});

		autoLogin.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					isAutoLogin = true;
					rememberPassword.setChecked(true);
				} else {
					isAutoLogin = false;
				}
			}
		});
		
		etPassword.setOnClickListener(new EditText.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isCiphertext) {
					isCiphertext = false;
					etPassword.setValue("");
				}
			}
		});
		
		etPassword.setOnTouchListener(new EditText.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				etPassword.requestFocus();
				return false;
			}
		});
		
		etUserName.setOnClickListener(new EditText.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isCiphertext) {
					isCiphertext = false;
					etUserName.setValue("");
					etPassword.setValue("");
				}
			}
		});
		
		etUserName.setOnTouchListener(new EditText.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				etUserName.requestFocus();
				return false;
			}
		});
	}
	
	
	/**
	 * 与服务端交互，请求登陆信息
	 */
	private void loginThread() {
		
		//初始化请求数据
        url = "mobileService/login";
    	
    	parameter = new JSONObject();
    	
    	if(isFirstLoad) {
    		
    		
    		userName = etUserName.getValue().toString();
    		password = etPassword.getValue().toString();
    		passwordWithMD5 = Md5Builder.getMd5(password);   		
    		 
    		Editor editor = sharedPreferences.edit();
    		editor.putString("username", userName);
    		editor.putString("password", passwordWithMD5);
    		editor.putBoolean("isFirstLoad", false);
    		
    		editor.commit();
    	}
		
		
		if(StringUtil.isBlank(userName)&&StringUtil.isBlank(passwordWithMD5)) {
			Toast.makeText(this, "用户名和密码不能为空！", Toast.LENGTH_SHORT).show();
			return;
		}else if(StringUtil.isBlank(userName)) {
			Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
			return;
		}else if(StringUtil.isBlank(passwordWithMD5)) {
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
			return;
		}
		
		parameter.put("username", userName);
		parameter.put("password", passwordWithMD5);
		
		
//		try {
//			StringUtil.unCompress(compress(passwordWithMD5));
//		} catch (SysException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
				
		Communication communication = new Communication(url,parameter,"afterLogin",LoginActivity.this);
		communication.getPostHttpContent();
	}
	
	// 点击两次返回键退出程序
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				exit();
				return false;
			} else {
				return super.onKeyDown(keyCode, event);
			}
		}

		public void exit() {
			if (!isExit) {
				isExit = true;
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mHandler.sendEmptyMessageDelayed(0, 2000);

			} else {

				new Handler().postDelayed(new Runnable() {
					public void run() {
						finish();
						System.exit(0);
					}
				}, 700);

			}
		}

		Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				isExit = false;

			}

		};
		
		
		public  byte[] compress(String str) throws Exception {

			byte[] bys = null;

			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				GZIPOutputStream gzip = new GZIPOutputStream(out);
				gzip.write(str.getBytes("UTF-8"));
				gzip.close();
				bys = out.toByteArray();
			} catch (Exception e) {
				
			}

			return bys;

		}

}
