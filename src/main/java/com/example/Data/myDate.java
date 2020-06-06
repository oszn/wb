package com.example.Data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class myDate {


    public String get_time(int nday){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = new Date();
        String endDate = sdf.format(today);//当前日期
        //获取三十天前日期
        Calendar theCa = Calendar.getInstance();
        theCa.setTime(today);
        theCa.add(theCa.DATE, -nday);//最后一个数字30可改，30天的意思
        Date start = theCa.getTime();
//        long time = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(start.toString (), new ParsePosition (0)).getTime() / 1000;
        return sdf.format (start);
//        String startDate = sdf.format(start);//三十天之前日期
    }

    public static void main(String[] args) {
        myDate my=new myDate ();
        System.out.println (my.get_time (20));

    }

}
