package com.cattsoft.wow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.base.BaseActivity;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.view.ProgressDialog;
import com.cattsoft.framework.view.pullableview.PullToRefreshLayout;
import com.cattsoft.framework.view.pullableview.PullableListView;
import com.cattsoft.wow.R;
import com.cattsoft.wow.adapter.CommonAdapter;
import com.cattsoft.wow.adapter.ViewHolder;
import com.cattsoft.wow.mudels.RelevanceWarning;

import java.util.ArrayList;
import java.util.List;

/**
 * 关联告警页面
 */
public class RelevanceWarningActivity extends BaseActivity {

    private Button back;
    private PullToRefreshLayout refreshLayout;
    private PullableListView list;
    private String resultC;

    private String eMosid;
    private String limit="6";//显示的条数
    private int page=1;//页码

    JSONObject resultJson;

    /**
     * 下拉刷新
     */
    public static final int PULL_DOWN_TO_REFRESH = 0;
    /**
     * 上拉加载
     */
    public static final int PULL_UP_TO_LOAD_MORE = 1;
    private int mRefreshMode = PULL_DOWN_TO_REFRESH;
    private int message = 0;//用来标示下拉刷新请求数据、上拉刷新数据、第一次请求数据

    private ProgressDialog progressDialog;//刷新进度条

    private List<RelevanceWarning> woList = new ArrayList<RelevanceWarning>();

    private CommonAdapter<RelevanceWarning> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_relevance_warning);
        Intent intent=getIntent();
        eMosid=intent.getStringExtra("emosid");

        initView();
        registerListener();

        initThread();
    }

    @Override
    protected void initView() {
        list=(PullableListView)findViewById(R.id.listview);
        back=(Button)findViewById(R.id.back_btn);
        refreshLayout = (PullToRefreshLayout)findViewById(R.id.refresh_view);
    }

    @Override
    protected void registerListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        refreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //下拉刷新
                mRefreshMode = PULL_DOWN_TO_REFRESH;
                message = 1;
                page = 1;
                refreshLayout.setCanLoadMore(true);
                woList.clear();
                initThread();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //上拉加载
                mRefreshMode = PULL_UP_TO_LOAD_MORE;
                page++;
                message = 2;
                initThread();
            }
        });
    }

    /**
     * 启动线程
     */
    private void initThread(){
        progressDialog = new ProgressDialog(this);
        progressDialog.showProcessDialog();
        new initWarnThread().start();
    }

    public class initWarnThread extends Thread{
        @Override
        public void run() {
            warnRequest();
        }
    }

    /**
     * 请求数据
     */
    public void warnRequest(){
        String url="tz/TZDeviceAction.do?method=getRealteAlarm";
        Message msg=new Message();
        try {
            JSONObject jsonParmters=new JSONObject();
            jsonParmters.put("VCEMOSID",eMosid);
            jsonParmters.put("limit",limit);
            jsonParmters.put("page",page+"");

            String parameter = jsonParmters.toString();
            resultC = Communication.getPostResponseForNetManagement(url, parameter);
            handleResult(resultC);
            msg.what = 1;
            handler.sendMessage(msg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 用于接收线程发送的消息
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    list.setAdapter(mAdapter = new CommonAdapter<RelevanceWarning>(
                            getApplicationContext(), woList, R.layout.relevance_warning_list_item)
                    {
                        @Override
                        public void convert(ViewHolder helper, RelevanceWarning item)
                        {
                            helper.setText(R.id.vcnecname, "站点名称: "+item.getVcnecname());
                            helper.setText(R.id.alarmlevel,item.getAlarmlevel());
                            helper.setText(R.id.dfirsteventtime, "发生时间: "+item.getDfirsteventtime());
                            helper.setText(R.id.vcspecificproblems, "告警内容: "+item.getVcspecificproblems());

                            helper.setText(R.id.ctime1, "告警时长: "+item.getCtime1());
                            helper.setText(R.id.cnastate,item.getCnastate());
                            helper.setText(R.id.vcmoiname,"网元内定位: "+item.getVcmoiname());
                            helper.setText(R.id.objecttype,"告警对象: "+item.getObjecttype());
                        }
                    });

                    list.setVisibility(View.VISIBLE);

                    if(page !=0){
                        list.setSelection(page * 5+1);
                    }
                    if (mRefreshMode == PULL_DOWN_TO_REFRESH) {
                        refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    } else {
                        refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                    break;
                }
                case 2:
                    Toast.makeText(RelevanceWarningActivity.this, "" + resultJson.getString("error"), Toast.LENGTH_SHORT).show();
                    break;
            }
            progressDialog.closeProcessDialog();
        }
    };

    private void handleResult(String result) {
        //解析数据，处理数据
       resultJson = JSON.parseObject(result);

        if (resultJson.containsKey("error")){
            //Toast.makeText(this, ""+resultJson.getString("error"), Toast.LENGTH_SHORT).show();
            Message msg=new Message();
            msg.what=2;
            handler.sendMessage(msg);
        }else {
            JSONArray jsonArray=resultJson.getJSONArray("alarms");
            for(int i=0;i<jsonArray.size();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);

                RelevanceWarning relevanceWarning=new RelevanceWarning();

                relevanceWarning.setVcnecname(jsonObject.getString("vcnecname"));
                relevanceWarning.setAlarmlevel(jsonObject.getString("alarmlevel"));
                relevanceWarning.setCtime1(jsonObject.getString("ctime1"));
                relevanceWarning.setDfirsteventtime(jsonObject.getString("dfirsteventtime"));
                relevanceWarning.setVcspecificproblems(jsonObject.getString("VCSPECIFICPROBLEMS"));
                relevanceWarning.setCnastate(jsonObject.getString("Cnastate"));
                relevanceWarning.setVcmoiname(jsonObject.getString("VCMOINAME"));
                relevanceWarning.setObjecttype(jsonObject.getString("OBJECTTYPE"));

                woList.add(relevanceWarning);
            }
        }
    }
}
