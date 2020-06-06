package com.example.demo.fenci;

import com.example.neo.tfs;
import com.example.red.fredis;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import org.apache.lucene.util.Version;
import org.neo4j.cypher.internal.frontend.v2_3.ast.functions.Str;
import org.neo4j.register.Register;
import org.thymeleaf.expression.Lists;


import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Sen2Word {
    private tfs tfs=null;
    public Sen2Word(){
        tfs=new tfs ();
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
            if(expectedNature.contains (nat)){
                rp.add (word);
            }
        }
        return rp;
    }
    public void ins(String str, String id, String owner,String t){
        List<String> tp=get_list (str);
        if(tp.size ()!=0)
        tfs.set_connect (tp,id,owner,str,t);
    }
    public void prtTimer(long start){
        System.out.println (System.currentTimeMillis ()-start);
    }
    private fredis f=new fredis ();
    public List<String> search_w(String str){
        long start=System.currentTimeMillis ();
        prtTimer (start);
        List<String> tp=get_list (str);
        prtTimer (start);
        List<String> x=tfs.search (tp);
        prtTimer (start);
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
        fredis fredis=new fredis ();
        List<String>k=s.get_list ("北京时间5月22日消息，2020全国两会首场代表通道开启，人大代表、中国女排队长朱婷讲述女排精神");
        fredis.toList ("news",k);
        System.out.println (fredis.getList ("news"));
//         s.search_w ("女排");
//        s.ins ("北京时间5月22日消息，2020全国两会首场代表通道开启，人大代表、中国女排队长朱婷讲述女排精神,我","11");
//        s.ins ("北京时间5月22日消息，2020全国两会首场代表通道开启，人大代表、中国女排队长朱婷讲述女排精神,我","14");
    }
}
