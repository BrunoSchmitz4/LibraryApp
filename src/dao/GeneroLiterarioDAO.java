package dao;

import models.GeneroLiterario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GeneroLiterarioDAO {

    private static final String INSERT = "INSERT INTO genero_literario (nome) VALUES (?)";
    private static final String UPDATE = "UPDATE genero_literario SET nome = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM genero_literario WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM genero_literario";
    private static final String FIND_BY_ID = "SELECT * FROM genero_literario WHERE id = ?";

    // Criar um novo gênero literário
    public void create(GeneroLiterario genero) {
        validateGenero(genero);

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(INSERT)) {
            stmt.setString(1, genero.getNome());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar o gênero literário: " + e.getMessage(), e);
        }
    }

    // Atualizar um gênero literário existente
    public void update(GeneroLiterario genero) {
        validateGenero(genero);

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(UPDATE)) {
            stmt.setString(1, genero.getNome());
            stmt.setInt(2, genero.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar o gênero literário: " + e.getMessage(), e);
        }
    }

    // Excluir um gênero literário pelo ID
    public void delete(int id) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(DELETE)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir o gênero literário: " + e.getMessage(), e);
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

    // Buscar um gênero literário por ID
    public GeneroLiterario findById(int id) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new GeneroLiterario(
                        rs.getInt("id"),
                        rs.getString("nome")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar o gênero literário por ID: " + e.getMessage(), e);
        }
        return null;
    }

    // Validação do gênero literário
    private void validateGenero(GeneroLiterario genero) {
        if (genero.getNome() == null || genero.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do gênero literário não pode estar vazio.");
        }
    }
}
