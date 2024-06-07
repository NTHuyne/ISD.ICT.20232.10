package com.hust.ict.aims.controller;

import com.hust.ict.aims.entity.user.User;
import com.hust.ict.aims.exception.LoginAccountException;
import com.hust.ict.aims.persistence.dao.user.UserDAO;

public class LoginController {
    public User validateLogin(String username, String password, UserDAO userDAO) throws LoginAccountException {

        User accountDb = userDAO.getUserByUsername(username);

        if (accountDb == null) {
            throw new LoginAccountException("Login fail - account or password is wrong");
        }

        if (!password.equals(accountDb.getPassword())) {
            throw new LoginAccountException("Login fail - account or password is wrong");
        }

        return accountDb;
    }
}
