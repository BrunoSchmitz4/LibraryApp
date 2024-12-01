package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.GerenciarLivrosView;
import com.formdev.flatlaf.FlatLightLaf;


public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("Menu Principal");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Bem-vindo ao sistema!", SwingConstants.CENTER);
        add(label);
        setTitle("Sistema de Gerenciamento de Biblioteca");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela

        // Criando o painel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10)); // 3 linhas, 2 colunas

        // Botões do menu
        JButton btnGerenciarLivros = new JButton("Gerenciar Livros");
        JButton btnGerenciarGeneros = new JButton("Gerenciar Gêneros Literários");
        JButton btnListarLivrosPorGenero = new JButton("Listar Livros por Gênero");
//        JButton btnLogin = new JButton("Login de Usuários");
//        JButton btnFavoritos = new JButton("Gerenciar Favoritos");
        JButton btnSair = new JButton("Sair");
        
        // Adicionando os botões ao painel
        panel.add(btnGerenciarLivros);
        panel.add(btnGerenciarGeneros);
        panel.add(btnListarLivrosPorGenero);
//        panel.add(btnLogin);
//        panel.add(btnFavoritos);
        panel.add(btnSair);

        // Adicionando o painel à janela
        add(panel);

        // Ações dos botões
        btnGerenciarLivros.addActionListener(e -> abrirTelaGerenciarLivros());
        btnGerenciarGeneros.addActionListener(e -> abrirTelaGerenciarGeneros());
        btnListarLivrosPorGenero.addActionListener(e -> abrirTelaListarLivrosPorGenero());
//        btnLogin.addActionListener(e -> abrirTelaLogin());
//        btnFavoritos.addActionListener(e -> abrirTelaGerenciarFavoritos());
        btnSair.addActionListener(e -> System.exit(0));
        
        btnGerenciarLivros.setIcon(new ImageIcon("icons/books.png"));
        btnGerenciarGeneros.setIcon(new ImageIcon("icons/genre.png"));
        btnSair.setIcon(new ImageIcon("icons/exit.png"));
    }

    // Métodos para abrir outras telas (a implementar)
    private void abrirTelaGerenciarLivros() {
    SwingUtilities.invokeLater(() -> {
        new GerenciarLivrosView().setVisible(true);
    });

}

    private void abrirTelaGerenciarGeneros() {
    SwingUtilities.invokeLater(() -> {
        new GerenciarGenerosView().setVisible(true);
    });
    }
    private void abrirTelaListarLivrosPorGenero() {
        SwingUtilities.invokeLater(() -> new ListarLivrosPorGeneroView().setVisible(true));
    }

//    // Método para abrir a tela de login
//    private void abrirTelaLogin() {
//        // Crie uma instância da tela de login e torne-a visível
//        LoginView loginView = new LoginView();
//        loginView.setVisible(true);
//    }
//
//    // Método para abrir a tela de gerenciar favoritos;
//    private void abrirTelaGerenciarFavoritos() {
//        // Crie uma instância da tela de gerenciar favoritos e torne-a visível
//        GerenciarFavoritosView favoritosView = new GerenciarFavoritosView();
//        favoritosView.setVisible(true);
//    }

    // Método principal para testar a tela
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MenuPrincipal frame = new MenuPrincipal();
            frame.setVisible(true);
        });
    }
}
