package com.navios.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LigacaoDB {

    private static final String URL = 
        "jdbc:sqlserver://localhost:1433;databaseName=ProjetoDB;encrypt=false;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "Admin123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}