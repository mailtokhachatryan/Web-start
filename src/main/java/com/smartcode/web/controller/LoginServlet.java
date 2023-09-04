package com.smartcode.web.controller;

import com.smartcode.web.exception.ValidationException;
import com.smartcode.web.model.User;
import com.smartcode.web.repository.user.UserRepository;
import com.smartcode.web.repository.user.impl.UserRepositoryImpl;
import com.smartcode.web.service.user.UserService;
import com.smartcode.web.service.user.impl.UserServiceImpl;
import com.smartcode.web.utils.AESManager;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private final UserRepository userRepository = new UserRepositoryImpl();
    private final UserService userService = new UserServiceImpl(userRepository);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String rememberMe = req.getParameter("rememberMe");


            System.out.println(rememberMe);

            User byUsername = userRepository.getByUsername(username);

            userService.login(username, password);
            if (rememberMe.equals("on")) {

                String usernamePassword = username + ":" + password;

                String encrypt = AESManager.encrypt(usernamePassword);

                Cookie cookie = new Cookie("userInfo", encrypt);
                cookie.setMaxAge(360000);
                resp.addCookie(cookie);
            }
            req.getSession().setAttribute("username", username);
            req.getSession().setAttribute("id", byUsername.getId());
            req.getSession().setMaxInactiveInterval(36000);
            req.getRequestDispatcher("/secure/home.jsp").forward(req, resp);
        } catch (ValidationException e) {
            req.setAttribute("message", e.getMessage());
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }

}
