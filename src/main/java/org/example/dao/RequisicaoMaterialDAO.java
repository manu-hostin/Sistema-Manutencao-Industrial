package org.example.dao;

import org.example.Database.Conexao;
import org.example.Model.RequisicaoItem;
import org.example.Model.RequisicaoMaterial;

import javax.swing.plaf.SliderUI;
import java.io.PipedInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequisicaoMaterialDAO {

    public void RegistrarRequisicao (RequisicaoMaterial requisicao) throws SQLException{
        String query = "INSERT INTO Requisicao (setor, status, dataSolicitacao) VALUES (?, 'PENDENTE', ?)";

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1, requisicao.getSetor());
            stmt.setDate(2, (Date) requisicao.getDataSolicitacao());
            stmt.executeUpdate();

            System.out.println("Requisição realizada com sucesso!");
        }
    }
    public int pegarIDRequisicao (String setor, Date data) throws SQLException{
        String query = "SELECT id FROM Requisicao WHERE setor = ? AND dataSolicitacao = ?";

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1, setor);
            stmt.setDate(2, data);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                return rs.getInt("id");
            }
        }
        return -1;
    }
    public List<RequisicaoMaterial> listarRequisicoes () throws SQLException{
        String query = "SELECT * FROM Requisicao WHERE status = 'PENDENTE'";
        List<RequisicaoMaterial> lista = new ArrayList<>();

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query)){

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String setor = rs.getString("setor");
                String status = rs.getString("status");
                Date data = rs.getDate("dataSolicitacao");

                var rq = new RequisicaoMaterial(id, setor, status, data);
                lista.add(rq);
            }
        }
        return lista;
    }
}
