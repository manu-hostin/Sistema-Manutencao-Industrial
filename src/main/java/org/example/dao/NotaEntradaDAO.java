package org.example.dao;

import org.example.Database.Conexao;
import org.example.Model.NotaEntrada;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class NotaEntradaDAO {

    public void registrarNotaEntrada (int idFornecedor, Date dataEntrada) throws SQLException{
        String query = "INSERT INTO nota_entrada (idFornecedor, dataEntrada) VALUES (?, ?)";


        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idFornecedor);
            stmt.setDate(2, dataEntrada);
            stmt.executeUpdate();

            System.out.println("Nota adicionada com sucesso!");
        }
    }
}
