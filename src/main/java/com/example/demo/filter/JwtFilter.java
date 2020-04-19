package com.example.demo.filter;


import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.Claim;
import com.example.demo.util.JwtUtil;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebFilter(filterName = "JwtFilter", urlPatterns = "/api/manage/*")
public class JwtFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServiceException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");

        /*
        "Request header field authorization is not allowed
        by Access-Control-Allow-Headers in preflight response"
        表示请求头里自定义的"authorization"没有被允许，需要在下面添加
         */
        response.setHeader("Access-Control-Allow-Headers", "authorization, content-type, Origin");

        /*
        header里面包含自定义字段，浏览器是会先发一次options请求，
        如果请求通过，则继续发送正式的post请求，而如果不通过
        则返回错误
         */
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(request, response);
        }
        else {
            // 获取header中的token
            final String token = request.getHeader("authorization");
            JSONObject data = new JSONObject();
            if (token == null) {
                data.put("code", 400);
                response.getWriter().write(data.toString());
                return;
            }

            Map<String, Claim> userData = JwtUtil.verifyToken(token);
            if (userData == null) {
                data.put("code", 401);
                response.getWriter().write(data.toString());
                return;
            }
            request.setAttribute("username", userData.get("username").asString());
            log.info(userData.get("username").asString());
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}
