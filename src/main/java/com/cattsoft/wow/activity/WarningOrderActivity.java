package com.cattsoft.wow.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.cattsoft.framework.base.BaseActivity;
import com.cattsoft.framework.view.TitleBarView;
import com.cattsoft.wow.R;


/**
 * 告警订阅
 * Created by lenovo on 2016/2/18.
 */
public class WarningOrderActivity extends BaseActivity {


    private CheckBox first_level_box;       //一级告警选中框
    private CheckBox second_level_box;
    private CheckBox third_level_box;
    private CheckBox fourth_level_box;
    private CheckBox flash_warning_box;      //闪速告警选中框
    private CheckBox avtivity_state_box;     //活动告警选中框
    private CheckBox recover_state_box;     //恢复告警选中框
    private CheckBox all_state_box;         //所有告警选中框

    private Button my_order_ok_btn;          //确定按钮
    private StringBuilder level_value = new StringBuilder();     //告警级别
    private String warning_level_value;
    private String warning_state_value;

    private boolean first_level_checked = false;
    private boolean second_level_checked = false;
    private boolean third_level_checked = false;
    private boolean fourth_level_checked = false;
    private boolean flash_checked = false;              //闪烁告警的标识
    private boolean avtivity_state_checked = false;
    private boolean recover_state_checked = false;
    private boolean all_state_checked = false;

    private SharedPreferences sharedPreferences;
    private TitleBarView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_order);
        title = (TitleBarView) findViewById(R.id.title1);
        title.setTitleHeight(50);
        title.setBackgroundColor(getResources().getColor(R.color.red));
        title.setTitleBar("我的订阅", View.VISIBLE, View.GONE, View.GONE, false);
        title.getTitleLeftButton().setImageDrawable(getResources().getDrawable(R.drawable.warning_order_titlebar_img_btn_back));
        title.getTitleLeftButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        initView();
        getData();                       //从缓存中拿出数据，判断显示的标识
        registerListener();
    }

    protected void initView() {

        first_level_box = (CheckBox) findViewById(R.id.first_level_check);
        second_level_box = (CheckBox) findViewById(R.id.second_level_check);
        third_level_box = (CheckBox) findViewById(R.id.third_level_check);
        fourth_level_box = (CheckBox) findViewById(R.id.fourth_level_check);
        flash_warning_box = (CheckBox) findViewById(R.id.flash_warning_check);
        avtivity_state_box = (CheckBox) findViewById(R.id.avtivity_state_check);
        recover_state_box = (CheckBox) findViewById(R.id.recover_state_check);
        all_state_box = (CheckBox) findViewById(R.id.all_state_check);

        my_order_ok_btn = (Button) findViewById(R.id.my_order_ok_btn);  //确定按钮
    }


    private void getData(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        first_level_checked = sharedPreferences.getBoolean("first_level_checked", false);
        second_level_checked = sharedPreferences.getBoolean("second_level_checked", false);
        third_level_checked = sharedPreferences.getBoolean("third_level_checked", false);
        fourth_level_checked = sharedPreferences.getBoolean("fourth_level_checked", false);
        flash_checked = sharedPreferences.getBoolean("flash_checked", false);
        avtivity_state_checked = sharedPreferences.getBoolean("avtivity_state_checked", false);
        recover_state_checked = sharedPreferences.getBoolean("recover_state_checked", false);
        all_state_checked = sharedPreferences.getBoolean("all_state_checked", false);
        if(first_level_checked == true){
            first_level_box.setChecked(true);
        }
        if(second_level_checked == true){
            second_level_box.setChecked(true);
        }
        if(third_level_checked == true){
            third_level_box.setChecked(true);
        }
        if(fourth_level_checked == true){
            fourth_level_box.setChecked(true);
        }
        if(flash_checked == true){
            flash_warning_box.setChecked(true);
        }
        if(avtivity_state_checked == true){
            avtivity_state_box.setChecked(true);
        }
        if(recover_state_checked == true){
            recover_state_box.setChecked(true);
        }
        if(all_state_checked == true){
            all_state_box.setChecked(true);
        }
    }

    @Override
    protected void registerListener() {
        first_level_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (ischecked) {
                    first_level_box.setChecked(true);
                    first_level_checked = true;
                } else {
                    first_level_box.setChecked(false);
                    first_level_checked = false;
                }
            }
        });
        second_level_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (ischecked) {
                    second_level_box.setChecked(true);
                    second_level_checked = true;
                } else {
                    second_level_box.setChecked(false);
                    second_level_checked = false;
                }
            }
        });
        third_level_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (ischecked) {
                    third_level_box.setChecked(true);
                    third_level_checked = true;
                } else {
                    third_level_box.setChecked(false);
                    third_level_checked = false;
                }
            }
        });
        fourth_level_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (ischecked) {
                    fourth_level_box.setChecked(true);
                    fourth_level_checked = true;
                } else {
                    fourth_level_box.setChecked(false);
                    fourth_level_checked = false;
                }
            }
        });
        flash_warning_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (ischecked) {
                    flash_warning_box.setChecked(true);
                    flash_checked = true;
                } else {
                    flash_warning_box.setChecked(false);
                    flash_checked = false;
                }
            }
        });
        avtivity_state_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    avtivity_state_box.setChecked(true);
                    avtivity_state_checked = true;
                    all_state_box.setChecked(false);
                } else {
                    avtivity_state_box.setChecked(false);
                    avtivity_state_checked = false;
                }
            }
        });
        recover_state_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    recover_state_box.setChecked(true);
                    recover_state_checked = true;
                    all_state_box.setChecked(false);
                } else {
                    recover_state_box.setChecked(false);
                    recover_state_checked = false;
                }
            }
        });
        all_state_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    all_state_box.setChecked(true);
                    all_state_checked = true;
                    avtivity_state_box.setChecked(false);
                    recover_state_box.setChecked(false);
                    avtivity_state_checked = false;
                    recover_state_checked = false;
                } else {
                    all_state_box.setChecked(false);
                    all_state_checked = false;
                }
            }
        });
        my_order_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//            if(first_level_checked == false && second_level_checked == false && third_level_checked == false
//                    && fourth_level_checked == false && flash_checked == false && avtivity_state_checked == false
//                    && recover_state_checked == false && all_state_checked == false){
//                Toast.makeText(WarningOrderActivity.this,"请选择条件",Toast.LENGTH_SHORT).show();
//                return;
//            }

            //告警级别传参。
            if (first_level_checked == true) {
                level_value.append("1,");
            }if(second_level_checked == true){
                level_value.append("2,");
            }if(third_level_checked == true){
                level_value.append("3,");
            }if(fourth_level_checked == true){
                level_value.append("4,");
            }

            //去掉结尾的逗号
            if(level_value.length() == 2) {
                level_value.deleteCharAt(1);
                warning_level_value = level_value.toString();
            }if(level_value.length() > 2){
                level_value.deleteCharAt(level_value.lastIndexOf(","));
                warning_level_value = level_value.toString();
            }
            level_value.setLength(0);

             //告警状态传参
            if(avtivity_state_checked == true){
                warning_state_value = "1";
            }if(recover_state_checked == true){
                warning_state_value = "2";
            }if(all_state_checked == true){
                warning_state_value = "";
            }

            setData();      //将此时的数据状态放入缓存中
            jumpInto();     //跳转至上层界面
            }
        });
    }


    private void setData() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WarningOrderActivity.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("first_level_checked",first_level_checked);
        editor.putBoolean("second_level_checked",second_level_checked);
        editor.putBoolean("third_level_checked", third_level_checked);
        editor.putBoolean("fourth_level_checked", fourth_level_checked);
        editor.putBoolean("flash_checked", flash_checked);
        editor.putBoolean("avtivity_state_checked", avtivity_state_checked);
        editor.putBoolean("recover_state_checked", recover_state_checked);
        editor.putBoolean("all_state_checked", all_state_checked);
        editor.commit();
    }

    private void jumpInto() {
        Intent intent = new Intent();
        intent.putExtra("ALARMLEVEL", warning_level_value);
        intent.putExtra("ALARMSTATE", warning_state_value);
        if(flash_checked == true){
            intent.putExtra("flash", "1");
        }
        setResult(RESULT_OK,intent);
        finish();
    }


}
