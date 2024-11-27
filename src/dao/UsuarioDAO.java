package dao;

import model.Usuario;

import java.sql.*;

public class UsuarioDAO {

    private static final String INSERT = "INSERT INTO usuario (nome, email, senha) VALUES (?, ?, ?)";
    private static final String FIND_BY_EMAIL = "SELECT * FROM usuario WHERE email = ?";

    // Criar um novo usuário
    public void create(Usuario usuario) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(INSERT)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar o usuário: " + e.getMessage(), e);
        }
    }

    // Buscar usuário por email
    public Usuario findByEmail(String email) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement stmt = connection.prepareStatement(FIND_BY_EMAIL)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Usuario(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar o usuário: " + e.getMessage(), e);
        }
        return null;
    }
    
    public Usuario autenticar(String email, String senha) {
        String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha")
                    );
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao autenticar o usuário: " + e.getMessage(), e);
        }

        return null;
    }
    
}
