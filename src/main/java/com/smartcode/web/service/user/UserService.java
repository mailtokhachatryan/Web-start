package com.smartcode.web.service.user;


import com.smartcode.web.model.User;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface UserService {


    void transfer(User from, User to, BigDecimal amount) throws SQLException;

    void register(User user);
    void update(User user, Integer id, String name, String lastName, String middleName, String username, Integer age, String password, BigDecimal balance);

    User getbyid(Integer id);

    void deletebyid(Integer id);

    void deleteall();
    void login(String username, String password);

    User getByUsername(String username);
}
