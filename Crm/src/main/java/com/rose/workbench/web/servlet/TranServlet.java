package com.rose.workbench.web.servlet;

import com.rose.settings.domain.user;
import com.rose.utils.DateTimeUtil;
import com.rose.utils.ResponseJson;
import com.rose.utils.ServiceFactory;
import com.rose.utils.UUIDUtil;
import com.rose.workbench.domain.Contacts;
import com.rose.workbench.domain.Tran;
import com.rose.workbench.domain.TranHistory;
import com.rose.workbench.domain.activity;
import com.rose.workbench.service.TranService;
import com.rose.workbench.service.impl.tranServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class TranServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/workbench/transaction/add.do".equals(path)){
            add(request,response);
        } else if ("/workbench/transaction/activityLike.do".equals(path)) {
            activityLike(request,response);
        } else if ("/workbench/transaction/ContactLike.do".equals(path)) {
            ContactLike(request,response);
        } else if ("/workbench/transaction/SaveTran.do".equals(path)) {
            saveTran(request,response);
        } else if ("/workbench/transaction/getTrans.do".equals(path)) {
            getTran(request,response);
        } else if ("/workbench/transaction/getTranByid.do".equals(path)) {
            getTranByid(request,response);
        } else if ("/workbench/transaction/getTranHistory.do".equals(path)) {
            getTranHistory(request,response);
        } else if ("/workbench/transaction/UpdateTran.do".equals(path)) {
            updateTran(request,response);
        }
    }

    /**
     * 点击图标修改tran表和tranHistory
     * @param request
     * @param response
     */
    private void updateTran(HttpServletRequest request, HttpServletResponse response) {
        String tranId = request.getParameter("tranId");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String data = request.getParameter("data");
        String createBy = ((user) request.getSession().getAttribute("user")).getName();
        String createDate = DateTimeUtil.getSysTime();
        Map<String,String> map = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        String possible = map.get(stage);
        String id = UUIDUtil.getUUID();
        TranHistory tranHistory=new TranHistory();
        tranHistory.setId(id);
        tranHistory.setKn(possible);
        tranHistory.setStage(stage);
        tranHistory.setCreateTime(createDate);
        tranHistory.setCreateBy(createBy);
        tranHistory.setMoney(money);
        tranHistory.setExpectedDate(data);
        tranHistory.setTranId(tranId);

        TranService service = (TranService) ServiceFactory.getService(new tranServiceImpl());
        Map<String,Object> mapBo=new HashMap<>();
        boolean bo=service.update(tranHistory);
        mapBo.put("bo",bo);
        mapBo.put("list",tranHistory);
        ResponseJson.JsonSend(mapBo,response);
    }

    //查询阶段历史
    private void getTranHistory(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        TranService service= (TranService) ServiceFactory.getService(new tranServiceImpl());
        List<TranHistory> list=service.getTranHistory(id);
        Map<String,String> map = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        for (int i = 0; i < list.size(); i++) {
            String kn = map.get(list.get(i).getStage());
            list.get(i).setKn(kn);
        }
        System.out.println(list);
        ResponseJson.JsonSend(list,response);
    }

    //根据id查询tran表
    private void getTranByid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        TranService service= (TranService) ServiceFactory.getService(new tranServiceImpl());
        Tran list= service.getTranByid(id);
        Map<String,String> map = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        String s = map.get(list.getStage());
        list.setMap(s);
        request.setAttribute("s",list);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);
    }

    //查询交易信息
    private void getTran(HttpServletRequest request, HttpServletResponse response) {
        TranService service= (TranService) ServiceFactory.getService(new tranServiceImpl());
        List<Tran>list=service.getTran();
        ResponseJson.JsonSend(list,response);
    }

    //添加交易
    private void saveTran(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String createTime= DateTimeUtil.getSysTime();
        String createBy=((user)request.getSession().getAttribute("user")).getName();
        String name = request.getParameter("name");
        String aid = request.getParameter("aid");
        String conId = request.getParameter("conId");
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String data = request.getParameter("data");
        String kname = request.getParameter("kname");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String sources = request.getParameter("sources");
        String description = request.getParameter("description");
        String jiYao = request.getParameter("JiYao");
        String nextData = request.getParameter("nextData");
        Tran tran=new Tran();
        tran.setName(name);
        tran.setStage(stage);
        tran.setExpectedDate(data);
        tran.setId(UUIDUtil.getUUID());
        tran.setOwner(owner);
        tran.setCreateTime(createTime);
        tran.setCreateBy(createBy);
        tran.setActivityId(aid);
        tran.setContactsId(conId);
        tran.setSource(sources);
        tran.setMoney(money);
        tran.setDescription(description);
        tran.setContactSummary(jiYao);
        tran.setType(type);
        tran.setNextContactTime(nextData);

        TranService service = (TranService) ServiceFactory.getService(new tranServiceImpl());
        boolean bo=service.SaveTran(tran,kname,createBy);
        if (bo){
            response.sendRedirect("workbench/transaction/index.jsp");
        }

    }

    //根据名字模糊查联系人
    private void ContactLike(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        TranService service = (TranService) ServiceFactory.getService(new tranServiceImpl());
        List<Contacts> list=service.contactLike(name);
        ResponseJson.JsonSend(list,response);
    }

    //根据市场活动名字模糊查联系人
    private void activityLike(HttpServletRequest request, HttpServletResponse response) {
        TranService service = (TranService) ServiceFactory.getService(new tranServiceImpl());
        String name = request.getParameter("name");
        List<activity> list=service.activityLike(name);
        ResponseJson.JsonSend(list,response);
    }

    /**
     * 将user表中的数据添加交易页面
     * @param request
     * @param response
     */
    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TranService service = (TranService) ServiceFactory.getService(new tranServiceImpl());
        List<user> list =service.add();
        request.setAttribute("UserList",list);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);
    }
}
