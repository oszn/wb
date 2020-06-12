package com.example.test;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.MybaitsInfo.passage;
import com.example.demo.control.login;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Objects;

public class TestUploadPassage {
    public String getWeiboId() {
        return WeiboId;
    }

    public void setWeiboId(String weiboId) {
        WeiboId = weiboId;
    }

    private String WeiboId;
    private String Id;
    private String Time;
    private String LikeCount;
    public String TestForPassage(String userid, Map<String,String> map, MultipartFile files[]){
        return new login ().asdax(userid,map,files);
    }
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getLikeCount() {
        return LikeCount;
    }

    public void setLikeCount(String likeCount) {
        LikeCount = likeCount;
    }

    public String getWeiBoContext() {
        return WeiBoContext;
    }

    public void setWeiBoContext(String weiBoContext) {
        WeiBoContext = weiBoContext;
    }

    private String WeiBoContext;
}
