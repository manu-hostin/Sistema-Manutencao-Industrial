package org.example.dao;

import org.example.Database.Conexao;
import org.example.Main;
import org.example.Model.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MaterialDAO {

    public void cadastrarMaterial (Material material) throws SQLException{
        String query = "INSERT INTO Material (nome, unidade, estoque) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1, material.getNome());
            stmt.setString(2, material.getUnidade());
            stmt.setDouble(3, material.getEstoque());
            stmt.executeUpdate();

            System.out.println("\nMaterial cadastrado com sucesso!");
        }
    }
    public boolean verificarDuplicacao (Material material) throws SQLException {
        String query = "SELECT COUNT(0) AS linhas FROM Material WHERE nome = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, material.getNome());
            ResultSet rs = stmt.executeQuery();

            if(rs.next() && rs.getInt("linhas") > 0){
                return true;
            }
        }
        return false;
    }
    public List<Material> listarMateriais () throws SQLException {
        String query = "SELECT (nome, unidade, estoque) FROM Material";
        List<Material> materiais = new ArrayList<>();

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                String nome = rs.getString("nome");
                String unidade = rs.getString("unidade");
                Double quantidade = rs.getDouble("estoque");

                var Material = new Material(nome, unidade, quantidade);
                materiais.add(Material);
            }
        }
        return materiais;
    }
}
