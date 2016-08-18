package com.cattsoft.wow.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cattsoft.framework.connect.Communication;
import com.cattsoft.framework.util.StringUtil;
import com.cattsoft.framework.view.ProgressDialog;
import com.cattsoft.framework.view.pullableview.PullToRefreshLayout;
import com.cattsoft.framework.view.pullableview.PullableListView;
import com.cattsoft.wow.R;
import com.cattsoft.wow.talk.CommentAdapter;
import com.cattsoft.wow.talk.CommentBean;
import com.cattsoft.wow.talk.NoScrollListView;
import com.cattsoft.wow.talk.ReplyAdapter;
import com.cattsoft.wow.talk.ReplyBean;
import com.cattsoft.wow.talk.ReplyPostBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TalkActivity extends Activity {

    private TextView back_text;
    /**
     * 下拉刷新
     */
    public static final int PULL_DOWN_TO_REFRESH = 0;
    /**
     * 上拉加载
     */
    public static final int PULL_UP_TO_LOAD_MORE = 1;
    private PullToRefreshLayout refreshLayout;
    private boolean isClearWoList = false;
    private int mRefreshMode = PULL_DOWN_TO_REFRESH;
    private int message = 0;//用来标示下拉刷新请求数据、上拉刷新数据、第一次请求数据
    private int pageNo = 0;
    private PullableListView commentList;//评论数据列表
    private Button commentButton;        //评论按钮
    private EditText commentEdit;        //评论输入框
//    private LinearLayout commentLinear;    //评论输入框线性布局

    private int commentId;                    //记录评论ID
    private int position = -1;                //记录回复评论的索引
    private boolean isReply;            //是否是回复
    private String comment = "";        //记录对话框中的内容
    private String userName;//当前账号的名字
    private String userId;//当前账号的id
    private CommentAdapter adapter;
    private List<CommentBean> list;
    private SharedPreferences sharedPreferences;
    private String result;
    private ArrayList<ReplyBean> replyList;
    private ProgressDialog progressDialog;
    private boolean isClearReplyData = false;
    private LinearLayout ll_talk_gen;
    private ReplyPostBean bean;
    private Boolean isShowInput = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_talk);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TalkActivity.this);
        initView();
        resterlistener();
        initData();
    }

    private void initView() {
        back_text = (TextView) findViewById(R.id.back_text);//返回
        refreshLayout = (PullToRefreshLayout) findViewById(R.id.comment_refresh_view);//下拉刷新布局
        commentList = (PullableListView) findViewById(R.id.commentList);//评论lv
        commentButton = (Button) findViewById(R.id.commentButton);//提交按钮
        commentEdit = (EditText) findViewById(R.id.commentEdit);//文本输入框
        ll_talk_gen = (LinearLayout) findViewById(R.id.ll_talk_gen);//跟布局
        ll_talk_gen.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = ll_talk_gen.getRootView().getHeight() - ll_talk_gen.getHeight();
                if (heightDiff > dpToPx(TalkActivity.this, 200)) { // if more than 200 dp, it's probably a keyboard...
                    // ... do something here
                    Log.d("TAG", "键盘显示");
                    if (isReply && position != -1) {
                        commentEdit.setHint("回复" + list.get(position).getCommentNickname() + ":");
                        commentButton.setText("回复");
                    }

                } else {

                    commentEdit.setHint("请输入评论");
                    commentButton.setText("评论");
                    if (isShowInput) {
                        Log.d("TAG", "键盘不显示");
                        if (isReply) {
                            isReply = false;
                        }

                        isShowInput = false;
                    }

                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        initDataThread(true);
    }

    private void initDataThread(boolean isprogress) {
        if (isprogress) {
            progressDialog = new ProgressDialog(this);
            progressDialog.showProcessDialog();
        }
        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                initTalkData();
            }
        });
        mThread.start();
    }

    public void initTalkData() {
        JSONObject requestJson = new JSONObject();
        userId = sharedPreferences.getString("userId", "");
        requestJson.put("UserID", userId);
        requestJson.put("INSERT", "0");
        String url = "tz/TZDeviceAction.do?method=MessageVer";
        Message m = new Message();
        try {
            result = Communication.getPostResponseForNetManagement(url, requestJson.toString());
            Log.d("TAG_2", "留言result：" + result);
            if (StringUtil.isBlank(result)) {
                m.what = 1;
                messageListHandler.sendMessage(m);
                return;
            }
            if (result.contains("解压或解密")) {
                m.what = 3;
                messageListHandler.sendMessage(m);
                return;
            }
            if (replyList != null && replyList.size() > 0) {
                replyList.clear();
            }
            getMessageData(result);
            m.what = 2;
            messageListHandler.sendMessage(m);
            return;
        } catch (IOException e) {
            m.what = 3;
            messageListHandler.sendMessage(m);
            e.printStackTrace();
        }

    }

    public void getMessageData(String result) {
        list = new ArrayList<CommentBean>();
//        replyList = new ArrayList<ReplyBean>();


        JSONObject returnJson = JSON.parseObject(result);
        JSONArray returnArray = returnJson.getJSONArray("allist");

        for (int i = 0; i < returnArray.size(); i++) {
            JSONObject obj = returnArray.getJSONObject(i);
            String IUPPERID = obj.getString("IUPPERID");//0是留言，其他是回复的留言的messageid
            if (IUPPERID == "0") {
                CommentBean commentBean = new CommentBean();
                commentBean.setCommnetUesrId(obj.getString("VCUSERID"));//留言的userId
                commentBean.setCommnetId(obj.getString("MESSAGEID"));//留言的ID
                commentBean.setCommentContent(obj.getString("MESSAGE"));//留言内容
                commentBean.setCommentNickname(obj.getString("VCREALNAME"));//留言人的名字
                commentBean.setCommentTime(obj.getString("DCOMMENTTIME"));//留言的时间
                commentBean.setCommentZan(Integer.parseInt(obj.getString("COUNTNUM")));//赞的数量
                commentBean.setIsExpend(false);
                int count = 0;
                for (int j = 0; j < returnArray.size(); j++) {
                    JSONObject obj1 = returnArray.getJSONObject(j);
                    if (obj1.getString("IUPPERID").equals(obj.getString("MESSAGEID"))) {
                        count++;
                        Log.d("TAG_2", "ReplyCount：" + count);

                    }
                }
                commentBean.setReplyCount(count);
                if (count != 0) {
                    commentBean.setHaveReply(true);
                } else {
                    commentBean.setHaveReply(false);
                }

                list.add(commentBean);
            }
        }

        Log.d("TAG_2", "getMessageData：" + list.size());

    }

    private Animation animation;
    Handler messageListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    Toast.makeText(TalkActivity.this, "服务端返回为空！", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 2: {
                    adapter = new CommentAdapter(TalkActivity.this, list, R.layout.comment_item, handler);
                    commentList.setAdapter(adapter);
                    if (mRefreshMode == PULL_DOWN_TO_REFRESH) {
                        refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    } else {
                        refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                    break;
                }
                case 3: {
                    Toast.makeText(TalkActivity.this, "请求服务端异常！", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 4: {
                    Toast.makeText(TalkActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 6: {
                    if (position == -1) {
                        return;
                    } else {

                        int childId = position - commentList.getFirstVisiblePosition();

                        if (childId >= 0) {
                            View viewItem = commentList.getChildAt(childId);
                            if (viewItem != null) {

                                TextView tv_zan = (TextView) viewItem.findViewById(R.id.tv_zan);
                                ImageView iv_zan = (ImageView) viewItem.findViewById(R.id.iv_zan);

                                if (adapter != null) {
                                    if (adapter.first[position] == 0) {
                                        adapter.first[position] = 1;
                                        if (tv_zan != null) {
                                            tv_zan.setText(list.get(position).getCommentZan() + 1 + "");
                                            list.get(position).setCommentZan(list.get(position).getCommentZan() + 1);
                                        }
                                        if (iv_zan != null) {
                                            iv_zan.setBackgroundResource(R.drawable.zan_press);
                                            animation = AnimationUtils.loadAnimation(TalkActivity.this, R.anim.anim);
                                            iv_zan.startAnimation(animation);
                                            iv_zan.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Toast.makeText(TalkActivity.this, "已点赞", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        }
                        Toast.makeText(TalkActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                case 5: {
                    Toast.makeText(TalkActivity.this, "回复成功", Toast.LENGTH_SHORT).show();
                    if (replyList != null && replyList.size() > 0) {
                        list.get(position).setReplyList(replyList);
                        int childId = position - commentList.getFirstVisiblePosition();

                        if (childId >= 0) {
                            View viewItem = commentList.getChildAt(childId);
                            if (viewItem != null) {
                                LinearLayout ll_hideReply = (LinearLayout) viewItem.findViewById(R.id.ll_hideReply);
                                TextView tv_reply_shouqi = (TextView) viewItem.findViewById(R.id.tv_repli_shouqi);
                                TextView LookReplyText = (TextView) viewItem.findViewById(R.id.LookReplyText);
                                NoScrollListView replyList = (NoScrollListView) viewItem.findViewById(R.id.replyList);
                                if (replyList != null) {
                                    list.get(position).setReplyList(list.get(position).getReplyList());
                                    ReplyAdapter replyAdapter = new ReplyAdapter(TalkActivity.this, list.get(position).getReplyList(), R.layout.reply_item, handler, position);
                                    replyList.setAdapter(replyAdapter);
                                    ll_hideReply.setVisibility(View.VISIBLE);
                                    LookReplyText.setVisibility(View.INVISIBLE);
                                    adapter.second[position] = 1;
                                }

                            }
                        }
                    }
                    break;
                }
                case 7: {
                    if (replyList != null && replyList.size() > 0) {
                        list.get(position).setReplyList(replyList);
                        Log.d("TAG_2", "case 2：" + list.size());
                        Log.d("TAG_2", "case 2：" + replyList.size());
                        int childId = position - commentList.getFirstVisiblePosition();

                        if (childId >= 0) {
                            View viewItem = commentList.getChildAt(childId);
                            if (viewItem != null) {

                                NoScrollListView replyList = (NoScrollListView) viewItem.findViewById(R.id.replyList);
                                LinearLayout ll_hideReply = (LinearLayout) viewItem.findViewById(R.id.ll_hideReply);
                                TextView tv_reply_shouqi = (TextView) viewItem.findViewById(R.id.tv_repli_shouqi);
                                TextView LookReplyText = (TextView) viewItem.findViewById(R.id.LookReplyText);
                                if (adapter.second[position] == 0) {
                                    if (replyList != null) {
                                        list.get(position).setReplyList(list.get(position).getReplyList());
                                        ReplyAdapter replyAdapter = new ReplyAdapter(TalkActivity.this, list.get(position).getReplyList(), R.layout.reply_item, handler, position);
                                        replyList.setAdapter(replyAdapter);
                                        ll_hideReply.setVisibility(View.VISIBLE);
                                        LookReplyText.setVisibility(View.INVISIBLE);
                                        adapter.second[position] = 1;

                                    }
                                }
                            }
                        }
                    }
                    break;
                }
                case 8: {
                    int childId = position - commentList.getFirstVisiblePosition();
                    if (childId >= 0) {
                        View viewItem = commentList.getChildAt(childId);
                        if (viewItem != null) {
                            LinearLayout ll_hideReply = (LinearLayout) viewItem.findViewById(R.id.ll_hideReply);
                            TextView tv_reply_shouqi = (TextView) viewItem.findViewById(R.id.tv_repli_shouqi);
                            TextView LookReplyText = (TextView) viewItem.findViewById(R.id.LookReplyText);
                            if (adapter.second[position] == 1) {
                                ll_hideReply.setVisibility(View.GONE);
                                LookReplyText.setVisibility(View.VISIBLE);
                                adapter.second[position] = 0;
                            }


                        }
                    }

                    break;
                }

            }
            if (isReply) {
                isReply = false;
            }
            if (progressDialog != null) {
                progressDialog.closeProcessDialog();
            }
        }
    };

    @SuppressLint("HandlerLeak") //10是回复  11是点赞
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Message m = new Message();
            if (msg.what == 10) {
                isReply = true;
                position = (Integer) msg.obj;
//                Toast.makeText(TalkActivity.this, "开始回复" + position, Toast.LENGTH_SHORT).show();
                onFocusChange(true);
            } else if (msg.what == 11) {
                position = (Integer) msg.obj;
//                Toast.makeText(TalkActivity.this, "已点赞" + position, Toast.LENGTH_SHORT).show();
                //点赞的逻辑方法
                postZan();
            } else if (msg.what == 12) {
                position = (Integer) msg.obj;
//                Toast.makeText(TalkActivity.this, "查看回复" + position, Toast.LENGTH_SHORT).show();
                //查看回复的逻辑方法
//                list.get(position).setIsExpend(true);
                lookReply();
            } else if (msg.what == 13) {
                position = (Integer) msg.obj;
//                Toast.makeText(TalkActivity.this, "收起" + position, Toast.LENGTH_SHORT).show();
                m.what = 8;
                messageListHandler.sendMessage(m);
            } else if (msg.what == 14) {
                bean = new ReplyPostBean();
                bean = (ReplyPostBean) msg.obj;
//                Toast.makeText(TalkActivity.this, "我是回复的人:"+bean.getBeReplyName() , Toast.LENGTH_SHORT).show();
//                m.what = 8;
//                messageListHandler.sendMessage(m);
            } else if (msg.what == 15) {
                bean = new ReplyPostBean();
                bean = (ReplyPostBean) msg.obj;
//                Toast.makeText(TalkActivity.this, "我是被回复的人:"+bean.getBeReplyName() , Toast.LENGTH_SHORT).show();
//                m.what = 8;
//                messageListHandler.sendMessage(m);
            }
        }
    };

    private void postZan() {
//        progressDialog = new ProgressDialog(this);
//        progressDialog.showProcessDialog();
        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                initZanData(list.get(position).getCommnetId());
            }
        });
        mThread.start();
    }

    private void initZanData(String MESSAGEID) {
        JSONObject requestJson = new JSONObject();
        userId = sharedPreferences.getString("userId", "");
        Log.d("TAG_2", "MESSAGEID:" + MESSAGEID);
        requestJson.put("UserID", userId);
        requestJson.put("INSERT", "2");
        requestJson.put("MESSAGEID", MESSAGEID);
        String url = "tz/TZDeviceAction.do?method=MessageVer";
        Message m = new Message();
        try {
            result = Communication.getPostResponseForNetManagement(url, requestJson.toString());
            Log.d("TAG_2", "回复result：" + result);
            if (StringUtil.isBlank(result) || !result.contains("VCUSERID")) {

                m.what = 3;
                messageListHandler.sendMessage(m);
                return;
            }
            if (result.contains("解压或解密")) {
                m.what = 3;
                messageListHandler.sendMessage(m);
                return;
            }
//            getReplyData(result);
//
            m.what = 6;
            messageListHandler.sendMessage(m);
        } catch (IOException e) {
            m.what = 3;
            messageListHandler.sendMessage(m);
            e.printStackTrace();
        }
    }

    private void lookReply() {
        progressDialog = new ProgressDialog(this);
        progressDialog.showProcessDialog();
        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                initReplyTalkData(list.get(position).getCommnetId());
            }
        });
        mThread.start();
    }

    public void initReplyTalkData(String MESSAGEID) {
        JSONObject requestJson = new JSONObject();
        userId = sharedPreferences.getString("userId", "");
        Log.d("TAG_2", "MESSAGEID:" + MESSAGEID);
        requestJson.put("UserID", userId);
        requestJson.put("INSERT", "0");
        requestJson.put("MESSAGEID", MESSAGEID);
        String url = "tz/TZDeviceAction.do?method=MessageVer";
        Message m = new Message();
        try {
            result = Communication.getPostResponseForNetManagement(url, requestJson.toString());
            Log.d("TAG_2", "回复result：" + result);
            if (StringUtil.isBlank(result) || !result.contains("VCUSERID")) {

                m.what = 3;
                messageListHandler.sendMessage(m);
                return;
            }
            if (result.contains("解压或解密")) {
                m.what = 3;
                messageListHandler.sendMessage(m);
                return;
            }
//            if (replyList != null && replyList.size() > 0) {
//                replyList.clear();
//            }
            getReplyData(result);

            m.what = 7;
            messageListHandler.sendMessage(m);
        } catch (IOException e) {
            m.what = 3;
            messageListHandler.sendMessage(m);
            e.printStackTrace();
        }
    }

    private void getReplyData(String result) {
        replyList = new ArrayList<>();

        JSONObject returnJson = JSON.parseObject(result);
        JSONArray returnArray = returnJson.getJSONArray("allist");
        for (int i = 0; i < returnArray.size(); i++) {
            JSONObject obj = returnArray.getJSONObject(i);
            ReplyBean replyBean = new ReplyBean();
            replyBean.setCommentUserId(obj.getString("REPLYUSERID"));//被回复人的userId
            replyBean.setCommentNickname(obj.getString("REPLYNAME"));//被回复人的名字
            replyBean.setReplyContent(obj.getString("MESSAGE"));//回复内容
            replyBean.setId(obj.getString("REPLYID"));//回复内容ID
            replyBean.setReplyUserId(obj.getString("VCUSERID"));//回复人的userId
            replyBean.setReplyNickname(obj.getString("VCREALNAME"));//回复人的名字
            replyList.add(replyBean);
        }
    }


    /**
     * 向服务器提交回复评论
     */
    private void replyComment() {
        initPostThread();
    }

    /**
     * 向服务器提交发表评论
     */
    private void publishComment() {
        initPostThread();
    }

    /**
     * 向服务器提交发表评论
     */
    private void initPostThread() {

        progressDialog = new ProgressDialog(this);
        progressDialog.showProcessDialog();

        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                initPostData();
            }
        });
        mThread.start();
    }

    public void initPostData() {
        JSONObject requestJson = new JSONObject();
        userId = sharedPreferences.getString("userId", "");
        Log.d("TAG", "isReply:" + isReply);
        if (isReply) {
            Log.d("TAG", "REPLYUSERID:" + userId + "||MESSAGE:" + comment + "||UserID:" + list.get(position).getCommnetUserId() + "||MESSAGEID:" + list.get(position).getCommnetId());
            requestJson.put("UserID", userId);//回复人ID
            requestJson.put("INSERT", "1");
            requestJson.put("MESSAGE", comment);//回复内容
            requestJson.put("MESSAGEID", list.get(position).getCommnetId());//留言ID
            requestJson.put("REPLYUSERID", list.get(position).getCommnetUserId());//被回复人ID
        } else {
            requestJson.put("UserID", userId);
            requestJson.put("INSERT", "1");
            requestJson.put("MESSAGE", comment);
        }
        String url = "tz/TZDeviceAction.do?method=MessageVer";
        Message m = new Message();
        try {
            result = Communication.getPostResponseForNetManagement(url, requestJson.toString());
            Log.d("TAG_2", "提交留言result：" + result);
            if (StringUtil.isBlank(result) || !result.contains("VCUSERID")) {
                m.what = 4;
                messageListHandler.sendMessage(m);
                return;
            }
            if (result.contains("解压或解密")) {
                m.what = 3;
                messageListHandler.sendMessage(m);
                return;
            }

            isClearWoList = true;
            //下拉刷新
            mRefreshMode = PULL_DOWN_TO_REFRESH;
            message = 1;
            pageNo = 0;
            refreshLayout.setCanLoadMore(true);
            if (isReply) {
                if (replyList != null && replyList.size() > 0) {
                    replyList.clear();
                }
                getReplyData(result);
                m.what = 5;
                messageListHandler.sendMessage(m);
                return;
            }
            getMessageData(result);
            m.what = 2;
            messageListHandler.sendMessage(m);
            return;
        } catch (IOException e) {
            m.what = 3;
            messageListHandler.sendMessage(m);
            e.printStackTrace();
        }
    }

    /**
     * 事件点击监听器 发表评论按钮向服务器post数据
     */
    private final class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.commentButton:    //发表评论按钮
                    if (isEditEmply()) {        //判断用户是否输入内容
                        if (isReply) {
                            Log.d("TAG", "回复");
                            replyComment();
                        } else {
                            publishComment();
                        }
                        commentEdit.setText("");
                        onFocusChange(false);
                    }
                    break;

            }
        }
    }

    private void resterlistener() {
        ClickListener cl = new ClickListener();
        commentButton.setOnClickListener(cl);
        back_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        refreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                isClearWoList = true;
                //下拉刷新
                mRefreshMode = PULL_DOWN_TO_REFRESH;
                message = 1;
                pageNo = 0;
                refreshLayout.setCanLoadMore(true);
                initDataThread(true);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //上拉加载
                mRefreshMode = PULL_UP_TO_LOAD_MORE;
                pageNo = pageNo + 5;
                message = 2;
                isClearWoList = false;
                initDataThread(true);
            }
        });
    }

    /**
     * 判断对话框中是否输入内容
     */
    private boolean isEditEmply() {
        comment = commentEdit.getText().toString().trim();
        if (comment.equals("") || comment == null) {
            Toast.makeText(this, "评论不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 显示或隐藏输入法
     */
    private void onFocusChange(boolean hasFocus) {
        final boolean isFocus = hasFocus;
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        commentEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (isFocus) {
//                    if (position != -1){
//                        commentEdit.setHint("回复"+list.get(position).getCommentNickname()+":");
//                    }
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    isShowInput = true;

                } else {
//                    commentEdit.setHint("请输入评论");

                    //隐藏输入法
                    imm.hideSoftInputFromWindow(commentEdit.getWindowToken(), 0);
                    isShowInput = false;
                }
            }
        }, 100);
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }
}
