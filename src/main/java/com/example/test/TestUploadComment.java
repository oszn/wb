package com.example.test;

import com.example.demo.control.login;

import java.util.Map;

public class TestUploadComment {
    public Integer getCommentId() {
        return CommentId;
    }

    public void setCommentId(Integer commentId) {
        CommentId = commentId;
    }

    public Integer getWeibold() {
        return WeiboId;
    }

    public void setWeibold(Integer weibold) {
        WeiboId = weibold;
    }

    public Integer getFromId() {
        return FromId;
    }

    public void setFromId(Integer fromId) {
        FromId = fromId;
    }

    public Integer getTold() {
        return ToId;
    }

    public void setTold(Integer told) {
        ToId = told;
    }

    public String getCommentContext() {
        return CommentContext;
    }

    public void setCommentContext(String commentContext) {
        CommentContext = commentContext;
    }

    public String getCommentLikeCount() {
        return CommentLikeCount;
    }

    public void setCommentLikeCount(String commentLikeCount) {
        CommentLikeCount = commentLikeCount;
    }
    private Integer CommentId;
    private Integer WeiboId;
    private Integer FromId;
    private Integer ToId;
    private String CommentContext;
    private String CommentLikeCount;
    private String Time;
    public String TestForComment(String id, Map<String,String> map){
        return new login ().xxxsad (id,map);
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
