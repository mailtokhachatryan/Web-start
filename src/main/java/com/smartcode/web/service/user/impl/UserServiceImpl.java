package com.smartcode.web.service.user.impl;


import com.smartcode.web.exception.UsernameAlreadyExistsException;
import com.smartcode.web.exception.ValidationException;
import com.smartcode.web.model.User;
import com.smartcode.web.repository.user.UserRepository;
import com.smartcode.web.service.user.UserService;
import com.smartcode.web.utils.DataSource;
import com.smartcode.web.utils.MD5Encoder;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final Connection connection = DataSource.getInstance().getConnection();

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void transfer(User from, User to, BigDecimal amount) throws SQLException {
        try {
            connection.setAutoCommit(false);
            from.setBalance(from.getBalance().subtract(amount));
            to.setBalance(to.getBalance().add(amount));
            userRepository.update(from);
            userRepository.update(to);
            connection.commit();
        } catch (Throwable e) {
            connection.rollback();
            System.out.println("Rollbacked");
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public void register(User user) {
        try {
            connection.setAutoCommit(false);
            validateUserRegistration(user);
            user.setPassword(MD5Encoder.encode(user.getPassword()));
            userRepository.create(user);
            connection.commit();
        } catch (SQLException e) {
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

    public void update(User user, Integer id, String name, String lastName, String middleName, String username, Integer age, String password, BigDecimal balance) {
        try {
            connection.setAutoCommit(false);
            user.setName(name);
            user.setLastName(lastName);
            user.setMiddleName(middleName);
            user.setUsername(username);
            user.setAge(age);
            user.setPassword(password);
            user.setBalance(balance);
            userRepository.update(user);
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
    public User getbyid(Integer id) {
        User a = new User();
        try {
            connection.setAutoCommit(false);
            a = userRepository.getById(id);
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
            userRepository.delete(id);
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
            userRepository.deleteall();
            connection.commit();
        } catch (Throwable e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            System.out.println("Rollbacked6");
        }
    }

    @Override
    public void login(String username, String password) {
        User byUsername = userRepository.getByUsername(username);
        if (!byUsername.getPassword().equals(password)) {
            throw new ValidationException("Invalid login or password");
        }
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    private void validateUserRegistration(User user) {
        if (userRepository.getByUsername(user.getUsername()) != null) {
            throw new UsernameAlreadyExistsException(String.format("User by username: %s already exists", user.getUsername()));
        }
        if (user.getPassword().length() < 8) {
            throw new ValidationException("Password must be more than 8 symbols");
        }

    }

    public void getAll() throws SQLException {

        connection.setTransactionIsolation(8);
        List<User> all = userRepository.getAll();


    }
}
