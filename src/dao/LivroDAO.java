package dao;

import models.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    // Recupera todos os livros do banco
    public List<Livro> findAll() {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT id, titulo, autor, genero_literario_id, classificacao, imagem, favorito FROM livro";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Livro livro = new Livro();
                livro.setId(rs.getInt("id"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutor(rs.getString("autor"));
                livro.setGeneroLiterario(rs.getInt("genero_literario_id"));
                livro.setClassificacao(rs.getInt("classificacao"));
                livro.setImagem(rs.getString("imagem"));
                livro.setFavorito(rs.getBoolean("favorito")); // Atribui o status de favorito

                livros.add(livro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return livros;
    }

    // Recupera apenas os livros favoritos
    public List<Livro> findFavoritos() {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT id, titulo, autor, genero_literario_id, classificacao, imagem, favorito FROM livro WHERE favorito = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Livro livro = new Livro();
                livro.setId(rs.getInt("id"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutor(rs.getString("autor"));
                livro.setGeneroLiterario(rs.getInt("genero_literario_id"));
                livro.setClassificacao(rs.getInt("classificacao"));
                livro.setImagem(rs.getString("imagem"));
                livro.setFavorito(rs.getBoolean("favorito"));

                livros.add(livro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return livros;
    }

    // Atualiza o status de favorito de um livro
    public void favoritar(int id, boolean favorito) {
        String sql = "UPDATE livro SET favorito = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, favorito);
            stmt.setInt(2, id);

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Exclui um livro pelo ID
    public boolean delete(int id) {
        String sql = "DELETE FROM livro WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Insere um novo livro
    public void insert(Livro livro) {
        String sql = "INSERT INTO livro (titulo, autor, genero_literario_id, classificacao, imagem, favorito) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setInt(3, livro.getGeneroLiterario());
            stmt.setInt(4, livro.getClassificacao());
            stmt.setString(5, livro.getImagem());
            stmt.setBoolean(6, livro.isFavorito());

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<Livro> findByGenero(int generoId) {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livro WHERE genero_literario_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, generoId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Livro livro = new Livro();
                    livro.setId(rs.getInt("id"));
                    livro.setTitulo(rs.getString("titulo"));
                    livro.setAutor(rs.getString("autor"));
                    livro.setGeneroLiterario(rs.getInt("genero_literario_id"));
                    livro.setClassificacao(rs.getInt("classificacao"));
                    livro.setImagem(rs.getString("imagem"));
                    livro.setFavorito(rs.getBoolean("favorito"));
                    livros.add(livro);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return livros;
    }


    // Atualiza um livro existente
    public void update(Livro livro) {
        String sql = "UPDATE livro SET titulo = ?, autor = ?, genero_literario_id = ?, classificacao = ?, imagem = ?, favorito = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setInt(3, livro.getGeneroLiterario());
            stmt.setInt(4, livro.getClassificacao());
            stmt.setString(5, livro.getImagem());
            stmt.setBoolean(6, livro.isFavorito());
            stmt.setInt(7, livro.getId());

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
