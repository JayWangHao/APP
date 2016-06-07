package com.cattsoft.wow.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.widget.TextView;

import com.cattsoft.framework.base.BaseFragmentActivity;
import com.cattsoft.framework.listener.OnFragmentInteractionListener;
import com.cattsoft.framework.view.ProgressDialog;
import com.cattsoft.framework.view.TitleBarView;
import com.cattsoft.wow.R;
import com.cattsoft.wow.fragment.WoDetailFragment;
import com.cattsoft.wow.mudels.Warning;

/**
 * 在此写用途
 *
 * @version V1.0
 * @FileName: com.cattsoft.wow.activity.WoDetailActivity
 * @author: LQY
 * @date: 2016-03-09 10:03
 */
public class WoDetailActivity extends BaseFragmentActivity  implements OnFragmentInteractionListener<String> {

    private TitleBarView title;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private TextView woLocus;

    private Warning warnVo;
    private String flowId;
    private String geoId;
    private int needpage;
    private int presentpage;
    private String status;
    private String  userId;
    private ProgressDialog progressDialog;   //进度条

    private ViewStub mViewStub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wo_detail);
        title =  (TitleBarView) findViewById(R.id.title1);

        title.setTitleHeight(50);
        title.setBackgroundColor(getResources().getColor(R.color.red));
        title.setTitleBar("工单详情", View.VISIBLE, View.GONE, View.GONE, false);
        title.getTitleLeftButton().setImageDrawable(getResources().getDrawable(R.drawable.warning_order_titlebar_img_btn_back));
        title.getTitleLeftButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initData();
        initView();

    }

    @Override
    protected void initView() {
        Fragment woDetailFragment = new WoDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("flowId", flowId);
        bundle.putString("geoId", geoId);
        bundle.putString("userId", userId);
        woDetailFragment.setArguments(bundle);

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.detail_wo_frame, woDetailFragment);
        transaction.commit();

       // woLocus = (TextView) findViewById(R.id.wo_locus);
        mViewStub = (ViewStub) this.findViewById(R.id.stub);


    }

    private void initData(){

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        warnVo = (Warning) bundle.getSerializable("marnInfo");
        flowId = warnVo.getTisyn();
        geoId = warnVo.getGeoId();
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(WoDetailActivity.this);
        userId = sharedPreferences.getString("userId","");
    }

    @Override
    protected void registerListener() {

    }

    @Override
    public void onFragmentInteraction(String s) {
        View inflated = mViewStub.inflate();
        woLocus = (TextView)inflated.findViewById(R.id.wo_locus);
        woLocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(WoDetailActivity.this, MySubscriptionActivity.class);
//                startActivity(intent);


            }
        });

    }

}
