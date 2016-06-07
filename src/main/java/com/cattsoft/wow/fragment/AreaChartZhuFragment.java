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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cattsoft.wow.R;

/**
 *
 */
public class AreaChartZhuFragment extends Fragment {

    private View view;

    private ImageView rank;
    private TextView ranktext;
    private ImageView woNum;
    private TextView wotext;
    private ImageView beipeiImg;
    private TextView rankbeipeitext;
    private TextView wobeipeitext;
    private ImageView yubeiImg;
    private TextView rankyubeitext;
    private TextView woyubeitext;
    private ImageView shapinbaImg;
    private TextView rankshapinbatext;
    private TextView woshapinbatext;
    private ImageView yuzhongImg;
    private TextView rankyuzhongtext;
    private TextView woyuzhongtext;
    private ImageView jiangbeiImg;
    private TextView rankjiangbeitext;
    private TextView wojiangbeitext;
    private ImageView beixinImg;
    private TextView rankbeixintext;
    private TextView wobeixintext;
    private ImageView jiulongpoImg;
    private TextView rankjiulongpotext;
    private TextView wojiulongpotext;
    private ImageView nananImg;
    private TextView ranknanantext;
    private TextView wonanantext;
    private ImageView dadukouImg;
    private TextView rankdadukoutext;
    private TextView wodadukoutext;
    private ImageView bananImg;
    private TextView rankbanantext;
    private TextView wobanantext;


    private com.alibaba.fastjson.JSONObject respsoneJson;
    private FragmentManager fm;

    String beipeiNGEOID;//得到各个地区的区域码，跳转界面要用到
    String yubeiNGEOID;
    String shapingbaNGEOID;
    String yuzhongNGEOID;
    String beixinNGEOID;
    String jiulongpoNGEOID;
    String dadukouNGEOID;
    String nananNGEOID;

    String bananNGEOID;
    String jiangbeiNGEOID;

    public AreaChartZhuFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fm = getActivity().getSupportFragmentManager();
        view = inflater.inflate(R.layout.fragment_area_chart_zhu, container, false);

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

        beipeiImg.setOnTouchListener(new View.OnTouchListener() {
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
                            FragmentTransaction transaction = fm.beginTransaction();
                            MapWoFragment mapWoFragment = new MapWoFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("geoId", beipeiNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });


        yubeiImg.setOnTouchListener(new View.OnTouchListener() {
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
                            FragmentTransaction transaction = fm.beginTransaction();
                            MapWoFragment mapWoFragment = new MapWoFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("geoId", yubeiNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });

        shapinbaImg.setOnTouchListener(new View.OnTouchListener() {
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
                            FragmentTransaction transaction = fm.beginTransaction();
                            MapWoFragment mapWoFragment = new MapWoFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("geoId", shapingbaNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });
        yuzhongImg.setOnTouchListener(new View.OnTouchListener() {
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
                            FragmentTransaction transaction = fm.beginTransaction();
                            MapWoFragment mapWoFragment = new MapWoFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("geoId", yuzhongNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });
        jiangbeiImg.setOnTouchListener(new View.OnTouchListener() {
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
                       // Log.e("数据", "" + redValue);
                        if (redValue == 0) {
                        } else {
                            //跳转到工单页面
                            FragmentTransaction transaction = fm.beginTransaction();
                            MapWoFragment mapWoFragment = new MapWoFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("geoId", jiangbeiNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });

        beixinImg.setOnTouchListener(new View.OnTouchListener() {
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
                            FragmentTransaction transaction = fm.beginTransaction();
                            MapWoFragment mapWoFragment = new MapWoFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("geoId", beixinNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });

        jiulongpoImg.setOnTouchListener(new View.OnTouchListener() {
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
                            FragmentTransaction transaction = fm.beginTransaction();
                            MapWoFragment mapWoFragment = new MapWoFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("geoId", jiulongpoNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });


        dadukouImg.setOnTouchListener(new View.OnTouchListener() {
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
                            FragmentTransaction transaction = fm.beginTransaction();
                            MapWoFragment mapWoFragment = new MapWoFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("geoId", dadukouNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });


        nananImg.setOnTouchListener(new View.OnTouchListener() {
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
                            FragmentTransaction transaction = fm.beginTransaction();
                            MapWoFragment mapWoFragment = new MapWoFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("geoId", nananNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });

        bananImg.setOnTouchListener(new View.OnTouchListener() {
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
                            FragmentTransaction transaction = fm.beginTransaction();
                            MapWoFragment mapWoFragment = new MapWoFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("geoId", bananNGEOID);

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
        JSONObject beipei = (JSONObject) respsoneJson.get("北碚");
        String rank = beipei.get("RANK").toString();
        String once = beipei.get("ONCE").toString();
        beipeiNGEOID = beipei.get("NGEOID").toString();
        rankbeipeitext.setText(rank);
        wobeipeitext.setText(once);

        //Log.e("数据", rank);
        //Log.e("数据", once);
        JSONObject yubei = (JSONObject) respsoneJson.get("渝北");
        rank = yubei.get("RANK").toString();
        once = yubei.get("ONCE").toString();
        yubeiNGEOID = yubei.get("NGEOID").toString();
        rankyubeitext.setText(rank);
        woyubeitext.setText(once);


        JSONObject shapingba = (JSONObject) respsoneJson.get("沙坪坝");
        rank = shapingba.get("RANK").toString();
        once = shapingba.get("ONCE").toString();
        shapingbaNGEOID = shapingba.get("NGEOID").toString();
        rankshapinbatext.setText(rank);
        woshapinbatext.setText(once);

        JSONObject yuzhong = (JSONObject) respsoneJson.get("渝中");
        rank = yuzhong.get("RANK").toString();
        once = yuzhong.get("ONCE").toString();
        yuzhongNGEOID = yuzhong.get("NGEOID").toString();
        rankyuzhongtext.setText(rank);
        woyuzhongtext.setText(once);

        JSONObject beixin = (JSONObject) respsoneJson.get("北新");
        rank = beixin.get("RANK").toString();
        once = beixin.get("ONCE").toString();
        beixinNGEOID = beixin.get("NGEOID").toString();
        rankbeixintext.setText(rank);
        wobeixintext.setText(once);


        JSONObject jiulongpo = (JSONObject) respsoneJson.get("九龙坡");
        rank = jiulongpo.get("RANK").toString();
        once = jiulongpo.get("ONCE").toString();
        jiulongpoNGEOID = jiulongpo.get("NGEOID").toString();
        rankjiulongpotext.setText(rank);
        wojiulongpotext.setText(once);

        JSONObject dadukou = (JSONObject) respsoneJson.get("大渡口");
        rank = dadukou.get("RANK").toString();
        once = dadukou.get("ONCE").toString();
        dadukouNGEOID = dadukou.get("NGEOID").toString();
        rankdadukoutext.setText(rank);
        wodadukoutext.setText(once);

        JSONObject nanan = (JSONObject) respsoneJson.get("南岸");
        rank = nanan.get("RANK").toString();
        once = nanan.get("ONCE").toString();
        nananNGEOID = nanan.get("NGEOID").toString();
        ranknanantext.setText(rank);
        wonanantext.setText(once);

        JSONObject banan = (JSONObject) respsoneJson.get("巴南");
        rank = banan.get("RANK").toString();
        once = banan.get("ONCE").toString();
        bananNGEOID = banan.get("NGEOID").toString();
        rankbanantext.setText(rank);
        wobanantext.setText(once);

        JSONObject jiangbei = (JSONObject) respsoneJson.get("江北");
        rank = jiangbei.get("RANK").toString();
        once = jiangbei.get("ONCE").toString();
        jiangbeiNGEOID = jiangbei.get("NGEOID").toString();
        rankjiangbeitext.setText(rank);
        wojiangbeitext.setText(once);
    }

    private void findViews() {
        rank = (ImageView) view.findViewById(R.id.rank);
        ranktext = (TextView) view.findViewById(R.id.ranktext);
        woNum = (ImageView) view.findViewById(R.id.woNum);
        wotext = (TextView) view.findViewById(R.id.wotext);
        beipeiImg = (ImageView) view.findViewById(R.id.beipeiImg);
        rankbeipeitext = (TextView) view.findViewById(R.id.rankbeipeitext);
        wobeipeitext = (TextView) view.findViewById(R.id.wobeipeitext);
        yubeiImg = (ImageView) view.findViewById(R.id.yubeiImg);
        rankyubeitext = (TextView) view.findViewById(R.id.rankyubeitext);
        woyubeitext = (TextView) view.findViewById(R.id.woyubeitext);
        shapinbaImg = (ImageView) view.findViewById(R.id.shapinbaImg);
        rankshapinbatext = (TextView) view.findViewById(R.id.rankshapinbatext);
        woshapinbatext = (TextView) view.findViewById(R.id.woshapinbatext);
        yuzhongImg = (ImageView) view.findViewById(R.id.yuzhongImg);
        rankyuzhongtext = (TextView) view.findViewById(R.id.rankyuzhongtext);
        woyuzhongtext = (TextView) view.findViewById(R.id.woyuzhongtext);
        jiangbeiImg = (ImageView) view.findViewById(R.id.jiangbeiImg);
        rankjiangbeitext = (TextView) view.findViewById(R.id.rankjiangbeitext);
        wojiangbeitext = (TextView) view.findViewById(R.id.wojiangbeitext);
        beixinImg = (ImageView) view.findViewById(R.id.beixinImg);
        rankbeixintext = (TextView) view.findViewById(R.id.rankbeixintext);
        wobeixintext = (TextView) view.findViewById(R.id.wobeixintext);
        jiulongpoImg = (ImageView) view.findViewById(R.id.jiulongpoImg);
        rankjiulongpotext = (TextView) view.findViewById(R.id.rankjiulongpotext);
        wojiulongpotext = (TextView) view.findViewById(R.id.wojiulongpotext);
        nananImg = (ImageView) view.findViewById(R.id.nananImg);
        ranknanantext = (TextView) view.findViewById(R.id.ranknanantext);
        wonanantext = (TextView) view.findViewById(R.id.wonanantext);
        dadukouImg = (ImageView) view.findViewById(R.id.dadukouImg);
        rankdadukoutext = (TextView) view.findViewById(R.id.rankdadukoutext);
        wodadukoutext = (TextView) view.findViewById(R.id.wodadukoutext);
        bananImg = (ImageView) view.findViewById(R.id.bananImg);
        rankbanantext = (TextView) view.findViewById(R.id.rankbanantext);
        wobanantext = (TextView) view.findViewById(R.id.wobanantext);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}
