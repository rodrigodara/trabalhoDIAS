package com.navios.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.navios.model.Viagem;

public class ViagemDAO {

    public List<Viagem> listarViagens() {
        List<Viagem> viagens = new ArrayList<>();
        String sql = "SELECT * FROM Viagem";

        try (Connection conn = LigacaoDB.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            while (rs.next()) {
                viagens.add(new Viagem(
                    rs.getInt("ID_Viagem"),
                    rs.getInt("ID_Navio"),
                    rs.getDate("Data_Partida").toLocalDate(),
                    rs.getDate("Data_Chegada_Prevista").toLocalDate(),
                    rs.getInt("Porto_Origem"),
                    rs.getInt("Porto_Destino"),
                    rs.getString("Estado_Viagem")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return viagens;
    }

    public void inserirViagem(Viagem viagem) {
        String sql = """
            INSERT INTO Viagem
                (ID_Navio, Data_Partida, Data_Chegada_Prevista,
                 Porto_Origem, Porto_Destino, Estado_Viagem)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn          = LigacaoDB.getConnection();
             PreparedStatement pstmt  = conn.prepareStatement(sql)) {

            pstmt.setInt   (1, viagem.getIdNavio());
            pstmt.setDate  (2, java.sql.Date.valueOf(viagem.getDataPartida()));
            pstmt.setDate  (3, java.sql.Date.valueOf(viagem.getDataChegadaPrevista()));
            pstmt.setInt   (4, viagem.getPortoOrigem());
            pstmt.setInt   (5, viagem.getPortoDestino());
            pstmt.setString(6, viagem.getEstadoViagem());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ─── NOVO ─────────────────────────────────────────────────────────────
    public void atualizarViagem(Viagem viagem) {
        String sql = """
            UPDATE Viagem
            SET Data_Partida = ?, Data_Chegada_Prevista = ?,
                Porto_Origem = ?, Porto_Destino = ?, Estado_Viagem = ?
            WHERE ID_Viagem = ?
            """;

        try (Connection conn         = LigacaoDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate  (1, java.sql.Date.valueOf(viagem.getDataPartida()));
            pstmt.setDate  (2, java.sql.Date.valueOf(viagem.getDataChegadaPrevista()));
            pstmt.setInt   (3, viagem.getPortoOrigem());
            pstmt.setInt   (4, viagem.getPortoDestino());
            pstmt.setString(5, viagem.getEstadoViagem());
            pstmt.setInt   (6, viagem.getIdViagem());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}