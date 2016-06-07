package com.cattsoft.framework.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * 图片处理类
 * 
 * @author xueweiwei
 * 
 */
public class BitMapUtil {

	/**
	 * 将Bitmap质量压缩
	 * 
	 * @param bitmap
	 *            原图
	 * @param size
	 *            将图片压缩到sizeKB
	 * @return
	 */
	public static byte[] bmp2ByteArray(Bitmap bitmap, int size) {
		ByteArrayOutputStream baArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baArrayOutputStream);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baArrayOutputStream.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baArrayOutputStream.reset();// 重置baos即清空baos
			bitmap.compress(Bitmap.CompressFormat.JPEG, options,
					baArrayOutputStream);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 50;// 每次都减少10
		}
		try {
			return baArrayOutputStream.toByteArray();
		} finally {
			try {
				baArrayOutputStream.flush();
				baArrayOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 把byte数组转换成Bitmap
	 * 
	 * @param 字节数组数据
	 * @return bitmap数据
	 */
	public static Bitmap byteToBitmap(byte[] b) {
		return (b == null || b.length == 0) ? null : BitmapFactory
				.decodeByteArray(b, 0, b.length);
	}

	/**
	 * 将图片缩小显示，用于照片上传之后，显示在页面上的小照片
	 * 
	 * @param srcBitmap
	 *            原图
	 * @param limitWidth
	 *            要压缩的宽度，试用值130
	 * @param limitHeight
	 *            要压缩的高度，试用值130
	 * @return
	 */
	public static Bitmap cutterBitmap(Bitmap srcBitmap, int limitWidth,
			int limitHeight) {
		float width = srcBitmap.getWidth();
		float height = srcBitmap.getHeight();

		float limitScale = limitWidth / limitHeight;
		float srcScale = width / height;

		if (limitScale > srcScale) {
			// 高小了，所以要去掉多余的高度
			height = limitHeight / ((float) limitWidth / width);
		} else {
			// 宽度小了，所以要去掉多余的宽度
			width = limitWidth / ((float) limitHeight / height);
		}

		Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height,
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(srcBitmap, 0, 0, new Paint());
		return bitmap;
	}

	/**
	 * 将uri转换转换成图片的路径
	 * 
	 * @param uri
	 * @param activity
	 * @return
	 */
	public static String uriToImagePath(Uri uri, Activity activity) {

		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor actualimagecursor = activity.managedQuery(uri, proj, null, null,
				null);
		int actual_image_column_index = actualimagecursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		actualimagecursor.moveToFirst();
		String imagePath = actualimagecursor
				.getString(actual_image_column_index);
		return imagePath;
	}

	/**
	 * 图片路径转换成bitmap
	 * 
	 * @param path图片路径
	 * @return
	 */
	public static Bitmap convertToBitmap(String path) {
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 先设置为TRUE不加载到内存中，但可以得到宽和高
		options.inJustDecodeBounds = true;
		Bitmap bm = BitmapFactory.decodeFile(path , options); // 此时返回bm为空
		options.inJustDecodeBounds = false;
		// 计算缩放比
		int be = (int) (options.outHeight / (float) 200);
		if (be <= 0)
			be = 1;
		options.inSampleSize = be;
		// 这样就不会内存溢出了
		bm = BitmapFactory.decodeFile(path, options);
		return bm;
	}

}
