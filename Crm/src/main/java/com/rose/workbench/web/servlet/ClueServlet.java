package com.rose.workbench.web.servlet;

import com.alibaba.fastjson2.JSON;
import com.rose.settings.domain.user;
import com.rose.settings.service.impl.userServiceImpl;
import com.rose.settings.service.userServlce;
import com.rose.utils.*;
import com.rose.workbench.domain.Clue;
import com.rose.workbench.domain.Tran;
import com.rose.workbench.domain.activity;
import com.rose.workbench.service.ActivityService;
import com.rose.workbench.service.ClueService;
import com.rose.workbench.service.impl.activityServiceImpl;
import com.rose.workbench.service.impl.clueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/workbench/clue/selectUser.do".equals(path)) {
            selectUser(request, response);
        } else if ("/workbench/clue/SaveClue.do".equals(path)) {
            SaveClue(request, response);
        } else if ("/workbench/clue/selectClue.do".equals(path)) {
            selectClue(request, response);
        } else if ("/workbench/clue/selectClueById.do".equals(path)) {
            selectClueById(request,response);
        } else if ("/workbench/clue/selectActivity.do".equals(path)) {
            selectActivity(request,response);
        } else if ("/workbench/clue/deleteClueActivityRelation.do".equals(path)) {
            deleteCAR(request,response);
        } else if ("/workbench/clue/selectActivityNotById.do".equals(path)) {
            selectActivityNotById(request,response);
        } else if ("/workbench/clue/saveActivityRelation.do".equals(path)) {
            saveActivityRelation(request,response);
        } else if ("/workbench/clue/selectActivityLikeNotById.do".equals(path)) {
            selectActivityLikeNotById(request,response);
        } else if ("/workbench/clue/selectActivityLikeById.do".equals(path)) {
            selectActivityLikeById(request,response);
        } else if ("/workbench/clue/convert.do".equals(path)) {
            Convert(request,response);
        }
    }

    /**
     * 线索转业务
     * @param request
     * @param response
     * @throws IOException
     */
    private void Convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String cid = request.getParameter("cid");
        String a = request.getParameter("a");
        String createBy=((user)request.getSession().getAttribute("user")).getName();
        Tran tran=null;
        if ("a".equals(a)){
             tran=new Tran();
            String data = request.getParameter("data");
            String stage = request.getParameter("stage");
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String aname = request.getParameter("aname");
            String aid = request.getParameter("aid");


            tran.setActivityId(aid);
            tran.setName(name);
            tran.setMoney(money);
            tran.setExpectedDate(data);
            tran.setStage(stage);
            tran.setId(UUIDUtil.getUUID());
            tran.setCreateTime(DateTimeUtil.getSysTime());
            tran.setCreateBy(createBy);
        }
        ClueService service = (ClueService) ServiceFactory.getService(new clueServiceImpl());
        boolean bol=service.convert(tran,cid,createBy);
        if (bol){
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }
    }

    /**
     * 线索转换搜索框模糊查询
     * @param request
     * @param response
     */
    private void selectActivityLikeById(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        ActivityService service = (ActivityService) ServiceFactory.getService(new activityServiceImpl());
        List<activity> list=service.selectActivityLikeById(name);
        ResponseJson.JsonSend(list,response);
    }

    /**
     * 关联市场活动搜索框模糊查询
     * @param request
     * @param response
     */
    private void selectActivityLikeNotById(HttpServletRequest request, HttpServletResponse response) {
        String cid = request.getParameter("cid");
        String text = request.getParameter("text");
        ActivityService service= (ActivityService) ServiceFactory.getService(new activityServiceImpl());
        List<activity> list=service.selectActivityLikeNotById(cid,text);
        ResponseJson.JsonSend(list,response);
    }

    /**
     * 关联市场活动方法
     * @param request
     * @param response
     */
    private void saveActivityRelation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] ids = request.getParameterValues("id");
        String cid = request.getParameter("cid");
        ClueService service= (ClueService) ServiceFactory.getService(new clueServiceImpl());
        int count=service.SaveActivityRelation(ids,cid);
        response.getWriter().write(count+"");
    }

    //    关联市场活动的查询
    private void selectActivityNotById(HttpServletRequest request, HttpServletResponse response) {
        String cid = request.getParameter("cid");
        ClueService service= (ClueService) ServiceFactory.getService(new clueServiceImpl());
        List<activity> list=service.selectActivityNId(cid);
        ResponseJson.JsonSend(list,response);
    }

    //    解除关联方法
    private void deleteCAR(HttpServletRequest request, HttpServletResponse response) {
        String cid = request.getParameter("cid");
        String aid = request.getParameter("aid");

        ClueService service= (ClueService) ServiceFactory.getService(new clueServiceImpl());
        boolean bo=service.selectCAR(cid,aid);
        Map<String,Object>map=new HashMap<>();
        map.put("bo",bo);
        ResponseJson.JsonSend(map,response);
    }

    //    展现市场活动
    private void selectActivity(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        ActivityService service = (ActivityService) ServiceFactory.getService(new activityServiceImpl());
        List<activity> sele = service.sele(id);
        String s = JSON.toJSONString(sele);
        response.getWriter().write(s);
    }

    //    根据id查询出clue表中的数据,转发给详情页面
    private void selectClueById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        ClueService service= (ClueService) ServiceFactory.getService(new clueServiceImpl());
         Clue clue=service.selectClueById(id);
         request.setAttribute("clue",clue);
         request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);
    }


    //查询线索,页面展开时进行展示
    private void selectClue(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer pageNo = Integer.parseInt(request.getParameter("pageNo"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
        pageNo = (pageNo - 1) * pageSize;

        String name = request.getParameter("select-name");
        String company = request.getParameter("select-company");
        String phone = request.getParameter("select-phone");
        String owner = request.getParameter("select-owner");
        String mphone = request.getParameter("select-mphone");


        Map<String, Object> map = new HashMap<>();
        map.put("pageNo", pageNo);
        map.put("pageSize", pageSize);
        map.put("name", name);
        map.put("company", company);
        map.put("phone", phone);
        map.put("owner", owner);
        map.put("mphone", mphone);


        ClueService service = (ClueService) ServiceFactory.getService(new clueServiceImpl());
        List<Clue> list = service.selectClue(map);
        String s = JSON.toJSONString(list);
        response.getWriter().write(s);
    }

    //    新增clue(线索)方法
    private void SaveClue(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = UUIDUtil.getUUID();
        String createBy = ((user) request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();

        String Owner = request.getParameter("create-Owner");
        String company = request.getParameter("create-company");
        String appellation = request.getParameter("create-appellation");
        String fullname = request.getParameter("create-fullname");
        String job = request.getParameter("create-job");
        String email = request.getParameter("create-email");
        String phone = request.getParameter("create-phone");
        String website = request.getParameter("create-website");
        String mphone = request.getParameter("create-mphone");
        String status = request.getParameter("create-status");
        String source = request.getParameter("create-source");
        String description = request.getParameter("create-description");
        String contactSummary = request.getParameter("create-contactSummary");
        String nextContactTime = request.getParameter("create-nextContactTime");
        String address = request.getParameter("create-address");

        Clue clue = new Clue();
        clue.setId(id);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setOwner(Owner);
        clue.setCompany(company);
        clue.setSource(source);
        clue.setState(status);
        clue.setEmail(email);
        clue.setFullname(fullname);
        clue.setDescription(description);
        clue.setAddress(address);
        clue.setNextContactTime(nextContactTime);
        clue.setContactSummary(contactSummary);
        clue.setPhone(phone);
        clue.setAppellation(appellation);
        clue.setJob(job);
        clue.setWebsite(website);
        clue.setMphone(mphone);


        ClueService service = (ClueService) ServiceFactory.getService(new clueServiceImpl());
        service.SaveClue(clue);
        response.getWriter().write("添加成功");
    }

    private void selectUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userServlce service = (userServlce) ServiceFactory.getService(new userServiceImpl());
        List<user> list = service.seleone();
        String s = JSON.toJSONString(list);
        response.getWriter().write(s);
    }
}
