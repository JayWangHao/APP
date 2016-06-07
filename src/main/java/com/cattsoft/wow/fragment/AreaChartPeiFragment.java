package com.cattsoft.wow.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class AreaChartPeiFragment extends Fragment {

    private View view;
    private ImageView ivDianjiang;
    private TextView tvDianjiangRankingNum;
    private TextView tvDianjiangWorkNum;
    private ImageView ivFengdu;
    private TextView tvFengduRankingNum;
    private TextView tvFengduWorkNum;
    private ImageView ivPeiling;
    private TextView tvPeilingRankingNum;
    private TextView tvPeilingWorkNum;
    private ImageView ivWulong;
    private TextView tvWulongRankingNum;
    private TextView tvWulongWorkNum;
    private ImageView ivNanchuan;
    private TextView tvNanchuanRankingNum;
    private TextView tvNanchuanWorkNum;

    String result;

    FragmentManager fm;

    String dianjiangNGEOID;//得到各个地区的区域码，跳转界面要用到
    String fengduNGEOID;
    String peilingNGEOID;
    String wulongNGEOID;
    String nanchuanNGEOID;

    private com.alibaba.fastjson.JSONObject respsoneJson;

    public AreaChartPeiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_area_chart_fu, container, false);

        fm = getActivity().getSupportFragmentManager();

        findViews();

        if (getArguments() != null) {
            result = getArguments().getString("result");

            respsoneJson = JSON.parseObject(result);

            setData();
            //Log.e("数据", result);
        }
        setLisenter();
        return view;
    }

    public void setLisenter() {

        ivDianjiang.setOnTouchListener(new View.OnTouchListener() {
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
                            bundle.putString("geoId", dianjiangNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });


        ivFengdu.setOnTouchListener(new View.OnTouchListener() {
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
                            bundle.putString("geoId", fengduNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });

        ivPeiling.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Bitmap bitmap =   BitmapFactory.decodeResource(getResources(), R.drawable.peiling);

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
                            bundle.putString("geoId", peilingNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });
        ivWulong.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Bitmap bitmap =   BitmapFactory.decodeResource(getResources(), R.drawable.wulong);

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
                            bundle.putString("geoId", wulongNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });
        ivNanchuan.setOnTouchListener(new View.OnTouchListener() {
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
                            bundle.putString("geoId", nanchuanNGEOID);

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

        JSONObject dianjiang = (JSONObject) respsoneJson.get("垫江");
        String rank = dianjiang.get("RANK").toString();
        String once = dianjiang.get("ONCE").toString();
        dianjiangNGEOID = dianjiang.get("NGEOID").toString();
        tvDianjiangRankingNum.setText(rank);
        tvDianjiangWorkNum.setText(once);

        //Log.e("数据", rank);
        //Log.e("数据", once);
        JSONObject fengdu = (JSONObject) respsoneJson.get("丰都");
        rank = fengdu.get("RANK").toString();
        once = fengdu.get("ONCE").toString();
        fengduNGEOID = fengdu.get("NGEOID").toString();
        tvFengduRankingNum.setText(rank);
        tvFengduWorkNum.setText(once);


        JSONObject peiling = (JSONObject) respsoneJson.get("涪陵");
        rank = peiling.get("RANK").toString();
        once = peiling.get("ONCE").toString();
        peilingNGEOID = peiling.get("NGEOID").toString();
        tvPeilingRankingNum.setText(rank);
        tvPeilingWorkNum.setText(once);

        JSONObject wulong = (JSONObject) respsoneJson.get("武隆");
        rank = wulong.get("RANK").toString();
        once = wulong.get("ONCE").toString();
        wulongNGEOID = wulong.get("NGEOID").toString();
        tvWulongRankingNum.setText(rank);
        tvWulongWorkNum.setText(once);

        JSONObject nanchuan = (JSONObject) respsoneJson.get("南川");
        rank = nanchuan.get("RANK").toString();
        once = nanchuan.get("ONCE").toString();
        nanchuanNGEOID = nanchuan.get("NGEOID").toString();
        tvNanchuanRankingNum.setText(rank);
        tvNanchuanWorkNum.setText(once);

    }


    private void findViews() {
        ivDianjiang = (ImageView) view.findViewById(R.id.iv_dianjiang);
        tvDianjiangRankingNum = (TextView) view.findViewById(R.id.tv_dianjiang_ranking_num);
        tvDianjiangWorkNum = (TextView) view.findViewById(R.id.tv_dianjiang_work_num);
        ivFengdu = (ImageView) view.findViewById(R.id.iv_fengdu);
        tvFengduRankingNum = (TextView) view.findViewById(R.id.tv_fengdu_ranking_num);
        tvFengduWorkNum = (TextView) view.findViewById(R.id.tv_fengdu_work_num);
        ivPeiling = (ImageView) view.findViewById(R.id.iv_peiling);
        tvPeilingRankingNum = (TextView) view.findViewById(R.id.tv_peiling_ranking_num);
        tvPeilingWorkNum = (TextView) view.findViewById(R.id.tv_peiling_work_num);
        ivWulong = (ImageView) view.findViewById(R.id.iv_wulong);
        tvWulongRankingNum = (TextView) view.findViewById(R.id.tv_wulong_ranking_num);
        tvWulongWorkNum = (TextView) view.findViewById(R.id.tv_wulong_work_num);
        ivNanchuan = (ImageView) view.findViewById(R.id.iv_nanchuan);
        tvNanchuanRankingNum = (TextView) view.findViewById(R.id.tv_nanchuan_ranking_num);
        tvNanchuanWorkNum = (TextView) view.findViewById(R.id.tv_nanchuan_work_num);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}
