package com.navios.DB;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.navios.model.Porto;
public class PortoDAO {
    public List<Porto> listarPortos() {
        List<Porto> portos = new ArrayList<>();
        String sql = "SELECT ID_Porto, Nome FROM Porto";

        try (Connection conn = LigacaoDB.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            while (rs.next()) {
                portos.add(new Porto(rs.getInt("ID_Porto"), rs.getString("Nome")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return portos;
    }
}