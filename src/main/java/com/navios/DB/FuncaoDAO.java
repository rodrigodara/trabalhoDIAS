package com.navios.DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.navios.model.Funcao;

public class FuncaoDAO {

    public List<Funcao> listarFuncoes() {
        List<Funcao> funcoes = new ArrayList<>();
        String sql = "SELECT ID_Funcao, Nome FROM Funcao ORDER BY ID_Funcao";

        try (Connection conn = LigacaoDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                funcoes.add(new Funcao(
                    rs.getInt("ID_Funcao"),
                    rs.getString("Nome")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return funcoes;
    }
}
