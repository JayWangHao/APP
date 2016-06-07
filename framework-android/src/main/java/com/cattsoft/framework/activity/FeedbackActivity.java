package com.cattsoft.framework.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.R;
import com.cattsoft.framework.base.BaseActivity;
import com.cattsoft.framework.cache.CacheProcess;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.connect.RequestListener;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.framework.view.EditLabelText;
import com.cattsoft.framework.view.TitleBarView;

import java.util.concurrent.TimeUnit;

public class FeedbackActivity extends BaseActivity{

//	private EditText feedBackContentEt;
//	private EditText feedBackContactEt;
//	private Button feedBackBtn;
//	private String feedbackContentStr;
//	private String feedbackContactStr;

    private EditLabelText mEditTextContent;
    private EditLabelText mEditTextContact;

    private Button mBtnFeedback;

	private JSONObject parameter;
	
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);


		TitleBarView title = (TitleBarView) findViewById(R.id.title1);
		title.setTitleBar("意见反馈", View.VISIBLE, View.GONE, View.GONE, false);
		title.getTitleLeftButton().setImageDrawable(getResources().getDrawable(R.drawable.title_left_btn_back));
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
		
//		feedBackContentEt = (EditText) findViewById(R.id.feed_back_content);
//		feedBackContactEt = (EditText) findViewById(R.id.feed_back_contact);
//		feedBackContentEt.setLongClickable(false);
//		feedBackContactEt.setLongClickable(false);
//		feedBackBtn = (Button) findViewById(R.id.feedback_btn_sure);
        mEditTextContent = (EditLabelText) findViewById(R.id.et_feedback_content);
        mEditTextContact = (EditLabelText) findViewById(R.id.et_feedback_contact);
        mBtnFeedback = (Button) findViewById(R.id.btn_feedback_submit);
	}

	@Override
	protected void registerListener() {

        mBtnFeedback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                parameter = new JSONObject();

                String content = mEditTextContent.getValue();
                String contact = mEditTextContact.getValue();

                if(TextUtils.isEmpty(content)){
                    Toast.makeText(FeedbackActivity.this, "请填写反馈内容！", Toast.LENGTH_SHORT).show();
					return;
                }

				parameter.put("feedDesc", content);
				parameter.put("email", contact);
				parameter.put("userId", CacheProcess.getCacheValueInSharedPreferences(FeedbackActivity.this, "sysUserId"));

				Communication communication = new Communication(parameter,"feedbackService","feedback", new AfterFeedbackListener(),FeedbackActivity.this);
				communication.setShowProcessDialog(true);
				communication.getPostHttpContent();

            }
        });

		
//		feedBackBtn.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				parameter = new JSONObject();
//
//				feedbackContentStr = feedBackContentEt.getText().toString();
//				feedbackContactStr = feedBackContactEt.getText().toString();
//
//				if(StringUtil.isBlank(feedbackContentStr)) {
//
//					Toast.makeText(FeedbackActivity.this, "请填写反馈内容！", Toast.LENGTH_SHORT).show();
//					return;
//				}
//
//				parameter.put("feedDesc", feedbackContentStr);
//				parameter.put("email", feedbackContactStr);
//				parameter.put("userId", CacheProcess.getCacheValueInSharedPreferences(FeedbackActivity.this, "sysUserId"));
//
//				Communication communication = new Communication(parameter,"feedbackService","feedback","afterFeedback",FeedbackActivity.this);
//				communication.getPostHttpContent();
//
//
//			}
//
//		});
		
	}

    private class AfterFeedbackListener implements RequestListener{
        @Override
		public void onComplete(String result) {
			mEditTextContent.setValue("");
            mEditTextContact.setValue("");
			Toast.makeText(FeedbackActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }

	
//	public void afterFeedback() {
		
//		Toast.makeText(FeedbackActivity.this, "用户反馈成功！", Toast.LENGTH_SHORT).show();
//	}

}
