package com.oneone.onehour.service;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionPool {
//    void initConnectionPool(String url, String user, String password) throws Exception;
    Connection getConnection() throws Exception;
    void returnConnection(Connection connection);
}
