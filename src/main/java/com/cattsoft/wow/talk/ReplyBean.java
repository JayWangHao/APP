package com.cattsoft.wow.talk;

public class ReplyBean {

	private String id;					//内容ID
	private String replyUserId;	//回复人账号
	private String replyNickname;	//回复人昵称
	private String commentUserId;	//被回复人账号
	private String commentNickname;	//被回复人昵称
	private String replyContent;	//回复的内容
	private String commentId; //所属留言ID

	public String getId() {
		return id;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public void setId(String id) {

		this.id = id;
	}
	public String getReplyUserId() {
		return replyUserId;
	}
	public void setReplyUserId(String replyUserId) {
		this.replyUserId = replyUserId;
	}
	public String getReplyNickname() {
		return replyNickname;
	}
	public void setReplyNickname(String replyNickname) {
		this.replyNickname = replyNickname;
	}
	public String getCommentUserId() {
		return commentUserId;
	}
	public void setCommentUserId(String commentUserId) {
		this.commentUserId = commentUserId;
	}
	public String getCommentNickname() {
		return commentNickname;
	}
	public void setCommentNickname(String commentNickname) {
		this.commentNickname = commentNickname;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

}
