package org.example.dao;

import org.example.Database.Conexao;
import org.example.Model.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {

    public void cadastrarMaterial(Material material) throws SQLException {
        String query = "INSERT INTO Material (nome, unidade, estoque) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, material.getNome());
            stmt.setString(2, material.getUnidade());
            stmt.setDouble(3, material.getEstoque());
            stmt.executeUpdate();

            System.out.println("\nMaterial cadastrado com sucesso!");
        }
    }

    public boolean verificarDuplicacao(Material material) throws SQLException {
        String query = "SELECT COUNT(0) AS linhas FROM Material WHERE nome = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, material.getNome());
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getInt("linhas") > 0) {
                return true;
            }
        }
        return false;
    }

    public List<Material> listarMateriais() throws SQLException {
        String query = "SELECT id, nome, unidade, estoque FROM Material";
        List<Material> materiais = new ArrayList<>();

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String unidade = rs.getString("unidade");
                Double quantidade = rs.getDouble("estoque");

                var Material = new Material(id, nome, unidade, quantidade);
                materiais.add(Material);
            }
        }
        return materiais;
    }

    public boolean verificarDuplicacaoID(Material material) throws SQLException {
        String query = "SELECT COUNT(0) AS linhas FROM Material WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, material.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getInt("linhas") > 0) {
                return true;
            }
        }
        return false;
    }

    public void atualizarMaterial(Double quantidade, int id) throws SQLException {
        String query = "UPDATE Material SET estoque = ? WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, quantidade);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public double buscarQuantidade(String nome) throws SQLException {
        String query = "SELECT estoque FROM Material WHERE nome = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("estoque");
            } else {
                return -1;
            }

        }
    }
    public int pegarIDMaterial(String nome) throws SQLException {
        String query = "SELECT id FROM Material WHERE nome = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }
    public boolean validarMaterial (String nome) throws SQLException{
        String query = "SELECT COUNT(0) AS linhas FROM Material WHERE nome = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getInt("linhas") > 0) {
                return true;
            }
        }
        return false;
    }
}