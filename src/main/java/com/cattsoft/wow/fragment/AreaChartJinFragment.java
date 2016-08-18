package com.cattsoft.wow.fragment;

import android.content.Intent;
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
import com.cattsoft.wow.activity.MapItemActivity;


public class AreaChartJinFragment extends Fragment {


    private View view;
    private ImageView ivTongnan;
    private TextView tvTongnanRankingNum;
    private TextView tvTongnanWorkNum;
    private ImageView ivHechuan;
    private TextView tvHechuanRankingNum;
    private TextView tvHechuanWorkNum;
    private ImageView ivTongliang;
    private TextView tvTongliangRankingNum;
    private TextView tvTongliangWorkNum;
    private ImageView ivDazu;
    private TextView tvDazuRankingNum;
    private TextView tvDazuWorkNum;
    private ImageView ivBishan;
    private TextView tvBishanRankingNum;
    private TextView tvBishanWorkNum;
    private ImageView ivRongchang;
    private TextView tvRongchangRankingNum;
    private TextView tvRongchangWorkNum;
    private ImageView ivYongchuan;
    private TextView tvYongchuanRankingNum;
    private TextView tvYongchuanWorkNum;
    private ImageView ivJiangjin;
    private TextView tvJiangjinRankingNum;
    private TextView tvJiangjinWorkNum;
    private ImageView ivCuanjiang;
    private TextView tvCuanjiangRankingNum;
    private TextView tvCuanjiangWorkNum;
    private ImageView ivWansheng;
    private TextView tvWanshengRankingNum;
    private TextView tvWanshengWorkNum;
    private ImageView iv_changshou;
    private TextView tv_changshou_ranking_num;
    private TextView tv_changshou_work_num;


    private com.alibaba.fastjson.JSONObject respsoneJson;

    String tongnanNGEOID;//得到各个地区的区域码，跳转界面要用到
    String hechuanNGEOID;
    String changshouNGEOID;
    String tongliangNGEOID;
    String dazuNGEOID;

    String rongchangNGEOID;//得到各个地区的区域码，跳转界面要用到
    String bishanNGEOID;
    String yongchuanNGEOID;
    String jiangjinNGEOID;
    String wanshengNGEOID;
    String cuanjiangNGEOID;

    FragmentManager fm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fm=getActivity().getSupportFragmentManager();
        view=inflater.inflate(R.layout.fragment_area_chart_jin, container, false);

        findViews();



        if (getArguments() != null) {
            String mParam1 = getArguments().getString("result");

            respsoneJson = JSON.parseObject(mParam1);

            setData();

        }

        setLisenter();
        return view;

    }
    public void setLisenter() {

        ivTongnan.setOnTouchListener(new View.OnTouchListener() {
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
                        //Log.e("数据",""+ redValue);
                        if (redValue == 0) {

                        } else {

                            //跳转到工单页面
                            Intent intent = new Intent(getActivity(), MapItemActivity.class);
                            intent.putExtra("geoId", tongnanNGEOID);
                            intent.putExtra("cityName", "潼南");
                            getActivity().startActivity(intent);

                        }

                        break;
                }
                return true;
            }
        });


        ivHechuan.setOnTouchListener(new View.OnTouchListener() {
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
                        //Log.e("数据",""+ redValue);
                        if (redValue == 0) {

                        } else {
                            //跳转到工单页面
                            Intent intent = new Intent(getActivity(), MapItemActivity.class);
                            intent.putExtra("geoId", hechuanNGEOID);
                            intent.putExtra("cityName", "合川");
                            getActivity().startActivity(intent);
                        }

                        break;
                }
                return true;
            }
        });

        iv_changshou.setOnTouchListener(new View.OnTouchListener() {
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
                        //Log.e("数据",""+ redValue);
                        if (redValue == 0) {

                        } else {
                            //跳转到工单页面
                            Intent intent = new Intent(getActivity(), MapItemActivity.class);
                            intent.putExtra("geoId", changshouNGEOID);
                            intent.putExtra("cityName", "长寿");
                            getActivity().startActivity(intent);
                        }

                        break;
                }
                return true;
            }
        });
        ivTongliang.setOnTouchListener(new View.OnTouchListener() {
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
                        //Log.e("数据", "" + redValue);
                        if (redValue == 0) {
                        } else {
                            //跳转到工单页面
                            Intent intent = new Intent(getActivity(), MapItemActivity.class);
                            intent.putExtra("geoId", tongliangNGEOID);
                            intent.putExtra("cityName", "铜梁");
                            getActivity().startActivity(intent);
                        }


                        break;
                }
                return true;
            }
        });
        ivDazu.setOnTouchListener(new View.OnTouchListener() {
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
                        //Log.e("数据",""+ redValue);
                        if (redValue == 0) {
                        } else {
                            //跳转到工单页面
                            Intent intent = new Intent(getActivity(), MapItemActivity.class);
                            intent.putExtra("geoId", dazuNGEOID);
                            intent.putExtra("cityName", "大足");
                            getActivity().startActivity(intent);

                        }

                        break;
                }
                return true;
            }
        });

        ivRongchang.setOnTouchListener(new View.OnTouchListener() {
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
                        //Log.e("数据",""+ redValue);
                        if (redValue == 0) {

                        } else {

                            //跳转到工单页面
                            Intent intent = new Intent(getActivity(), MapItemActivity.class);
                            intent.putExtra("geoId", rongchangNGEOID);
                            intent.putExtra("cityName", "荣昌");
                            getActivity().startActivity(intent);
                        }

                        break;
                }
                return true;
            }
        });

        ivYongchuan.setOnTouchListener(new View.OnTouchListener() {
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
                        //Log.e("数据",""+ redValue);


                        if (redValue == 0) {





                        } else {
                            //跳转到工单页面
                            Intent intent = new Intent(getActivity(), MapItemActivity.class);
                            intent.putExtra("geoId", bishanNGEOID);
                            intent.putExtra("cityName", "璧山");
                            getActivity().startActivity(intent);
                        }

                        break;
                }
                return true;
            }
        });





        ivBishan.setOnTouchListener(new View.OnTouchListener() {
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
                        //Log.e("数据",""+ redValue);
                        if (redValue == 0) {

                            if(x<20&&y<20){
                                FragmentTransaction transaction = fm.beginTransaction();
                                MapWoFragment mapWoFragment = new MapWoFragment();

                                Bundle bundle = new Bundle();
                                bundle.putString("geoId", tongliangNGEOID);

                                mapWoFragment.setArguments(bundle);
                                transaction.replace(R.id.layout, mapWoFragment);

                                transaction.commit();

                            }



                        } else {
                            //跳转到工单页面
                            Intent intent = new Intent(getActivity(), MapItemActivity.class);
                            intent.putExtra("geoId", yongchuanNGEOID);
                            intent.putExtra("cityName", "永川");
                            getActivity().startActivity(intent);
                        }

                        break;
                }
                return true;
            }
        });


        ivJiangjin.setOnTouchListener(new View.OnTouchListener() {
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
                        //Log.e("数据",""+ redValue);
                        if (redValue == 0) {


                            if(x<30&&y<30){

//                                Toast.makeText(getActivity(), "点击的是永川", Toast.LENGTH_SHORT).show();

                                FragmentTransaction transaction = fm.beginTransaction();
                                MapWoFragment mapWoFragment = new MapWoFragment();

                                Bundle bundle = new Bundle();
                                bundle.putString("geoId", yongchuanNGEOID);

                                mapWoFragment.setArguments(bundle);
                                transaction.replace(R.id.layout, mapWoFragment);

                                transaction.commit();






                            }

                        } else {

                            //跳转到工单页面
                            Intent intent = new Intent(getActivity(), MapItemActivity.class);
                            intent.putExtra("geoId", jiangjinNGEOID);
                            intent.putExtra("cityName", "江津");
                            getActivity().startActivity(intent);

                        }

                        break;
                }
                return true;
            }
        });

        ivCuanjiang.setOnTouchListener(new View.OnTouchListener() {
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
                        //Log.e("数据",""+ redValue);
                        if (redValue == 0) {
                        } else {
                            //跳转到工单页面
                            Intent intent = new Intent(getActivity(), MapItemActivity.class);
                            intent.putExtra("geoId", wanshengNGEOID);
                            intent.putExtra("cityName", "万盛");
                            getActivity().startActivity(intent);
                        }

                        break;
                }
                return true;
            }
        });


        ivWansheng.setOnTouchListener(new View.OnTouchListener() {
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
                        //Log.e("数据",""+ redValue);
                        if (redValue == 0) {
                        } else {
                            //跳转到工单页面
                            Intent intent = new Intent(getActivity(), MapItemActivity.class);
                            intent.putExtra("geoId", cuanjiangNGEOID);
                            intent.putExtra("cityName", "綦江");
                            getActivity().startActivity(intent);
                        }

                        break;
                }
                return true;
            }
        });
    }

    public void setData() {

        JSONObject tongnan = (JSONObject) respsoneJson.get("潼南");
//        String rank =  18+"";
        String rank =  tongnan.get("RANK").toString();
        String once =  tongnan.get("ONCE").toString();
        tongnanNGEOID = tongnan.get("GEO_ID").toString();
        tvTongnanRankingNum.setText(rank);
        tvTongnanWorkNum.setText(once);

        //Log.e("数据", rank);
        //Log.e("数据", once);
        JSONObject hechuan = (JSONObject) respsoneJson.get("合川");
        rank =  hechuan.get("RANK").toString();
        once =  hechuan.get("ONCE").toString();
        hechuanNGEOID = hechuan.get("GEO_ID").toString();
        tvHechuanRankingNum.setText(rank);
        tvHechuanWorkNum.setText(once);


        JSONObject changshou = (JSONObject) respsoneJson.get("长寿");
        rank =  changshou.get("RANK").toString();
        once =  changshou.get("ONCE").toString();
        changshouNGEOID = changshou.get("GEO_ID").toString();
        tv_changshou_ranking_num.setText(rank);
        tv_changshou_work_num.setText(once);

        JSONObject tongliang = (JSONObject) respsoneJson.get("铜梁");
        rank =  tongliang.get("RANK").toString();
        once =  tongliang.get("ONCE").toString();
        tongliangNGEOID = tongliang.get("GEO_ID").toString();
        tvTongliangRankingNum.setText(rank);
        tvTongliangWorkNum.setText(once);

        JSONObject dazu = (JSONObject) respsoneJson.get("大足");
        rank =  dazu.get("RANK").toString();
        once =  dazu.get("ONCE").toString();
        dazuNGEOID = dazu.get("GEO_ID").toString();
        tvDazuRankingNum.setText(rank);
        tvDazuWorkNum.setText(once);

        JSONObject rongchang = (JSONObject) respsoneJson.get("荣昌");
        rank =  rongchang.get("RANK").toString();
        once =  rongchang.get("ONCE").toString();
        rongchangNGEOID = rongchang.get("GEO_ID").toString();
        tvRongchangRankingNum.setText(rank);
        tvRongchangWorkNum.setText(once);


        JSONObject  bishan= (JSONObject) respsoneJson.get("璧山");
        rank =  bishan.get("RANK").toString();
        once =  bishan.get("ONCE").toString();
        bishanNGEOID = bishan.get("GEO_ID").toString();
        tvBishanRankingNum.setText(rank);
        tvBishanWorkNum.setText(once);

        JSONObject yongchuan = (JSONObject) respsoneJson.get("永川");
        rank =  yongchuan.get("RANK").toString();
        once =  yongchuan.get("ONCE").toString();
        yongchuanNGEOID = yongchuan.get("GEO_ID").toString();
        tvYongchuanRankingNum.setText(rank);
        tvYongchuanWorkNum.setText(once);

        JSONObject jiangjin = (JSONObject) respsoneJson.get("江津");
        rank =  jiangjin.get("RANK").toString();
        once =  jiangjin.get("ONCE").toString();
        jiangjinNGEOID = jiangjin.get("GEO_ID").toString();
        tvJiangjinRankingNum.setText(rank);
        tvJiangjinWorkNum.setText(once);


        JSONObject wansheng = (JSONObject) respsoneJson.get("万盛");
        rank =  wansheng.get("RANK").toString();
        once =  wansheng.get("ONCE").toString();
        wanshengNGEOID = wansheng.get("GEO_ID").toString();
        tvWanshengRankingNum.setText(rank);
        tvWanshengWorkNum.setText(once);

        JSONObject cuanjiang = (JSONObject) respsoneJson.get("綦江");
        rank =  cuanjiang.get("RANK").toString();
        once =  cuanjiang.get("ONCE").toString();
        cuanjiangNGEOID = cuanjiang.get("GEO_ID").toString();
        tvCuanjiangRankingNum.setText(rank);
        tvCuanjiangWorkNum.setText(once);
    }
    private void findViews() {
        ivTongnan = (ImageView)view.findViewById(R.id.iv_tongnan);
        tvTongnanRankingNum = (TextView)view.findViewById(R.id.tv_tongnan_ranking_num);
        tvTongnanWorkNum = (TextView)view.findViewById(R.id.tv_tongnan_work_num);
        ivHechuan = (ImageView)view.findViewById(R.id.iv_hechuan);
        tvHechuanRankingNum = (TextView)view.findViewById(R.id.tv_hechuan_ranking_num);
        tvHechuanWorkNum = (TextView)view.findViewById(R.id.tv_hechuan_work_num);
        ivTongliang = (ImageView)view.findViewById(R.id.iv_tongliang);
        tvTongliangRankingNum = (TextView)view.findViewById(R.id.tv_tongliang_ranking_num);
        tvTongliangWorkNum = (TextView)view.findViewById(R.id.tv_tongliang_work_num);
        ivDazu = (ImageView)view.findViewById( R.id.iv_dazu );
        tvDazuRankingNum = (TextView)view.findViewById(R.id.tv_dazu_ranking_num);
        tvDazuWorkNum = (TextView)view.findViewById(R.id.tv_dazu_work_num);
        ivBishan = (ImageView)view.findViewById(R.id.iv_bishan);
        tvBishanRankingNum = (TextView)view.findViewById(R.id.tv_bishan_ranking_num);
        tvBishanWorkNum = (TextView)view.findViewById(R.id.tv_bishan_work_num);
        ivRongchang = (ImageView)view.findViewById(R.id.iv_rongchang);
        tvRongchangRankingNum = (TextView)view.findViewById(R.id.tv_rongchang_ranking_num);
        tvRongchangWorkNum = (TextView)view.findViewById(R.id.tv_rongchang_work_num);
        ivYongchuan = (ImageView)view.findViewById(R.id.iv_yongchuan);
        tvYongchuanRankingNum = (TextView)view.findViewById(R.id.tv_yongchuan_ranking_num);
        tvYongchuanWorkNum = (TextView)view.findViewById(R.id.tv_yongchuan_work_num);
        ivJiangjin = (ImageView)view.findViewById(R.id.iv_jiangjin);
        tvJiangjinRankingNum = (TextView)view.findViewById(R.id.tv_jiangjin_ranking_num);
        tvJiangjinWorkNum = (TextView)view.findViewById(R.id.tv_jiangjin_work_num);
        ivCuanjiang = (ImageView)view.findViewById(R.id.iv_cuanjiang);
        tvCuanjiangRankingNum = (TextView)view.findViewById(R.id.tv_cuanjiang_ranking_num);
        tvCuanjiangWorkNum = (TextView)view.findViewById(R.id.tv_cuanjiang_work_num);
        ivWansheng = (ImageView)view.findViewById( R.id.iv_wansheng );
        tvWanshengRankingNum = (TextView)view.findViewById(R.id.tv_wansheng_ranking_num);
        tvWanshengWorkNum = (TextView)view.findViewById(R.id.tv_wansheng_work_num);
        iv_changshou = (ImageView)view.findViewById( R.id.iv_changshou );
        tv_changshou_ranking_num = (TextView)view.findViewById(R.id.tv_changshou_ranking_num);
        tv_changshou_work_num = (TextView)view.findViewById(R.id.tv_changshou_work_num);

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}
