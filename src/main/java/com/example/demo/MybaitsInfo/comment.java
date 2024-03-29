package com.example.demo.MybaitsInfo;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;


public class comment {

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
    public JSONObject to_js(int i){
        JSONObject jsonObject=new JSONObject ();
        try {
//            jsonObject.put ("fromId",FromId);
            jsonObject.put ("toId",ToId);
            jsonObject.put ("comment",CommentContext);
            jsonObject.put ("time",Time);
            jsonObject.put ("like",Integer.parseInt (CommentLikeCount));
            jsonObject.put ("inputShow",false);
            jsonObject.put ("commentNum",i);
            jsonObject.put("commentId",CommentId);
//            jsonObject.put ("fromHeadImg","http://localhost/files/photo/15900768000.jpg");
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        return jsonObject;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
