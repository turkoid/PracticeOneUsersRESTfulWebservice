package com.turkoid.rest.dao;

import com.turkoid.rest.model.User;

import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by turkoid on 8/21/2016.
 */
public class UserDao {

    public static List<User> getUsers() throws Exception {
        Connection conn = DatabaseUtils.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from tUsers");
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setAttending(rs.getBoolean("status"));
            users.add(user);
        }
        return users;
    }

    public static User getUser(int id) throws Exception {
        Connection conn = DatabaseUtils.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from tUsers where Id=" + id);
        User user = null;
        if (rs.next()) {
            user = new User();
            user.setId(id);
            user.setName(rs.getString("name"));
            user.setAttending(rs.getBoolean("status"));
        }
        return user;
    }
}
