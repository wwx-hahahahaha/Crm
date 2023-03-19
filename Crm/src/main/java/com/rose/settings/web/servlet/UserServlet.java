package com.rose.settings.web.servlet;

import com.alibaba.fastjson2.JSON;
import com.rose.exception.LoginExption;
import com.rose.settings.domain.user;
import com.rose.settings.service.impl.userServiceImpl;
import com.rose.settings.service.userServlce;
import com.rose.utils.ServiceFactory;
import com.rose.utils.TransactionInvocationHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@WebServlet("/settings/user/seleone.do")
public class UserServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
//        response.setContentType("text/html;charset=utf-8");
        String path = request.getServletPath();

        if ("/settings/user/login.do".equals(path)){
            login(request,response);
        }
    }

    //查询出所有用户



    //登录方法
    public void login(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //获取本地ip地址
        String addr = InetAddress.getLocalHost().getHostAddress();
        System.out.println(addr);
        PrintWriter writer = response.getWriter();
        //调用动态代理中的方法,传入A使用加强A
        userServlce o =(userServlce) ServiceFactory.getService(new userServiceImpl());
        //定义集合用来向前端传递数据
        Map<String,String>map=new HashMap<>();
        user user = null;
        try {
            user = o.seleuser(username, password, addr);
//            if (user!=null){
            //登录成功将user保存到session域中
                request.getSession().setAttribute("user",user);
                //将要响应给前面的数据true封装到map集合里面
                map.put("sucess","true");
                //将map集合转为JSON字符串
                String boo=JSON.toJSONString(map);
                //将数据响应给前端
                writer.write(boo);
//            }
        } catch (Exception e) {//如果登录失败的话，会执行自定义的异常
            //得到自定义的异常信息
            String s = e.getMessage();
            System.out.println(s);
            //将异常信息封装到map集合里面
            map.put("cuowu",s);
            //将map集合转为JSON类型的字符串
            String s1 = JSON.toJSONString(map);
            //响应给前端
            writer.write(s1);

        }

    }
}
