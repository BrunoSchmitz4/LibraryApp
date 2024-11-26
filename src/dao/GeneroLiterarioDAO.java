package dao;

import model.GeneroLiterario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GeneroLiterarioDAO {

    private static final String INSERT = "INSERT INTO genero_literario (nome) VALUES (?)";
    private static final String FIND_ALL = "SELECT * FROM genero_literario";

    // Criar um novo gênero literário
    public void create(GeneroLiterario genero) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(INSERT)) {
            stmt.setString(1, genero.getNome());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar o gênero literário: " + e.getMessage(), e);
        }
    }

    // Buscar todos os gêneros literários
    public List<GeneroLiterario> findAll() {
        List<GeneroLiterario> generos = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                generos.add(new GeneroLiterario(
                    rs.getInt("id"),
                    rs.getString("nome")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar os gêneros literários: " + e.getMessage(), e);
        }
        return generos;
    }
}
