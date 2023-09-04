package com.smartcode.web.service.comment;

import com.smartcode.web.model.Comment;

public interface CommentService {
    void create(Comment comment);
    void update(Comment comment,Integer id,String title,String description);
    Comment getbyid(Integer id);
    void deletebyid(Integer id);
    void deleteall();
}
