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
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        JButton btnGerenciarLivros = new JButton("Gerenciar Livros");
        JButton btnGerenciarGeneros = new JButton("Gerenciar Gêneros Literários");
        JButton btnListarLivrosPorGenero = new JButton("Listar Livros por Gênero");
        JButton btnSair = new JButton("Sair");

        panel.add(btnGerenciarLivros);
        panel.add(btnGerenciarGeneros);
        panel.add(btnListarLivrosPorGenero);
        panel.add(btnSair);
        add(panel);

        btnGerenciarLivros.addActionListener(e -> abrirTelaGerenciarLivros());
        btnGerenciarGeneros.addActionListener(e -> abrirTelaGerenciarGeneros());
        btnListarLivrosPorGenero.addActionListener(e -> abrirTelaListarLivrosPorGenero());

        btnSair.addActionListener(e -> System.exit(0));
        
        btnGerenciarLivros.setIcon(new ImageIcon("icons/books.png"));
        btnGerenciarGeneros.setIcon(new ImageIcon("icons/genre.png"));
        btnSair.setIcon(new ImageIcon("icons/exit.png"));
    }

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
