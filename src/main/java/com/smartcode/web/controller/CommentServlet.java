package com.smartcode.web.controller;

import com.smartcode.web.model.Comment;
import com.smartcode.web.model.User;
import com.smartcode.web.repository.comment.impl.CommentRepositoryimpl;
import com.smartcode.web.repository.user.UserRepository;
import com.smartcode.web.repository.user.impl.UserRepositoryImpl;
import com.smartcode.web.service.comment.CommentService;
import com.smartcode.web.service.comment.impl.CommentServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommentServlet extends HttpServlet {

    private CommentService commentService = new CommentServiceImpl(new CommentRepositoryimpl());
    private UserRepository userRepository = new UserRepositoryImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        String description = req.getParameter("description");

        String username = (String) req.getSession().getAttribute("username");
        User byUsername = userRepository.getByUsername(username);

        Comment comment = new Comment();
        comment.setTitle(title);
        comment.setDescription(description);
        comment.setUserId(byUsername.getId());
        commentService.create(comment);

        req.getRequestDispatcher("/secure/home.jsp").forward(req, resp);

    }
}
