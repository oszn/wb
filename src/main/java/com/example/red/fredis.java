package com.example.red;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class fredis {
    private JedisPool jedisPool;
    public fredis(){
        jedisPool=new JedisPool ("121.89.166.24",6379);
    }
    public Boolean  exist(String key){
        Jedis jedis=jedisPool.getResource ();
        Boolean ok=jedis.exists (key);
        jedis.close ();
        return ok;
    }
    public void tostr(String key,String str){
        Jedis jedis=jedisPool.getResource ();
//        Boolean exit=jedis.exists (key)
        jedis.set (key,str);
        jedis.expire (key,7200);
        jedis.close ();
      }
    public void toMap(String key, HashMap map){
        Jedis jedis=jedisPool.getResource ();
        jedis.hmset (key,map);
        jedis.expire (key,120);
        jedis.close ();
    }
    public String getstr(String key){
        Jedis jedis=jedisPool.getResource ();
        jedis.close ();
        return jedis.get (key);
    }
    public HashMap getMap(String key){
        Jedis jedis=jedisPool.getResource ();
        jedis.close ();
        return (HashMap) jedis.hgetAll (key);
    }
//    public boolean exit(){}

    public void toList(String key,List<String> x){
        Jedis jedis=jedisPool.getResource ();
        int l= Math.toIntExact (len (key));
        List<String> temp=getList (key);
        for(String p:x)
        {
            temp.remove (p);
            temp.add (p);
        }
        if(l>60){
            List<String> np=new ArrayList<> ();
            jedis.del (key);
            for(int i=0;i<30;i++){
                jedis.lpush (key,temp.get (i));
            }
        }else{
            jedis.del (key);
            for(String p:temp){
                jedis.lpush (key,p);
            }
        }
        jedis.expire (key,600);
        jedis.close ();
    }
    public Long len(String key){
        Jedis jedis=jedisPool.getResource ();
        Long length=jedis.llen (key);
        jedis.close ();
        return length;
    }
    public List getList(String key){
        int l= Math.toIntExact (len (key));
        Jedis jedis=jedisPool.getResource ();
        List<String> list=jedis.lrange (key,0,l-1);
        return list;
    }
    public static void main(String[] args) {

    }
}
