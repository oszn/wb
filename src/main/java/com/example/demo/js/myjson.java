package com.example.demo.js;

import com.google.gson.annotations.JsonAdapter;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class myjson {
    private JSONObject job=new JSONObject ();
    public JSONObject getJob(){return job;}
    public myjson(String id,String name,String count){
        try {

            job.put ("myId",id);
            job.put ("myName",name);
            job.put ("WeiboCount",count);
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    }
    public void insert(String fans,String guanzhu){
        try {
            job.put ("fansCount",fans);
            job.put ("guanzhu",guanzhu);
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    }

}
