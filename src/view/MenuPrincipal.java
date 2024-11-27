package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.GerenciarLivrosView;

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
        JButton btnLogin = new JButton("Login de Usuários");
        JButton btnFavoritos = new JButton("Gerenciar Favoritos");
        JButton btnSair = new JButton("Sair");

        // Adicionando os botões ao painel
        panel.add(btnGerenciarLivros);
        panel.add(btnGerenciarGeneros);
        panel.add(btnListarLivrosPorGenero);
        panel.add(btnLogin);
        panel.add(btnFavoritos);
        panel.add(btnSair);

        // Adicionando o painel à janela
        add(panel);

        // Ações dos botões
        btnGerenciarLivros.addActionListener(e -> abrirTelaGerenciarLivros());
        btnGerenciarGeneros.addActionListener(e -> abrirTelaGerenciarGeneros());
        btnListarLivrosPorGenero.addActionListener(e -> abrirTelaListarLivrosPorGenero());
        btnLogin.addActionListener(e -> abrirTelaLogin());
        btnFavoritos.addActionListener(e -> abrirTelaGerenciarFavoritos());
        btnSair.addActionListener(e -> System.exit(0));
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

    private void abrirTelaLogin() {
        JOptionPane.showMessageDialog(this, "Abrir Tela Login (a implementar)");
    }

    private void abrirTelaGerenciarFavoritos() {
        JOptionPane.showMessageDialog(this, "Abrir Tela Gerenciar Favoritos (a implementar)");
    }

    // Método principal para rodar a aplicação
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MenuPrincipal().setVisible(true);
        });
    }
}
