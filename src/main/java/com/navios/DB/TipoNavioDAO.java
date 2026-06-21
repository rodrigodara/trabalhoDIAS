package com.navios.DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.navios.model.TipoNavio;

public class TipoNavioDAO {

    public List<TipoNavio> listarTipos() {
        List<TipoNavio> tipos = new ArrayList<>();
        String sql = "SELECT ID_Tipo_Navio, Nome, N_Maximo_Cargas FROM Tipo_Navio ORDER BY ID_Tipo_Navio";

        try (Connection conn = LigacaoDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tipos.add(new TipoNavio(
                    rs.getInt("ID_Tipo_Navio"),
                    rs.getString("Nome"),
                    rs.getInt("N_Maximo_Cargas")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipos;
    }
}
