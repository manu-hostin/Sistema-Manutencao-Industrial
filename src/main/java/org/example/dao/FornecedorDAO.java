package org.example.dao;

import org.example.Database.Conexao;
import org.example.Model.Fornecedor;
import org.example.Model.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO {

    public void cadastrarFornecedor (Fornecedor fornecedor) throws SQLException{
        String query = "INSERT INTO Fornecedor (nome, cnpj) VALUES (?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1, fornecedor.getNome());
            stmt.setString(2, fornecedor.getCnpj());
            stmt.executeUpdate();

            System.out.println("\nFornecedor cadastrado com sucesso!");
        }
    }
    public boolean validarDuplicacao (Fornecedor fornecedor) throws SQLException{
        String query = "SELECT COUNT(0) AS linhas FROM Fornecedor WHERE cnpj = ?";

        try (Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1, fornecedor.getCnpj());
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getInt("linhas") > 0){
                return true;
            }
        }
        return false;
    }
    public List<Fornecedor> listarFornecedores () throws SQLException {
        String query = "SELECT (id, cnpj, nome) FROM Fornecedor";
        List<Fornecedor> fornecedores = new ArrayList<>();

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cnpj = rs.getString("cnpj");

                var fornecedor = new Fornecedor(nome, cnpj, id);
                fornecedores.add(fornecedor);
            }
        }
        return fornecedores;
    }

}
