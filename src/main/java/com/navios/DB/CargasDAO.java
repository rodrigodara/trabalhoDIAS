package com.navios.DB;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.navios.model.Carga;

public class CargasDAO {

    public List<Carga> listarCargas() {
        List<Carga> cargas = new ArrayList<>();
        String sql = "SELECT * FROM Carga";

        try (Connection conn = LigacaoDB.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            while (rs.next()) {
                cargas.add(new Carga(
                    rs.getInt("ID_Carga"),
                    rs.getString("Designacao"),
                    rs.getInt("Tipo"),
                    rs.getFloat("Volume"),
                    rs.getFloat("Peso"),
                    rs.getInt("Porto_Carga"),
                    rs.getInt("Porto_Descarga")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cargas;
    }

    public void inserirCarga(Carga carga) {
        String sql = """
            INSERT INTO Carga
                (Designacao, Tipo, Volume, Peso, Porto_Carga, Porto_Descarga)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn         = LigacaoDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, carga.getDesignacao());
            pstmt.setInt   (2, carga.getTipo());
            pstmt.setFloat (3, carga.getVolume());
            pstmt.setFloat (4, carga.getPeso());
            pstmt.setInt   (5, carga.getPortoCarga());
            pstmt.setInt   (6, carga.getPortoDescarga());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void atualizarCarga(Carga carga) {
    String sql = """
        UPDATE Carga
        SET Designacao = ?, Tipo = ?, Volume = ?,
            Peso = ?, Porto_Carga = ?, Porto_Descarga = ?
        WHERE ID_Carga = ?
        """;

    try (Connection conn         = LigacaoDB.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, carga.getDesignacao());
        pstmt.setInt   (2, carga.getTipo());
        pstmt.setFloat (3, carga.getVolume());
        pstmt.setFloat (4, carga.getPeso());
        pstmt.setInt   (5, carga.getPortoCarga());
        pstmt.setInt   (6, carga.getPortoDescarga());
        pstmt.setInt   (7, carga.getIdCarga());  // WHERE no fim

        pstmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }
}