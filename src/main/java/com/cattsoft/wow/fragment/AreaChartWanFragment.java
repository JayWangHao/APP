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

public class AreaChartWanFragment extends Fragment {


    private ImageView ivChengkou;
    private TextView tvChengkouRankingNum;
    private TextView tvChengkouWorkNum;
    private ImageView ivWuxi;
    private TextView tvWuxiRankingNum;
    private TextView tvWuxiWorkNum;
    private ImageView ivWushan;
    private TextView tvWushanRankingNum;
    private TextView tvWushanWorkNum;
    private ImageView ivYunyang;
    private TextView tvYunyangRankingNum;
    private TextView tvYunyangWorkNum;
    private ImageView ivKaixian;
    private TextView tvKaixianRankingNum;
    private TextView tvKaixianWorkNum;
    private ImageView ivFengjie;
    private TextView tvFengjieRankingNum;
    private TextView tvFengjieWorkNum;
    private ImageView ivWanzhou;
    private TextView tvWanzhouRankingNum;
    private TextView tvWanzhouWorkNum;
    private ImageView ivLiangpin;
    private TextView tvLiangpinRankingNum;
    private TextView tvLiangpinWorkNum;
    private ImageView ivZhongxian;
    private TextView tvZhongxianRankingNum;
    private TextView tvZhongxianWorkNum;

    private com.alibaba.fastjson.JSONObject respsoneJson;

    String chengkouNGEOID;//得到各个地区的区域码，跳转界面要用到
    String wuxiNGEOID;
    String kaixianNGEOID;
    String wushanNGEOID;
    String fengjieNGEOID;
    String yunyangNGEOID;
    String wanzhouNGEOID;
    String zhongxianNGEOID;
    String liangpingNGEOID;

    FragmentManager fm;


    public AreaChartWanFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fm = getActivity().getSupportFragmentManager();
        View view = inflater.inflate(R.layout.fragment_area_chart, container, false);

        findViews(view);

        if (getArguments() != null) {
            String mParam1 = getArguments().getString("result");
            respsoneJson = JSON.parseObject(mParam1);

            setData();
        }

        setLisenter();
        return view;
    }

    private void findViews(View view) {
        ivChengkou = (ImageView) view.findViewById(R.id.iv_chengkou);
        tvChengkouRankingNum = (TextView) view.findViewById(R.id.tv_chengkou_ranking_num);
        tvChengkouWorkNum = (TextView) view.findViewById(R.id.tv_chengkou_work_num);
        ivWuxi = (ImageView) view.findViewById(R.id.iv_wuxi);
        tvWuxiRankingNum = (TextView) view.findViewById(R.id.tv_wuxi_ranking_num);
        tvWuxiWorkNum = (TextView) view.findViewById(R.id.tv_wuxi_work_num);
        ivWushan = (ImageView) view.findViewById(R.id.iv_wushan);
        tvWushanRankingNum = (TextView) view.findViewById(R.id.tv_wushan_ranking_num);
        tvWushanWorkNum = (TextView) view.findViewById(R.id.tv_wushan_work_num);
        ivYunyang = (ImageView) view.findViewById(R.id.iv_yunyang);
        tvYunyangRankingNum = (TextView) view.findViewById(R.id.tv_yunyang_ranking_num);
        tvYunyangWorkNum = (TextView) view.findViewById(R.id.tv_yunyang_work_num);
        ivKaixian = (ImageView) view.findViewById(R.id.iv_kaixian);
        tvKaixianRankingNum = (TextView) view.findViewById(R.id.tv_kaixian_ranking_num);
        tvKaixianWorkNum = (TextView) view.findViewById(R.id.tv_kaixian_work_num);
        ivFengjie = (ImageView) view.findViewById(R.id.iv_fengjie);
        tvFengjieRankingNum = (TextView) view.findViewById(R.id.tv_fengjie_ranking_num);
        tvFengjieWorkNum = (TextView) view.findViewById(R.id.tv_fengjie_work_num);
        ivWanzhou = (ImageView) view.findViewById(R.id.iv_wanzhou);
        tvWanzhouRankingNum = (TextView) view.findViewById(R.id.tv_wanzhou_ranking_num);
        tvWanzhouWorkNum = (TextView) view.findViewById(R.id.tv_wanzhou_work_num);
        ivLiangpin = (ImageView) view.findViewById(R.id.iv_liangpin);
        tvLiangpinRankingNum = (TextView) view.findViewById(R.id.tv_liangpin_ranking_num);
        tvLiangpinWorkNum = (TextView) view.findViewById(R.id.tv_liangpin_work_num);
        ivZhongxian = (ImageView) view.findViewById(R.id.iv_zhongxian);
        tvZhongxianRankingNum = (TextView) view.findViewById(R.id.tv_zhongxian_ranking_num);
        tvZhongxianWorkNum = (TextView) view.findViewById(R.id.tv_zhongxian_work_num);
    }

    public void setLisenter() {

        ivChengkou.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ImageView imageView = ((ImageView) view);
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_UP:
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();
                        if (x > bitmap.getWidth()) {
                            x = bitmap.getWidth() - 1;
                        }
                        if (y > bitmap.getHeight()) {
                            x = bitmap.getHeight() - 1;
                        }
                        if (x < 0) {
                            x = 1;
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
                            bundle.putString("geoId", chengkouNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();

                            //Toast.makeText(getActivity(), "点击位置正确", Toast.LENGTH_SHORT).show();
                        }

                        break;
                }
                return true;
            }
        });


        ivWuxi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ImageView imageView = ((ImageView) view);
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_UP:
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();
                        if (x > bitmap.getWidth()) {
                            x = bitmap.getWidth() - 1;
                        }
                        if (y > bitmap.getHeight()) {
                            x = bitmap.getHeight() - 1;
                        }
                        if (x < 0) {
                            x = 1;
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
                            bundle.putString("geoId", wuxiNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });

        ivKaixian.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ImageView imageView = ((ImageView) view);
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_UP:
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();

                        if (x > bitmap.getWidth()) {
                            x = bitmap.getWidth() - 1;
                        }
                        if (y > bitmap.getHeight()) {
                            x = bitmap.getHeight() - 1;
                        }
                        if (x < 0) {
                            x = 1;
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


                           // Log.e("数据xy", "" + x + y);

                            if(x>164&&y>132){

                                FragmentTransaction transaction = fm.beginTransaction();
                                MapWoFragment mapWoFragment = new MapWoFragment();

                                Bundle bundle = new Bundle();
                                bundle.putString("geoId", yunyangNGEOID);

                                mapWoFragment.setArguments(bundle);
                                transaction.replace(R.id.layout, mapWoFragment);

                                transaction.commit();


                            }


                        } else {
                            //跳转到工单页面
                            FragmentTransaction transaction = fm.beginTransaction();
                            MapWoFragment mapWoFragment = new MapWoFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("geoId", kaixianNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });
        ivWushan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ImageView imageView = ((ImageView) view);
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_UP:
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();
                        if (x > bitmap.getWidth()) {
                            x = bitmap.getWidth() - 1;
                        }
                        if (y > bitmap.getHeight()) {
                            x = bitmap.getHeight() - 1;
                        }
                        if (x < 0) {
                            x = 1;
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
                            bundle.putString("geoId", wushanNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();

                        }

                        break;
                }
                return true;
            }
        });
        ivFengjie.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ImageView imageView = ((ImageView) view);
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_UP:
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();
                        if (x > bitmap.getWidth()) {
                            x = bitmap.getWidth() - 1;
                        }
                        if (y > bitmap.getHeight()) {
                            x = bitmap.getHeight() - 1;
                        }
                        if (x < 0) {
                            x = 1;
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
                            bundle.putString("geoId", fengjieNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });
        ivYunyang.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ImageView imageView = ((ImageView) view);
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_UP:
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();
                        if (x > bitmap.getWidth()) {
                            x = bitmap.getWidth() - 1;
                        }
                        if (y > bitmap.getHeight()) {
                            x = bitmap.getHeight() - 1;
                        }
                        if (x < 0) {
                            x = 1;
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
                            bundle.putString("geoId", yunyangNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });

        ivWanzhou.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ImageView imageView = ((ImageView) view);
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_UP:
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();
                        if (x > bitmap.getWidth()) {
                            x = bitmap.getWidth() - 1;
                        }
                        if (y > bitmap.getHeight()) {
                            x = bitmap.getHeight() - 1;
                        }
                        if (x < 0) {
                            x = 1;
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
                            bundle.putString("geoId", wanzhouNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });
        ivZhongxian.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ImageView imageView = ((ImageView) view);
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_UP:
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();
                        if (x > bitmap.getWidth()) {
                            x = bitmap.getWidth() - 1;
                        }
                        if (y > bitmap.getHeight()) {
                            x = bitmap.getHeight() - 1;
                        }
                        if (x < 0) {
                            x = 1;
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
                            bundle.putString("geoId", zhongxianNGEOID);

                            mapWoFragment.setArguments(bundle);
                            transaction.replace(R.id.layout, mapWoFragment);

                            transaction.commit();
                        }

                        break;
                }
                return true;
            }
        });
        ivLiangpin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ImageView imageView = ((ImageView) view);
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_UP:
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();
                        if (x > bitmap.getWidth()) {
                            x = bitmap.getWidth() - 1;
                        }
                        if (y > bitmap.getHeight()) {
                            x = bitmap.getHeight() - 1;
                        }
                        if (x < 0) {
                            x = 1;
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
                            bundle.putString("geoId", liangpingNGEOID);

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

        JSONObject chengkou = (JSONObject) respsoneJson.get("城口");
        String rank = chengkou.get("RANK").toString();
        String once = chengkou.get("ONCE").toString();
        chengkouNGEOID = chengkou.get("NGEOID").toString();
        tvChengkouRankingNum.setText(rank);
        tvChengkouWorkNum.setText(once);

        //Log.e("数据", rank);
        //Log.e("数据", once);
        JSONObject wuxi = (JSONObject) respsoneJson.get("巫溪");
        rank = wuxi.get("RANK").toString();
        once = wuxi.get("ONCE").toString();
        wuxiNGEOID = wuxi.get("NGEOID").toString();
        tvWuxiRankingNum.setText(rank);
        tvWuxiWorkNum.setText(once);


        JSONObject kaixian = (JSONObject) respsoneJson.get("开县");
        rank = kaixian.get("RANK").toString();
        once = kaixian.get("ONCE").toString();
        kaixianNGEOID = kaixian.get("NGEOID").toString();
        tvKaixianRankingNum.setText(rank);
        tvKaixianWorkNum.setText(once);

        JSONObject wushan = (JSONObject) respsoneJson.get("巫山");
        rank = wushan.get("RANK").toString();
        once = wushan.get("ONCE").toString();
        wushanNGEOID = wushan.get("NGEOID").toString();
        tvWushanRankingNum.setText(rank);
        tvWushanWorkNum.setText(once);

        JSONObject fengjie = (JSONObject) respsoneJson.get("奉节");
        rank = fengjie.get("RANK").toString();
        once = fengjie.get("ONCE").toString();
        fengjieNGEOID = fengjie.get("NGEOID").toString();
        tvFengjieRankingNum.setText(rank);
        tvFengjieWorkNum.setText(once);

        JSONObject yunyang = (JSONObject) respsoneJson.get("云阳");
        rank = yunyang.get("RANK").toString();
        once = yunyang.get("ONCE").toString();
        yunyangNGEOID = yunyang.get("NGEOID").toString();
        tvYunyangRankingNum.setText(rank);
        tvYunyangWorkNum.setText(once);


        JSONObject wanzhou = (JSONObject) respsoneJson.get("万州");
        rank = wanzhou.get("RANK").toString();
        once = wanzhou.get("ONCE").toString();
        wanzhouNGEOID = wanzhou.get("NGEOID").toString();
        tvWanzhouRankingNum.setText(rank);
        tvWanzhouWorkNum.setText(once);

        JSONObject zhongxian = (JSONObject) respsoneJson.get("忠县");
        rank = zhongxian.get("RANK").toString();
        once = zhongxian.get("ONCE").toString();
        zhongxianNGEOID = zhongxian.get("NGEOID").toString();
        tvZhongxianRankingNum.setText(rank);
        tvZhongxianWorkNum.setText(once);

        JSONObject liangping = (JSONObject) respsoneJson.get("梁平");
        rank = liangping.get("RANK").toString();
        once = liangping.get("ONCE").toString();
        liangpingNGEOID = liangping.get("NGEOID").toString();
        tvLiangpinRankingNum.setText(rank);
        tvLiangpinWorkNum.setText(once);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}
