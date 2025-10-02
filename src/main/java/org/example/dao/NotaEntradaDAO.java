package org.example.dao;

import org.example.Database.Conexao;
import org.example.Model.NotaEntrada;

import java.sql.*;
import java.time.LocalDate;

public class NotaEntradaDAO {

    public void registrarNotaEntrada (int idFornecedor, java.sql.Date dataEntrada) throws SQLException{
        String query = "INSERT INTO NotaEntrada (idFornecedor, dataEntrada) VALUES (?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idFornecedor);
            stmt.setDate(2, dataEntrada);
            stmt.executeUpdate();

            System.out.println("\nNota adicionada com sucesso!");
        }
    }
    public int pegarIDNota (int idFornecedor, Date dataEntrada) throws SQLException{
        String query = "SELECT id FROM NotaEntrada WHERE idFornecedor = ? && dataEntrada = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idFornecedor);
            stmt.setDate(2, dataEntrada);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                return rs.getInt("id");
            }

        }
        return -1;
    }
}
