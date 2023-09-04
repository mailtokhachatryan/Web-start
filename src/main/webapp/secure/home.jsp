<%@ page import="com.smartcode.web.repository.comment.CommentRepository" %>
<%@ page import="com.smartcode.web.repository.user.UserRepository" %>
<%@ page import="com.smartcode.web.repository.user.impl.UserRepositoryImpl" %>
<%@ page import="com.smartcode.web.model.Comment" %>
<%@ page import="com.smartcode.web.repository.comment.impl.CommentRepositoryimpl" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home</title>
</head>

<style>
    table, th, td {
        border: 1px solid black;
    }
</style>
<body>

<form action="/logout" method="get">
    <input type="submit" value="Log Out">
</form>

<h2>Welcome dear <%=request.getSession().getAttribute("username")%>
</h2>
<form action="/comment" method="post">
    title <input type="text" name="title"></br>
    description <input type="text" name="description"></br>
    <input type="submit" value="create">
</form>

<br>
<br>
<br>

<table>

    <tr>
        <th>Comment table</th>
        <th>Description</th>
    </tr>

    <%
        CommentRepository commentRepository = new CommentRepositoryimpl();

        UserRepository userRepository = new UserRepositoryImpl();

        Integer id = (Integer) request.getSession().getAttribute("id");

        List<Comment> list = commentRepository.getAll(id);

        for (Comment comment : list) {
    %>
    <tr>
        <td>
            <%=comment.getId()%>
        </td>
        <td>
            <%=comment.getTitle()%>
        </td>
        <td>
            <%=comment.getDescription()%>
        </td>
    </tr>
    <%
        }
    %>

</table>

</body>
</html>