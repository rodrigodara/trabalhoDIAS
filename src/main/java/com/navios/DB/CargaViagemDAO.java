package com.navios.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class CargaViagemDAO {

    public Set<Integer> listarIdsCargasPorViagem(int idViagem) {
        Set<Integer> ids = new HashSet<>();
        String sql = "SELECT ID_Carga FROM Carga_Viagem WHERE ID_Viagem = ?";

        try (Connection conn = LigacaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idViagem);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("ID_Carga"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    public void inserirLigacao(int idViagem, int idCarga) {
        String sql = "INSERT INTO Carga_Viagem (ID_Viagem, ID_Carga) VALUES (?, ?)";

        try (Connection conn = LigacaoDB.getConnection()) {
            boolean usaIdentityInsert = colunaViagemIdentity(conn);
            setIdentityInsert(conn, usaIdentityInsert, true);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idViagem);
                stmt.setInt(2, idCarga);
                stmt.executeUpdate();
            } finally {
                setIdentityInsert(conn, usaIdentityInsert, false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removerLigacao(int idViagem, int idCarga) {
        String sql = "DELETE FROM Carga_Viagem WHERE ID_Viagem = ? AND ID_Carga = ?";

        try (Connection conn = LigacaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idViagem);
            stmt.setInt(2, idCarga);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void substituirCargasDaViagem(int idViagem, Set<Integer> idsCargas) {
        String deleteSql = "DELETE FROM Carga_Viagem WHERE ID_Viagem = ?";
        String insertSql = "INSERT INTO Carga_Viagem (ID_Viagem, ID_Carga) VALUES (?, ?)";

        try (Connection conn = LigacaoDB.getConnection()) {
            conn.setAutoCommit(false);
            boolean usaIdentityInsert = colunaViagemIdentity(conn);

            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, idViagem);
                deleteStmt.executeUpdate();
            }

            setIdentityInsert(conn, usaIdentityInsert, true);
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                for (Integer idCarga : idsCargas) {
                    insertStmt.setInt(1, idViagem);
                    insertStmt.setInt(2, idCarga);
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
            SELECT COLUMNPROPERTY(OBJECT_ID('Carga_Viagem'), 'ID_Viagem', 'IsIdentity') AS IsIdentity
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
            stmt.execute("SET IDENTITY_INSERT Carga_Viagem " + (ligado ? "ON" : "OFF"));
        }
    }
}
