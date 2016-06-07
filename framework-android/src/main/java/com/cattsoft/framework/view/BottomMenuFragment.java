package com.cattsoft.framework.view;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.R;
import com.cattsoft.framework.cache.CacheProcess;
import com.cattsoft.framework.util.ViewUtil;

/**
 * 详情页面底部按钮
 * A simple {@link Fragment} subclass.
 * Use the {@link BottomMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public  class BottomMenuFragment extends Fragment {

    private GridView gridView;
    private SlidingDrawer slidingDrawer;
    private LinearLayout linearLayout;

    private ImageView handleImageBtn;

    public interface MenuClickListener{

        public void menuClickListener(MenuImg menuImg);
    }

    private MenuClickListener menuClickListener;

    public MenuClickListener getMenuClickListener() {
        return menuClickListener;
    }

    public void setMenuClickListener(MenuClickListener menuClickListener) {
        this.menuClickListener = menuClickListener;
    }

    public GridView getGridView() {
        return gridView;
    }

    public void setGridView(GridView gridView) {
        this.gridView = gridView;
    }

    com.alibaba.fastjson.JSONArray menuListJsonArray = new com.alibaba.fastjson.JSONArray();


    private String woType;


    public static BottomMenuFragment newInstance(String woType){

        BottomMenuFragment fragment = new BottomMenuFragment();

        Bundle args = new Bundle();

        args.putString("woType",woType);

        fragment.setArguments(args);
        return fragment;

    }
    public BottomMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bottom_menu, container, false);
        gridView = (GridView) view.findViewById(R.id.gridview);
        slidingDrawer = (SlidingDrawer) view.findViewById(R.id.slidingdrawer);
        linearLayout = (LinearLayout) view.findViewById(R.id.content);

        handleImageBtn = (ImageView) view.findViewById(R.id.scanhandle);

        slidingDrawer.setOnDrawerScrollListener(new SlidingDrawer.OnDrawerScrollListener() {

            @Override
            public void onScrollStarted() {
            }

            @Override
            public void onScrollEnded() {
                handleImageBtn
                        .setBackgroundResource(R.drawable.detail_image_btn_toorbar_down);
            }
        });
        /**监听抽屉关闭事件*/
        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {

            @Override
            public void onDrawerClosed() {
                handleImageBtn
                        .setBackgroundResource(R.drawable.detail_image_btn_toorbar);
            }
        });


        Bundle bundle = getArguments();

        woType = bundle.getString("woType");

        initData();

        initUI();

        return view;
    }

    /**
     * 获取缓存数据
     */
    private void initData(){

        String menuStr = CacheProcess.getCacheValueInSharedPreferences(getActivity(),"operations");

        JSONObject menuJson = JSON.parseObject(menuStr);

        menuListJsonArray = menuJson.getJSONArray(woType);

    }

    /**
     * 初始化Ui
     */
    private void initUI(){

        gridView.setAdapter(new GridViewAdapter(getActivity()));

        LinearLayout contentLayout=(LinearLayout)slidingDrawer.findViewById(R.id.content);


        if(menuListJsonArray.size()<=5){

            slidingDrawer.setVisibility(View.GONE);
        }else{
            RelativeLayout.LayoutParams layp= (RelativeLayout.LayoutParams) slidingDrawer.getLayoutParams();
            int pxHeight=0;
            if(menuListJsonArray.size()-5<=5){
                pxHeight=80;//dip值

            }else if(menuListJsonArray.size()-10<=5){
                pxHeight=130;//dip
            }else{

                pxHeight=180;//dip
            }

            slidingDrawer.setVisibility(View.VISIBLE);
            layp.height= ViewUtil.dip2px(getActivity(),pxHeight);


            slidingDrawer.setLayoutParams(layp);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);

            LinearLayout layout = null;
            for(int i=5;i<menuListJsonArray.size();i++){

                JSONObject jsonObject = (JSONObject) menuListJsonArray.get(i);

                String menuName = jsonObject.getString("name");
                String imageName = jsonObject.getString("code");

                if((i+1)%5==1){//重新加布局

                    layout = new LinearLayout(getActivity());

                    layout.setLayoutParams(lp);

                    layout.setOrientation(LinearLayout.HORIZONTAL);

                    contentLayout.addView(layout);
                }

                MenuImg menuImg = initMenuImg(imageName,menuName);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                layoutParams.setMargins(ViewUtil.dip2px(getActivity(),30),0,0,0);

                layout.addView(menuImg,layoutParams);

              //  contentLayout.addView(textView);
    //            slidingDrawer.addView(layout);
            }

        }

    }

    public class GridViewAdapter extends BaseAdapter{

        private Context context;

        public GridViewAdapter(Context context){

            this.context = context;
        }

        @Override
        public int getCount() {

            return menuListJsonArray.size()>5?5:menuListJsonArray.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {


            JSONObject jsonObject = (JSONObject)menuListJsonArray.get(i);

            String menuName = jsonObject.getString("name");
            String imageName = jsonObject.getString("code");

            MenuImg menuImg = initMenuImg(imageName,menuName);

            return menuImg;

//            TextView textView = new TextView(getActivity());
//            textView.setText("菜单");
//
//            return textView;
        }
    }


    /**
     * 初始化菜单按钮
     */
    private MenuImg initMenuImg(String imageName,String menuName){

        final MenuImg menuImg = new MenuImg(getActivity());



        String packageName = getActivity().getApplicationInfo().packageName;


        final int resID = getResources().getIdentifier(imageName, "drawable",packageName);
        final int selectResId = getResources().getIdentifier(imageName+ "_pressed", "drawable", packageName );

        menuImg.setMenuImagValue(resID);
        menuImg.setMenuCodeTextValue(imageName);

        menuImg.setMenuTextValue(menuName);
        menuImg.setTag(false);

        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                boolean isSelect = (Boolean)menuImg.getTag();
//
//                if(!isSelect){
//                    menuImg.setMenuImagValue(selectResId);
//                    menuImg.setTag(true);
//                }else{
//                    menuImg.setMenuImagValue(resID);
//                    menuImg.setTag(false);
//
//                }


                if(menuClickListener!=null){

                    menuClickListener.menuClickListener(menuImg);
                }




            }
        });

        Log.e("MenuImg=======",menuImg.getHeight()+"");

        return menuImg;

    }


}
