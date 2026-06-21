package com.navios.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TipoCargaNavioDAO {

    public boolean isCompativel(int idTipoNavio, int idTipoCarga) {
        String sql = """
            SELECT COUNT(*) FROM Tipo_Carga_Navio
            WHERE ID_Tipo_Navio = ? AND ID_Tipo_Carga = ?
            """;

        try (Connection conn = LigacaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTipoNavio);
            stmt.setInt(2, idTipoCarga);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}