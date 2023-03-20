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

import com.rose.workbench.domain.activity_remark;
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
        } else if ("/workbench/activity/selectActivity.do".equals(path)) {
            selectActivity(request,response);
        }else if ("/workbench/activity/selectRemark.do".equals(path)){
            selectRemark(request,response);
        }else if ("/workbench/activity/deleteRemark.do".equals(path)){
            deleteRemark(request,response);
        }else if ("/workbench/activity/saveRemark.do".equals(path)){
            savaRemark(request,response);
        } else if ("/workbench/activity/updateRemark.do".equals(path)) {
            updateRemark(request,response);
        }
    }

    //修改备注方法
    private void updateRemark(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        String remark = request.getParameter("remark");
        String editBy=((user)request.getSession().getAttribute("user")).getName();
        String editTime=DateTimeUtil.getSysTime();
        String editFlag="1";

        activity_remark remark1=new activity_remark();
        remark1.setId(id);
        remark1.setNoteContent(remark);
        remark1.setEditBy(editBy);
        remark1.setEditTime(editTime);
        remark1.setEditFlag(editFlag);

        activityService service=(activityService) new TransactionInvocationHandler(new  activityServiceImpl()).nun();
        boolean bo=service.updateRemark(remark1);

        Map<String,Object>map=new HashMap<>();
        map.put("bo",bo);
        map.put("list",remark1);
        String s = JSON.toJSONString(map);
        response.getWriter().write(s);
    }

    //增加评论方法
    private void savaRemark(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String aid = request.getParameter("id");
        String content = request.getParameter("remark");
        String id=UUIDUtil.getUUID();
        String createBy=((user)request.getSession().getAttribute("user")).getName();
        String createTime=DateTimeUtil.getSysTime();
        String editFlag="0";

        activity_remark remarks=new activity_remark();
        remarks.setId(id);
        remarks.setActivityId(aid);
        remarks.setNoteContent(content);
        remarks.setCreateBy(createBy);
        remarks.setCreateTime(createTime);
        remarks.setEditFlag(editFlag);

        activityService service = (activityService) new TransactionInvocationHandler(new activityServiceImpl()).nun();
        Map<String,Object> map=service.SavaAndSelectRemark(remarks);
        String s = JSON.toJSONString(map);
        response.getWriter().write(s);
    }

    //删除评论方法
    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        activityService service = (activityService) new TransactionInvocationHandler(new activityServiceImpl()).nun();
        boolean bo=service.deleteRemark(id);
        Map<String,Object>map=new HashMap<>();
        map.put("bo",bo);
        String s = JSON.toJSONString(map);
        response.getWriter().write(s);
    }

    //查询备注
    private void selectRemark(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        activityService o = (activityService)new TransactionInvocationHandler(new activityServiceImpl()).nun();
        List<activity_remark> remark=o.getRemark(id);
        String s = JSON.toJSONString(remark);
        response.getWriter().write(s);
    }

    //查询activity信息，展示到详情页
    private void selectActivity(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        activityService o =(activityService) new TransactionInvocationHandler(new activityServiceImpl()).nun();
        activity activity=o.selectActivity(id);
        String s = JSON.toJSONString(activity);
        response.getWriter().write(s);
    }

    //根据id查询activity和user
    private void selectActivityByid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        activityService ac =(activityService)new TransactionInvocationHandler(new activityServiceImpl()).nun();
        Map<String,Object> map = ac.selectActivityByid(id);
        String s = JSON.toJSONString(map);
        response.getWriter().write(s);
    }

    //修改activity方法
    private void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String date = request.getParameter("startTime");
        String endDate = request.getParameter("endTime");
        String cost = request.getParameter("cost");
        String description = request.getParameter("describe");
        String editBy=((user)request.getSession().getAttribute("user")).getName();
        String editTime=DateTimeUtil.getSysTime();

        activity activity=new activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setCost(cost);
        activity.setStartDate(date);
        activity.setEndDate(endDate);
        activity.setDescription(description);
        activity.setEditBy(editBy);
        activity.setEditTime(editTime);

        activityService ac =(activityService)new TransactionInvocationHandler(new activityServiceImpl()).nun();
        boolean vo = ac.update(activity);
        Map<String,Object>map=new HashMap<>();
        map.put("bo",vo);
        String s = JSON.toJSONString(map);
        response.getWriter().write(s);
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
