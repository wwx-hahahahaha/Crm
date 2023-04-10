package com.rose.workbench.web.servlet;

import com.rose.utils.ResponseJson;
import com.rose.utils.ServiceFactory;
import com.rose.workbench.service.CustomerService;
import com.rose.workbench.service.impl.customerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CustomerServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/workbench/transaction/getCustomerName.do".equals(path)){
            getCustomerName(request,response);
        }
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        String name1 = request.getParameter("name");
        CustomerService service= (CustomerService) ServiceFactory.getService(new customerServiceImpl());
        List<String> name =service.getCustomerName(name1);
        ResponseJson.JsonSend(name,response);
    }
}
