package com.example.demo.person;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class passage {
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

    public JSONObject get_js(){
        JSONObject temp=new JSONObject ();
        try {
            temp.put ("id",Id);
            temp.put ("time",Time);
            temp.put ("like",Integer.parseInt (LikeCount));
            temp.put ("comment",WeiBoContext);
            temp.put ("inputShow",false);
            temp.put ("weiboId",WeiboId);
//            temp.put ("headImg","http://121.89.166.24/files/photo/15902053910.png");
        } catch (JSONException e) {
            e.printStackTrace ();
        }

        return temp;
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
