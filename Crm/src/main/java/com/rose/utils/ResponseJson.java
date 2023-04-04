package com.rose.utils;

import com.alibaba.fastjson2.JSON;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseJson {
    public static void JsonSend(Object json, HttpServletResponse response){
        String s = JSON.toJSONString(json);
        try {
            response.getWriter().write(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
