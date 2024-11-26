package dao;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        System.out.println("Iniciando teste de conexão..."); // Debug extra
        try (Connection connection = ConnectionFactory.getConnection()) {
            System.out.println("Conexão estabelecida com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao conectar: " + e.getMessage());
        }
    }
}
