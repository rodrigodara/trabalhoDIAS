package com.navios.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.navios.model.Navio;

public class NavioDAO {

    public List<Navio> listarNavios() {
        List<Navio> navio = new ArrayList<>();
        // JOIN com Tipo_Navio para obter Capacidade_Carga
        String sql = """
            SELECT n.*, tn.Capacidade_Carga
            FROM Navio n
            JOIN Tipo_Navio tn ON n.Tipo = tn.ID_Tipo_Navio
        """;

        try (Connection conn = LigacaoDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                navio.add(new Navio(
                    rs.getInt("ID_Navio"),
                    rs.getString("Nome"),
                    rs.getString("IdentificadorIMO"),
                    rs.getInt("Tipo"),
                    rs.getInt("N_Compartimentos"),
                    rs.getInt("N_Maximo_Cargas"),
                    rs.getDouble("Capacidade_Carga"),  // vem do JOIN
                    rs.getString("Bandeira"),
                    rs.getInt("Ano_Fabrico"),
                    rs.getString("Estado_Operacional")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return navio;
    }

    public void inserirNavio(Navio navio) {
        // Capacidade_Carga NÃO é coluna de Navio — removida
        String sql = """
            INSERT INTO Navio 
                (Nome, IdentificadorIMO, Tipo, N_Compartimentos, N_Maximo_Cargas, Bandeira, Ano_Fabrico, Estado_Operacional)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = LigacaoDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, navio.getNome());
            pstmt.setString(2, navio.getIdentificadorIMO());
            pstmt.setInt   (3, navio.getTipo());
            pstmt.setInt   (4, navio.getNCompartimentos());
            pstmt.setInt   (5, navio.getNMaximoCargas());
            pstmt.setString(6, navio.getBandeira());
            pstmt.setInt   (7, navio.getAnoFabrico());
            pstmt.setString(8, navio.getEstadoOperacional());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizarNavio(Navio navio) {
        // Capacidade_Carga NÃO é coluna de Navio — removida
        String sql = """
            UPDATE Navio
            SET Nome = ?, IdentificadorIMO = ?, Tipo = ?,
                N_Compartimentos = ?, N_Maximo_Cargas = ?, Bandeira = ?,
                Ano_Fabrico = ?, Estado_Operacional = ?
            WHERE ID_Navio = ?
        """;

        try (Connection conn = LigacaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, navio.getNome());
            stmt.setString(2, navio.getIdentificadorIMO());
            stmt.setInt   (3, navio.getTipo());
            stmt.setInt   (4, navio.getNCompartimentos());
            stmt.setInt   (5, navio.getNMaximoCargas());
            stmt.setString(6, navio.getBandeira());
            stmt.setInt   (7, navio.getAnoFabrico());
            stmt.setString(8, navio.getEstadoOperacional());
            stmt.setInt   (9, navio.getIdNavio());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}