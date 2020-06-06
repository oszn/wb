package com.example.demo.cl;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.fenci.Sen2Word;
import com.example.configs.mytoken.loginTickets;
import com.example.demo.send_file.amd;
import com.example.mail.rest_send;
import com.example.mySql.bzd;
import com.example.neo.tfs;
import com.alibaba.fastjson.JSONObject;
import com.example.red.fredis;
import org.apache.ibatis.annotations.Param;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
public class  login {
    public int grand(){
      return  (int)(10000+Math.random()*(100000-10000+1));
    }
    private amd dx=null;
    private rest_send re=null;
    private Map<String,String> user_map;
    private fredis template_redis;
    private Map<String,String> yzm_map;
    private Sen2Word sens;
    public String addLoginTicket(int user_id){
        loginTickets ticket = new loginTickets();
        ticket.setUserId(user_id);
        Date nowDate = new Date();
        nowDate.setTime(3600*24*100 + nowDate.getTime());
        ticket.setExpired(nowDate);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("_",""));
        template_redis.tostr (ticket.getTicket (),String.valueOf (ticket.getUserId ()));
        return ticket.getTicket();

    }

    public login(){
        re=new rest_send ();
        dx=new amd ();
        user_map=new HashMap<> ();
        yzm_map=new HashMap<> ();
        sens=new Sen2Word ();
        template_redis=new fredis ();
//        fredis fredis=new fredis ();
//        tfs tfs=new tfs ();
//        Map<String,List<String>> imp=new HashMap<> ();
//        for(int i=0;i<15;i++){
//            System.out.println (i);
//            List<String> impword=tfs.impWord (String.valueOf (i));
//            fredis.toList (String.valueOf (i),impword);
//
//        }
        System.out.println ("end");

    }
    @ResponseBody
    @RequestMapping("/index")
    @CrossOrigin(origins = "http://localhost:8088")
    public String hello(HttpServletResponse response)     {
        Cookie c=new Cookie ("user","122");
    //        Session.Cookie cookie=new Session.Cookie ("user",user);
        response.addCookie (c);
        return "hello";
    }

    @ResponseBody
    @RequestMapping("/upload/headImg")
    @CrossOrigin(origins = "http://localhost:8088",allowCredentials = "true")
    public String headimg(@CookieValue("user")String user,@RequestParam("file")MultipartFile files[]){
        bzd bzd=new bzd ();
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        String time=sdf.format(date);
        String url=null;
        for(MultipartFile file:files){
//            long name=
            long name = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(time, new ParsePosition (0)).getTime() / 1000;
            dx.dd (String.valueOf (name),file);
            String f=file.getOriginalFilename ().substring (file.getOriginalFilename ().lastIndexOf ('.'));
            url = "http://121.89.166.24/files/photo/" + String.valueOf (name) + f;
            bzd.update_head_img (user,url);
        }
        bzd.close_conn();
        return url;
    }

    @ResponseBody
    @RequestMapping("/register/yzm")
    @CrossOrigin(origins = "http://localhost:8088")
    public String mm(@RequestParam("email")String email){
        bzd bzd=new bzd ();
        int rand=grand ();
        String res=bzd.sl_user_email (email);
        if(res.contains ("ok:"))
        {
            bzd.close_conn();
            return "error:邮箱已存在";
        }
        else {
            yzm_map.put (email, String.valueOf (rand));
            re.sendEmailBatch (email,String.valueOf (rand));
            bzd.close_conn();
            return "ok";
        }
    }

    @ResponseBody
    @RequestMapping("/register")
    @CrossOrigin(origins = "http://localhost:8088",
            allowCredentials = "true")
    public String login_x(@RequestParam("user")String user,@RequestParam("pwd")String pwd,@RequestParam("yzm")String yzm,@RequestParam("email")String email,@RequestParam("name")
            String name){
        bzd bzd=new bzd();
        tfs tfs=new tfs();
        System.out.println (user);
        System.out.println (pwd);
        String yanzhengma=yzm_map.get (email);
        if(yanzhengma.equals (yzm)){
            String answer=bzd.login (user,pwd,email,name);
            System.out.println (email);
            tfs.insert_user (user);
            if(answer.equals ("ok:用户插入成功")) {
//           re.sendEmailBatch(email,"注册成功！！！");
                yzm_map.remove (email);
                bzd.close_conn();
                tfs.close_conn();
                return answer;
            }
            bzd.close_conn();
            tfs.close_conn();
            return answer;
        }else {
            bzd.close_conn();
            tfs.close_conn();
            return "error:验证码错误";
        }
    }

    @ResponseBody
    @RequestMapping("/login")
    @CrossOrigin(origins = "http://localhost:8088",
            allowCredentials = "true")
    public String login_deng(HttpServletRequest request, @RequestParam("user")String user, @RequestParam("pwd")String pwd, HttpServletResponse response) {
        bzd bzd=new bzd();
        System.out.println (user);
        System.out.println (pwd);
        Cookie cookie=new Cookie ("user",user);
        String answer=bzd.denglu (user,pwd);
        response.addCookie (cookie);
        bzd.close_conn();
        return answer;
    }

    @ResponseBody
    @RequestMapping("/change")
    @CrossOrigin(origins = "http://localhost:8088",
            allowCredentials = "true")
    public String nobb( @RequestParam("email")String email) {
        bzd bzd=new bzd();
        int rand=grand ();
        String res=bzd.sl_user_email (email);
        System.out.println (res);
        user_map.put (email,String.valueOf (rand));
        re.sendEmailBatch (email,String.valueOf (rand));
        bzd.close_conn();
        return res;
    }


    @ResponseBody
    @RequestMapping("/reset")
    @CrossOrigin(origins = "http://localhost:8088",
            allowCredentials = "true")
    public String xxxbb(@RequestParam("yzm")String yzm,@RequestParam("email") String email, HttpServletResponse response,@RequestParam("pwd")String pwd) {
        bzd bzd=new bzd();
        String wd=user_map.get (email);
        System.out.println (email);
        System.out.println (user_map.keySet ());
        System.out.println (user_map.get (email));
        System.out.println (yzm);

        if(wd.equals (yzm)){
            String res=bzd.sl_user_email (email);
            if(res.contains ("ok")) {
                System.out.println (res);
//                String res=bzd.sl_user_email (email);
                System.out.println (res);
//                System.out.println (res.replaceFirst ("ok:",""));
                String result=bzd.update_u (res.replaceFirst ("ok:",""), pwd);
                Cookie c = new Cookie ("user", res.replaceFirst ("ok:",""));
                response.addCookie (c);
                user_map.remove (email);
                bzd.close_conn();
                return "ok";
            }
            else{
                bzd.close_conn();
                return "error:执行错误";
            }
        }
        bzd.close_conn();
        return "error";
    }


//    @ResponseBody
//    @RequestMapping("/reset")
//    @CrossOrigin
//    public String asdaxada(@RequestParam("pwd")String pwd,@CookieValue("user") String session) {
//        if (session != null) {
//            String result=bzd.update_u (session, pwd);
//
//            return result;
//        }
//        return "error";
//    }
public static String get(String p) {
    String path= null;
    try {
        path = "http://121.89.166.24:2000/vec?word="+java.net.URLEncoder.encode (p,"utf-8");
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace ();
    }
    System.out.println (path);
    HttpURLConnection httpConn=null;
    BufferedReader in=null;
    try {
        URL url=new URL(path);
        httpConn=(HttpURLConnection)url.openConnection();
        httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        httpConn.setRequestProperty("Charset", "utf-8");
        httpConn.setRequestProperty("Accept-Charset", "utf-8");
        //读取响应
        if(httpConn.getResponseCode()==HttpURLConnection.HTTP_OK){
            StringBuffer content=new StringBuffer();
            String tempStr="";
            in=new BufferedReader(new InputStreamReader (httpConn.getInputStream()));
            while((tempStr=in.readLine())!=null){
                content.append(tempStr);
            }
            return content.toString();
        }else{
            throw new Exception("请求出现了问题!");
        }
    } catch (IOException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace ();
    } finally{
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace ();
        }
        httpConn.disconnect();
    }
    return null;
}
    public String get_tp(String text){
        String res=get (text);
        String p[]=res.split (":");
        Map<String,Integer> map=new HashMap<> ();
        for(String s:p){
            if(map.containsKey (s)){
                map.put (s,map.get (s)+1);
            }else{
                map.put (s,1);
            }
        };
        int max=0;
        String tp = "";
        for(String key:map.keySet ()){
            if(max<map.get (key)){
                max=map.get (key);
                tp=key;
            }
        }
        return tp;
    }
    @ResponseBody
    @RequestMapping("/upload/weibo")
    @CrossOrigin(origins = "http://localhost:8088",
            allowCredentials = "true")//@CookieValue("user") String session,
    public String asdax( @CookieValue("user") String session, @RequestParam Map<String,String> map, @RequestParam("file")MultipartFile[] files) {
        System.out.println ("upload--begin");
        bzd bzd=new bzd();
        tfs tfs=new tfs ();
        if (session != null) {
            String content=map.get ("WeiBoContext").toString ();
            String time=map.get ("time").toString ();
            System.out.println (content);
            System.out.println (time);
            String tp=get_tp (content);
            String result=bzd.insert_weibo (session,time,content,tp);
            int id=bzd.how (session,time,content);
            sens.ins (content, String.valueOf (id),session,tp);
            long name = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(time, new ParsePosition (0)).getTime() / 1000;
//            String name=ts.toString ();
            int qq=0;
            for (MultipartFile file:files) {
                if(file.isEmpty ())
                    continue;
                dx.dd (String.valueOf (name)+String.valueOf (qq), file);
                String f = file.getOriginalFilename ().substring (file.getOriginalFilename ().lastIndexOf ('.'));
                String url = "http://121.89.166.24/files/photo/" + String.valueOf (name) +String.valueOf (qq)+ f;
                String r = bzd.insert_photo (String.valueOf (id), url);
                qq++;
            }
            /***
             * new mod
             * step 1 分类
             * step 2 将作者和文章都插入neo4j
             * step 3 将分类插入
             */

            bzd.close_conn();
            return result;
        }
        return "error";
    }



    @ResponseBody
    @RequestMapping("/upload/comment")
    @CrossOrigin(origins = "http://localhost:8088",
            allowCredentials = "true")
    public String xxxsad( @CookieValue("user") String session,@RequestParam Map<String,String> map){
        bzd bzd=new bzd();
        if (session != null) {
            String weiboId = map.get ("WeiboId").toString ();
            String FromeId = map.get ("FromId").toString ();
            String Told=map.get ("ToId").toString ();
            String context=map.get ("CommentContext").toString ();
            String time=map.get ("Time").toString ();
            String x=bzd.insert_comment (weiboId,FromeId,Told,context,time);
            bzd.close_conn();
            return x;
        }
        bzd.close_conn();
        return "error";
    }

    @ResponseBody
    @RequestMapping("/like/passage")
    @CrossOrigin(origins = "http://localhost:8088",
            allowCredentials = "true")
    public void like(@RequestParam("weiboid") String id){
        bzd bzd=new bzd();
        bzd.like (id);
        bzd.close_conn();
    }

    @ResponseBody
    @RequestMapping("/like/comment")
    @CrossOrigin(origins = "http://localhost:8088",
            allowCredentials = "true")
    public void like2(@RequestParam("commentid") String id){
        bzd bzd=new bzd();
        bzd.like2 (id);
        bzd.close_conn();
    }

    @ResponseBody
    @RequestMapping(value = "/respone/passage",produces="application/json;charset=UTF-8")
    @CrossOrigin(origins = "http://localhost:8088",
            allowCredentials = "true")
    public JSONObject web_back(@CookieValue("user") String id){
        bzd bzd=new bzd();
        JSONObject j=bzd.weibo_comment (id);
        bzd.close_conn();
        System.out.println(j);
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "/response/friend",produces="application/json;charset=UTF-8")
    @CrossOrigin(origins = "http://localhost:8088",
            allowCredentials = "true")
    public JSONObject fri_web_back(@CookieValue("user") String id){
        bzd bzd=new bzd();
        JSONObject j=bzd.fried_weibo (id);
        bzd.close_conn();
        return j;
    }

    @ResponseBody
    @RequestMapping("/link")
    @CrossOrigin(origins = "http://localhost:8088",
            allowCredentials = "true")
    public String link(@CookieValue("user")String user,@RequestParam("linked") String id){
        tfs tfs=new tfs();
        tfs.create_link (user,id);
//        tfs.close_conn();
        return "ok";
    }


    @ResponseBody
    @RequestMapping("/info/update")
    @CrossOrigin(origins = "http://localhost:8088",allowCredentials = "true")
    public JSONObject fans_update(@CookieValue("user")String user){
        bzd bzd=new bzd();
        tfs tfs=new tfs();
        JSONObject myjs=new JSONObject ();
        tfs.selectfans_at (user,myjs);
        String s=bzd.get_weibo_count (user);
        myjs.put ("weiboCount",Integer.parseInt (s));
        bzd.close_conn();
        tfs.close_conn();
        return myjs;
//        tfs.create_link (user,id);
//        return "ok";
    }

    public void nmdwsm(String x,JSONArray p){
        if(x.length ()>2){
            x=x.substring (2);
            String q[]=x.split (";");
            for(int i=0;i<q.length;i++){
                JSONObject user=new JSONObject ();
                String user_name[]=q[i].split (":");
                user.put ("id",user_name[0]);
                user.put ("name",user_name[1]);
                p.add (user);
            }
        }
    }

    @ResponseBody
    @RequestMapping("/info")
    @CrossOrigin(origins = "http://localhost:8088",allowCredentials = "true")
    public JSONObject inf(@CookieValue("user")String user){
        bzd bzd=new bzd();
        JSONObject j=bzd.get_own_inf (user);
        bzd.close_conn();
        return j;
    }


    @ResponseBody
    @RequestMapping("/list/fans")
    @CrossOrigin(origins = "http://localhost:8088",allowCredentials = "true")
    public JSONObject fans_list(@CookieValue("user")String user){
        bzd bzd=new bzd();
        JSONObject j=bzd.get_fans_info (user);
        bzd.close_conn();
        return j;
    }
    @ResponseBody
    @RequestMapping("/list/attention")
    @CrossOrigin(origins = "http://localhost:8088",allowCredentials = "true")
    public JSONObject attention_list(@CookieValue("user")String user){
        bzd bzd=new bzd();
        JSONObject j=bzd.get_attentions_info (user);
        bzd.close_conn();
        return j;
    }

    @ResponseBody
    @RequestMapping("/cancel/link")
    @CrossOrigin(origins = "http://localhost:8088",allowCredentials = "true")
    public String cancel_link(@CookieValue("user")String user,@RequestParam("id") String id){
        tfs tfs=new tfs();
        System.out.println (id);
        tfs.cancel_attention (user,id);
//        tfs.close_conn();
        return "ok";
    }
    public void prtTimer(long start){
        System.out.println (System.currentTimeMillis ()-start);
    }
    @ResponseBody
    @RequestMapping("/search")
    @CrossOrigin(origins = "http://localhost:8088",allowCredentials = "true")
    public JSONObject search(@CookieValue("user")String user,@RequestParam("text") String text){
        fredis r=new fredis ();
        r.set_hot_word (text);
        long start=System.currentTimeMillis ();
        bzd bzd=new bzd();
        prtTimer (start);
        List<String> pas=sens.search_w (text);
        String tp=get_tp (text);
        String tag="";
        for(int i=0;i<pas.size ();i++){
            tag+=pas.get (i)+" ";
        }
        bzd.insert_search_record (user,String.valueOf (start/1000),"\'"+text+"\'",tp);
        prtTimer (start);
        String up=bzd.select_user (text);
        JSONObject result=bzd.search_it (user,pas);
        JSONArray U=new JSONArray ();
        JSONArray P=new JSONArray ();
        String[] x=up.split ("\\*");
        nmdwsm (x[0],U);
        nmdwsm (x[1],P);
        result.put ("search_id",P);
        result.put ("search_user",U);
        bzd.close_conn();
        return result;
//        tfs.create_link (user,id);
//        return "ok";
    }
    /***
     * 2020/6/3 18:14
     * 添加点击头像返回信息界面
     * 参数1.cookie 2.点击人的账号
     */
    @ResponseBody
    @RequestMapping("/friend/info")
    @CrossOrigin(origins = "http://localhost:8088",allowCredentials = "true")
    public JSONObject friend_info(@CookieValue("user")String user,@RequestParam("id") String id){
        bzd bzd=new bzd();
        JSONObject jb=bzd.visit_info (user,id);
        bzd.close_conn ();
        return jb;
    }

    /***
     * 2020-6/5 23:13:11
     * 热搜通过redis来记录词语
     *返回为一个entryset
     * @param <>cp
     * 在/search那个函数前面加了2行。
     */
    @ResponseBody
    @RequestMapping("/hot")
    @CrossOrigin(origins = "http://localhost:8088",allowCredentials = "true")
    public JSONObject hot_info(){
        fredis r=new fredis ();
        List<Map.Entry<String, Integer>> c = r.getHot ("hot");
        JSONObject myjson=new JSONObject ();
        for(int i=0;i<Math.min (100,c.size ());i++){
            System.out.println (c.get(i).getValue ());
            JSONObject tj=new JSONObject ();
            tj.put (c.get (i).getKey (),c.get (i).getValue ());
            myjson.put (String.valueOf (i),tj);
        }
        return myjson;
    }

    /***
     * 多的不谈我快裂开了v0.1.1 板块-推荐
     * 架构 思路
     * step 1 首先搭建一个数据群，有类别有关键字的。
     * step 2 每次文章插入都会讲数据进行一次分类判断。然后插入数据库，连同文章，一起插入
     * step 3 每次搜索都会做一次类别判断并放入关键字，因为用户浏览的东西，无法返回，我不知道它点进去了啥。
     * step 4 推荐用户自己所发的内容做一个topn推荐，推荐3篇
     * step 5 用户搜索的关键字做一个topn的推荐，推荐2篇。
     * @param args
     */
    @ResponseBody
    @RequestMapping("/response/recommend")
    @CrossOrigin(origins = "http://localhost:8088",allowCredentials = "true")
    public JSONObject response_recommend(@CookieValue("user")String user){
        bzd bzd=new bzd ();
       return bzd.recomment_wb (user);
//        return myjson;
    }
    public static void main(String[] args){
//        String time="2020-11-11";
//        System.out.println(time);
//        System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(time, new ParsePosition (0)).getTime() / 1000);
//        long name = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(time, new ParsePosition (0)).getTime() / 1000;
//        bzd bzd=new bzd ();
//        tfs tfs=new tfs ();
//        String user="";
//        String pwd="123456";
//        String email="null";
//        String name="robot";
//        for(int i=5;i<1000;i++) {
//            String answer = bzd.login (user+String.valueOf (i), pwd, email+String.valueOf (i), name+String.valueOf (i));
//            System.out.println (email);
//            tfs.insert_user (user+String.valueOf (i));
//        }
//        bzd.close_conn();
//        tfs.close_conn();
    }
}
