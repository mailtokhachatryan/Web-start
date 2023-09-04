package com.smartcode.web.filter;

import com.smartcode.web.model.User;
import com.smartcode.web.repository.user.impl.UserRepositoryImpl;
import com.smartcode.web.service.user.UserService;
import com.smartcode.web.service.user.impl.UserServiceImpl;
import com.smartcode.web.utils.AESManager;
import com.smartcode.web.utils.CookieUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("filter initialized");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        var username = (String) request.getSession().getAttribute("username");

        if (username == null) {

            Cookie[] cookies = request.getCookies();

            Cookie userInfo = CookieUtil.getCookie(cookies, "userInfo");

            if (userInfo != null) {
                String usernamePasswordToken = userInfo.getValue();
                String usernamePassword = AESManager.decrypt(usernamePasswordToken);
                String[] split = usernamePassword.split(":");

                String usernameFromCookie = split[0];
                String passwordFromCookie = split[1];

                UserService userService = new UserServiceImpl(new UserRepositoryImpl());
                userService.login(usernameFromCookie, passwordFromCookie);
                request.getSession().setAttribute("username", usernameFromCookie);
                User byUsername = userService.getByUsername(usernameFromCookie);
                request.getSession().setAttribute("id", byUsername.getId());
                request.getSession().setMaxInactiveInterval(36000);
                chain.doFilter(request, response);
                return;
            }

            request.setAttribute("message", "Please Login");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            chain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        System.out.println("filter destroyed");
    }
}
