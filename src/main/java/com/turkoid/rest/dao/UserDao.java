package com.turkoid.rest.dao;

import com.turkoid.rest.model.User;
import org.jooq.DSLContext;
import org.jooq.conf.ParamType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

/**
 * Created by turkoid on 8/21/2016.
 */
public class UserDao {

    public static List<User> getUsers() throws Exception {
        DSLContext jooq = DatabaseUtils.getJooqDslContext();
        String sql = jooq.select()
                .from(table("tUsers"))
                .getSQL();

        Connection conn = DatabaseUtils.getConnection();
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(sql);
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
        DSLContext jooq = DatabaseUtils.getJooqDslContext();
        String sql = jooq.select()
                .from(table("tUsers"))
                .where(field("id").equal(id))
                .getSQL(ParamType.INLINED);

        Connection conn = DatabaseUtils.getConnection();
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(sql);
        User user = null;
        if (rs.next()) {
            user = new User();
            user.setId(id);
            user.setName(rs.getString("name"));
            user.setAttending(rs.getBoolean("status"));
        }
        return user;
    }

    public static User addUser(User user) throws Exception {
        if (user != null) {
            DSLContext jooq = DatabaseUtils.getJooqDslContext();
            String sql = jooq.insertInto(table("tUsers"))
                    .columns(field("name"), field("status"))
                    .values(user.getName(), user.isAttending())
                    .getSQL(ParamType.INLINED);

            Connection conn = DatabaseUtils.getConnection();
            Statement stmt = conn.createStatement();

            if (stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS) > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }
        }
        return user;
    }

    public static boolean updateUser(User user) throws Exception {
        if (user != null) {
            DSLContext jooq = DatabaseUtils.getJooqDslContext();
            String sql = jooq.update(table("tUsers"))
                    .set(field("name"), user.getName())
                    .set(field("status"), user.isAttending())
                    .where(field("id").eq(user.getId()))
                    .getSQL(ParamType.INLINED);

            Connection conn = DatabaseUtils.getConnection();
            Statement stmt = conn.createStatement();

            return stmt.executeUpdate(sql) > 0;
        }
        return false;
    }

    public static boolean deleteUser(int id) throws Exception {
        DSLContext jooq = DatabaseUtils.getJooqDslContext();
        String sql = jooq.delete(table("tUsers"))
                .where(field("id").eq(id))
                .getSQL(ParamType.INLINED);

        Connection conn = DatabaseUtils.getConnection();
        Statement stmt = conn.createStatement();

        return stmt.executeUpdate(sql) > 0;
    }
}
