package com.example.red;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

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
        HashMap hmap= (HashMap) jedis.hgetAll (key);
        jedis.close ();
        return hmap;
    }
    public void set_hot_word(String key){
        Jedis jedis=jedisPool.getResource ();
        if(jedis.hexists ("hot",key)){
            jedis.hincrBy ("hot",key,1);
        }else {
            jedis.hset ("hot",key, String.valueOf (1));
        }
        jedis.close ();
    }
    public List<Map.Entry<String, Integer>> value_sort( HashMap<String,String> map){
        List<Map.Entry<String, Integer>> listEntry=new ArrayList<> ();
        HashMap<String,Integer> x=new HashMap<> ();
        for(String ky:map.keySet ()){
            x.put (ky,Integer.parseInt (map.get (ky)));
        }
        listEntry.addAll (x.entrySet ());

        Collections.sort (listEntry, new Comparator<Map.Entry<String, Integer>> () {
            @Override
            public int compare(Map.Entry<String,  Integer> o1, Map.Entry<String,  Integer> o2) {
                int a1=Integer.valueOf (o1.getValue ());
                int a2=Integer.valueOf (o2.getValue ());
                return o2.getValue ().compareTo (o1.getValue ());
            }
        });
        return listEntry;
    }
//    public boolean exit(){}
    public List<Map.Entry<String, Integer>> getHot(String key){
        Jedis jedis=jedisPool.getResource ();
        HashMap<String,String> map=getMap (key);
        List<Map.Entry<String, Integer>> entryList=value_sort (map);
        jedis.close ();
        return entryList;
    }

    public void toList(String key,List<String> x){
        Jedis jedis=jedisPool.getResource ();
        for(int i=0;i<x.size ();i++){
            jedis.lpush (key,x.get (i));
        }
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
        fredis r=new fredis ();
        for(int i=0;i<10;i++) {
            r.set_hot_word ("hello");
//            r.set_hot_word ("liuyi");
            r.set_hot_word ("ouye");
        }
        List<Map.Entry<String, Integer>> c = r.getHot ("hot");

        System.out.println (1);
    }
}
