package com.cattsoft.framework.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.R;
import com.cattsoft.framework.cache.CacheProcess;
import com.cattsoft.framework.cache.MosApp;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.connect.RequestHeadData;
import com.cattsoft.framework.connect.RequestListener;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.framework.view.ProgressDialog;

/**
 * 版本更新功能类
 *
 * @author xueweiwei
 */

public class UpdateManager {
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* SD卡不存在，下载失败*/
    private static final int DOWNLOAD_FAULT = 3;
    /* 网络连接失败，下载失败*/
    private static final int DOWNLOAD_CONNECT_FAIL = 4;
    /*网络连接失败*/
    private static final int CONNECT_FAIL = 5;
    /*服务端出错*/
    private static final int SEVER_FAIL = 6;
    /*显示更新提示框*/
    private static final int SHOWDIALOG = 7;
    /*最新版本*/
    private static final int ISNEWVERSION = 8;

    private static final int NO_RESPONSE = 9;
    private static final int EXCEPTION = 10;
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;
    /* 是否强制更新 */
    private boolean isForced = false;
    private Context mContext = MosApp.getInstance().getApplicationContext();
    /* 更新进度条 */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;
    private String versionUrl;
    private Message m = new Message();
    private boolean isShowProcess;//是否显示请求时的进度条

    private ProgressDialog progressDialog; //请求时的进度条

    private boolean isToastMessage = false;//如果是最新版本是否提示，默认不提示，只有在设置中的检测更新才会提示

    private String versionDesc;//更新版本信息
    private String publishUrl;//apk包存放地址
    private String packageName;
    private Context context;
    public MosApp mosApp;//缓存

    public boolean isToastMessage() {
        return isToastMessage;
    }

    public void setToastMessage(boolean isToastMessage) {
        this.isToastMessage = isToastMessage;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                case DOWNLOAD_FAULT:
                    Toast.makeText(mContext, "您的SD卡已拔出，不能下载更新！",
                            Toast.LENGTH_SHORT).show();
                    break;
                case DOWNLOAD_CONNECT_FAIL:
                    Toast.makeText(mContext, "网络出错，下载失败！",
                            Toast.LENGTH_SHORT).show();
                    break;
                case CONNECT_FAIL:

                    if (isShowProcess) {
                        progressDialog.closeProcessDialog();
                    }
                    Toast.makeText(mContext, "网络连接失败，请检查网络设置！",
                            Toast.LENGTH_SHORT).show();

                    break;
                case SEVER_FAIL:
                    if (isShowProcess) {
                        progressDialog.closeProcessDialog();
                    }
                    String errorMsg = m.getData().getString("errMsg");
                    Toast.makeText(MosApp.getInstance().getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    break;
                case SHOWDIALOG:
                    if (isShowProcess) {
                        progressDialog.closeProcessDialog();
                    }
                    showNoticeDialog();
                    break;
                case ISNEWVERSION:
                    if (isShowProcess) {
                        progressDialog.closeProcessDialog();
                    }
                    if (isToastMessage) {
                        Toast.makeText(MosApp.getInstance().getApplicationContext(), "已是最新版本!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case NO_RESPONSE:

                    if (isShowProcess) {
                        progressDialog.closeProcessDialog();
                    }
                    Toast.makeText(mContext, "服务端无响应结果",
                            Toast.LENGTH_SHORT).show();

                    break;
                case EXCEPTION:
                    if (isShowProcess) {
                        progressDialog.closeProcessDialog();
                    }
                    Toast.makeText(mContext, "服务端出错",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * @param url           请求自动更新的地址
     * @param isShowProcess 请求时是否显示进度条
     * @param packageName   应用程序包名
     * @param activity      哪个activity调用改方法
     */
    public UpdateManager(String url, boolean isShowProcess, String packageName, Object
            activity) {

        this.isShowProcess = isShowProcess;

        this.packageName = packageName;

        context = (Context) activity;

    }

    public UpdateManager(boolean isShowProcess, Activity activity) {
        context = activity;
        this.isShowProcess = isShowProcess;
        this.packageName = context.getPackageName();
    }

    public Thread checkThread = new Thread(new Runnable() {

        @Override
        public void run() {


            // 获取当前软件版本
            int versionCode = getVersionCode(context);

            try {
                String versionJson = Communication.getPostResponse(versionUrl, RequestHeadData.getRequestHeadData().toString());

                if (StringUtil.isBlank(versionJson)) {

                    m.what = NO_RESPONSE;
                    mHandler.sendMessage(m);
                    return;
                }
                if (StringUtil.isExcetionInfo(versionJson)) {

                    m.what = EXCEPTION;
                    mHandler.sendMessage(m);
                    return;
                }
                JSONObject resultJson = JSONObject.parseObject(versionJson);

                JSONObject mobileHeadJSONObject = resultJson.getJSONObject("mobileHead");
                String code = mobileHeadJSONObject.getString("returnCode");
                JSONObject mobileBodyJSONObject = resultJson.getJSONObject("mobileBody");
                if ("0".equals(code)) {

                    int serviceCode = Integer.valueOf(mobileBodyJSONObject.getJSONObject("msg").getString("versionNum"));
                    versionDesc = mobileBodyJSONObject.getJSONObject("msg").getString("versionDesc");
                    publishUrl = mobileBodyJSONObject.getJSONObject("msg").getString("publishPath");
                    // 版本判断
                    if (serviceCode > versionCode) {
                        if ("Y".equals(resultJson.getString("isForce"))) {
                            isForced = true;//是否强制更新
                        }
                        m.what = SHOWDIALOG;
                        mHandler.sendMessage(m);
                    } else {

                        m.what = ISNEWVERSION;
                        mHandler.sendMessage(m);

                    }

                } else {
                    String msg = (String) mobileBodyJSONObject.get("msg");
                    Bundle bundle = new Bundle();
                    bundle.putString("errMsg", msg);
                    m.setData(bundle);
                    m.what = SEVER_FAIL;

                    mHandler.sendMessage(m);
                }
            } catch (ClientProtocolException e) {

                m.what = CONNECT_FAIL;
                mHandler.sendMessage(m);
            } catch (JSONException e) {
                m.what = CONNECT_FAIL;
                mHandler.sendMessage(m);
            } catch (IOException e) {
                m.what = CONNECT_FAIL;
                mHandler.sendMessage(m);
            }

        }

    });

    public void checkVersion(String serviceName, String methodName) {
        JSONObject obj = new JSONObject();
        Communication conn = new Communication(obj, serviceName, methodName, new CheckVersionRequestListener(), context);
        conn.setShowProcessDialog(isShowProcess);
        conn.getPostHttpContent();
    }

    private class CheckVersionRequestListener implements RequestListener {

        @Override
        public void onComplete(String result) {
            // 获取当前软件版本
            int versionCode = getVersionCode(context);

            JSONObject resultJson = JSONObject.parseObject(result);
            int serviceCode = Integer.valueOf(resultJson.getString("versionNum"));
            versionDesc = resultJson.getString("versionDesc");
            publishUrl = resultJson.getString("publishPath");
            // 版本判断
            if (serviceCode > versionCode) {
                if ("Y".equals(resultJson.getString("isForce"))) {
                    isForced = true;//是否强制更新
                }
                showNoticeDialog();
            }
        }
    }

    public void checkVersion() {

        if (isShowProcess) {

            progressDialog = new ProgressDialog(context);//该context必须是对应的activity的，而不能是整个应用程序的
            progressDialog.showProcessDialog();
        }

        checkThread.start();
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    private int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog() {
        // 构造对话框
        AlertDialog.Builder builder = new Builder(context);
        builder.setTitle(R.string.soft_update_title);
        builder.setMessage(versionDesc);
        // 更新
        builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
            }
        });
        // 稍后更新
        builder.setNegativeButton(R.string.soft_update_later, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ("Y".equals(isForced)) {
                    CacheProcess cacheProcess=new CacheProcess();
                    cacheProcess.clearCache(mosApp);
                } else {
                    dialog.dismiss();
                }

            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * 显示软件下载对话框
     */
    public void showDownloadDialog() {
        // 构造软件下载对话框
        AlertDialog.Builder builder = new Builder(context);
        builder.setTitle(R.string.soft_updating);
        builder.setCancelable(false);
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        if (!isForced) {
            // 取消更新
            builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    // 设置取消状态
                    cancelUpdate = true;
                }
            });
        }
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // 现在文件
        //downloadApk();
        new downloadApkThread().start();
    }

//	/**
//	 * 下载apk文件
//	 */
//	private void downloadApk()
//	{
//		// 启动新线程下载软件
//		new downloadApkThread().start();
//	}

    /**
     * 下载文件线程
     *
     * @author coolszy
     * @date 2012-4-26
     * @blog http://blog.92coding.com
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "Download";
                    URL url = new URL(publishUrl);
                  //  http://61.139.140.32:8022/web_mos/mos.apk
//					url = new URL("http://192.168.105.16:8080/booking/main!getResource?resource=book_android3.0.apk");
//					http://192.168.100.211:9090/booking/main!getResource?resource=book_android.apk
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Accept-Encoding", "identity");
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
                    File apkFile = new File(mSavePath, "mos");
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                } else {
                    mHandler.sendEmptyMessage(DOWNLOAD_FAULT);
                }
            } catch (MalformedURLException e) {
                mHandler.sendEmptyMessage(DOWNLOAD_CONNECT_FAIL);
                e.printStackTrace();
            } catch (IOException e) {
                mHandler.sendEmptyMessage(DOWNLOAD_CONNECT_FAIL);
                e.printStackTrace();
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    }

    ;

    /**
     * 安装APK文件
     */
    private void installApk() {
        File apkfile = new File(mSavePath, "mos");
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }

    public boolean isForced() {
        return isForced;
    }

    public void setCancelUpdate(boolean cancelUpdate) {
        this.cancelUpdate = cancelUpdate;
    }

    public void setIsForced(boolean isForced) {
        this.isForced = isForced;
    }

    public String getPublishUrl() {
        return publishUrl;
    }

    public void setPublishUrl(String publishUrl) {
        this.publishUrl = publishUrl;
    }
}
