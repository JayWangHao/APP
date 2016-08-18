package com.cattsoft.wow.talk;

import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cattsoft.wow.R;
import com.cattsoft.wow.talk.CommentBean;
import com.cattsoft.wow.talk.NoScrollListView;
import com.cattsoft.wow.talk.ReplyAdapter;
import com.cattsoft.wow.talk.ReplyBean;


public class CommentAdapter extends BaseAdapter {

    private int resourceId;
    private Context context;
    private Handler handler;
    private List<CommentBean> list;
    private LayoutInflater inflater;
    private Boolean isHaveReply = false;
    private Boolean isExpend = false;
    public int first[];
    public int second[];


    public CommentAdapter(Context context, List<CommentBean> list, int resourceId, Handler handler) {
        this.list = list;
        this.context = context;
        this.handler = handler;
        this.resourceId = resourceId;
        inflater = LayoutInflater.from(context);
        first = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            first[i] = 0;
        }
        second = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            second[i] = 0;
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CommentBean bean = list.get(position);
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(resourceId, null);
            holder.commentNickname = (TextView) convertView.findViewById(R.id.commentNickname);
            holder.replyText = (TextView) convertView.findViewById(R.id.replyText);
            holder.LookReplyText = (TextView) convertView.findViewById(R.id.LookReplyText);
            holder.tv_zan = (TextView) convertView.findViewById(R.id.tv_zan);
            holder.iv_zan = (ImageView) convertView.findViewById(R.id.iv_zan);
            holder.commentItemTime = (TextView) convertView.findViewById(R.id.commentItemTime);
            holder.commentItemContent = (TextView) convertView.findViewById(R.id.commentItemContent);
            holder.replyList = (NoScrollListView) convertView.findViewById(R.id.replyList);
            holder.ll_hideReply = (LinearLayout) convertView.findViewById(R.id.ll_hideReply);
            holder.tv_reply_shouqi = (TextView) convertView.findViewById(R.id.tv_repli_shouqi);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        isHaveReply = bean.getHaveReply();
        holder.commentNickname.setText(bean.getCommentNickname());
        holder.commentItemTime.setText(bean.getCommentTime());
        holder.commentItemContent.setText(bean.getCommentContent());
        holder.tv_zan.setText(bean.getCommentZan() + "");

        if (isHaveReply) {
            holder.LookReplyText.setVisibility(View.VISIBLE);
            holder.LookReplyText.setText("查看对话(" + bean.getReplyCount() + ")");
        } else {
            holder.LookReplyText.setVisibility(View.INVISIBLE);
        }

        if (first[position] == 0) {
            holder.iv_zan.setBackgroundResource(R.drawable.zan);
        } else {
            holder.iv_zan.setBackgroundResource(R.drawable.zan_press);
        }

        if (second[position] == 0) {
            holder.ll_hideReply.setVisibility(View.GONE);

        } else {
            list.get(position).setReplyList(list.get(position).getReplyList());
            ReplyAdapter replyAdapter = new ReplyAdapter(context, list.get(position).getReplyList(), R.layout.reply_item,this.handler,position);
            holder.replyList.setAdapter(replyAdapter);
            holder.ll_hideReply.setVisibility(View.VISIBLE);
            holder.LookReplyText.setVisibility(View.INVISIBLE);
        }


        TextviewClickListener tcl = new TextviewClickListener(position);
        holder.replyText.setOnClickListener(tcl);
        holder.LookReplyText.setOnClickListener(tcl);
        holder.iv_zan.setOnClickListener(tcl);
        holder.tv_reply_shouqi.setOnClickListener(tcl);
        return convertView;
    }

    private final class ViewHolder {
        public TextView commentNickname;            //评论人昵称
        public TextView replyText;                    //回复
        public TextView LookReplyText;                    //查看回复
        public ImageView iv_zan;                    //点赞
        public TextView tv_zan;                    //点赞数量
        public TextView commentItemTime;            //评论时间
        public TextView commentItemContent;            //评论内容
        public NoScrollListView replyList;            //评论回复列表
        public LinearLayout ll_hideReply;            //隐藏布局
        public TextView tv_reply_shouqi;                //收起
    }

    /**
     * 把从服务器获取的最新评论添加到最后一个位置
     */
    public void getReplyComment(ReplyBean bean, int position) {
        List<ReplyBean> rList = list.get(position).getReplyList();
        rList.add(rList.size(), bean);
    }

    /**
     * 事件点击监听器
     */
    private final class TextviewClickListener implements OnClickListener {

        private int position;

        public TextviewClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.replyText:
                    handler.sendMessage(handler.obtainMessage(10, position));
                    break;
                case R.id.iv_zan:
                    if (first[position] == 0) {
                        handler.sendMessage(handler.obtainMessage(11, position));
                    } else {
                        Toast.makeText(context, "已点赞", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.LookReplyText:
                    handler.sendMessage(handler.obtainMessage(12, position));
                    break;
                case R.id.tv_repli_shouqi:
                    handler.sendMessage(handler.obtainMessage(13, position));
                    break;
            }
        }
    }

}
