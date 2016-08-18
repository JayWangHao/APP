package com.cattsoft.wow.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.framework.view.ProgressDialog;
import com.cattsoft.wow.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase;

import java.io.IOException;
import java.util.ArrayList;
/*
统计界面
 */

public class ReportFragment extends Fragment {

    private PieChart pieChart1;
    private PieChart  pieChart2;
    private ImageView leftImg;
    private TextView today;         //今日
    private TextView week;          //本周
    private TextView month;         //本月
    private TextView recoverCountView;//已恢复
    private TextView alarmCountView;//断站数
    private TextView processCountView;//已处理
    private TextView areaCountView;//工单数

    private float alarmCount;//告警断站数
    private float recoverCount;//断站已恢复数
    private float areaCount;//故障单总数
    private float processCount;//故障单已处理数

    /**卡片头标 */
    private ArrayList<TextView> pageTitles = new ArrayList<TextView>();
    private ImageView cursor;       // 动画图片
    private int screenW;
    private int perSpacing = 0; // 每个动画图片间距
    private int currIndex = 0;// 当前页卡编号
    Animation animation = null;

    private String timeRange="1";
    private String userId;
    private String Break_Value = "1";
    private String Data_Value = "1";

    private String result;

    private  ProgressDialog progressDialog;

   private SharedPreferences sharedPreferences;
    private PieData pieData1;
    private PieData pieData2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        View view=inflater.inflate(R.layout.fragment_report2, container, false);

        initView(view);
        resterlistener();

        initPageTitles(view);
        initImageView(view);

//        progressDialog = new ProgressDialog(getActivity()); //显示进度条
//        progressDialog.showProcessDialog();
//        new initThread().start();//启动线程请求数据
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        progressDialog = new ProgressDialog(getActivity()); //显示进度条
        progressDialog.showProcessDialog();
        new initThread().start();//启动线程请求数据
    }

    private void initView(View view) {
        pieChart1=(PieChart)view.findViewById(R.id.pieChart1);
        pieChart2=(PieChart)view.findViewById(R.id.pieChart2);

        leftImg=(ImageView)view.findViewById(R.id.user_name_img);

        today = (TextView) view.findViewById(R.id.today);
        today.setTextColor(getResources().getColor(R.color.small_title_text_color));
        week = (TextView) view.findViewById(R.id.week);
        month = (TextView) view.findViewById(R.id.month);
        recoverCountView=(TextView)view.findViewById(R.id.recover_count_text);
        alarmCountView=(TextView)view.findViewById(R.id.alarm_count_text);
        processCountView=(TextView)view.findViewById(R.id.process_count_text);
        areaCountView=(TextView)view.findViewById(R.id.area_count_text);
    }
    private void resterlistener() {
        leftImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() instanceof SlidingActivityBase){
                    ((SlidingActivityBase)getActivity()).toggle();
                }
            }
        });
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeRange="1";
                today.setTextColor(getResources().getColor(R.color.small_title_text_color));
                week.setTextColor(getResources().getColor(R.color.black));
                month.setTextColor(getResources().getColor(R.color.black));
                if (currIndex == 1) {
                    moveOnetoLeft(perSpacing);      // 从2 到 1
                } else if (currIndex == 2) {
                    moveTwoToLeft(perSpacing);       // 从3 到 1
                }
                currIndex = 0;
                if(animation==null){

                }else{
                    animation.setFillAfter(true);   // True:图片停在动画结束位置
                    animation.setDuration(300);
                    cursor.startAnimation(animation);
                    Data_Value = "1";
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.showProcessDialog();
                    new initThread().start();
                }
            }
        });
        week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeRange="7";
                today.setTextColor(getResources().getColor(R.color.black));
                week.setTextColor(getResources().getColor(R.color.small_title_text_color));
                month.setTextColor(getResources().getColor(R.color.black));
                if (currIndex == 0) {
                    moveOneToRight(perSpacing);      // 从1 到 2
                } else if (currIndex == 2) {
                    moveOnetoLeft(perSpacing);       // 从 3到 2
                }
                currIndex = 1;
                animation.setFillAfter(true);   // True:图片停在动画结束位置
                animation.setDuration(300);
                cursor.startAnimation(animation);
                Data_Value = "7";
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.showProcessDialog();
                new initThread().start();
            }
        });
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeRange="30";
                today.setTextColor(getResources().getColor(R.color.black));
                week.setTextColor(getResources().getColor(R.color.black));
                month.setTextColor(getResources().getColor(R.color.small_title_text_color));
                if (currIndex == 0) {
                    moveTwoToRight(perSpacing);      // 从 1 到3
                } else if (currIndex == 1) {
                    moveOneToRight(perSpacing);      // 从 2 到3
                }
                currIndex = 2;
                animation.setFillAfter(true);   // True:图片停在动画结束位置
                animation.setDuration(300);
                cursor.startAnimation(animation);
                Data_Value = "30";
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.showProcessDialog();
                new initThread().start();
            }
        });
    }

    // 初始化页卡(上方)的标题
    private void initPageTitles(View view) {
        pageTitles.clear();
        pageTitles.add((TextView) view.findViewById(R.id.today));
        pageTitles.add((TextView) view.findViewById(R.id.week));
        pageTitles.add((TextView) view.findViewById(R.id.month));
    }

    private void initImageView(View view) {
        cursor = (ImageView) view.findViewById(R.id.cursor);
        getScreenWidth();
        Bitmap bmp = Bitmap.createBitmap(perSpacing, 5, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.drawColor(0xFFBC1E28);
        cursor.setImageBitmap(bmp);
        if("1".equals(timeRange)){
            setCursorPosition();
        }else if("7".equals(timeRange)){
            today.setTextColor(getResources().getColor(R.color.black));
            week.setTextColor(getResources().getColor(R.color.small_title_text_color));
            month.setTextColor(getResources().getColor(R.color.black));
            if (currIndex == 0) {
                moveOneToRight(perSpacing);      // 从1 到 2
            } else if (currIndex == 2) {
                moveOnetoLeft(perSpacing);       // 从 3到 2
            }
            currIndex = 1;
            animation.setFillAfter(true);   // True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }else {
            timeRange="30";
            today.setTextColor(getResources().getColor(R.color.black));
            week.setTextColor(getResources().getColor(R.color.black));
            month.setTextColor(getResources().getColor(R.color.small_title_text_color));
            if (currIndex == 0) {
                moveTwoToRight(perSpacing);      // 从 1 到3
            } else if (currIndex == 1) {
                moveOneToRight(perSpacing);      // 从 2 到3
            }
            currIndex = 2;
            animation.setFillAfter(true);   // True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }
    }

    public Object getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenW = dm.widthPixels;       // 获取屏幕宽度
        perSpacing = screenW / 3;
        return perSpacing;
    }

    //设置cursor初始位置
    private void setCursorPosition() {
        Matrix matrix = new Matrix();
        matrix.postTranslate(0, 1);
        cursor.setImageMatrix(matrix);      // 设置动画初始位置
    }

    //向左移动一个单位
    private void moveOnetoLeft(int one) {
        if (pageTitles.get(currIndex).getLeft() > one * 5 / 2) {
            animation = new TranslateAnimation(3 * one, 2 * one, 0, 0);
        } else if ((pageTitles.get(currIndex).getLeft() > one * 3 / 2)
                && (pageTitles.get(currIndex).getLeft() < one * 5 / 2)) {
            animation = new TranslateAnimation(2 * one, one, 0, 0);
        } else if ((pageTitles.get(currIndex).getLeft() > one / 2)
                && (pageTitles.get(currIndex).getLeft() < one * 3 / 2)) {
            animation = new TranslateAnimation(one, 0, 0, 0);
        } else if (pageTitles.get(currIndex).getLeft() < one / 2) {
            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) pageTitles
                    .get(0).getLayoutParams();
            params1.leftMargin = pageTitles.get(0).getLeft() + one;
            pageTitles.get(0).setLayoutParams(params1);
            animation = new TranslateAnimation(0, 0, 0, 0);
        }
    }

    //向左移动2个单位
    private void moveTwoToLeft(int one) {
        if (pageTitles.get(currIndex).getLeft() > one * 5 / 2) {
            animation = new TranslateAnimation(3 * one, one, 0, 0);
        } else if ((pageTitles.get(currIndex).getLeft() > one * 3 / 2)
                && (pageTitles.get(currIndex).getLeft() < one * 5 / 2)) {
            animation = new TranslateAnimation(2 * one, 0, 0, 0);
        }
    }

    //  向右移动1个单位
    private void moveOneToRight(int one) {
        if (pageTitles.get(currIndex).getLeft() > one * 5 / 2) {
            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) pageTitles
                    .get(0).getLayoutParams();
            params1.leftMargin = pageTitles.get(0).getLeft() - one;
            pageTitles.get(0).setLayoutParams(params1);
            animation = new TranslateAnimation(3 * one, 3 * one, 0, 0);
        } else if ((pageTitles.get(currIndex).getLeft() > one * 3 / 2)
                && (pageTitles.get(currIndex).getLeft() < one * 5 / 2)) {
            animation = new TranslateAnimation(2 * one, 3 * one, 0, 0);
        } else if ((pageTitles.get(currIndex).getLeft() > one / 2)
                && (pageTitles.get(currIndex).getLeft() < one * 3 / 2)) {
            animation = new TranslateAnimation(one, 2 * one, 0, 0);
        } else if (pageTitles.get(currIndex).getLeft() < one / 2) {
            animation = new TranslateAnimation(0, one, 0, 0);
        }
    }

    // 向右移2个单位
    private void moveTwoToRight(int one) {
        if (pageTitles.get(currIndex).getLeft() > one * 5 / 2) {
            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) pageTitles
                    .get(0).getLayoutParams();
            params1.leftMargin = pageTitles.get(0).getLeft() - one;
            pageTitles.get(0).setLayoutParams(params1);
            animation = new TranslateAnimation(3 * one, 3 * one, 0, 0);
        } else if ((pageTitles.get(currIndex).getLeft() > one * 3 / 2)
                && (pageTitles.get(currIndex).getLeft() < one * 5 / 2)) {
            animation = new TranslateAnimation(2 * one, 3 * one, 0, 0);
        } else if ((pageTitles.get(currIndex).getLeft() > one / 2)
                && (pageTitles.get(currIndex).getLeft() < one * 3 / 2)) {
            animation = new TranslateAnimation(one, 3 * one, 0, 0);
        } else if (pageTitles.get(currIndex).getLeft() < one / 2) {
            animation = new TranslateAnimation(0, 2 * one, 0, 0);
        }
    }

    /**
     * 设置显示饼状图
     * @param pieChart
     * @param pieData
     */
    private void showChart(PieChart pieChart, PieData pieData,int where) {
        pieChart.setHoleColorTransparent(true);

        pieChart.setHoleRadius(60f);  //半径
        pieChart.setTransparentCircleRadius(64f); // 半透明圈
        //pieChart.setHoleRadius(0)  //实心圆

        pieChart.setDescription("");

        // mChart.setDrawYValues(true);
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90); // 初始旋转角度

        // draws the corresponding description value into the slice
        // mChart.setDrawXValues(true);

        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true); // 可以手动旋转

        // display percentage values
        pieChart.setUsePercentValues(false);  //显示成百分比
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
//      mChart.setOnChartValueSelectedListener(this);
        // mChart.setTouchEnabled(false);

//      mChart.setOnAnimationListener(this);
        if(where==1) {
            pieChart.setCenterText((float)(Math.round((recoverCount / alarmCount)*100*10))/10+"%");//饼状图中间的文字
        }else{
            pieChart.setCenterText((float)(Math.round((processCount / areaCount)*100*10))/10+"%");//饼状图中间的文字
        }

        pieChart.setCenterTextColor(R.color.wh_blue);//圆心字体颜色
        //设置数据
        pieChart.setData(pieData);

        // undo all highlights
//      pieChart.highlightValues(null);
//      pieChart.invalidate();

        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);
        mLegend.setTextColor(R.color.wh_text);

        pieChart.animateXY(1000, 1000);  //设置动画
        // mChart.spin(2000, 0, 360);
    }

    /**
     * 在饼状图中的数据
     * @param count 分成几部分
     * @param range
     */
    private PieData getPieData(int count, float range,int where) {
        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容
        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据
        if (where==1) {
            xValues.add("已恢复");
            xValues.add("未恢复");

            float quarterly1 = recoverCount;
            float quarterly2 = alarmCount-recoverCount;
//        Log.i("TAG",quarterly1+"---"+quarterly2);
            yValues.add(new Entry(quarterly1, 0));
            yValues.add(new Entry(quarterly2, 1));
        }else{
            xValues.add("已处理");
            xValues.add("未处理");

            float quarterly1 = processCount;
            float quarterly2 =areaCount-processCount;
            Log.i("TAG",quarterly1+"---"+quarterly2);
            yValues.add(new Entry(quarterly1, 0));
            yValues.add(new Entry(quarterly2, 1));
        }

        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, ""/*显示在比例图上*/);
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离

        ArrayList<Integer> colors = new ArrayList<Integer>();

        // 饼图颜色
        colors.add(Color.rgb(30, 144, 255));//已回复/已处理颜色
//        colors.add(Color.rgb(114, 188, 223));//已回复/已处理颜色
        colors.add(Color.rgb(255, 64, 64));//未恢复颜色
//        colors.add(Color.rgb(205, 205, 205));//未恢复颜色

        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 3 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        PieData pieData = new PieData(xValues, pieDataSet);

        return pieData;
    }

    /**
     * 启动线程
     */
    private class initThread extends Thread{
        @Override
        public void run() {
            initData();
        }
    }

    /**
     * 请求数据
     */
    private void initData(){
        userId=sharedPreferences.getString("userId","");
        String url="tz/TZDeviceAction.do?method=PersonalStatistics";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TimeRange",timeRange);
        jsonObject.put("UserId",userId);

        String parameter=jsonObject.toString();

        Message msg=new Message();

        try{
            result= Communication.getPostResponseForNetManagement(url,parameter);
            Log.i("TAG", "返回的是：" + result);
            if(StringUtil.isBlank(result)){
                msg.what = 1;
                handler.sendMessage(msg);
                return;
            }
            getData(result);
            msg.what = 3;
            handler.sendMessage(msg);
            return;
        } catch (IOException e) {
            e.printStackTrace();
            msg.what = 2;
            handler.sendMessage(msg);
            return;

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    Toast.makeText(ReportFragment.super.getActivity(), "服务端返回为空！", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 2: {
                    Toast.makeText(ReportFragment.super.getActivity(),"请求服务端异常！",Toast.LENGTH_SHORT).show();
                    break;
                }
                case 3: {
                    recoverCountView.setText((int)(recoverCount)+"");
                    alarmCountView.setText((int)(alarmCount)+"");
                    processCountView.setText((int)(processCount)+"");
                    areaCountView.setText((int) (areaCount) + "");
                    showChart(pieChart1, pieData1,1);
                    showChart(pieChart2, pieData2,2);
                    break;
                }
                case 4: {
                    Toast.makeText(ReportFragment.super.getActivity(),"登录调用服务端异常！",Toast.LENGTH_SHORT).show();
                    break;
                }
                case 5: {
                    Toast.makeText(ReportFragment.super.getActivity(),result,Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            progressDialog.closeProcessDialog();

        }
    };

    public void getData(String result){
        JSONObject jsonObject= JSON.parseObject(result);
        alarmCount=Float.parseFloat(jsonObject.getString("AlarmCount"));
        recoverCount=Float.parseFloat(jsonObject.getString("RecoverCount"));
        processCount=Float.parseFloat(jsonObject.getString("ProcessCount"));
        areaCount=Float.parseFloat(jsonObject.getString("AreaCount"));
        pieData1 = getPieData(2, alarmCount, 1);
        pieData2 = getPieData(2,areaCount,2);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
