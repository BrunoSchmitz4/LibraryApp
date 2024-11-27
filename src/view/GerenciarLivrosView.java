package view;

import dao.LivroDAO;
import model.Livro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class GerenciarLivrosView extends JFrame {

    private JTable tabelaLivros;
    private DefaultTableModel tableModel;

    public GerenciarLivrosView() {
        setTitle("Gerenciar Livros");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha apenas esta janela
        setLocationRelativeTo(null); // Centraliza a janela

        // Layout principal
        setLayout(new BorderLayout());

        // Tabela de livros
        tabelaLivros = new JTable();
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Título", "Autor", "Gênero", "Classificação", "Imagem"}, 0
        );
        tabelaLivros.setModel(tableModel);

        // Adicionar a tabela em um painel de rolagem
        JScrollPane scrollPane = new JScrollPane(tabelaLivros);
        add(scrollPane, BorderLayout.CENTER);

        // Painel de botões
        JPanel panelBotoes = new JPanel();
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnAdicionar = new JButton("Adicionar Novo");

        panelBotoes.add(btnAdicionar);
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnExcluir);
        panelBotoes.add(btnAtualizar);

        add(panelBotoes, BorderLayout.SOUTH);

        // Ações dos botões
        btnAdicionar.addActionListener(e -> abrirTelaCadastro());
        btnEditar.addActionListener(e -> editarLivro());
        btnExcluir.addActionListener(e -> excluirLivro());
        btnAtualizar.addActionListener(e -> carregarLivros());

        // Carregar os livros ao abrir a janela
        carregarLivros();
    }

    // Método para carregar os livros na tabela
    private void carregarLivros() {
        tableModel.setRowCount(0); // Limpa a tabela
        LivroDAO livroDAO = new LivroDAO();
        List<Livro> livros = livroDAO.findAll();

        for (Livro livro : livros) {
            tableModel.addRow(new Object[]{
                livro.getId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getGeneroLiterarioId(),
                livro.getClassificacao(),
                livro.getImagem()
            });
        }
    }

    // Método para abrir a tela de cadastro
    private void abrirTelaCadastro() {
        SwingUtilities.invokeLater(() -> {
            CadastroLivroView cadastroLivroView = new CadastroLivroView();
            cadastroLivroView.setVisible(true);
        });
    }

    // Método para editar o livro selecionado
    private void editarLivro() {
    int selectedRow = tabelaLivros.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Selecione um livro para editar.", "Erro", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Pega os dados da linha selecionada
    int id = (int) tableModel.getValueAt(selectedRow, 0);
    String titulo = (String) tableModel.getValueAt(selectedRow, 1);
    String autor = (String) tableModel.getValueAt(selectedRow, 2);
    int generoId = (int) tableModel.getValueAt(selectedRow, 3);
    int classificacao = (int) tableModel.getValueAt(selectedRow, 4);
    String imagem = (String) tableModel.getValueAt(selectedRow, 5);

    // Abre uma nova janela para edição
    SwingUtilities.invokeLater(() -> {
        CadastroLivroView telaEdicao = new CadastroLivroView();
        telaEdicao.setDadosLivro(id, titulo, autor, generoId, classificacao, imagem);
        telaEdicao.setVisible(true);
    });
}


    // Método para excluir o livro selecionado
    private void excluirLivro() {
        int selectedRow = tabelaLivros.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um livro para excluir.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir o livro?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            LivroDAO livroDAO = new LivroDAO();
            livroDAO.delete(id);
            carregarLivros(); // Atualiza a tabela
            JOptionPane.showMessageDialog(this, "Livro excluído com sucesso!");
        }
    }

    // Método principal para testes isolados
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GerenciarLivrosView().setVisible(true);
        });
    }
}
