package com.oneone.onehour.service.impl;

import com.oneone.onehour.service.IConnectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class ConnectionPool implements IConnectionPool {
    String url;
    String user;
    String password;

    //使用中的连接
    private List<Connection> useConnectionList = new LinkedList<>();
    //空闲的连接
    private Deque<Connection> freeConnectionList = new LinkedList<>();

    public ConnectionPool(String url, String user, String password,String className) throws Exception {
        this.url = url;
        this.user = user;
        this.password = password;

        Class.forName(className);
        for (int i = 0; i < 5; i++) {
            open();
        }
    }

    private Connection open() throws SQLException {
        Connection connection = DriverManager.getConnection(url,user,password);
        freeConnectionList.add(connection);
        return connection;
    }


    @Override
    public Connection getConnection() throws Exception{
        if(freeConnectionList == null && freeConnectionList.size() + useConnectionList.size() == 60){
            throw new Exception("连接池无空余连接");
        }
        if(freeConnectionList.size() < 2){
            for (int i = 0; i < 5; i++) {
                open();
            }
        }else {
            removeFreeConnection();
        }
        Connection connection = freeConnectionList.removeFirst();
        useConnectionList.add(connection);
        return connection;
    }

    @Override
    public void returnConnection(Connection connection) {
        useConnectionList.remove(connection);
        freeConnectionList.add(connection);
    }

    private void removeFreeConnection() {
        if(freeConnectionList.size() >= 8){
            for(int i = 0;i < 5;i++){
                Connection connection = freeConnectionList.removeFirst();
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
