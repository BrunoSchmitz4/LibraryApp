package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/organo-app";
        String username = "root";
        String password = "";

        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) {
        System.out.println("Iniciando teste de conexão...");
        try (Connection connection = getConnection()) {
            System.out.println("Conexão estabelecida com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao conectar: " + e.getMessage());
        }
    }
}
