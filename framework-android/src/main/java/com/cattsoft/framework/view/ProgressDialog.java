package com.cattsoft.framework.view;


import android.app.Dialog;
import android.content.Context;

import com.cattsoft.framework.R;
/**
 * 进度条
 * @author xueweiwei
 *
 */
public class ProgressDialog {

	public Dialog mProgressDialog;//进度条
	public Context context;
	
	public ProgressDialog(Context context){
		this.context = context;
	}
	/**
	 * 显示进度条
	 */
	public void showProcessDialog() {
		mProgressDialog = new Dialog(context,
				R.style.process_dialog);//创建自定义进度条
		mProgressDialog.setContentView(R.layout.progress_dialog);//自定义进度条的内容
		mProgressDialog.setCancelable(true);
//		mProgressDialog
//				.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {
//					public void onCancel(DialogInterface dialog) {
//
//						
//					}
//				});
		mProgressDialog.show();//显示进度条
	
	}
	/**
	 * 关闭进度条
	 */
	public void closeProcessDialog() {
		if(mProgressDialog!=null) {
			mProgressDialog.dismiss();
		}
	}
	public Dialog getmProgressDialog() {
		return mProgressDialog;
	}
	public void setmProgressDialog(Dialog mProgressDialog) {
		this.mProgressDialog = mProgressDialog;
	}
}
