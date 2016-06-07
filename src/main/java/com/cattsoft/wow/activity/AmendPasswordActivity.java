package com.cattsoft.wow.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.wow.R;


/**
 * 修改密码
 */
public class AmendPasswordActivity extends Activity {

    private EditText oldPassWord;
    private EditText newPassWord;
    private EditText repeatPassWord;
    private Button sure;
    private Button back;

    private String oldPassWordText;
    private String newPassWordText;
    private String repeatPassWordText;

    private String userId;
    private String result;

    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_amend_password);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        initView();
        resterlistener();

    }




    private void initView() {
        oldPassWord=(EditText)findViewById(R.id.old_password_edit_text);
        newPassWord=(EditText)findViewById(R.id.new_password_edit_text);
        repeatPassWord=(EditText)findViewById(R.id.repeat_password_edit_text);
        sure=(Button)findViewById(R.id.sure_btn);
        back=(Button)findViewById(R.id.back_btn);
    }
    private void initData() {
        oldPassWordText=oldPassWord.getText().toString();
        newPassWordText=newPassWord.getText().toString();
        repeatPassWordText=repeatPassWord.getText().toString();

    }

    private void resterlistener() {
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
                if(newPassWordText.equals(repeatPassWordText)){
                    new initThread().start();   // 线程请求数据
                }else {
                    Toast.makeText(AmendPasswordActivity.this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
                }


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private class initThread extends Thread{
        @Override
        public void run() {
            initDataThread();
        }
    }
    public void initDataThread()  {
        // 发起请求从后台获取数据
        userId=sharedPreferences.getString("userId","");
        String url="tz/TZDeviceAction.do?method=updatePWD";
        Message message=new Message();
        try {
            // 把条件数据以JSON形式存储好
            JSONObject jsonParmter=new JSONObject();
            jsonParmter.put("newpassword", newPassWordText);    //新密码
            jsonParmter.put("oldpassword", oldPassWordText);    //旧密码
            jsonParmter.put("userid",userId);                   //用户Id

            String parameter=jsonParmter.toString();
            result= Communication.getPostResponseForNetManagement(url,parameter);

            /*if(StringUtil.isExcetionInfo(result)){
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                return;
            }*/
            //处理数据，解析数据
            /*JSONObject jsonResult = JSON.parseObject(result);
            Log.i("TAG", "返回的是：" + jsonResult + "");
            rel=jsonResult.getJSONObject("error");*/
            message.what=1;
            handler.sendMessage(message);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    // 用于接收线程发送的消息
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    handleResult(result);
                    break;
                }
            }
        }
    };
    public void handleResult(String result) {

        com.alibaba.fastjson.JSONObject resultJson = com.alibaba.fastjson.JSONObject.parseObject(result);
        Log.i("TAG","返回的是："+resultJson);
        if(resultJson.containsKey("error")) {
            Toast.makeText(getApplicationContext(), "修改失败  "+resultJson.getString("error"),
                    Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "修改成功",
                    Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(AmendPasswordActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }
}
