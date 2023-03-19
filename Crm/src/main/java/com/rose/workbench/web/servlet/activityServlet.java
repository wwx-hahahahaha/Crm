package com.rose.workbench.web.servlet;

import com.alibaba.fastjson2.JSON;
import com.rose.settings.domain.user;
import com.rose.settings.service.impl.userServiceImpl;
import com.rose.settings.service.userServlce;
import com.rose.utils.DateTimeUtil;
import com.rose.utils.TransactionInvocationHandler;
import com.rose.utils.UUIDUtil;
import com.rose.vo.vo;
import com.rose.workbench.domain.activity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import com.rose.workbench.service.*;
import com.rose.workbench.service.impl.activityServiceImpl;

public class activityServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path=request.getServletPath();

        if ("/workbench/activity/getUserList.do".equals(path)){
            seleone(request, response);
        } else if ("/workbench/activity/saveActivity.do".equals(path)) {
            sava(request,response);
        }else if ("/workbench/activity/getActivity.do".equals(path)){
            getActivity(request,response);
        } else if ("/workbench/activity/delete.do".equals(path)) {
            delete(request,response);
        } else if ("/workbench/activity/selectActivityByid.do".equals(path)) {
            selectActivityByid(request,response);
        } else if ("/workbench/activity/update.do".equals(path)) {
            update(request, response);

        }
    }

    //根据id查询activity
    private void selectActivityByid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        activityService ac =(activityService)new TransactionInvocationHandler(new activityServiceImpl()).nun();
        activity activity = ac.selectActivityByid(id);
        String s = JSON.toJSONString(activity);
        response.getWriter().write(s);
    }

    //修改activity方法
    private void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String date = request.getParameter("startTime");
        String endDate = request.getParameter("endTime");
        String cost = request.getParameter("cost");
        String description = request.getParameter("describe");
        activity activity=new activity();
        activity.setId(id);
        activity.setName(name);
        activity.setCost(cost);
        activity.setStartDate(date);
        activity.setEndDate(endDate);
        activity.setDescription(description);
         activityService ac =(activityService)new TransactionInvocationHandler(new activityServiceImpl()).nun();
        System.out.println("哈哈哈啊哈大苏打"+activity);
        boolean vo = ac.update(activity);
        Map<String,Object>map=new HashMap<>();
        map.put("bo",vo);
        String s = JSON.toJSONString(map);
        response.getWriter().write(s);
        System.out.println(map);
    }

    //删除市场活动方法
    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //
        String[] ids = request.getParameterValues("id");
        activityService nun =(activityService) new TransactionInvocationHandler(new activityServiceImpl()).nun();
        boolean b = nun.delete(ids);
        Map<String,Object>map=new HashMap<>();
        map.put("bo",b);
        String s = JSON.toJSONString(map);
        response.getWriter().write(s);
    }

    //查询Activity(市场活动)数据方法
    private void getActivity(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer pageNo =Integer.parseInt(request.getParameter("pageNo"));
        Integer pageSize =Integer.parseInt(request.getParameter("pageSize"));
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startTime");
        String endDate = request.getParameter("endDate");

        Integer pageno1=(pageNo-1)*pageSize;

        Map<String, Object>map=new HashMap<>();
        map.put("pageNo",pageno1);
        map.put("pageSize",pageSize);
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);

        activityService o = (activityService) new TransactionInvocationHandler(new activityServiceImpl()).nun();
        vo v=o.getActivity(map);
        String s = JSON.toJSONString(v);
        response.getWriter().write(s);

    }

    //添加Activity(市场活动)数据方法
    private void sava(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id= UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String date = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createTime= DateTimeUtil.getSysTime();

        //将登录成功时保存的session中的真实姓名设置成创建人
        String createBy=((user)request.getSession().getAttribute("user")).getName();

        activity activity=new activity();//创建activity对象进行数据传递
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(date);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);

//        动态代理的方式调用方法，传入对象A,使用对象A的增强方法B;
        activityService nun =(activityService) new TransactionInvocationHandler(new activityServiceImpl()).nun();
        boolean s = nun.SavaActivity(activity);
        Map<String,Object>map=new HashMap<>();
        map.put("bo",s);
        String s1 = JSON.toJSONString(map);
        response.getWriter().write(s1);
    }

    //查询所有User(用户)信息
    private void seleone(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //动态代理的方式调用方法，传入对象A,使用对象A的增强方法B;
        userServlce nun = (userServlce) new TransactionInvocationHandler(new userServiceImpl()).nun();
        //得到所有的user对象信息
        List<user> users = nun.seleone();
        //将user对象转换成JSON字符串响应到前端
        String s = JSON.toJSONString(users);
        response.getWriter().write(s);
    }
}
