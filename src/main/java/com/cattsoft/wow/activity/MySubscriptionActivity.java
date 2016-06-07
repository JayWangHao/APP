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
import com.cattsoft.wow.R;


/**
 * 告警订阅
 * Created by chenlipeng
 */
public class MySubscriptionActivity extends BaseActivity {


    private CheckBox first_level_box;       //一级告警选中框
    private CheckBox second_level_box;
    private CheckBox third_level_box;
    private CheckBox fourth_level_box;
    private CheckBox all_level_check;

    private CheckBox today_checkbox;     //
    private CheckBox week_checkbox;     //
    private CheckBox month_checkbox;

    private CheckBox flash_warn_checkbox;
    //
    private CheckBox avtivity_check;     //
    private CheckBox recover_check;
    private CheckBox all_check;

    private Button back_btn;
    private Button btn_commit;



    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private StringBuilder level_value = new StringBuilder();     //告警级别
    private StringBuilder time_value = new StringBuilder();     //告警级别
    private String company_level_value;//公司类别
    private String warning_state_value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.order);

        initView();
        getData();
//        initDataThread(false);                    //网络得到数据
        registerListener();
    }

    protected void initView() {

        back_btn=(Button)findViewById(R.id.back_btn);

        first_level_box = (CheckBox) findViewById(R.id.first_level_check);
        second_level_box = (CheckBox) findViewById(R.id.second_level_check);
        third_level_box = (CheckBox) findViewById(R.id.third_level_check);
        fourth_level_box = (CheckBox) findViewById(R.id.fourth_level_check);
        all_level_check = (CheckBox) findViewById(R.id.all_level_check);

        today_checkbox = (CheckBox) findViewById(R.id.today_checkbox);
        week_checkbox = (CheckBox) findViewById(R.id.week_checkbox);
        month_checkbox = (CheckBox) findViewById(R.id.month_checkbox);


        flash_warn_checkbox = (CheckBox) findViewById(R.id.flash_warn_checkbox);

        avtivity_check = (CheckBox) findViewById(R.id.avtivity_check);
        recover_check = (CheckBox) findViewById(R.id.recover_check);
        all_check = (CheckBox) findViewById(R.id.all_check);

        btn_commit=(Button)findViewById(R.id.btn_commit);
    }
    private boolean first_level_checked = false;
    private boolean second_level_checked = false;
    private boolean third_level_checked = false;
    private boolean fourth_level_checked = false;
    private boolean all_level_checked = false;

    private boolean today_checkboxed = false;
    private boolean week_checkboxed = false;
    private boolean month_checkboxed = false;

    private boolean flash_warn_checkboxed = false;

    private boolean avtivity_checked = false;
    private boolean recover_checked = false;
    private boolean all_checked = false;


    private void getData(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
         editor = sharedPreferences.edit();

        first_level_checked = sharedPreferences.getBoolean("first_level_checked", false);
        second_level_checked = sharedPreferences.getBoolean("second_level_checked", false);
        third_level_checked = sharedPreferences.getBoolean("third_level_checked", false);
        fourth_level_checked = sharedPreferences.getBoolean("fourth_level_checked", false);
        all_level_checked = sharedPreferences.getBoolean("all_level_checked", false);

        today_checkboxed = sharedPreferences.getBoolean("today_checkboxed", false);
        week_checkboxed = sharedPreferences.getBoolean("week_checkboxed", false);
        month_checkboxed = sharedPreferences.getBoolean("month_checkboxed", false);

        flash_warn_checkboxed = sharedPreferences.getBoolean("flash_warn_checkboxed", false);

        avtivity_checked = sharedPreferences.getBoolean("avtivity_checked", false);
        recover_checked = sharedPreferences.getBoolean("recover_checked", false);
        all_checked = sharedPreferences.getBoolean("all_checked", false);


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
        if(all_level_checked == true){
            all_level_check.setChecked(true);
        }
        if(today_checkboxed == true){
            today_checkbox.setChecked(true);
        }
        if(week_checkboxed == true){
            week_checkbox.setChecked(true);
        }
        if(month_checkboxed == true){
            month_checkbox.setChecked(true);
        }

        if(flash_warn_checkboxed == true){
            flash_warn_checkbox.setChecked(true);
        }
        if(avtivity_checked == true){
            avtivity_check.setChecked(true);
        }
        if(recover_checked == true){
            recover_check.setChecked(true);
        }
        if(all_checked == true){
            all_check.setChecked(true);
        }
    }

    @Override
    protected void registerListener() {


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        first_level_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (ischecked) {
                    first_level_box.setChecked(true);
                    second_level_box.setChecked(false);
                    third_level_box.setChecked(false);
                    fourth_level_box.setChecked(false);
                    all_level_check.setChecked(false);

                    first_level_checked = true;
                    second_level_checked = false;
                    third_level_checked = false;
                    fourth_level_checked = false;
                    all_level_checked = false;

                    editor.putBoolean("first_level_checked", first_level_checked);
                    editor.putBoolean("second_level_checked", second_level_checked);
                    editor.putBoolean("third_level_checked", third_level_checked);
                    editor.putBoolean("fourth_level_checked", fourth_level_checked);
                    editor.putBoolean("all_level_checked", all_level_checked);
                    editor.commit();
                } else {
                    first_level_box.setChecked(false);
                    first_level_checked = false;
                    editor.putBoolean("first_level_checked", first_level_checked);
                    editor.commit();
                }
            }
        });
        second_level_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (ischecked) {
                    first_level_box.setChecked(false);
                    second_level_box.setChecked(true);
                    third_level_box.setChecked(false);
                    fourth_level_box.setChecked(false);
                    all_level_check.setChecked(false);

                    first_level_checked = false;
                    second_level_checked = true;
                    third_level_checked = false;
                    fourth_level_checked = false;
                    all_level_checked = false;

                    editor.putBoolean("first_level_checked",first_level_checked);
                    editor.putBoolean("second_level_checked",second_level_checked);
                    editor.putBoolean("third_level_checked",third_level_checked);
                    editor.putBoolean("fourth_level_checked",fourth_level_checked);
                    editor.putBoolean("all_level_checked",all_level_checked);
                    editor.commit();
                } else {
                    second_level_box.setChecked(false);
                    second_level_checked = false;
                    editor.putBoolean("second_level_checked",second_level_checked);
                    editor.commit();
                }
            }
        });
        third_level_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (ischecked) {
                    first_level_box.setChecked(false);
                    second_level_box.setChecked(false);
                    third_level_box.setChecked(true);
                    fourth_level_box.setChecked(false);
                    all_level_check.setChecked(false);

                    first_level_checked = false;
                    second_level_checked = false;
                    third_level_checked = true;
                    fourth_level_checked = false;
                    all_level_checked = false;

                    editor.putBoolean("first_level_checked",first_level_checked);
                    editor.putBoolean("second_level_checked",second_level_checked);
                    editor.putBoolean("third_level_checked",third_level_checked);
                    editor.putBoolean("fourth_level_checked",fourth_level_checked);
                    editor.putBoolean("all_level_checked",all_level_checked);
                    editor.commit();
                } else {
                    third_level_box.setChecked(false);
                    third_level_checked = false;
                    editor.putBoolean("third_level_checked",third_level_checked);
                    editor.commit();
                }

            }
        });
        fourth_level_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (ischecked) {
                    first_level_box.setChecked(false);
                    second_level_box.setChecked(false);
                    third_level_box.setChecked(false);
                    fourth_level_box.setChecked(true);
                    all_level_check.setChecked(false);

                    first_level_checked = false;
                    second_level_checked = false;
                    third_level_checked = false;
                    fourth_level_checked = true;
                    all_level_checked = false;

                    editor.putBoolean("first_level_checked",first_level_checked);
                    editor.putBoolean("second_level_checked",second_level_checked);
                    editor.putBoolean("third_level_checked",third_level_checked);
                    editor.putBoolean("fourth_level_checked",fourth_level_checked);
                    editor.putBoolean("all_level_checked",all_level_checked);
                    editor.commit();
                } else {
                    fourth_level_box.setChecked(false);
                    fourth_level_checked = false;
                    editor.putBoolean("fourth_level_checked",fourth_level_checked);
                    editor.commit();
                }
            }
        });
        all_level_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (ischecked) {
                    first_level_box.setChecked(false);
                    second_level_box.setChecked(false);
                    third_level_box.setChecked(false);
                    fourth_level_box.setChecked(false);
                    all_level_check.setChecked(true);

                    first_level_checked = false;
                    second_level_checked = false;
                    third_level_checked = false;
                    fourth_level_checked = false;
                    all_level_checked = true;

                    editor.putBoolean("first_level_checked",first_level_checked);
                    editor.putBoolean("second_level_checked",second_level_checked);
                    editor.putBoolean("third_level_checked",third_level_checked);
                    editor.putBoolean("fourth_level_checked",fourth_level_checked);
                    editor.putBoolean("all_level_checked",all_level_checked);
                    editor.commit();
                } else {
                    all_level_check.setChecked(false);
                    all_level_checked = false;
                    editor.putBoolean("all_level_checked",all_level_checked);
                    editor.commit();
                }
            }
        });
        today_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    today_checkbox.setChecked(true);
                    week_checkbox.setChecked(false);
                    month_checkbox.setChecked(false);
                    today_checkboxed = true;
                    week_checkboxed = false;
                    month_checkboxed = false;

                    editor.putBoolean("today_checkboxed",today_checkboxed);
                    editor.putBoolean("week_checkboxed",week_checkboxed);
                    editor.putBoolean("month_checkboxed",month_checkboxed);
                    editor.commit();


                } else {
                    today_checkbox.setChecked(false);
                    today_checkboxed = false;
                    editor.putBoolean("today_checkboxed",today_checkboxed);
                    editor.commit();
                }
            }
        });
        week_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    week_checkbox.setChecked(true);
                    today_checkbox.setChecked(false);
                    month_checkbox.setChecked(false);
                    week_checkboxed = true;
                    today_checkboxed = false;
                    month_checkboxed = false;
                    editor.putBoolean("week_checkboxed",week_checkboxed);
                    editor.putBoolean("today_checkboxed",today_checkboxed);
                    editor.putBoolean("month_checkboxed",month_checkboxed);
                    editor.commit();
                } else {
                    week_checkbox.setChecked(false);
                    week_checkboxed = false;
                    editor.putBoolean("week_checkboxed",week_checkboxed);
                    editor.commit();
                }
            }
        });
        month_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    month_checkbox.setChecked(true);
                    today_checkbox.setChecked(false);
                    week_checkbox.setChecked(false);
                    month_checkboxed = true;
                    today_checkboxed = false;
                    week_checkboxed = false;
                    editor.putBoolean("month_checkboxed",month_checkboxed);
                    editor.putBoolean("week_checkboxed", week_checkboxed);
                    editor.putBoolean("today_checkboxed",today_checkboxed);
                    editor.commit();

                } else {
                    month_checkbox.setChecked(false);
                    month_checkboxed = false;
                    editor.putBoolean("month_checkboxed",month_checkboxed);
                    editor.commit();
                }
            }
        });
        flash_warn_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    flash_warn_checkbox.setChecked(true);
                    flash_warn_checkboxed = true;
                    editor.putBoolean("flash_warn_checkboxed",flash_warn_checkboxed);
                    editor.commit();

                } else {
                    flash_warn_checkbox.setChecked(false);
                    flash_warn_checkboxed = false;
                    editor.putBoolean("flash_warn_checkboxed",flash_warn_checkboxed);
                    editor.commit();
                }
            }
        });
        avtivity_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    avtivity_check.setChecked(true);
                    avtivity_checked = true;
                    editor.putBoolean("avtivity_checked",avtivity_checked);
                    editor.commit();

                } else {
                    avtivity_check.setChecked(false);
                    avtivity_checked = false;
                    editor.putBoolean("avtivity_checked",avtivity_checked);
                    editor.commit();
                }
            }
        });
        recover_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    recover_check.setChecked(true);
                    recover_checked = true;
                    editor.putBoolean("recover_checked",recover_checked);
                    editor.commit();

                } else {
                    recover_check.setChecked(false);
                    recover_checked = false;
                    editor.putBoolean("recover_checked",recover_checked);
                    editor.commit();
                }
            }
        });
        all_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    all_check.setChecked(true);
                    all_checked = true;
                    editor.putBoolean("all_checked",all_checked);
                    editor.commit();

                } else {
                    all_check.setChecked(false);
                    all_checked = false;
                    editor.putBoolean("all_checked",all_checked);
                    editor.commit();
                }
            }
        });

        //提交数据
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //告警级别传参。
                if (first_level_checked == true) {
                    level_value.append("1,");
                }if(second_level_checked == true){
                    level_value.append("2,");
                }if(third_level_checked == true){
                    level_value.append("3,");
                }if(fourth_level_checked == true){
                    level_value.append("4,");
                }if(all_level_checked){
                    level_value.append("5,");
                }

                //去掉最后的逗号
                if(level_value.length() == 2) {
                    level_value.deleteCharAt(1);
                    company_level_value = level_value.toString();
                }if(level_value.length() > 2){
                    level_value.deleteCharAt(level_value.lastIndexOf(","));
                    company_level_value = level_value.toString();
                }
                level_value.setLength(0);


                //1本日，7本周，30本月
                if (today_checkboxed == true) {
                    time_value.append("1");
                }if( week_checkboxed== true){
                    time_value.append("2");
                }if(month_checkboxed == true){
                    time_value.append("30");
                }


//                //告警状态传参
                if(avtivity_checked == true){
                    warning_state_value = "1";
                }if(recover_checked == true){
                    warning_state_value = "2";
                }if(all_checked == true){
                    warning_state_value = "";
                }

                jumpInto();     //跳转至上层界面
            }
        });
    }



    private void jumpInto() {
        Intent intent = new Intent();
        intent.putExtra("ALARMLEVEL", company_level_value);
        intent.putExtra("ALARMSTATE", warning_state_value);
        intent.putExtra("TIME",time_value.toString() );
        if(flash_warn_checkboxed == true){
            intent.putExtra("flash", "1");
        }

        setResult(RESULT_OK,intent);
        time_value.setLength(0);
        finish();

    }


}
