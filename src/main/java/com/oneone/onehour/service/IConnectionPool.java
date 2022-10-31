package com.oneone.onehour.service;

import java.sql.Connection;

public interface IConnectionPool {
    void createConnectPool(String url,String user,String password);
    Connection getConnection(String url,String user,String password);
    void returnConnection(Connection connection);
    void removeFreeConnection();
}
