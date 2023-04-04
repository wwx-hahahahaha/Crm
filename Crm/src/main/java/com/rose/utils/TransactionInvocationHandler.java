package com.rose.utils;

import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TransactionInvocationHandler implements InvocationHandler {
    private Object tage;

    public TransactionInvocationHandler(Object tage) {
        this.tage = tage;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object o = null;
        SqlSession session = null;

        try {
            o = method.invoke(tage, args);
            session = SqlSessionUtils.getSessiom();
            session.commit();
        } catch (Exception e) {
            if (session != null) {
                session.rollback();
            }
            e.printStackTrace();
            //将错误抛出去给控制器处理，不抛出的话这里会自动处理这个异常,控制器层逻辑代码将行不通
            throw e.getCause();
        } finally {
            SqlSessionUtils.opendown(session);
        }


        return o;
    }

    public Object nun() {
        return Proxy.newProxyInstance(tage.getClass().getClassLoader(), tage.getClass().getInterfaces(), this);
    }
}
