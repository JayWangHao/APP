package com.cattsoft.framework.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.cattsoft.framework.pub.Constant;
import com.cattsoft.framework.service.CameraCallBack;
import com.cattsoft.framework.template.UploadPhotoActivity;
/**
 * 拍照上传公用类
 * @author xueweiwei
 *
 */
public class CameraUtil {

	private String[] items = new String[]{"选择本地图片", "拍照"};

	private String imageFileName;//存储到本地照片的名字
	private int zoomWidth;//剪切图片的宽度
	private int zoomHight;//剪切图片的高度
	private String dialogTitle = "上传图片";
	private CameraCallBack cameraCallBack; // 声明一个回调函数
	private boolean isPhotoZoom;//是否剪切图片

	private Activity mActivity;
	private Map<String,Object> map;//mActivity传送过来的参数
	private ArrayList<Map<String,Object>> list = new ArrayList<Map<String,Object>>();//用于bundle传送Map

	/**
	 * 构造函数 拍照之后不对图片进行剪切
	 * @param activity 哪个activity调用的该方法
	 * @param map 发送图片时需要传送到服务端的参数，比如工单号(woNbr)、工区(localNetId)、订单号(soNbr)，key值是固定的
	 */
	public CameraUtil(Activity activity,Map<String,Object> map,CameraCallBack cameraCallBack) {
		this.mActivity = activity;
		imageFileName = DateUtil.getCurrentDateStr()+".jpg";
		this.cameraCallBack = cameraCallBack;
		this.map = map;
		isPhotoZoom = false;
	}

	/**
	 * 构造函数 拍照之后对图片进行剪切
	 * @param activity 哪个activity调用的该方法
	 * @param cameraCallBack 图片选择后的一个回调函数
	 * @param zoomWidth 剪切图片的宽度
	 * @param zoomHight 剪切图片的高度
	 */
	public CameraUtil(Activity activity, CameraCallBack cameraCallBack, int zoomWidth, int zoomHight) {
		this.mActivity = activity;
		this.cameraCallBack = cameraCallBack;
		this.zoomWidth = zoomWidth;
		this.zoomHight = zoomHight;
		imageFileName = DateUtil.getCurrentDateStr()+".jpg";
		isPhotoZoom = true;
	}

	/**
	 * 弹出的选择对话框（拍照还是从本地选择图片）
	 */
	public void showUplaodImageDialog() {
		new AlertDialog.Builder(mActivity).setTitle(getDialogTitle()).setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case 0 :
					{
						Intent intentFromGallery = new Intent();
						intentFromGallery.setType("image/*"); // 设置文件类型
						intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
						if(isPhotoZoom){
							//到剪切页面
							mActivity.startActivityForResult(intentFromGallery, Constant.IMAGE_REQUEST_CODE);
						}else{
							//到预览界面
							mActivity.startActivityForResult(intentFromGallery, Constant.IMAGE_PREVIEW_RESULT_CODE);
						}
						
					}break;
					case 1 :
						Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						// 判断存储卡是否可以用，可用进行存储
						if (SDCardUtil.IsSDCardExist()) {
							intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(SDCardUtil.ReadSDPath(), imageFileName)));
						}
						if(isPhotoZoom){
							//到剪切界面
							mActivity.startActivityForResult(intentFromCapture, Constant.CAMERA_REQUEST_CODE);
						}else{
							//到预览界面
							mActivity.startActivityForResult(intentFromCapture, Constant.CAMERA_PREVIEW_RESULT_CODE);
						}
						
						break;
				}
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
	}
	/*
	 * 相应结果码
	 */
 
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_CANCELED) {
			return;
		}
		// 结果码不等于取消时候
		switch (requestCode) {
			case Constant.IMAGE_REQUEST_CODE ://选择本地图片之后，进入剪切图片界面
				
				startPhotoZoom(data.getData());
				
				break;
			case Constant.CAMERA_REQUEST_CODE ://拍照之后，进入剪切图片界面
				if (SDCardUtil.IsSDCardExist()) {
					File tempFile = new File(SDCardUtil.ReadSDPath() + "/" + imageFileName);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(mActivity, "未找到存储卡，无法存储照片到本机！", Toast.LENGTH_LONG).show();
				}

				break;
			case Constant.CAMERA_PREVIEW_RESULT_CODE://拍照之后，进入预览界面
				Intent cameraPreviewIntent = new Intent(mActivity,UploadPhotoActivity.class);
				Bundle cameraBundle = new Bundle();
				list.add(map);
				cameraBundle.putSerializable("map",list);
				cameraBundle.putString("imagePath", SDCardUtil.ReadSDPath() + "/" + imageFileName);
				cameraPreviewIntent.putExtras(cameraBundle);
				mActivity.startActivityForResult(cameraPreviewIntent, Constant.UPLAOD_RESULT_REQUEST_CODE);
				break;
			case Constant.IMAGE_PREVIEW_RESULT_CODE://拍照之后，进入预览界面
				Intent imagePreviewIntent = new Intent(mActivity,UploadPhotoActivity.class);
				Bundle imageBundle = new Bundle();
				map.put("uri", data.getData());
				list.add(map);
				imageBundle.putSerializable("map",list);
				imagePreviewIntent.putExtras(imageBundle);
				mActivity.startActivityForResult(imagePreviewIntent, Constant.UPLAOD_RESULT_REQUEST_CODE);
				break;
			case Constant.CAOP_RESULT_REQUEST_CODE ://剪切图片之后，回调cropResult该方法
				if (data != null) {
					if (cameraCallBack != null) {
						cameraCallBack.cropResult(mActivity, data);
					}
				}
				break;

			case Constant.UPLAOD_RESULT_REQUEST_CODE ://预览界面点击发送图片之后，回调upLoadImageResult方法
				
				if (cameraCallBack != null) {
					cameraCallBack.upLoadImageResult(mActivity, data);
				}
				break;
		}
	}

	/**
	 * 剪切图片
	 * */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例 按照系统默认 的 5:4 的比例
		intent.putExtra("aspectX", Constant.IMAGE_CROP_WIDTH_SCALE);
		intent.putExtra("aspectY", Constant.IMAGE_CROP_HIGHT_SCALE);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", getZoomWidth());
		intent.putExtra("outputY", getZoomHight());
		intent.putExtra("return-data", true);
		mActivity.startActivityForResult(intent, Constant.CAOP_RESULT_REQUEST_CODE);// 截切图片相应结果
	}
	public int getZoomWidth() {
		return zoomWidth;
	}

	public void setZoomWidth(int zoomWidth) {
		this.zoomWidth = zoomWidth;
	}

	public int getZoomHight() {
		return zoomHight;
	}

	public void setZoomHight(int zoomHight) {
		this.zoomHight = zoomHight;
	}

	public String getDialogTitle() {
		return dialogTitle;
	}

	public void setDialogTitle(String dialogTitle) {
		this.dialogTitle = dialogTitle;
	}

	public void setCameraCallBack(CameraCallBack cameraCallBack) {
		this.cameraCallBack = cameraCallBack;
	}

}
