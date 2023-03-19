package com.rose.utils;

public class ServiceFactory {
    public static Object getService(Object o){
        return new TransactionInvocationHandler(o).nun();
    }
}
