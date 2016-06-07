package com.cattsoft.framework.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.cattsoft.framework.R;
import com.cattsoft.framework.adapter.AboutAdapter;
import com.cattsoft.framework.base.BaseActivity;
import com.cattsoft.framework.view.TitleBarView;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tianli on 2015/7/17.
 */
public class AboutActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private TitleBarView title;
    private ListView aboutLst;
    private TextView aboutVersion;
    private AboutAdapter aboutAdapter;
    String[] data={"关于我们","使用帮助"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView( );

    }

    @Override
    protected void initView() {
        title = (TitleBarView) findViewById(R.id.title1);
        title.setTitleBar("关于", View.VISIBLE,
                View.GONE, View.GONE, false);
        title.getTitleLeftButton().setImageDrawable(getResources().
                getDrawable(R.drawable.title_left_btn_back));
        title.getTitleLeftButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        aboutVersion=(TextView)findViewById(R.id.about_version);
        try {
          String  name=getApplicationContext().getPackageManager().getPackageInfo("com.cattsoft.mos.wohandle",0).versionName;
            String versionName="版本号：V"+name;
            aboutVersion.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        aboutLst=(ListView)findViewById(R.id.about_lst);
        aboutAdapter = new AboutAdapter(this, data);
        aboutLst.setAdapter(aboutAdapter);
        aboutLst.setOnItemClickListener(this);
        aboutLst.setTextFilterEnabled(true);

    }

    @Override
    protected void registerListener() {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


    }
}
