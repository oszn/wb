package com.example.demo.person;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class bait {
    private SqlSession sqlSession=null;
    public bait(){
        String resourse="Mapper/mybaits-config.xml";
        try {
            InputStream inputStream= Resources.getResourceAsStream (resourse);
            SqlSessionFactory sqlSessionFactory=new SqlSessionFactoryBuilder ().build (inputStream);
            sqlSession=sqlSessionFactory.openSession ();
//            List<passage> x=sqlSession.selectList ("MyMapper.selectComment","1091756452");
            System.out.println ("x");
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }
    public SqlSession getSqlSession(){
        return sqlSession;
    }
    public static void main(String[] args) {
        bait my=new bait ();
        SqlSession sqlSession=my.getSqlSession ();
        List<passage> pas=sqlSession.selectList ("selectPassage","1091756452");
        for(int i=0;i<pas.size();i++){
            passage p=pas.get (i);
            List<comment> c=sqlSession.selectList ("selectComment",p.getWeiboId ());
            System.out.println (i);
        }
    }
}
