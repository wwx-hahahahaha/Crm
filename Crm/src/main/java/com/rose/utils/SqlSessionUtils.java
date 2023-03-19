package com.rose.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SqlSessionUtils {

    private static SqlSessionFactory sqlSessionFactory;
    private static ThreadLocal<SqlSession> th=new ThreadLocal<>();
    static {
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
             sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static SqlSession getSessiom(){
        if (th.get()==null){
            th.set(sqlSessionFactory.openSession());
        }
        return th.get();
    }

    public static void opendown(SqlSession session){
        if (session!=null){
            session.close();
            th.remove();
        }
    }
}
