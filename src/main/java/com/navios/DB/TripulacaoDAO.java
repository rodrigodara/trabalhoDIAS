package com.navios.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class TripulacaoDAO {

    public Map<Integer, Integer> listarFuncoesPorTripulante(int idViagem) {
        Map<Integer, Integer> funcoesPorTripulante = new HashMap<>();
        String sql = "SELECT ID_Tripulante, ID_Funcao FROM Tripulacao WHERE ID_Viagem = ?";

        try (Connection conn = LigacaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idViagem);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    funcoesPorTripulante.put(
                        rs.getInt("ID_Tripulante"),
                        rs.getInt("ID_Funcao")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return funcoesPorTripulante;
    }

    public void inserirLigacao(int idViagem, int idTripulante, int idFuncao) {
        String sql = "INSERT INTO Tripulacao (ID_Viagem, ID_Tripulante, ID_Funcao) VALUES (?, ?, ?)";

        try (Connection conn = LigacaoDB.getConnection()) {
            boolean usaIdentityInsert = colunaViagemIdentity(conn);
            setIdentityInsert(conn, usaIdentityInsert, true);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idViagem);
                stmt.setInt(2, idTripulante);
                stmt.setInt(3, idFuncao);
                stmt.executeUpdate();
            } finally {
                setIdentityInsert(conn, usaIdentityInsert, false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removerLigacao(int idViagem, int idTripulante) {
        String sql = "DELETE FROM Tripulacao WHERE ID_Viagem = ? AND ID_Tripulante = ?";

        try (Connection conn = LigacaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idViagem);
            stmt.setInt(2, idTripulante);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void substituirTripulacaoDaViagem(int idViagem, Map<Integer, Integer> funcoesPorTripulante) {
        String deleteSql = "DELETE FROM Tripulacao WHERE ID_Viagem = ?";
        String insertSql = "INSERT INTO Tripulacao (ID_Viagem, ID_Tripulante, ID_Funcao) VALUES (?, ?, ?)";

        try (Connection conn = LigacaoDB.getConnection()) {
            conn.setAutoCommit(false);
            boolean usaIdentityInsert = colunaViagemIdentity(conn);

            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, idViagem);
                deleteStmt.executeUpdate();
            }

            setIdentityInsert(conn, usaIdentityInsert, true);
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                for (Map.Entry<Integer, Integer> entry : funcoesPorTripulante.entrySet()) {
                    insertStmt.setInt(1, idViagem);
                    insertStmt.setInt(2, entry.getKey());
                    insertStmt.setInt(3, entry.getValue());
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            } finally {
                setIdentityInsert(conn, usaIdentityInsert, false);
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean colunaViagemIdentity(Connection conn) throws SQLException {
        String sql = """
            SELECT COLUMNPROPERTY(OBJECT_ID('Tripulacao'), 'ID_Viagem', 'IsIdentity') AS IsIdentity
            """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() && rs.getInt("IsIdentity") == 1;
        }
    }

    private void setIdentityInsert(Connection conn, boolean aplicar, boolean ligado) throws SQLException {
        if (!aplicar) {
            return;
        }
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("SET IDENTITY_INSERT Tripulacao " + (ligado ? "ON" : "OFF"));
        }
    }
}
