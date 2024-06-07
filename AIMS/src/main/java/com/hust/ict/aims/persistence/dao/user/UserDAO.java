package com.hust.ict.aims.persistence.dao.user;

import com.hust.ict.aims.entity.user.User;
import com.hust.ict.aims.persistence.database.ConnectJDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public List<User> getAll(){
        List<User> users = new ArrayList<>();
        Connection conn = null;
        String query = "SELECT * FROM User";

        try{
            conn = ConnectJDBC.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(query);
            while (res.next()){
                User user = new User();
                user.setId(res.getInt("id"));
                user.setUsername(res.getString("username"));
                user.setPassword(res.getString("password"));
                user.setIsAdmin(res.getBoolean("isAdmin"));
                users.add(user);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    public User getUserByUsername(String username){
        User user = null;
        Connection conn = null;
        String query = "SELECT * FROM User WHERE username = ?";

        try{
            conn = ConnectJDBC.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet res = stmt.executeQuery();
            while (res.next()){
                user = new User();
                user.setId(res.getInt("id"));
                user.setUsername(res.getString("username"));
                user.setPassword(res.getString("password"));
                user.setIsAdmin(res.getBoolean("isAdmin"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return user;
    }
}
