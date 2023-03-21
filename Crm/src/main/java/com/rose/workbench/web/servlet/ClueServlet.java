package com.rose.workbench.web.servlet;

import com.alibaba.fastjson2.JSON;
import com.rose.settings.domain.user;
import com.rose.settings.service.impl.userServiceImpl;
import com.rose.settings.service.userServlce;
import com.rose.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ClueServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/workbench/clue/selectUser.do".equals(path)){
            selectUser(request,response);
        }
    }

    private void selectUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userServlce service= (userServlce) ServiceFactory.getService(new userServiceImpl());
        List<user> list = service.seleone();
        String s = JSON.toJSONString(list);
        response.getWriter().write(s);
    }
}
