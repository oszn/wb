package com.example.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.control.login;

public class TestSearch {
    private String searchword;

    public String getSearchword() {
        return searchword;
    }

    public void setSearchword(String searchword) {
        this.searchword = searchword;
    }
    public void TestForSearch(String userid,String text){
        JSONObject JS= new login ().search (userid,text);
    }

}
