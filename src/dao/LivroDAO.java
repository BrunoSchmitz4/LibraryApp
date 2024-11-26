package dao;

import model.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    private static final String INSERT = "INSERT INTO livro (titulo, autor, genero_literario_id, classificacao, imagem) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE livro SET titulo = ?, autor = ?, genero_literario_id = ?, classificacao = ?, imagem = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM livro WHERE id = ?";
    private static final String FIND_BY_ID = "SELECT * FROM livro WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM livro";

    // Criar um novo livro
    public void create(Livro livro) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(INSERT)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setInt(3, livro.getGeneroLiterarioId());
            stmt.setInt(4, livro.getClassificacao());
            stmt.setString(5, livro.getImagem());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar o livro: " + e.getMessage(), e);
        }
    }

    // Atualizar um livro
    public void update(Livro livro) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(UPDATE)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setInt(3, livro.getGeneroLiterarioId());
            stmt.setInt(4, livro.getClassificacao());
            stmt.setString(5, livro.getImagem());
            stmt.setInt(6, livro.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar o livro: " + e.getMessage(), e);
        }
    }

    // Deletar um livro
    public void delete(int id) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(DELETE)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar o livro: " + e.getMessage(), e);
        }
    }

    // Buscar livro por ID
    public Livro findById(int id) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Livro(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getInt("genero_literario_id"),
                    rs.getInt("classificacao"),
                    rs.getString("imagem")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar o livro: " + e.getMessage(), e);
        }
        return null;
    }

    // Buscar todos os livros
    public List<Livro> findAll() {
        List<Livro> livros = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                livros.add(new Livro(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getInt("genero_literario_id"),
                    rs.getInt("classificacao"),
                    rs.getString("imagem")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar os livros: " + e.getMessage(), e);
        }
        return livros;
    }
}
