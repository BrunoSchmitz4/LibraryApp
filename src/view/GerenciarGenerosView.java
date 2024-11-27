package view;

import dao.GeneroLiterarioDAO;
import models.GeneroLiterario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class GerenciarGenerosView extends JFrame {

    private JTable tabelaGeneros;
    private DefaultTableModel tableModel;

    public GerenciarGenerosView() {
        setTitle("Gerenciar Gêneros Literários");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha apenas esta janela
        setLocationRelativeTo(null); // Centraliza a janela

        // Layout principal
        setLayout(new BorderLayout());

        // Tabela de gêneros
        tabelaGeneros = new JTable();
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Nome"}, 0
        );
        tabelaGeneros.setModel(tableModel);

        // Adicionar a tabela em um painel de rolagem
        JScrollPane scrollPane = new JScrollPane(tabelaGeneros);
        add(scrollPane, BorderLayout.CENTER);

        // Painel de botões
        JPanel panelBotoes = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar Novo");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnAtualizar = new JButton("Atualizar");

        panelBotoes.add(btnAdicionar);
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnExcluir);
        panelBotoes.add(btnAtualizar);

        add(panelBotoes, BorderLayout.SOUTH);

        // Ações dos botões
        btnAdicionar.addActionListener(e -> abrirTelaCadastro());
        btnEditar.addActionListener(e -> editarGenero());
        btnExcluir.addActionListener(e -> excluirGenero());
        btnAtualizar.addActionListener(e -> carregarGeneros());

        // Carregar os gêneros ao abrir a janela
        carregarGeneros();
    }

    // Método para carregar os gêneros na tabela
    private void carregarGeneros() {
        tableModel.setRowCount(0); // Limpa a tabela
        GeneroLiterarioDAO generoDAO = new GeneroLiterarioDAO();
        List<GeneroLiterario> generos = generoDAO.findAll();

        for (GeneroLiterario genero : generos) {
            tableModel.addRow(new Object[]{
                genero.getId(),
                genero.getNome()
            });
        }
    }

    // Método para abrir a tela de cadastro
    private void abrirTelaCadastro() {
        String nome = JOptionPane.showInputDialog(this, "Digite o nome do gênero literário:");
        if (nome != null && !nome.trim().isEmpty()) {
            GeneroLiterarioDAO generoDAO = new GeneroLiterarioDAO();
            GeneroLiterario genero = new GeneroLiterario(0, nome);
            generoDAO.create(genero);
            carregarGeneros();
            JOptionPane.showMessageDialog(this, "Gênero cadastrado com sucesso!");
        }
    }

    // Método para editar o gênero selecionado
    private void editarGenero() {
        int selectedRow = tabelaGeneros.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um gênero para editar.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String nomeAtual = (String) tableModel.getValueAt(selectedRow, 1);

        String novoNome = JOptionPane.showInputDialog(this, "Editar nome do gênero:", nomeAtual);
        if (novoNome != null && !novoNome.trim().isEmpty()) {
            GeneroLiterarioDAO generoDAO = new GeneroLiterarioDAO();
            GeneroLiterario genero = new GeneroLiterario(id, novoNome);
            generoDAO.update(genero);
            carregarGeneros();
            JOptionPane.showMessageDialog(this, "Gênero atualizado com sucesso!");
        }
    }

    // Método para excluir o gênero selecionado
    private void excluirGenero() {
        int selectedRow = tabelaGeneros.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um gênero para excluir.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir o gênero?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            GeneroLiterarioDAO generoDAO = new GeneroLiterarioDAO();
            generoDAO.delete(id);
            carregarGeneros();
            JOptionPane.showMessageDialog(this, "Gênero excluído com sucesso!");
        }
    }

    // Método principal para testes isolados
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GerenciarGenerosView().setVisible(true);
        });
    }
}
