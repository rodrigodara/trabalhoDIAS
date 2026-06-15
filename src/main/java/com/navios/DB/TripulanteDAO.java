package com.navios.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.navios.model.Tripulante;

public class TripulanteDAO {

    public List<Tripulante> listarTripulantes() {
        List<Tripulante> tripulantes = new ArrayList<>();
        String sql = "SELECT * FROM Tripulante";

        try (Connection conn = LigacaoDB.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tripulantes.add(new Tripulante(
                    rs.getInt("ID_Tripulante"),
                    rs.getString("Nome"),
                    rs.getString("Sobrenome"),
                    rs.getString("Estado_Disponibilidade"),
                    rs.getString("Nacionalidade"),
                    rs.getDate("Data_Nascimento").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tripulantes;
    }

    public void inserirTripulante(Tripulante t) {
        String sql = """
            INSERT INTO Tripulante
                (Nome, Sobrenome, Estado_Disponibilidade, Nacionalidade, Data_Nascimento)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conn         = LigacaoDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, t.getNome());
            pstmt.setString(2, t.getSobrenome());
            pstmt.setString(3, t.getEstadoDisponibilidade());
            pstmt.setString(4, t.getNacionalidade());
            pstmt.setDate  (5, java.sql.Date.valueOf(t.getDataNascimento()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}