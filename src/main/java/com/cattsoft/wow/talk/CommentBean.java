package com.cattsoft.wow.talk;


import java.util.ArrayList;
import java.util.List;

public class CommentBean {

	private String commnetId;					//评论记录ID
	private String CommnetUesrId;	//评论人账号
	private String commentNickname;	//评论人昵称
	private String commentTime;		//评论时间
	private String commentContent;	//评论内容
	private Boolean haveReply;	//是否有回复
	private int ReplyCount;	//回复数量
	private Boolean isExpend;//是否展开

	public int getReplyCount() {
		return ReplyCount;
	}

	public void setReplyCount(int replyCount) {
		ReplyCount = replyCount;
	}

	public Boolean getIsExpend() {
		return isExpend;
	}

	public void setIsExpend(Boolean isExpend) {
		this.isExpend = isExpend;
	}

	public Boolean getHaveReply() {
		return haveReply;
	}

	public void setHaveReply(Boolean haveReply) {
		this.haveReply = haveReply;
	}

	public int getCommentZan() {
		return commentZan;
	}

	public void setCommentZan(int commentZan) {
		this.commentZan = commentZan;
	}

	private int commentZan;	//赞的数量
	private List<ReplyBean> replyList = new ArrayList<ReplyBean>();
	//回复内容列表
	public String getCommnetId() {
		return commnetId;
	}
	public void setCommnetId(String id) {
		this.commnetId = id;
	}
	public String getCommnetUserId() {
		return CommnetUesrId;
	}
	public void setCommnetUesrId(String CommnetUesrId) {
		this.CommnetUesrId = CommnetUesrId;
	}
	public String getCommentNickname() {
		return commentNickname;
	}
	public void setCommentNickname(String commentNickname) {
		this.commentNickname = commentNickname;
	}
	public String getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public List<ReplyBean> getReplyList() {
		return replyList;
	}
	public void setReplyList(List<ReplyBean> replyList) {
		this.replyList = replyList;
	}

}
