package com.turkoid.rest.dao;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by turkoid on 8/22/2016.
 */
public class DatabaseUtils {
    public static final String DATABASE_RESOURCE = "java:comp/env/jdbc/practiceone";
    private static DataSource dataSource;

    public static synchronized Connection getConnection() throws SQLException, NamingException {
        if (dataSource == null) {
            initDataSource();
        }
        return dataSource.getConnection();
    }

    public static void initDataSource() throws NamingException {
        InitialContext ic = new InitialContext();
        dataSource = (DataSource) ic.lookup(DATABASE_RESOURCE);
    }

    public static DSLContext getJooqDslContext() throws Exception {
        return DSL.using(getConnection(), SQLDialect.MYSQL);
    }
}
