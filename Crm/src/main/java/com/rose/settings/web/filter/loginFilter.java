package com.rose.settings.web.filter;

import com.sun.net.httpserver.HttpExchange;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class loginFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String[]paths={"/login.jsp","/settings/user/login.do","/image","/jquery"};


        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        String path=request.getRequestURI();

        for (String s : paths) {
            if (path.contains(s)){
                filterChain.doFilter(servletRequest,servletResponse);
                return;
            }
        }

        HttpSession session = request.getSession();
        if (session!=null&&session.getAttribute("user")!=null){
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            response.sendRedirect(request.getContextPath()+"/login.jsp");
        }
    }


}
