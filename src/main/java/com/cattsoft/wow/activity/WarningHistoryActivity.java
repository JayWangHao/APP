package com.cattsoft.wow.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.wow.R;

import java.io.IOException;

/**
 * 历史告警功能
 * Created by lenovo on 2016/2/17.
 */
public class WarningHistoryActivity extends Activity{

    private ImageView back_img;         //标题栏返回按钮
    private TextView back_text;         //标题栏返回文本
    private LinearLayout mainline;      //主内容
    private int index = 0;
    com.alibaba.fastjson.JSONArray alarmsArrayList;
    private String addressId;
    private String error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_warning_history);

        Intent intent = getIntent();
        addressId = intent.getStringExtra("VCSITECODE");

        initView();
        registerListener();
        new initThread().start();   // 线程请求数据
    }

    private void initView() {
        back_img = (ImageView) findViewById(R.id.back_img);
        back_text = (TextView) findViewById(R.id.back_text);
        mainline = (LinearLayout) findViewById(R.id.warning_history_content);
    }

    private void registerListener() {
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        back_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private class initThread extends Thread {
        public void run() {
            initPageMap();
        }
    }

    // 初始化数据
    private void initPageMap() {
        // 发起请求从后台获取数据后显示出来
        String url = "tz/TZDeviceAction.do?method=getHisAlarmList";
        // 把条件数据以JSON形式存储好
        Message msg = new Message();
        try {
            JSONObject jsonParmter = new JSONObject();
            jsonParmter.put("VCSITECODE", addressId);
            jsonParmter.put("limit", 30);       //显示条数
            jsonParmter.put("page", 1);         //页码
            String parameter = jsonParmter.toString();
            String result = Communication.getPostResponseForNetManagement(url, parameter);
            // 把得到的数据变成map形式，然后再赋值
            if (StringUtil.isBlank(result)) {
                msg.what = 1;
                handler.sendMessage(msg);
                return;
            }

            //处理数据，解析数据
            JSONObject resultJson = JSON.parseObject(result);
            if(resultJson.containsKey("error")){
                error = resultJson.getString("error");
                msg.what = 2;
                handler.sendMessage(msg);
                return;
            }

            alarmsArrayList = resultJson.getJSONArray("alarms");

            msg.what = 3;
            handler.sendMessage(msg);
            return;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    // 用于接收线程发送的消息
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    Toast.makeText(getApplicationContext(), "服务端返回为空", Toast.LENGTH_LONG).show();
                    break;
                }
                case 2:{
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                    break;
                }
                case 3:{
                    handleResult(alarmsArrayList);
                    break;
                }
            }
        }
    };

    public void handleResult(JSONArray jsonArray) {
        mainline.removeAllViewsInLayout();
        mainline = (LinearLayout) findViewById(R.id.warning_history_content);
        initViewByResult(jsonArray);
    }

    /*
    *根据服务端返回的数据填充界面
    */
    private void initViewByResult(JSONArray jsonArray)  {
        try{
            JSONObject jsonObject;
            int position = 0;
            int length=jsonArray.size()-1;
            for (int i = 0; i < jsonArray.size(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                String flagEndLine="N";
                if(i==length){//判断是否是最后一个元素
                    flagEndLine="Y";
                }
                scanAddRow(jsonObject,flagEndLine,position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 手动或扫描增加一行
     */
    private void scanAddRow(JSONObject woInfoJson,String flag,int position) {

        final View itemview = View.inflate(this, R.layout.activity_warning_history_item, null);
        //ID自增
        index=index+1;
        // 给item布局添加ID
        itemview.setId(index);
        initOneRowValue(itemview, woInfoJson,position);
        mainline.addView(itemview);
    }

    /**
     * @param itemview
     * 初始化新增行数据
     */
    private void initOneRowValue(final View itemview,JSONObject woInfoJson,int position) {

        TextView wraning_detail_year = (TextView)itemview.findViewById(R.id.wraning_detail_year);
        TextView wraning_detail_month = (TextView)itemview.findViewById(R.id.wraning_detail_month);
        ImageView detail_small_img = (ImageView)itemview.findViewById(R.id.detail_small_img);
        ImageView detail_img = (ImageView)itemview.findViewById(R.id.detail_img);
        TextView wraning_detail_text = (TextView)itemview.findViewById(R.id.wraning_detail_text);
        TextView detail_begin_time = (TextView)itemview.findViewById(R.id.detail_begin_time);
        TextView detail_warning_time = (TextView)itemview.findViewById(R.id.detail_warning_time);
        TextView detail_warning_title = (TextView)itemview.findViewById(R.id.detail_warning_title);
        TextView detail_warning_level = (TextView)itemview.findViewById(R.id.detail_warning_level);

        String wraning_detail_year_text = woInfoJson.getString("timey");           //年份
        String wraning_detail_month_text = woInfoJson.getString("timem");          //月份
        String detail_begin_time_text = woInfoJson.getString("dfirsteventtime");  //开始时间
        String detail_warning_time_text = woInfoJson.getString("ctime1");         //告警时长
        String detail_warning_title_text = woInfoJson.getString("vcalmstdname");  //告警标题
        String detail_warning_level_text = woInfoJson.getString("ALARMELEVEL");   //告警级别

        wraning_detail_year.setText(wraning_detail_year_text);
        wraning_detail_month.setText(wraning_detail_month_text);
        detail_begin_time.setText(detail_begin_time_text);
        detail_warning_time.setText(detail_warning_time_text);
        detail_warning_title.setText(detail_warning_title_text);
        detail_warning_level.setText(detail_warning_level_text);

        for(position = 0; position < itemview.getId();position++) {
            if ((position + 0) % 2 == 0) {
                wraning_detail_year.setTextColor(getResources().getColor(R.color.wraning_history_text_green_color));
                wraning_detail_month.setTextColor(getResources().getColor(R.color.wraning_history_text_green_color));
                detail_small_img.setImageResource(R.drawable.warning_history_item_green);
                detail_img.setImageResource(R.drawable.warning_history_item_btn_green);
                wraning_detail_text.setTextColor(getResources().getColor(R.color.wraning_history_text_green_color));
                detail_warning_title.setTextColor(getResources().getColor(R.color.wraning_history_text_green_color));
            }
            if ((position + 1) % 2 == 0) {
                wraning_detail_year.setTextColor(getResources().getColor(R.color.wraning_history_text_red_color));
                wraning_detail_month.setTextColor(getResources().getColor(R.color.wraning_history_text_red_color));
                detail_small_img.setImageResource(R.drawable.warning_history_item_red);
                detail_img.setImageResource(R.drawable.warning_history_item_btn_red);
                wraning_detail_text.setTextColor(getResources().getColor(R.color.wraning_history_text_red_color));
                detail_warning_title.setTextColor(getResources().getColor(R.color.wraning_history_text_red_color));
            }
        }


    }





}
