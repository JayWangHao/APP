package com.cattsoft.wow.talk;

/**
 * Created by sjzb on 2016/6/29.
 */
public class ReplyPostBean {
    private int commentPosition;//条目位置
    private String BeReplyUesrId;//被回复人ID
    private String BeReplyName;//被回复人名字

    public String getBeReplyName() {
        return BeReplyName;
    }

    public void setBeReplyName(String beReplyName) {
        BeReplyName = beReplyName;
    }

    public int getCommentPosition() {
        return commentPosition;
    }

    public void setCommentPosition(int commentPosition) {
        this.commentPosition = commentPosition;
    }

    public String getBeReplyUesrId() {
        return BeReplyUesrId;
    }

    public void setBeReplyUesrId(String beReplyUesrId) {
        BeReplyUesrId = beReplyUesrId;
    }
}
