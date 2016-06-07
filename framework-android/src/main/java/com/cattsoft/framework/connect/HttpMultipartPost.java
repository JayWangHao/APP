package com.cattsoft.framework.connect;

import java.io.File;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;

import com.cattsoft.framework.connect.CustomMultipartEntity.ProgressListener;
import com.cattsoft.framework.pub.Constant;
/**
 * 用于向服务端发送文件，如图片、错误日志
 * @author xueweiwei
 *
 */
public class HttpMultipartPost extends AsyncTask<String, Integer, String> {

	private Context context;
	private String filePath;
	private ProgressDialog pd;
	private long totalSize;
	private String serverUrl=Constant.ServerURL;//服务器的ip端口号，如http://svr913.cattsoft.com:9961/web_mos/
	private String url;//调用服务器的方法，如tm/LcyfAction.do?method=uploadFile
    private boolean isShowProcessDialog;
	private String serverResponse = null;
	private File file;
	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public HttpMultipartPost(Context context, String filePath,String url,boolean isShowProcessDialog) {
		this.context = context;
		this.filePath = filePath;
		this.url = url;
		this.isShowProcessDialog = isShowProcessDialog;
		
		try {
			ApplicationInfo appInfo = context.getPackageManager()
			        .getApplicationInfo(context.getPackageName(),
			PackageManager.GET_META_DATA);
			
			serverUrl = appInfo.metaData.getString("serverURL");
			
		} catch (NameNotFoundException e) {
			
		}
	}

	@Override
	protected void onPreExecute() {//启动任务
		
		if(isShowProcessDialog){
			
			pd = new ProgressDialog(context);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setMessage("上传中...");
			pd.setCancelable(false);
			pd.show();
		}

	}

	@Override
	protected String doInBackground(String... params) {//与服务端交互
		
		HttpClient httpClient = Communication.createHttpClient();
		HttpContext httpContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(serverUrl+url);

		try {
			CustomMultipartEntity multipartContent = new CustomMultipartEntity(
					new ProgressListener() {
						@Override
						public void transferred(long num) {
							if(isShowProcessDialog){
								
								publishProgress((int) ((num / (float) totalSize) * 100));
							}

						}
					});

			file = new File(filePath);
			multipartContent.addPart("data", new FileBody(file));
			totalSize = multipartContent.getContentLength();

			// Send it
			httpPost.setEntity(multipartContent);
			HttpResponse response = httpClient.execute(httpPost, httpContext);
			serverResponse = EntityUtils.toString(response.getEntity());
			
			
		} catch (Exception e) {
//			if(isShowProcessDialog){
//				pd.dismiss();
//			}
		}

		return serverResponse;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {//更新进度条
		if(isShowProcessDialog){
			pd.setProgress((int) (progress[0]));
		}
	
	}

	@Override
	protected void onPostExecute(String result) {//结束任务
		System.out.println("result: " + result);
		if(isShowProcessDialog){
			pd.dismiss();
		}
//		if(!StringUtil.isBlank(serverResponse)){
//			Toast.makeText(context, "网络连接失败，文件上传失败", Toast.LENGTH_SHORT).show();
//		}else if("crashLogSuccess".equals(serverResponse)){
//			Toast.makeText(context, "程序报错，已将问题发送给开发人员！", Toast.LENGTH_SHORT).show();
//			// 退出程序
//			android.os.Process.killProcess(android.os.Process.myPid());
//			System.exit(1);
//			file.delete();//如果发送的是错误日志，并且发送成功，则删除本地的错误日志文件
//		}else{
//			Toast.makeText(context, "程序报错，已将问题发送给开发人员！", Toast.LENGTH_SHORT).show();
//			// 退出程序
//			android.os.Process.killProcess(android.os.Process.myPid());
//			System.exit(1);
//			file.delete();//如果发送的是错误日志，并且发送成功，则删除本地的错误日志文件
//		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
//		file.delete();//如果发送的是错误日志，并且发送成功，则删除本地的错误日志文件
	
	}

	@Override
	protected void onCancelled() {//取消任务
		if(isShowProcessDialog){
			pd.dismiss();
		}
	}

}
