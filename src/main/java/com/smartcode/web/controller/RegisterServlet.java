package com.smartcode.web.controller;

import com.smartcode.web.model.User;
import com.smartcode.web.repository.user.UserRepository;
import com.smartcode.web.repository.user.impl.UserRepositoryImpl;
import com.smartcode.web.service.user.UserService;
import com.smartcode.web.service.user.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

public class RegisterServlet extends HttpServlet {

    private final UserRepository userRepository = new UserRepositoryImpl();
    private final UserService userService = new UserServiceImpl(userRepository);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String lastName = req.getParameter("lastName");
        String middleName = req.getParameter("middleName");
        Integer age = Integer.valueOf(req.getParameter("age"));
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = new User(null, name, lastName, middleName, username, age, password, new BigDecimal(0));

        userService.register(user);

        resp.sendRedirect("login.jsp");

    }


}
