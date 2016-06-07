package com.cattsoft.framework.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.R;
import com.cattsoft.framework.base.BaseListActivity;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.view.TitleBarView;

/**
 * demo分页查询listview
 * 
 * @author wei
 * 
 */
public class ListMoreActivity extends BaseListActivity {

	private ListView listView;
	private SimpleAdapter listAdapter;// 适配器
	private List<Map<String, String>> itemList = new ArrayList<Map<String, String>>();// 数据源

	private View footViewBar;// 下滑加载条
	// 请求数据
	private int pageNo = 1;
	private int pageSize = 20;

	// 返回数据
	private int total;// 总条数

	private JSONObject parameter;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_more_activity);

		// 导航栏初始化
		TitleBarView title = (TitleBarView) findViewById(R.id.title1);
		title.setTitleBar(getString(R.string.app_name), View.VISIBLE,
				View.GONE, View.GONE, false);

		title.getTitleLeftButton().setOnClickListener(
				new Button.OnClickListener() {

					@Override
					public void onClick(View arg0) {

						onBackPressed();

					}

				});

		initView();
		registerListener();

		//请求数据
		parameter = new JSONObject();
		parameter.put("pageNo", pageNo);
		parameter.put("pageSize", pageSize);

		Communication communication = new Communication(parameter,
				"feedbackService", "feedback", "initUI", ListMoreActivity.this);
		communication.getPostHttpContent();

	}

	@Override
	protected void initView() {

		listView = (ListView) findViewById(R.id.listview);
		footViewBar = View.inflate(ListMoreActivity.this,
				R.layout.foot_view_loading, null);
		listAdapter = new SimpleAdapter(ListMoreActivity.this, itemList,
				R.layout.list_more_item, new String[] { "item_content" },
				new int[] { R.id.item_content });
		listView.setAdapter(listAdapter);
		listView.setOnScrollListener(listener);

	}

	@Override
	protected void registerListener() {

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

	/**
	 * 滑动进度条所触发的监听事件
	 */
	private AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener() {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

			if ((view.getLastVisiblePosition() == view.getCount() - 1)
					&& (total > pageSize * pageNo)) {

				pageNo++;
				// 滑动list请求数据

				parameter = new JSONObject();
				parameter.put("pageNo", pageNo);
				parameter.put("pageSize", pageSize);

				Communication communication = new Communication(parameter,
						"feedbackService", "feedback", "updateUI",
						ListMoreActivity.this);

				communication.setShowProcessDialog(false);// 不显示进度条
				communication.getPostHttpContent();

			}

		}
	};

	// 第一次请求列表后
	public void initUI(String jsonStr) {

		analyzeResult(jsonStr);

		if (total > pageSize * pageNo) {
			listView.addFooterView(footViewBar);// 添加list底部更多按钮
		}

		listView.setAdapter(listAdapter);

	}
	//滑动listview请求数据
	public void updateUI(String jsonStr) {
		
		analyzeResult(jsonStr);
		
		if (total <= pageSize * pageNo) {
			listView.removeFooterView(footViewBar);// 添加list底部更多按钮
		}
		
		listAdapter.notifyDataSetChanged();
	}

	//解析返回数据
	private void analyzeResult(String jsonStr) {
		// JSON的解析过程
		JSONObject responseJsonObject = JSONObject.parseObject(jsonStr);

		total = Integer.parseInt(responseJsonObject.getString("total"));

		JSONArray resultJsonArray = responseJsonObject
				.getJSONArray("resultList");
		for (int i = 0; i < resultJsonArray.size(); i++) {

			JSONObject json = resultJsonArray.getJSONObject(i);

			Map<String, String> map = new HashMap<String, String>();

			map.put("item_content", json.getString("content"));
			map.put("item_id", json.getString("id"));

			itemList.add(map);
		}
	}
}
