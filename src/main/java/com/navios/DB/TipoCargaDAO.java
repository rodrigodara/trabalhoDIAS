package com.navios.DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.navios.model.TipoCarga;

public class TipoCargaDAO {

    public List<TipoCarga> listarTipos() {
        List<TipoCarga> tipos = new ArrayList<>();
        String sql = "SELECT * FROM Tipo_Carga";

        try (Connection conn = LigacaoDB.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tipos.add(new TipoCarga(
                    rs.getInt("ID_Tipo_Carga"),
                    rs.getString("Nome"),
                    rs.getString("Designacao")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipos;
    }
}