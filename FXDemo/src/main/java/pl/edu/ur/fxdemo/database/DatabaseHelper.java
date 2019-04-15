/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.ur.fxdemo.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author marcin
 */
public class DatabaseHelper {

    public Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "postgres");
        //props.setProperty("password", "student");
        //props.setProperty("ssl", "true");
        return DriverManager.getConnection(url, props);
    }

    public void closeConnection(Connection conn) throws SQLException {
        conn.close();
    }

    public ResultSet executeQuery(String query) throws SQLException {

        Connection conn = getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        closeConnection(conn);
        return resultSet;

    }

}
