package org.example.dao;

import org.example.Database.Conexao;
import org.example.Model.RequisicaoItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RequisicaoItemDAO {

    public void registrarRequisicaoItem (int idReq, int idMaterial, double quantidade) throws SQLException {
        String query = "INSERT INTO RequisicaoItem (idRequisicao, idMaterial, quantidade) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idReq);
            stmt.setInt(2, idMaterial);
            stmt.setDouble(3, quantidade);
            stmt.executeUpdate();

            System.out.println("Requisição item realizada com sucesso!");
        }
    }
}
