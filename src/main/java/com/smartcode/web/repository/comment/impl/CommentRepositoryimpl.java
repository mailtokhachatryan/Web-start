package com.smartcode.web.repository.comment.impl;

import com.smartcode.web.model.Comment;
import com.smartcode.web.repository.comment.CommentRepository;
import com.smartcode.web.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CommentRepositoryimpl implements CommentRepository {

    private Connection connection = DataSource.getInstance().getConnection();

    public CommentRepositoryimpl() {
        try {
            connection.createStatement().execute(
                    """
                            CREATE TABLE IF NOT EXISTS comments (
                                id serial primary key ,
                                title varchar(255) not null ,
                                description varchar(255) not null,
                                user_id integer not null
                                references users
                            );
                            """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Comment comment) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO comments (title ,description , user_id) values(?,?,?)");
            preparedStatement.setString(1, comment.getTitle());
            preparedStatement.setString(2, comment.getDescription());
            preparedStatement.setInt(3, comment.getUserId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Comment update(Integer commentId, Comment comment) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                            UPDATE users SET
                            title = ?,
                            description = ?,
                            WHERE id = ?
                                """);

            preparedStatement.setString(1, comment.getTitle());
            preparedStatement.setString(2, comment.getDescription());
            preparedStatement.setInt(3, comment.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return comment;
    }

    public List<Comment> getAll(Integer userId) {
        List<Comment> comments = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM comments WHERE user_id = ?");

            preparedStatement.setInt(1, userId);

            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                int commentId = result.getInt(userId);
                String commentTitle = result.getString("title");
                String commentDescription = result.getString("description");

                Comment comment = new Comment(commentId, commentTitle, commentDescription);

                comments.add(comment);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return comments;
    }

    @Override
    public Comment getById(Integer commentId) {
        Comment comment = new Comment();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("SELECT * from comment WHERE id = ?");
            preparedStatement.setInt(1, commentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return fromResultSet(resultSet);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return comment;
    }

    @Override
    public void delete(Integer commentId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM comment WHERE id = ?");
            preparedStatement.setInt(1, commentId);
            int i = preparedStatement.executeUpdate();
            System.out.println(i);
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteall() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("drop table public.comment");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Comment fromResultSet(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();
        comment.setId(resultSet.getInt("id"));
        comment.setTitle(resultSet.getString("title"));
        comment.setDescription(resultSet.getString("description"));
        return comment;
    }
}
