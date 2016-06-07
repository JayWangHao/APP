package com.cattsoft.framework.template;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cattsoft.framework.R;
import com.cattsoft.framework.base.BaseActivity;
import com.cattsoft.framework.cache.DataBaseManager;
import com.cattsoft.framework.view.EditLabelText;
import com.cattsoft.framework.view.TitleBarView;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试ORMLite数据库使用
 */
public class TestORMLiteActivity extends BaseActivity {

    private EditLabelText edit1;
    private EditLabelText edit2;
    private EditLabelText edit3;
    private EditLabelText edit4;
    private EditLabelText edit5;
    private EditLabelText edit6;

    private Button saveBtn;
    private Button deleteThird;
    private Button queryThird;
    private Button updateThird;
    private Button queryAll;

    private TitleBarView title;

    private List<TestORMLiteBean> list = new ArrayList<TestORMLiteBean>();

    private List<EditLabelText> editList = new ArrayList<EditLabelText>();

    private int thirdId = -1;//第三条数据的主键id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ormlite);

        title = (TitleBarView) findViewById(R.id.title1);
        title.setTitleBar("数据保存测试", View.VISIBLE,
                View.GONE, View.GONE, false);
        title.getTitleLeftButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initView();

        registerListener();
    }

    @Override
    protected void initView() {

        edit1 = (EditLabelText) findViewById(R.id.eidt1);
        edit2 = (EditLabelText) findViewById(R.id.eidt2);
        edit3 = (EditLabelText) findViewById(R.id.eidt3);
        edit4 = (EditLabelText) findViewById(R.id.eidt4);
        edit5 = (EditLabelText) findViewById(R.id.eidt5);
        edit6 = (EditLabelText) findViewById(R.id.eidt6);

        editList.add(edit1);
        editList.add(edit2);
        editList.add(edit3);
        editList.add(edit4);
        editList.add(edit5);
        editList.add(edit6);

        saveBtn = (Button) findViewById(R.id.save_all);
        queryAll = (Button) findViewById(R.id.query_all);
        deleteThird = (Button) findViewById(R.id.delete_third);
        queryThird = (Button) findViewById(R.id.query_third);
        updateThird = (Button) findViewById(R.id.update_third);


    }

    @Override
    protected void registerListener() {

        //清空表后插入数据列表
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<TestORMLiteBean> saveList = new ArrayList<TestORMLiteBean>();

                for(int i=0;i<editList.size();i++){

                    EditLabelText edit = editList.get(i);

                    TestORMLiteBean bean = new TestORMLiteBean();//表所对应的对象类

                    bean.setLabel(edit.getLabel());
                    bean.setValue(edit.getValue());

                    saveList.add(bean);

                }

                //批量插入数据
               int success = DataBaseManager.getInstance(TestORMLiteActivity.this).insert(TestORMLiteBean.class,saveList,true);

               if(success==1){

                   Toast.makeText(TestORMLiteActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
               }else{

                   Toast.makeText(TestORMLiteActivity.this,"保存失败",Toast.LENGTH_SHORT).show();
               }
            }
        });

        //查询表里所有数据
        queryAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                list = DataBaseManager.getInstance(TestORMLiteActivity.this).selectAll(TestORMLiteBean.class);

                if(list!=null&&list.size()!=0){

                    for(int i=0;i<list.size()&&i<editList.size();i++){

                        TestORMLiteBean bean = list.get(i);
                        editList.get(i).setLabel(bean.getLabel());
                        editList.get(i).setValue(bean.getValue());
                    }

                }

            }
        });

        //根据条件查询
        queryThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<TestORMLiteBean> listBean = DataBaseManager.getInstance(TestORMLiteActivity.this).selectByField(TestORMLiteBean.class,"label","工单号码：");

                if(listBean!=null&&listBean.size()>0){

                    TestORMLiteBean bean = listBean.get(0);
                    thirdId = bean.getGeneratedId();

                    Toast.makeText(TestORMLiteActivity.this,bean.getLabel()+bean.getValue(),Toast.LENGTH_SHORT).show();
                }else{


                    Toast.makeText(TestORMLiteActivity.this,"没有查询到相关数据",Toast.LENGTH_SHORT).show();
                }

            }
        });

        //更新某条数据
        updateThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(thirdId==-1){

                    Toast.makeText(TestORMLiteActivity.this,"请先查询出第三条数据",Toast.LENGTH_SHORT).show();
                }else{

                    TestORMLiteBean bean = new TestORMLiteBean();

                    bean.setGeneratedId(thirdId);
                    bean.setLabel(edit3.getLabel());
                    bean.setValue(edit3.getValue());

                    DataBaseManager.getInstance(TestORMLiteActivity.this).saveOrUpdate(TestORMLiteBean.class,bean);

                    Toast.makeText(TestORMLiteActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
                }

            }
        });

        //删除某条数据
        deleteThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(thirdId==-1){

                    Toast.makeText(TestORMLiteActivity.this,"请先查询出第三条数据",Toast.LENGTH_SHORT).show();
                }else{

                    TestORMLiteBean bean = new TestORMLiteBean();

                    bean.setGeneratedId(thirdId);

                    DataBaseManager.getInstance(TestORMLiteActivity.this).delete(bean,TestORMLiteBean.class);

                    Toast.makeText(TestORMLiteActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
