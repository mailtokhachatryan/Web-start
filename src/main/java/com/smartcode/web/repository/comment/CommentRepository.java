package com.smartcode.web.repository.comment;

import com.smartcode.web.model.Comment;

import java.util.List;

public interface CommentRepository {
    void create(Comment comment);
    Comment update(Integer commentId,Comment comment);
    List<Comment> getAll(Integer userId);
    Comment getById(Integer commentId);
    void delete(Integer commentId);
    void deleteall();
}
