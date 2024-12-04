package dao;

import models.Livro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LivroDAO {
    private Connection connection;

    public LivroDAO(Connection connection) {
        this.connection = connection;
    }

    public String adicionarLivro(Livro livro) {
        if (livro.getTitulo() == null || livro.getTitulo().isEmpty()) {
            return "O título não pode ser vazio.";
        }
        if (tituloExiste(livro.getTitulo())) {
            return "Já existe um livro com o mesmo título.";
        }
        if (livro.getAutor() == null || livro.getAutor().isEmpty()) {
            return "O autor não pode ser vazio.";
        }
        if (livro.getGeneroLiterario() <= 0 || !generoExiste(livro.getGeneroLiterario())) {
            return "Escolha um gênero literário existente ou disponível.";
        }
        if (livro.getClassificacao() < 1 || livro.getClassificacao() > 5) {
            return "A classificação precisa ser uma quantidade válida.";
        }
        if (livro.getImagem() == null || !livro.getImagem().matches("^https?://.*\\.(jpeg|png|jpg|svg)$")) {
            return "O link da imagem deve começar com http ou https e terminar com .jpeg, .png, .jpg ou .svg.";
        }
        if (livro.getDataLeitura() == null) {
            return "A data de leitura não pode ser nula.";
        }
        if (livro.getDataLeitura().after(new Date())) {
            return "A data de leitura não pode ser maior que o dia atual.";
        }
        if (livro.getTitulo().length() > 200) {
            return "O título deve ter no máximo 200 caracteres.";
        }
        if (livro.getAutor().length() > 150) {
            return "A autoria deve ter no máximo 150 caracteres.";
        }
        if (livro.getAutor() == null || livro.getAutor().isEmpty()) {
            return "O autor não pode ser vazio.";
        }

        String sql = "INSERT INTO livro (titulo, autor, genero_literario_id, classificacao, imagem, favorito, data_leitura) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setInt(3, livro.getGeneroLiterario());
            stmt.setInt(4, livro.getClassificacao());
            stmt.setString(5, livro.getImagem());
            stmt.setBoolean(6, livro.isFavorito());
            stmt.setDate(7, new java.sql.Date(livro.getDataLeitura().getTime()));

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Livro adicionado com sucesso!";
            } else {
                return "Erro ao adicionar o livro.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao adicionar o livro: " + e.getMessage();
        }
    }

    private boolean tituloExiste(String titulo) {
        String sql = "SELECT COUNT(*) FROM livro WHERE titulo = ? order by id desc";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, titulo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean generoExiste(int generoId) {
        String sql = "SELECT COUNT(*) FROM genero_literario WHERE id = ? order by id desc";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, generoId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
    
    public List<Livro> findAll() {
        List<Livro> livros = new ArrayList<>();
        String sql = "select * from livro order by id desc";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                livros.add(mapearResultado(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livros;
    }

    public Livro findById(int id) {
        String sql = "SELECT * FROM livro WHERE id = ? order by id desc";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearResultado(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(int id) {
        String sql = "DELETE FROM livro WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Livro> findFavoritos() {
        List<Livro> favoritos = new ArrayList<>();
        String sql = "SELECT * FROM livro WHERE favorito = true order by id desc";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                favoritos.add(mapearResultado(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favoritos;
    }
    
    public int countByGenero(int idGenero) {
        String sql = "SELECT COUNT(*) FROM livro WHERE genero_literario_id = ? order by id desc";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idGenero);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    
    public List<Livro> findByGenero(int generoId) {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livro WHERE genero_literario_id = ? order by id desc";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, generoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                livros.add(mapearResultado(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livros;
    }


    public void favoritar(int id, boolean favorito) {
        String sql = "UPDATE livro SET favorito = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, favorito);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Livro mapearResultado(ResultSet rs) throws SQLException {
        Livro livro = new Livro();
        livro.setId(rs.getInt("id"));
        livro.setTitulo(rs.getString("titulo"));
        livro.setAutor(rs.getString("autor"));
        livro.setGeneroLiterario(rs.getInt("genero_literario_id"));
        livro.setClassificacao(rs.getInt("classificacao"));
        livro.setImagem(rs.getString("imagem"));
        livro.setFavorito(rs.getBoolean("favorito"));
        livro.setDataLeitura(rs.getDate("data_leitura"));
        return livro;
    }

}