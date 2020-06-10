package com.example.demo.fenci;

import com.example.neo.neosql;
import com.example.Mredis.MyRedis;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;


import java.util.*;
import java.util.Timer;
public class Sen2Word {
    private neosql neosql =null;
    public Sen2Word(){
        neosql =new neosql ();
//        Timer timer=new Timer ();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
////cacheTime重置，生成大于4个小时，小于5个小时的任意时间
//               neosql.close_conn ();
//               neosql =new neosql ();
//            }
//        }, 10*1000, 60*1000);
    }


    public List<String> get_list(String str) {

//        String str ="北京时间5月22日消息，2020全国两会首场代表通道开启，人大代表、中国女排队长朱婷讲述女排精神,我。";
        Set<String> expectedNature = new HashSet<String> () {{
            add("n");
            add("nt");add("nz");add("nw");add("nl");add ("nr");
            add("ng");add("userDefine");add("wh");
        }};
        Result re= (Result) ToAnalysis.parse (str);
        List<Term> terms=re.getTerms ();
        List<String> rp=new ArrayList<> ();
        for(int i=0;i<terms.size();i++){
            String word=terms.get (i).getName ();
            String nat=terms.get (i).getNatureStr ();

                rp.add (word);

        }
        return rp;
    }
    public void ins(String str, String id, String owner,String t){
        neosql neosql=new neosql ();
        List<String> tp=get_list (str);
        if(tp.size ()!=0)
        neosql.set_connect (tp,id,owner,str,t);
        neosql.close_conn ();
    }
    public void prtTimer(long start){
        System.out.println (System.currentTimeMillis ()-start);
    }
    private MyRedis f=new MyRedis ();
    public List<String> search_w(String str){
        neosql neosql=new neosql ();
        long start=System.currentTimeMillis ();
        List<String> tp=get_list (str);
        List<String> x= neosql.search (tp);
        neosql.close_conn ();
        return x;
    }
//    public  void readCsvFile(){
//        File csv=new File ("src/main/java/com/example/demo/fenci/weibo_senti_100k.csv");
//        try {
//            BufferedReader text=new BufferedReader (new FileReader (csv));
//            String line="";
//            int k=0;
//            while((line=text.readLine ())!=null){
//                String a=line.substring (line.indexOf (',')+1);
//                this.ins (a,String.valueOf (k));
//                k++;
////                System.out.println (b);
//            }
//        } catch (IOException e) {
//            e.printStackTrace ();
//        }
//    }


    public static void main(String[] args) {
        Sen2Word s=new Sen2Word ();
        MyRedis MyRedis =new MyRedis ();
        List<String>k=s.get_list ("北京时间5月22日消息，2020全国两会首场代表通道开启，人大代表、中国女排队长朱婷讲述女排精神");
        MyRedis.toList ("news",k);
        System.out.println (MyRedis.getList ("news"));
//         s.search_w ("女排");
//        s.ins ("北京时间5月22日消息，2020全国两会首场代表通道开启，人大代表、中国女排队长朱婷讲述女排精神,我","11");
//        s.ins ("北京时间5月22日消息，2020全国两会首场代表通道开启，人大代表、中国女排队长朱婷讲述女排精神,我","14");
    }
}
