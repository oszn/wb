package com.example.test;

import com.example.demo.control.login;

public class TestRegister {
    private String name;
    private String email;
    private String acount;
    private String pwd;
    public void TestForRegister(String yzm){
        new login ().login_x (acount,pwd,yzm,name,email);
    }
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getAcount() {
        return acount;
    }

    public void setAcount(String acount) {
        this.acount = acount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
