package com.example.test;

import com.example.demo.control.login;

public class TestCancelAttention {
    private String userid;
    private String attentionid;

    public String getUserid() {
        return userid;
    }
    public void TestForCancelAttention(){
        new login ().cancel_link (userid,attentionid);
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAttentionid() {
        return attentionid;
    }

    public void setAttentionid(String attentionid) {
        this.attentionid = attentionid;
    }
}
