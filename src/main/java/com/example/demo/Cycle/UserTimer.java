package com.example.demo.Cycle;

import com.example.mySql.bzd;
import com.example.neo.tfs;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component                //实例化
@Configurable             //注入bean
@EnableScheduling         //开启计划任务
public class UserTimer {
    @Scheduled(fixedRate = 4*60*60*1000)
    public void doTimeUpdate(){
        bzd bzd=new bzd ();
        bzd.set_all_info ();
        bzd.close_conn ();
    }


}
