package com.example.demo.TimerCyclePlan;

import com.example.mySql.Mysql;
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
        Mysql Mysql =new Mysql ();
        Mysql.set_all_info ();
        Mysql.close_conn ();
    }


}
