package com.example.test;

import com.example.demo.control.login;

public class TestAttention {
    private String attentionID;
    private String userid;
    public void TestForAttention(){
        new login ().link (userid,attentionID);
    }

    public String getAttentionID() {
        return attentionID;
    }

    public void setAttentionID(String attentionID) {
        this.attentionID = attentionID;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
