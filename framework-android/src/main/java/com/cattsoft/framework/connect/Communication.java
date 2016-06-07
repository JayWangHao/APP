package com.cattsoft.framework.connect;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.cache.MosApp;
import com.cattsoft.framework.exception.SysException;
import com.cattsoft.framework.pub.Constant;
import com.cattsoft.framework.util.EncryptUtil;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.framework.view.ProgressDialog;

/**
 * 用于和服务器端通信
 * 
 * @author xueweiwei
 * 
 */
public class Communication {

	private static HttpClient httpClient;
	public static HttpPost post;
	private static String serverURL = Constant.ServerURL;// ip和端口号地址
	private String methodName;// 请求完数据之后调用的方法名称
	private String url = "mobileService/execute";// 具体请求方法的相对路径
	private String parameter;// 请求参数，json字符串
	private String filePath;// 请求参数，文件路径
	private boolean isFinish = true;// 有进度条时点击取消按钮时，当前的activity是否finish
	private ProgressDialog progressDialog;
	private Message m = new Message();
	private Class<?> activity;
	private Object superObject;
	private JSONObject jsonResult;
	private boolean isWithImage = false;// 传送的数据是否有图片，默认无图片传输
	private boolean isShowProcessDialog = true;//是否显示进度条

	private SharedPreferences sharedPreferences;
	private Context context;
	
	private String serviceName;//请求服务端名称
	private String requestMethodName;//请求方法名

    private RequestListener callbackInterface;

    private RequestFailListener callbackFailInterface;

    private boolean handleException = false;//是否单独处理错误数据

	public String getServerURL() {
		return serverURL;
	}

	public void setServerURL(String serverURL) {
		Communication.serverURL = serverURL;
	}

	public boolean isFinish() {
		return isFinish;
	}

	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}

	public boolean isWithImage() {
		return isWithImage;
	}

	public void setWithImage(boolean isWithImage) {
		this.isWithImage = isWithImage;
	}
	

	public boolean isShowProcessDialog() {
		return isShowProcessDialog;
	}

	public void setShowProcessDialog(boolean isShowProcessDialog) {
		this.isShowProcessDialog = isShowProcessDialog;
	}


    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    /**
     * 设置进度条
     * @param progressDialog
     */
    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    /**
	 * 
	 * @param parameter
	 *            请求的json字符串
	 * @param serviceName
	 *            请求服务端的名称
	 * @param requestMethodName
	 *            请求方法
	 * @param methodName
	 *            服务端传来数据后，该Acitivity对数据处理的方法名
	 * @param superObject
	 *            反射的实例(Activity)，指定是哪个activity
	 */
	public Communication(JSONObject parameter, String serviceName,
			String requestMethodName, String methodName, Object superObject) {

		JSONObject requestHeadData;

		try {
			requestHeadData = RequestHeadData.getRequestHeadData();
			if (parameter != null) {
				requestHeadData.put("mobileBody", parameter);
			}
			
			JSONObject paraObject = new JSONObject();
			paraObject.put("serviceName", serviceName);
			paraObject.put("methodName", requestMethodName);
			
			requestHeadData.put("mobileParam", paraObject);
			
		} catch (JSONException e) {
			Toast.makeText(MosApp.getInstance().getApplicationContext(),
					"将请求参数写入mobileBody时出错！", Toast.LENGTH_SHORT).show();
			return;
		}
		this.methodName = methodName;
		this.superObject = superObject;
		this.activity = superObject.getClass();
		this.parameter = requestHeadData.toString();
		this.serviceName = serviceName;
		this.requestMethodName = requestMethodName;
		context = (Context) superObject;

		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		
		//获取AndroidManifest.xml中meta-data的数据（ip和端口号）
		try {
			ApplicationInfo appInfo = context.getPackageManager()
			        .getApplicationInfo(context.getPackageName(),
			PackageManager.GET_META_DATA);
			
			serverURL = appInfo.metaData.getString("serverURL");
			
		} catch (NameNotFoundException e) {
			
		}


	}

	/**
	 * 传送的数据中没有文件的构造方法，初始化一些变量值
	 * @param url
	 *            服务器的地址，直接写Action的地址，如tm/WoHandleAction.do?method=query
	 * @param parameter
	 *            请求的json字符串
	 * @param methodName
	 *            服务端传来数据后，该Acitivity对数据处理的方法名
	 * @param superObject
	 *            反射的实例(Activity)，指定是哪个activity
	 */
	public Communication(String url, JSONObject parameter, String methodName,
			Object superObject) {

		JSONObject requestHeadData;

		try {
			requestHeadData = RequestHeadData.getRequestHeadData();
			if (parameter != null) {
				requestHeadData.put("mobileBody", parameter);
			}
		} catch (JSONException e) {
			Toast.makeText(MosApp.getInstance().getApplicationContext(),
					"将请求参数写入mobileBody时出错！", Toast.LENGTH_SHORT).show();
			return;
		}
		this.methodName = methodName;
		this.superObject = superObject;
		this.activity = superObject.getClass();
		this.parameter = requestHeadData.toString();
		this.url = url;
		context = (Context) superObject;
		
		try {
			ApplicationInfo appInfo = context.getPackageManager()
			        .getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			serverURL = appInfo.metaData.getString("serverURL");
		} catch (NameNotFoundException e) {
			
		}

		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		
	}

    /**
     * 传送的数据中没有文件的构造方法，初始化一些变量值
     * @param url
     *            服务器的地址，直接写Action的地址，如tm/WoHandleAction.do?method=query
     * @param parameter
     *            请求的json字符串
     * @param callbackInterface
     *            服务端传来数据后，该Acitivity对数据处理的方法名
     * @param superObject
     *            反射的实例(Activity)，指定是哪个activity
     */
    public Communication(String url, JSONObject parameter, RequestListener callbackInterface,
                         Object superObject) {

        JSONObject requestHeadData;

        try {
            requestHeadData = RequestHeadData.getRequestHeadData();
            if (parameter != null) {
                requestHeadData.put("mobileBody", parameter);
            }
        } catch (JSONException e) {
            Toast.makeText(MosApp.getInstance().getApplicationContext(),
                    "将请求参数写入mobileBody时出错！", Toast.LENGTH_SHORT).show();
            return;
        }
        this.callbackInterface = callbackInterface;
        this.superObject = superObject;
        this.activity = superObject.getClass();
        this.parameter = requestHeadData.toString();
        this.url = url;
        context = (Context) superObject;

        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
            serverURL = appInfo.metaData.getString("serverURL");
        } catch (NameNotFoundException e) {

        }

        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

    }

    /**
     * 主要用于Fragment页面请求数据
     * @param parameter
     * @param serviceName
     * @param requestMethodName
     * @param callbackInterface
     * @param superObject
     */
    public Communication(JSONObject parameter, String serviceName,
                         String requestMethodName, RequestListener callbackInterface, Object superObject) {

        JSONObject requestHeadData;

        try {
            requestHeadData = RequestHeadData.getRequestHeadData();
            if (parameter != null) {
                requestHeadData.put("mobileBody", parameter);
            }

            JSONObject paraObject = new JSONObject();
            paraObject.put("serviceName", serviceName);
            paraObject.put("methodName", requestMethodName);

            requestHeadData.put("mobileParam", paraObject);

        } catch (JSONException e) {
            Toast.makeText(MosApp.getInstance().getApplicationContext(),
                    "将请求参数写入mobileBody时出错！", Toast.LENGTH_SHORT).show();
            return;
        }


        this.callbackInterface = callbackInterface;
        this.superObject = superObject;
        this.activity = superObject.getClass();
        this.parameter = requestHeadData.toString();
        this.serviceName = serviceName;
        this.requestMethodName = requestMethodName;
        context = (Context) superObject;

        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        //获取AndroidManifest.xml中meta-data的数据（ip和端口号）
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);

            serverURL = appInfo.metaData.getString("serverURL");

        } catch (NameNotFoundException e) {

        }


    }

	/**
	 * 传送数据中带有文件的构造方法，
	 * 
	 * @param url
	 *            服务器的地址，直接写Action的地址，如tm/WoHandleAction.do?method=query
	 * @param parameter
	 *            请求的json字符串
	 * @param filePath
	 *            要传送的文件的路径
	 * @param methodName
	 *            服务端传来数据后，该Acitivity对数据处理的方法名
	 * @param superObject
	 *            反射的实例(Activity)，指定是哪个activity
	 * 
	 */
	public Communication(String url, JSONObject parameter, String filePath,
			String methodName, Object superObject) {

		JSONObject requestHeadData;

		try {
			requestHeadData = RequestHeadData.getRequestHeadData();
			if (parameter != null) {
				requestHeadData.put("mobileBody", parameter);
			}
		} catch (JSONException e) {
			Toast.makeText(MosApp.getInstance().getApplicationContext(),
					"将请求参数写入mobileBody时出错！", Toast.LENGTH_SHORT).show();
			return;
		}
		this.isWithImage = true;
		this.methodName = methodName;
		this.superObject = superObject;
		this.activity = superObject.getClass();
		this.parameter = requestHeadData.toString();
		this.filePath = filePath;
		this.url = url;
		context = (Context) superObject;

		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);

	}


  //  public void setLoading

	/**
	 * 获取HttpClient静态对象，用于和服务器端通信
	 * 
	 * @return httpClient
	 */
	public static HttpClient createHttpClient() {
		if (httpClient == null) {
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params,
					Constant.REQUEST_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, Constant.SO_TIMEOUT);
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params,
					HTTP.DEFAULT_CONTENT_CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);
			httpClient = new DefaultHttpClient(conMgr, params);

		}
		return httpClient;

	}

	/**
	 * 以post方式提交数据
	 * 
	 * @param url
	 *            服务器的地址，直接写Action的地址，如tm/WoHandleAction.do?method=query
	 * @param parameter
	 *            拼接好的json字符串
	 * @return 服务端json字符串，当服务器端返回AppException或SysException时，得到的字符串是一个html文档
	 * @throws com.cattsoft.framework.exception.SysException
	 * @throws Exception
	 */
	public static String getPostResponse(String url, String parameter)
			throws ClientProtocolException, IOException {

		HttpClient client = createHttpClient();
		String serverUrl = serverURL + url;
		HttpPost post = new HttpPost(serverUrl);
		post.addHeader("Content-Type", "application/json");
		String result = null;
		String encoderJson = URLEncoder.encode(parameter, HTTP.UTF_8);
		StringEntity resEntity = new StringEntity(encoderJson, "UTF-8");
		post.setEntity(resEntity);
		// 获取响应的结果
		HttpResponse response = client.execute(post);
		// 获取HttpEntity
		HttpEntity respEntity = response.getEntity();
		// 获取响应的结果信息
		byte[] resultByteType = EntityUtils.toByteArray(respEntity);
		// 解压返回的字符串
		try {
			result = StringUtil.unCompress(new String(resultByteType));
		} catch (IOException e) {
			result = StringUtil.getAppException4MOS("解压或解密出现异常！");
		}
		return result;
	}


	/**
	 * 以post方式提交数据（wow网管只能查询调用此方法）
	 *
	 * @param url
	 *            服务器的地址，直接写Action的地址，如tm/WoHandleAction.do?method=query
	 * @param parameter
	 *            拼接好的json字符串
	 * @return 服务端json字符串，当服务器端返回AppException或SysException时，得到的字符串是一个html文档
	 * @throws com.cattsoft.framework.exception.SysException
	 * @throws Exception
	 */
	public static String getPostResponseForNetManagement(String url, String parameter)
			throws ClientProtocolException, IOException {
		HttpClient client = createHttpClient();
		String serverUrl = serverURL + url;
		HttpPost post = new HttpPost(serverUrl);
		post.addHeader("Content-Type", "application/json");
		String result = null;

		StringEntity resEntity = new StringEntity(parameter, "UTF-8");
		post.setEntity(resEntity);
		// 获取响应的结果
		HttpResponse response = client.execute(post);
		// 获取HttpEntity
		HttpEntity respEntity = response.getEntity();
		// 获取响应的结果信息
		byte[] resultByteType = EntityUtils.toByteArray(respEntity);
		// 解压返回的字符串
		try {
			result = StringUtil.unCompressGbk(resultByteType);
		} catch (IOException e) {
			result = StringUtil.getAppException4MOS("解压或解密出现异常！");
		}
		return result;
	}

	public static String getPostSearchWarning(String url, String parameter)
			throws ClientProtocolException, IOException {
		HttpClient client = createHttpClient();
//		String serverUrl = "http://221.5.203.219:8080/wonms2/tz/TZDeviceAction.do?method=QuerytFaultList";
		String serverUrl = "http://221.5.203.219:8080/wonms2/tz/TZDeviceAction.do?method=getQueryAlarmList";
		HttpPost post = new HttpPost(serverUrl);
		post.addHeader("Content-Type", "application/json");
		String result = null;

		StringEntity resEntity = new StringEntity(parameter, "UTF-8");
		post.setEntity(resEntity);
		// 获取响应的结果
		HttpResponse response = client.execute(post);
		// 获取HttpEntity
		HttpEntity respEntity = response.getEntity();
		// 获取响应的结果信息
		byte[] resultByteType = EntityUtils.toByteArray(respEntity);
		// 解压返回的字符串
		try {
			result = StringUtil.unCompressGbk(resultByteType);
		} catch (IOException e) {
			result = StringUtil.getAppException4MOS("解压或解密出现异常！");
		}
		return result;
	}

	public void getPostHttpContent() {

		mosThread();

	}

    public void getPostHttpContentWithHandleException(boolean isHandleException,RequestFailListener listener){

        handleException = isHandleException;

        callbackFailInterface = listener;

        mosThread();

    }
	/**
	 * 请求数据
	 */
	private void mosThread() {
		
		if(isShowProcessDialog) {
			// 开启线程的时候 显示进度条
			progressDialog = new ProgressDialog(context);// 该context必须是对应的activity的，而不能是整个应用程序的
			progressDialog.showProcessDialog();
			progressDialog.getmProgressDialog().setOnCancelListener(
					new DialogInterface.OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {
							if (isFinish) {
								Method activityMethod;
								try {
									activityMethod = activity.getDeclaredMethod(
											"finish", new Class[] { String.class });
									activityMethod.invoke(superObject, "");
								} catch (Exception e) {
									return;
								}

							}
						}

					});

		}
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				HttpClient client = createHttpClient();
				String serverUrl = serverURL + url;
				HttpPost post = new HttpPost(serverUrl);

				String result = null;
				String parameterEncrypted = null;

				try {
					
						// 数据加密（请求数据不进行压缩）
						parameterEncrypted = EncryptUtil
								.encryptThreeDESECB(parameter);

                    Log.e("请求参数：＝＝＝＝",parameter);

                    if (!isWithImage) {
						post.addHeader("Content-Type", "application/json");
						StringEntity resEntity = new StringEntity(
								parameterEncrypted, "UTF-8");
						post.setEntity(resEntity);
						// 获取响应的结果
						HttpResponse response = client.execute(post);
						// 获取HttpEntity
						HttpEntity respEntity = response.getEntity();
						// // 获取响应的结果信息
						// result = EntityUtils.toString(respEntity, "UTF-8");
						// 获取响应的结果信息
						byte[] resultByteType = EntityUtils
								.toByteArray(respEntity);
						// 解压返回的字符串
						// 解压返回的字符串
						try {
							result = StringUtil.unCompress(new String(
									resultByteType));
						} catch (IOException e) {
							// e.printStackTrace();
							m.what = Constant.DESCRYPTED_OR_DECOMPRESSED_EXCEPTION;
							baseHandler.sendMessage(m);
							return;
						}
						
						result = EncryptUtil.decryptThreeDESECB(result);

                        Log.e("返回参数：＝＝＝＝",result);
						
					} else {
						HttpContext httpContext = new BasicHttpContext();
						MultipartEntity mpEntity = new MultipartEntity();
						File file = new File(filePath);
						mpEntity.addPart("file", new FileBody(file));
						mpEntity.addPart(
								"data",
								new StringBody(
										parameterEncrypted,
										Charset.forName(HTTP.UTF_8)));
						post.setEntity(mpEntity);
						HttpResponse response = httpClient.execute(post,
								httpContext);
						result = EntityUtils.toString(response.getEntity());
					}

					if (result == null || "".equals(result)) {
						m.what = Constant.NO_RESPONSE_EXCEPTION;
						baseHandler.sendMessage(m);
					} else {
						ReturnHeadData returnHeadData = ReturnHeadData
								.parserReturnHeadFromStr(result);
						if (returnHeadData.returnCode.equals("1")) {

							Bundle bundle = new Bundle();
							bundle.putString("errorMsg", returnHeadData.msg);
							m.setData(bundle);
							m.what = Constant.SERVER_EXCEPTION;
							baseHandler.sendMessage(m);
						} else {
							Bundle bundle = new Bundle();
							bundle.putString("result", returnHeadData.msg);

							m.setData(bundle);
							m.what = Constant.RETURN_DATA_SUCCESS;
							baseHandler.sendMessage(m);
						}
					}

				} catch (ClientProtocolException e) {
					m.what = Constant.NETWORK_CONNECTION_EXCEPTION;
					baseHandler.sendMessage(m);
				} catch (IOException e) {
					e.printStackTrace();
					m.what = Constant.NETWORK_CONNECTION_EXCEPTION;
					baseHandler.sendMessage(m);
				} catch (SysException e) {
					e.printStackTrace();
					m.what = Constant.DESCRYPTED_OR_DECOMPRESSED_EXCEPTION;
					baseHandler.sendMessage(m);
				}
			}

		});
		thread.start();
	}

	// 消息发送
	public Handler baseHandler = new Handler() {
		@SuppressLint("ParserError")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case Constant.JSON_ANALYZE_EXCEPTION: {// 处理异常信息

                if(handleException&&callbackFailInterface!=null){

                    callbackFailInterface.failure("解析服务端返回json数据错误");

                    return;
                }
				Toast.makeText(MosApp.getInstance().getApplicationContext(),
						"解析服务端返回json数据错误", Toast.LENGTH_SHORT).show();
				break;
			}
			case Constant.NETWORK_CONNECTION_EXCEPTION: {// 处理异常信息

                if(handleException&&callbackFailInterface!=null){

                    callbackFailInterface.failure("网络连接异常，请检查网络设置！");

					Toast.makeText(MosApp.getInstance().getApplicationContext(),
							"网络连接异常，请检查网络设置！", Toast.LENGTH_SHORT).show();

					if(isShowProcessDialog) {
						// 关闭进度条
						progressDialog.closeProcessDialog();
					}

                    return;
                }

				Toast.makeText(MosApp.getInstance().getApplicationContext(),
						"网络连接异常，请检查网络设置！", Toast.LENGTH_LONG).show();
				break;
			}
			case Constant.DESCRYPTED_OR_DECOMPRESSED_EXCEPTION: {// 处理异常信息

                if(handleException&&callbackFailInterface!=null){

                    callbackFailInterface.failure("解压或解密异常");

                    return;
                }

				Toast.makeText(MosApp.getInstance().getApplicationContext(),
						"解压或解密异常", Toast.LENGTH_SHORT).show();
				break;
			}
			case Constant.RETURN_DATA_SUCCESS: {// 服务器成功返回数据
				try {

                    if(callbackInterface != null){
                        String result = m.getData().getString("result");
                        callbackInterface.onComplete(result);
                    }

					if (!StringUtil.isBlank(methodName)) {
                        Method activityMethod = activity.getDeclaredMethod(
                                methodName, new Class[] { String.class });
                        String result = m.getData().getString("result");
                        activityMethod.invoke(superObject, result);
                    }
				} catch (Exception e) {
			        e.printStackTrace();


                    if(handleException&&callbackFailInterface!=null){

                        callbackFailInterface.failure("解析返回的结果时出现json解析异常");

                        return;
                    }

					Toast.makeText(
							MosApp.getInstance().getApplicationContext(),
							"解析返回的结果时出现json解析异常", Toast.LENGTH_SHORT).show();
				}
				break;
			}
			case Constant.NO_RESPONSE_EXCEPTION: {// 未获得服务器响应结果

                if(handleException&&callbackFailInterface!=null){

                    callbackFailInterface.failure("未获得服务器响应结果!");

                    return;
                }
				Toast.makeText(MosApp.getInstance().getApplicationContext(),
						"未获得服务器响应结果!", Toast.LENGTH_SHORT).show();
				break;
			}
			case Constant.SERVER_EXCEPTION: {// 服务端结果错误


                String errorMsg = m.getData().getString("errorMsg");

                if(handleException&&callbackFailInterface!=null){

                    callbackFailInterface.failure(errorMsg);

                    return;
                }

                if(!"weatherHandlerService".equals(serviceName)){//天气接口，有的省份部署在内网，获取不到百度天气接口

                    Toast.makeText(MosApp.getInstance().getApplicationContext(),
                            errorMsg, Toast.LENGTH_SHORT).show();
                }

				break;
			}
			}
			if(isShowProcessDialog) {
				// 关闭进度条
				progressDialog.closeProcessDialog();
			}
			
		}
	};

}
