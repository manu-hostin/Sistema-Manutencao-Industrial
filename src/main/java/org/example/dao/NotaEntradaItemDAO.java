package org.example.dao;

import org.example.Database.Conexao;
import org.example.Model.NotaEntradaItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NotaEntradaItemDAO {

    public void registrarNotaEntradaItem (NotaEntradaItem nota) throws SQLException{
        String query = "INSERT INTO NotaEntradaItem (idNotaEntrada, idMaterial, quantidade) VALUES (?, ? , ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, nota.getIdNotaEntrada());
            stmt.setInt(2, nota.getIdMaterial());
            stmt.setDouble(3, nota.getQuantidade());
            stmt.executeUpdate();

        }
    }
}
