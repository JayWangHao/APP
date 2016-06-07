package com.cattsoft.framework.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cattsoft.framework.R;
import com.cattsoft.framework.util.SDCardUtil;

import java.io.File;

/**
 * 拍照上传dialog
 * Created by admin_local on 15/6/9.
 */
public class TakePhotoDialog extends Dialog{

    private Context context;
    private View customView;
    private TextView takePhotoText;
    private TextView photoAlbumText;
    private Button cancleBtn;

    public interface OnDialogClickListener{
        void clickListener(int i,Intent intent);
    }
    private OnDialogClickListener onDialogClickListener;



    public TakePhotoDialog(Context context,OnDialogClickListener onDialogClickListener) {

        super(context,R.style.CustomDialog);

        this.context = context;

        this.onDialogClickListener = onDialogClickListener;

        customView = LayoutInflater.from(context).inflate(R.layout.select_take_photo_dialog,null);
        takePhotoText = (TextView) customView.findViewById(R.id.take_photo);
        photoAlbumText = (TextView) customView.findViewById(R.id.photo_album);
        cancleBtn = (Button) customView.findViewById(R.id.cancle_btn);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(customView);


        cancleBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                TakePhotoDialog.this.dismiss();
            }
        });

        takePhotoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TakePhotoDialog.this.dismiss();

                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 判断存储卡是否可以用，可用进行存储
                if (SDCardUtil.IsSDCardExist()) {
                    intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "mosTempImg.png")));
                }else{
                    Toast.makeText(context,"内存卡不存在无法进行拍照",Toast.LENGTH_SHORT).show();
                    return;
                }

                onDialogClickListener.clickListener(0,intentFromCapture);
            }
        });

        photoAlbumText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TakePhotoDialog.this.dismiss();

                Intent intentFromGallery = new Intent();
                intentFromGallery.setType("image/*"); // 设置文件类型
                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);

                onDialogClickListener.clickListener(1,intentFromGallery);

            }
        });


    }

    public void showDialog(){

        Window window = getWindow();

        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.CustomDialogStyle);

        setCanceledOnTouchOutside(true);

        show();

    }

}
