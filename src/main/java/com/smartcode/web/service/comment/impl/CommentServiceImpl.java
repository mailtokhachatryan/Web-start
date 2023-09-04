package com.smartcode.web.service.comment.impl;

import com.smartcode.web.model.Comment;
import com.smartcode.web.repository.comment.CommentRepository;
import com.smartcode.web.service.comment.CommentService;
import com.smartcode.web.utils.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final Connection connection = DataSource.getInstance().getConnection();

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void create(Comment comment) {
        try {

            connection.setAutoCommit(false);
            commentRepository.create(comment);
            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void update(Comment comment, Integer id, String title, String description) {
        try {
            connection.setAutoCommit(false);
            comment.setTitle(title);
            comment.setDescription(description);
            commentRepository.update(1, comment);
            connection.commit();
        } catch (Throwable e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            System.out.println("Rollbacked3");
        }
    }

    @Override
    public Comment getbyid(Integer id) {
        Comment a = new Comment();
        try {
            connection.setAutoCommit(false);
            a = commentRepository.getById(id);
            connection.commit();
        } catch (Throwable e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            System.out.println("Rollbacked4");
        }
        return a;
    }

    @Override
    public void deletebyid(Integer id) {
        try {
            connection.setAutoCommit(false);
            commentRepository.delete(id);
            connection.commit();
        } catch (Throwable e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            System.out.println("Rollbacked5");
        }
    }

    @Override
    public void deleteall() {
        try {
            connection.setAutoCommit(false);
            commentRepository.deleteall();
            connection.commit();
        } catch (Throwable e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            System.out.println("Rollbacked6");
        }
    }
}
