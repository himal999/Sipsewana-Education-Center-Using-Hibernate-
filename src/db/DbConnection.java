package db;/*
author :Himal
version : 0.0.1
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    private static DbConnection dbConnection;
    private final Connection connection;

    private DbConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/SipsewanaEducationCenter","root","");
    }

    public  static DbConnection getInstance() throws ClassNotFoundException, SQLException {
        if(dbConnection == null){
            dbConnection = new DbConnection();
        }
        return dbConnection;
    }

    public  Connection getConnection(){
        return  connection;
    }
}
