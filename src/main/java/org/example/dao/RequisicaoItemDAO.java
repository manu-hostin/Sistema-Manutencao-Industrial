package org.example.dao;

import org.example.Database.Conexao;
import org.example.Model.RequisicaoItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RequisicaoItemDAO {

    public void registrarRequisicaoItem(int idReq, int idMaterial, double quantidade) throws SQLException {
        String query = "INSERT INTO RequisicaoItem (idRequisicao, idMaterial, quantidade) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idReq);
            stmt.setInt(2, idMaterial);
            stmt.setDouble(3, quantidade);
            stmt.executeUpdate();

            System.out.println("✅ Item da requisição registrado com sucesso! (idRequisicao=" + idReq + ")");
        }
    }

    public List<RequisicaoItem> buscarItensPorRequisicao(int idRequisicao) throws SQLException {
        List<RequisicaoItem> itens = new ArrayList<>();
        String query = "SELECT * FROM RequisicaoItem WHERE idRequisicao = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idRequisicao);
            var rs = stmt.executeQuery();

            while (rs.next()) {
                itens.add(new RequisicaoItem(
                        rs.getInt("idRequisicao"),
                        rs.getInt("idMaterial"),
                        rs.getDouble("quantidade")
                ));
            }
        }
        return itens;
    }
    public List<RequisicaoItem> listarItens()throws SQLException{
        List<RequisicaoItem> itensDaRequisicao = new ArrayList<>();

        String query = """
                SELECT idRequisicao, idMaterial, quantidade
                FROM RequisicaoItem;
                """;

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query)){

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                int idRequisicao = rs.getInt("idRequisicao");
                int idMaterial = rs.getInt("idMaterial");
                double quantidade = rs.getDouble("quantidade");

                var itensRequisicao = new RequisicaoItem(idRequisicao, idMaterial, quantidade);
                itensDaRequisicao.add(itensRequisicao);
            }
            return itensDaRequisicao;


        }

    }
}
