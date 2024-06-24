package com.hust.ict.aims.persistence.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hust.ict.aims.entity.user.User;
import com.hust.ict.aims.persistence.database.ConnectJDBC;

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
                user.setId(res.getInt("user_id"));
                user.setUsername(res.getString("username"));
                user.setPassword(res.getString("password"));
                user.setIsAdmin(res.getBoolean("isAdmin"));
                user.setEmail(res.getString("email"));
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
                user.setId(res.getInt("user_id"));
                user.setUsername(res.getString("username"));
                user.setPassword(res.getString("password"));
                user.setIsAdmin(res.getBoolean("isAdmin"));
                user.setEmail(res.getString("email"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return user;
    }

    public void updateUser(User user) {
        Connection conn = null;

        try {
            conn = ConnectJDBC.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE User SET username = ?, password = ?, email = ?, isAdmin = ? WHERE user_id = ?;");
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setBoolean(4, user.getIsAdmin());
            stmt.setInt(5, user.getId());
            stmt.executeUpdate();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUserPassword(String password, int id) {
        Connection conn = null;

        try {
            conn = ConnectJDBC.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE User SET password = ? WHERE user_id = ?;");
            stmt.setString(1, password);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int id) {
        Connection conn = null;

        try {
            conn = ConnectJDBC.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM User WHERE user_id = ?;");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUser(User user) {
        Connection conn = null;

        try {
            conn = ConnectJDBC.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO User (username, password, email, isAdmin) VALUES (?, ?, ?, ?)");
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setBoolean(4, user.getIsAdmin());
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
