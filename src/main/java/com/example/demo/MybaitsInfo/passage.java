package com.example.demo.MybaitsInfo;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass () != o.getClass ()) return false;
        passage passage = (passage) o;
        return WeiboId.equals (passage.WeiboId) &&
                Id.equals (passage.Id) &&
                Time.equals (passage.Time) &&
                LikeCount.equals (passage.LikeCount) &&
                WeiBoContext.equals (passage.WeiBoContext);
    }

    @Override
    public int hashCode() {
        return Objects.hash (WeiboId, Id, Time, LikeCount, WeiBoContext);
    }

    public JSONObject get_js(){
        JSONObject temp=new JSONObject ();
        try {
            temp.put ("id",Id);
            temp.put ("time",Time);
            temp.put ("like",Integer.parseInt (LikeCount));
            temp.put ("comment",WeiBoContext);
            temp.put ("inputShow",false);
            temp.put ("weiboId",WeiboId);
//            temp.put ("headImg","http://localhost/files/photo/15902053910.png");
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
