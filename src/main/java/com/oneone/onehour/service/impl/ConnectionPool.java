package com.oneone.onehour.service.impl;

import com.mysql.cj.jdbc.ConnectionImpl;
import com.oneone.onehour.service.IConnectionPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ConnectionPool implements IConnectionPool {
    //使用中的连接
    private LinkedList<Connection> useConnectionList = new LinkedList<>();
    //空闲的连接
    private LinkedList<Connection> freeConnectionList = new LinkedList<>();


    @Override
    public void createConnectPool(String url,String user,String password) {
        try {
            Connection connection = DriverManager.getConnection(url,user,password);
            for(int i = 0;i < 5;i++){
                freeConnectionList.add(connection);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnection(String url,String user,String password) {
        if(freeConnectionList == null && freeConnectionList.size() + useConnectionList.size() == 60){
            return null;
        }
        if(freeConnectionList.size() < 2){
            createConnectPool(url,user,password);
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

    @Override
    public void removeFreeConnection() {
        if(freeConnectionList.size() >= 8){
            for(int i = 0;i < 5;i++){
                freeConnectionList.remove(freeConnectionList.get(i));
            }
        }
    }


}
