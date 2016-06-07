package com.cattsoft.wow.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cattsoft.wow.R;


public class AreaChartQianFragment extends Fragment {
  private   View view;

    private ImageView ivShizhu;
    private TextView tvShizhuRankingNum;
    private TextView tvShizhuWorkNum;
    private ImageView ivQianjiang;
    private TextView tvQianjiangRankingNum;
    private TextView tvQianjiangWorkNum;
    private ImageView ivPengshui;
    private TextView tvPengshuiRankingNum;
    private TextView tvPengshuiWorkNum;
    private ImageView ivYouyang;
    private TextView tvYouyangRankingNum;
    private TextView tvYouyangWorkNum;
    private ImageView ivXiushan;
    private TextView tvXiushanRankingNum;
    private TextView tvXiushanWorkNum;


    private com.alibaba.fastjson.JSONObject respsoneJson;

    String shizhuNGEOID;//得到各个地区的区域码，跳转界面要用到
    String qianjiangNGEOID;
    String pengshuiNGEOID;
    String qiuyangNGEOID;
    String xiushanNGEOID;

    FragmentManager fm;;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fm=getActivity().getSupportFragmentManager();
     view=  inflater.inflate(R.layout.fragment_area_chart_qian, container, false);


        findViews();


        if (getArguments() != null) {
            String result = getArguments().getString("result");

            respsoneJson = JSON.parseObject(result);

            setData();

        }

        setLisenter();
        return view;


    }

    public void setLisenter() {

        ivShizhu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ImageView imageView = ((ImageView) view);
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_UP:
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();
                        if(x>bitmap.getWidth()){
                            x=bitmap.getWidth()-1;
                        }
                        if(y>bitmap.getHeight()){
                            x=bitmap.getHeight()-1;
                        }
                        if(x<0){
                            x=1;
                        }
                        if (y <= 0) {
                            y = 1;
                        }
                        int pixel = bitmap.getPixel(x, y);

                        int redValue = Color.red(pixel);
                        int blueValue = Color.blue(pixel);
                        int greenValue = Color.green(pixel);

                        if (redValue == 0) {
                        } else {
                            //跳转到工单页面
                            FragmentTransaction transaction = fm.beginTransaction();
                            MapWoFragment mapWoFragment = new MapWoFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("geoId", shizhuNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });


        ivQianjiang.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ImageView imageView = ((ImageView) view);
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_UP:
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();
                        if(x>bitmap.getWidth()){
                            x=bitmap.getWidth()-1;
                        }
                        if(y>bitmap.getHeight()){
                            x=bitmap.getHeight()-1;
                        }
                        if(x<0){
                            x=1;
                        }
                        if (y <= 0) {
                            y = 1;
                        }
                        int pixel = bitmap.getPixel(x, y);

                        int redValue = Color.red(pixel);
                        int blueValue = Color.blue(pixel);
                        int greenValue = Color.green(pixel);

                        if (redValue == 0) {
                        } else {
                            //跳转到工单页面
                            FragmentTransaction transaction = fm.beginTransaction();
                            MapWoFragment mapWoFragment = new MapWoFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("geoId", qianjiangNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });

        ivPengshui.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ImageView imageView = ((ImageView) view);
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_UP:
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();
                        if(x>bitmap.getWidth()){
                            x=bitmap.getWidth()-1;
                        }
                        if(y>bitmap.getHeight()){
                            x=bitmap.getHeight()-1;
                        }
                        if(x<0){
                            x=1;
                        }
                        if (y <= 0) {
                            y = 1;
                        }
                        int pixel = bitmap.getPixel(x, y);

                        int redValue = Color.red(pixel);
                        int blueValue = Color.blue(pixel);
                        int greenValue = Color.green(pixel);

                        if (redValue == 0) {
                        } else {
                            //跳转到工单页面
                            FragmentTransaction transaction = fm.beginTransaction();
                            MapWoFragment mapWoFragment = new MapWoFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("geoId", pengshuiNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });
        ivYouyang.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ImageView imageView = ((ImageView) view);
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_UP:
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();
                        if(x>bitmap.getWidth()){
                            x=bitmap.getWidth()-1;
                        }
                        if(y>bitmap.getHeight()){
                            x=bitmap.getHeight()-1;
                        }
                        if(x<0){
                            x=1;
                        }
                        if (y <= 0) {
                            y = 1;
                        }
                        int pixel = bitmap.getPixel(x, y);

                        int redValue = Color.red(pixel);
                        int blueValue = Color.blue(pixel);
                        int greenValue = Color.green(pixel);

                        if (redValue == 0) {
                        } else {

                            //跳转到工单页面
                            FragmentTransaction transaction = fm.beginTransaction();
                            MapWoFragment mapWoFragment = new MapWoFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("geoId", qiuyangNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });
        ivXiushan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ImageView imageView = ((ImageView) view);
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_UP:
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();
                        if(x>bitmap.getWidth()){
                            x=bitmap.getWidth()-1;
                        }
                        if(y>bitmap.getHeight()){
                            x=bitmap.getHeight()-1;
                        }
                        if(x<0){
                            x=1;
                        }
                        if (y <= 0) {
                            y = 1;
                        }
                        int pixel = bitmap.getPixel(x, y);

                        int redValue = Color.red(pixel);
                        int blueValue = Color.blue(pixel);
                        int greenValue = Color.green(pixel);

                        if (redValue == 0) {
                        } else {
                            //跳转到工单页面
                            FragmentTransaction transaction = fm.beginTransaction();
                            MapWoFragment mapWoFragment = new MapWoFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("geoId", xiushanNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });


    }


    public void setData() {

        JSONObject shizhu = (JSONObject) respsoneJson.get("石柱");
        String rank =  shizhu.get("RANK").toString();
        String once =  shizhu.get("ONCE").toString();
        shizhuNGEOID = shizhu.get("NGEOID").toString();
        tvShizhuRankingNum.setText(rank);
        tvShizhuWorkNum.setText(once);

        //Log.e("数据", rank);
        //Log.e("数据", once);
        JSONObject qianjiang = (JSONObject) respsoneJson.get("黔江");
        rank =  qianjiang.get("RANK").toString();
        once =  qianjiang.get("ONCE").toString();
        qianjiangNGEOID = qianjiang.get("NGEOID").toString();
        tvQianjiangRankingNum.setText(rank);
        tvQianjiangWorkNum.setText(once);


        JSONObject pengshui = (JSONObject) respsoneJson.get("彭水");
        rank =  pengshui.get("RANK").toString();
        once =  pengshui.get("ONCE").toString();
        pengshuiNGEOID = pengshui.get("NGEOID").toString();
        tvPengshuiRankingNum.setText(rank);
        tvPengshuiWorkNum.setText(once);

        JSONObject qiuyang = (JSONObject) respsoneJson.get("酉阳");
        rank =  qiuyang.get("RANK").toString();
        once =  qiuyang.get("ONCE").toString();
        qiuyangNGEOID = qiuyang.get("NGEOID").toString();
        tvYouyangRankingNum.setText(rank);
        tvYouyangWorkNum.setText(once);

        JSONObject xiushan = (JSONObject) respsoneJson.get("秀山");
        rank =  xiushan.get("RANK").toString();
        once =  xiushan.get("ONCE").toString();
        xiushanNGEOID = xiushan.get("NGEOID").toString();
        tvXiushanRankingNum.setText(rank);
        tvXiushanWorkNum.setText(once);

    }
    private void findViews() {
        ivShizhu = (ImageView)view.findViewById(R.id.iv_shizhu);
        tvShizhuRankingNum = (TextView)view.findViewById(R.id.tv_shizhu_ranking_num);
        tvShizhuWorkNum = (TextView)view.findViewById(R.id.tv_shizhu_work_num);
        ivQianjiang = (ImageView)view.findViewById(R.id.iv_qianjiang);
        tvQianjiangRankingNum = (TextView)view.findViewById(R.id.tv_qianjiang_ranking_num);
        tvQianjiangWorkNum = (TextView)view.findViewById( R.id.tv_qianjiang_work_num );
        ivPengshui = (ImageView)view.findViewById(R.id.iv_pengshui);
        tvPengshuiRankingNum = (TextView)view.findViewById(R.id.tv_pengshui_ranking_num);
        tvPengshuiWorkNum = (TextView)view.findViewById(R.id.tv_pengshui_work_num);
        ivYouyang = (ImageView)view.findViewById(R.id.iv_youyang);
        tvYouyangRankingNum = (TextView)view.findViewById(R.id.tv_youyang_ranking_num);
        tvYouyangWorkNum = (TextView)view.findViewById(R.id.tv_youyang_work_num);
        ivXiushan = (ImageView)view.findViewById(R.id.iv_xiushan);
        tvXiushanRankingNum = (TextView)view.findViewById(R.id.tv_xiushan_ranking_num);
        tvXiushanWorkNum = (TextView)view.findViewById( R.id.tv_xiushan_work_num );
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}
