package com.example.demo.control;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.fenci.Sen2Word;
import com.example.configs.mytoken.loginTickets;
import com.example.demo.send_file.File2Server;
import com.example.mail.rest_send;
import com.example.mySql.Mysql;
import com.example.neo.neosql;
import com.alibaba.fastjson.JSONObject;
import com.example.Mredis.MyRedis;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class  login {
    public int grand(){
      return  (int)(10000+Math.random()*(100000-10000+1));
    }
    private File2Server dx=null;
    private rest_send re=null;
    private Map<String,String> user_map;
    private MyRedis template_redis;
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
        dx=new File2Server ();
        user_map=new HashMap<> ();
        yzm_map=new HashMap<> ();
        sens=new Sen2Word ();
        template_redis=new MyRedis ();
//        MyRedis MyRedis=new MyRedis ();
//        neosql neosql=new neosql ();
//        Map<String,List<String>> imp=new HashMap<> ();
//        for(int i=0;i<15;i++){
//            System.out.println (i);
//            List<String> impword=neosql.impWord (String.valueOf (i));
//            MyRedis.toList (String.valueOf (i),impword);
//
//        }
        System.out.println ("end");

    }
    @ResponseBody
    @RequestMapping("/index")
    @CrossOrigin(origins = "http://121.89.166.24:8088")
    public String hello(HttpServletResponse response)     {
        Cookie c=new Cookie ("user","122");
    //        Session.Cookie cookie=new Session.Cookie ("user",user);
        response.addCookie (c);
        return "hello";
    }

    @ResponseBody
    @RequestMapping("/upload/headImg")
    @CrossOrigin(origins = "http://121.89.166.24:8088",allowCredentials = "true")
    public String headimg(@CookieValue("user")String user,@RequestParam("file")MultipartFile files[]){
        Mysql Mysql =new Mysql ();
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
            Mysql.update_head_img (user,url);
        }
        Mysql.close_conn();
        return url;
    }

    @ResponseBody
    @RequestMapping("/register/yzm")
    @CrossOrigin(origins = "http://121.89.166.24:8088")
    public String mm(@RequestParam("email")String email){
        Mysql Mysql =new Mysql ();
        int rand=grand ();
        String res= Mysql.sl_user_email (email);
        if(res.contains ("ok:"))
        {
            Mysql.close_conn();
            return "error:邮箱已存在";
        }
        else {
            yzm_map.put (email, String.valueOf (rand));
            re.sendEmailBatch (email,String.valueOf (rand));
            Mysql.close_conn();
            return "ok";
        }
    }

    @ResponseBody
    @RequestMapping("/register")
    @CrossOrigin(origins = "http://121.89.166.24:8088",
            allowCredentials = "true")
    public String login_x(@RequestParam("user")String user,@RequestParam("pwd")String pwd,@RequestParam("yzm")String yzm,@RequestParam("email")String email,@RequestParam("name")
            String name){
        Mysql Mysql =new Mysql ();
        neosql neosql =new neosql ();
        System.out.println (user);
        System.out.println (pwd);
        String yanzhengma=yzm_map.get (email);
        if(yanzhengma.equals (yzm)){
            String answer= Mysql.login (user,pwd,email,name);
            System.out.println (email);
            neosql.insert_user (user);
            if(answer.equals ("ok:用户插入成功")) {
//           re.sendEmailBatch(email,"注册成功！！！");
                yzm_map.remove (email);
                Mysql.close_conn();
                neosql.close_conn();
                return answer;
            }
            Mysql.close_conn();
            neosql.close_conn();
            return answer;
        }else {
            Mysql.close_conn();
            neosql.close_conn();
            return "error:验证码错误";
        }
    }

    @ResponseBody
    @RequestMapping("/login")
    @CrossOrigin(origins = "http://121.89.166.24:8088",
            allowCredentials = "true")
    public String login_deng(HttpServletRequest request, @RequestParam("user")String user, @RequestParam("pwd")String pwd, HttpServletResponse response) {
        Mysql Mysql =new Mysql ();
        System.out.println (user);
        System.out.println (pwd);
        Cookie cookie=new Cookie ("user",user);
        String answer= Mysql.denglu (user,pwd);
        response.addCookie (cookie);
        Mysql.close_conn();
        return answer;
    }

    @ResponseBody
    @RequestMapping("/change")
    @CrossOrigin(origins = "http://121.89.166.24:8088",
            allowCredentials = "true")
    public String nobb( @RequestParam("email")String email) {
        Mysql Mysql =new Mysql ();
        int rand=grand ();
        String res= Mysql.sl_user_email (email);
        System.out.println (res);
        user_map.put (email,String.valueOf (rand));
        re.sendEmailBatch (email,String.valueOf (rand));
        Mysql.close_conn();
        return res;
    }


    @ResponseBody
    @RequestMapping("/reset")
    @CrossOrigin(origins = "http://121.89.166.24:8088",
            allowCredentials = "true")
    public String xxxbb(@RequestParam("yzm")String yzm,@RequestParam("email") String email, HttpServletResponse response,@RequestParam("pwd")String pwd) {
        Mysql Mysql =new Mysql ();
        String wd=user_map.get (email);
        System.out.println (email);
        System.out.println (user_map.keySet ());
        System.out.println (user_map.get (email));
        System.out.println (yzm);

        if(wd.equals (yzm)){
            String res= Mysql.sl_user_email (email);
            if(res.contains ("ok")) {
                System.out.println (res);
//                String res=Mysql.sl_user_email (email);
                System.out.println (res);
//                System.out.println (res.replaceFirst ("ok:",""));
                String result= Mysql.update_u (res.replaceFirst ("ok:",""), pwd);
                Cookie c = new Cookie ("user", res.replaceFirst ("ok:",""));
                response.addCookie (c);
                user_map.remove (email);
                Mysql.close_conn();
                return "ok";
            }
            else{
                Mysql.close_conn();
                return "error:执行错误";
            }
        }
        Mysql.close_conn();
        return "error";
    }


//    @ResponseBody
//    @RequestMapping("/reset")
//    @CrossOrigin
//    public String asdaxada(@RequestParam("pwd")String pwd,@CookieValue("user") String session) {
//        if (session != null) {
//            String result=Mysql.update_u (session, pwd);
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
    @CrossOrigin(origins = "http://121.89.166.24:8088",
            allowCredentials = "true")//@CookieValue("user") String session,
    public String asdax( @CookieValue("user") String session, @RequestParam Map<String,String> map, @RequestParam("file")MultipartFile[] files) {
        System.out.println ("upload--begin");
        Mysql Mysql =new Mysql ();
        neosql neosql =new neosql ();
        if (session != null) {
            String content=map.get ("WeiBoContext").toString ();
            String time=map.get ("time").toString ();
            System.out.println (content);
            System.out.println (time);
            String tp=get_tp (content);
            String result= Mysql.insert_weibo (session,time,content,tp);
            int id= Mysql.how (session,time,content);
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
                String r = Mysql.insert_photo (String.valueOf (id), url);
                qq++;
            }
            /***
             * new mod
             * step 1 分类
             * step 2 将作者和文章都插入neo4j
             * step 3 将分类插入
             */

            Mysql.close_conn();
            neosql.close_conn ();
            return result;
        }
        return "error";
    }



    @ResponseBody
    @RequestMapping("/upload/comment")
    @CrossOrigin(origins = "http://121.89.166.24:8088",
            allowCredentials = "true")
    public String xxxsad( @CookieValue("user") String session,@RequestParam Map<String,String> map){
        Mysql Mysql =new Mysql ();
        if (session != null) {
            String weiboId = map.get ("WeiboId").toString ();
            String FromeId = map.get ("FromId").toString ();
            String Told=map.get ("ToId").toString ();
            String context=map.get ("CommentContext").toString ();
            String time=map.get ("Time").toString ();
            String x= Mysql.insert_comment (weiboId,FromeId,Told,context,time);
            Mysql.close_conn();
            return x;
        }
        Mysql.close_conn();
        return "error";
    }

    @ResponseBody
    @RequestMapping("/like/passage")
    @CrossOrigin(origins = "http://121.89.166.24:8088",
            allowCredentials = "true")
    public void like(@RequestParam("weiboid") String id){
        Mysql Mysql =new Mysql ();
        Mysql.like (id);
        Mysql.close_conn();
    }

    @ResponseBody
    @RequestMapping("/like/comment")
    @CrossOrigin(origins = "http://121.89.166.24:8088",
            allowCredentials = "true")
    public void like2(@RequestParam("commentid") String id){
        Mysql Mysql =new Mysql ();
        Mysql.like2 (id);
        Mysql.close_conn();
    }

    @ResponseBody
    @RequestMapping(value = "/respone/passage",produces="application/json;charset=UTF-8")
    @CrossOrigin(origins = "http://121.89.166.24:8088",
            allowCredentials = "true")
    public JSONObject web_back(@CookieValue("user") String id){
        Mysql Mysql =new Mysql ();
        JSONObject j= Mysql.weibo_comment (id);
        Mysql.close_conn();
        System.out.println(j);
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "/response/friend",produces="application/json;charset=UTF-8")
    @CrossOrigin(origins = "http://121.89.166.24:8088",
            allowCredentials = "true")
    public JSONObject fri_web_back(@CookieValue("user") String id){
        Mysql Mysql =new Mysql ();
        JSONObject j= Mysql.fried_weibo (id);
        Mysql.close_conn();
        return j;
    }

    @ResponseBody
    @RequestMapping("/link")
    @CrossOrigin(origins = "http://121.89.166.24:8088",
            allowCredentials = "true")
    public String link(@CookieValue("user")String user,@RequestParam("linked") String id){
        neosql neosql =new neosql ();
        neosql.create_link (user,id);
        neosql.close_conn();
        return "ok";
    }


    @ResponseBody
    @RequestMapping("/info/update")
    @CrossOrigin(origins = "http://121.89.166.24:8088",allowCredentials = "true")
    public JSONObject fans_update(@CookieValue("user")String user){
        Mysql Mysql =new Mysql ();
        neosql neosql =new neosql ();
        JSONObject myjs=new JSONObject ();
        neosql.selectfans_at (user,myjs);
        String s= Mysql.get_weibo_count (user);
        myjs.put ("weiboCount",Integer.parseInt (s));
        Mysql.close_conn();
        neosql.close_conn();
        return myjs;
//        neosql.create_link (user,id);
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
                user.put ("headimg",user_name[2]+":"+user_name[3]);
                p.add (user);
            }
        }
    }

    @ResponseBody
    @RequestMapping("/info")
    @CrossOrigin(origins = "http://121.89.166.24:8088",allowCredentials = "true")
    public JSONObject inf(@CookieValue("user")String user){
        Mysql Mysql =new Mysql ();
        JSONObject j= Mysql.get_own_inf (user);
        Mysql.close_conn();
        return j;
    }


    @ResponseBody
    @RequestMapping("/list/fans")
    @CrossOrigin(origins = "http://121.89.166.24:8088",allowCredentials = "true")
    public JSONObject fans_list(@CookieValue("user")String user){
        Mysql Mysql =new Mysql ();
        JSONObject j= Mysql.get_fans_info (user);
        Mysql.close_conn();
        return j;
    }
    @ResponseBody
    @RequestMapping("/list/attention")
    @CrossOrigin(origins = "http://121.89.166.24:8088",allowCredentials = "true")
    public JSONObject attention_list(@CookieValue("user")String user){
        Mysql Mysql =new Mysql ();
        JSONObject j= Mysql.get_attentions_info (user);
        Mysql.close_conn();
        return j;
    }

    @ResponseBody
    @RequestMapping("/cancel/link")
    @CrossOrigin(origins = "http://121.89.166.24:8088",allowCredentials = "true")
    public String cancel_link(@CookieValue("user")String user,@RequestParam("id") String id){
        neosql neosql =new neosql ();
        System.out.println (id);
        neosql.cancel_attention (user,id);
        neosql.close_conn();
        return "ok";
    }
    public void prtTimer(long start){
        System.out.println (System.currentTimeMillis ()-start);
    }
    @ResponseBody
    @RequestMapping("/search")
    @CrossOrigin(origins = "http://121.89.166.24:8088",allowCredentials = "true")
    public JSONObject search(@CookieValue("user")String user,@RequestParam("text") String text){
        MyRedis r=new MyRedis ();
        r.set_hot_word (text);
        long start=System.currentTimeMillis ();
        Mysql Mysql =new Mysql ();
        //prtTimer (start);
        List<String> pas=sens.search_w (text);
        String tp=get_tp (text);
        String tag="";
        for(int i=0;i<pas.size ();i++){
            tag+=pas.get (i)+" ";
        }
        Mysql.insert_search_record (user,String.valueOf (start/1000),"\'"+text+"\'",tp);
        prtTimer (start);
        String up= Mysql.select_user (text);
        JSONObject result= Mysql.search_it (user,pas);
        JSONArray U=new JSONArray ();
        JSONArray P=new JSONArray ();
        String[] x=up.split ("\\*");
        nmdwsm (x[0],U);
        nmdwsm (x[1],P);
        result.put ("search_id",P);
        result.put ("search_user",U);
        Mysql.close_conn();
        return result;
//        neosql.create_link (user,id);
//        return "ok";
    }
    /***
     * 2020/6/3 18:14
     * 添加点击头像返回信息界面
     * 参数1.cookie 2.点击人的账号
     */
    @ResponseBody
    @RequestMapping("/friend/info")
    @CrossOrigin(origins = "http://121.89.166.24:8088",allowCredentials = "true")
    public JSONObject friend_info(@CookieValue("user")String user,@RequestParam("id") String id){
        Mysql Mysql =new Mysql ();
        JSONObject jb= Mysql.visit_info (user,id);
        Mysql.close_conn ();
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
    @CrossOrigin(origins = "http://121.89.166.24:8088",allowCredentials = "true")
    public JSONObject hot_info(){
        MyRedis r=new MyRedis ();
        List<Map.Entry<String, Integer>> c = r.getHot ("hot");
        JSONObject myjson=new JSONObject ();
        JSONArray js=new JSONArray ();
        for(int i=0;i<Math.min (100,c.size ());i++){
            JSONArray js2=new JSONArray ();
            js2.add (c.get (i).getKey ());
            js2.add (c.get (i).getValue ());
            js.add (js2);
        }
        myjson.put ("hot",js);
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
     * @param
     */
    @ResponseBody
    @RequestMapping("/response/recommend")
    @CrossOrigin(origins = "http://121.89.166.24:8088",allowCredentials = "true")
    public JSONObject response_recommend(@CookieValue("user")String user){
        Mysql Mysql =new Mysql ();
       return Mysql.recomment_wb (user);
//        return myjson;
    }
    public static void main(String[] args){
//        String time="2020-11-11";
//        System.out.println(time);
//        System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(time, new ParsePosition (0)).getTime() / 1000);
//        long name = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(time, new ParsePosition (0)).getTime() / 1000;
//        Mysql Mysql=new Mysql ();
//        neosql neosql=new neosql ();
//        String user="";
//        String pwd="123456";
//        String email="null";
//        String name="robot";
//        for(int i=5;i<1000;i++) {
//            String answer = Mysql.login (user+String.valueOf (i), pwd, email+String.valueOf (i), name+String.valueOf (i));
//            System.out.println (email);
//            neosql.insert_user (user+String.valueOf (i));
//        }
//        Mysql.close_conn();
//        neosql.close_conn();
    }
}
