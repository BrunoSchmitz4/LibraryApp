package view;

import dao.LivroDAO;
import models.Livro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GerenciarLivrosView extends JFrame {

    private JTable tabelaLivros;
    private DefaultTableModel tableModel;

    public GerenciarLivrosView() {
        setTitle("Gerenciar Livros");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        inicializarComponentes();
        carregarLivros();
    }

    // Inicializa os componentes da interface
    private void inicializarComponentes() {
        // Painel de botões
        JPanel panelBotoes = new JPanel();
        panelBotoes.setLayout(new FlowLayout());

        // Botão para adicionar um livro
        JButton btnAdicionar = new JButton("Adicionar Livro");
        btnAdicionar.addActionListener(e -> abrirTelaCadastro());
        panelBotoes.add(btnAdicionar);

        // Botão para editar um livro
        JButton btnEditar = new JButton("Editar Livro");
        btnEditar.addActionListener(e -> editarLivroSelecionado());
        panelBotoes.add(btnEditar);

        // Botão para excluir um livro
        JButton btnExcluir = new JButton("Excluir Livro");
        btnExcluir.addActionListener(e -> excluirLivroSelecionado());
        panelBotoes.add(btnExcluir);

        // Botão para filtrar favoritos
        JButton btnFavoritos = new JButton("Exibir Favoritos");
        btnFavoritos.addActionListener(e -> exibirFavoritos());
        panelBotoes.add(btnFavoritos);

        // Adiciona o painel de botões à parte inferior da janela
        add(panelBotoes, BorderLayout.SOUTH);

        // Configura a tabela de livros
        configurarTabela();
        JScrollPane scrollPane = new JScrollPane(tabelaLivros);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Configura a tabela para exibir os livros
    private void configurarTabela() {
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Título", "Autor", "Gênero", "Classificação", "Favorito"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Permite edição apenas na coluna "Favorito"
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5) {
                    return Boolean.class; // A coluna "Favorito" será exibida como checkbox
                }
                return String.class;
            }
        };

        tabelaLivros = new JTable(tableModel);

        // Listener para atualização do favorito
        tabelaLivros.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();

            if (column == 5) { // Coluna "Favorito"
                int livroId = (int) tableModel.getValueAt(row, 0);
                boolean favorito = (boolean) tableModel.getValueAt(row, 5);

                LivroDAO livroDAO = new LivroDAO();
                livroDAO.favoritar(livroId, favorito);
            }
        });
    }

    // Carrega todos os livros no banco de dados
    private void carregarLivros() {
        LivroDAO livroDAO = new LivroDAO();
        List<Livro> livros = livroDAO.findAll();
        atualizarTabela(livros);
    }

    // Atualiza os dados na tabela
    private void atualizarTabela(List<Livro> livros) {
        tableModel.setRowCount(0); // Limpa a tabela

        for (Livro livro : livros) {
            tableModel.addRow(new Object[]{
                livro.getId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getGeneroLiterario(),
                livro.getClassificacao(),
                livro.isFavorito() // Status de favorito
            });
        }
    }

    // Abre a tela de cadastro de livros
    private void abrirTelaCadastro() {
        // Implemente a abertura da tela de cadastro
        JOptionPane.showMessageDialog(this, "Funcionalidade de cadastro ainda não implementada.");
    }

    // Edita o livro selecionado
    private void editarLivroSelecionado() {
        int selectedRow = tabelaLivros.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um livro para editar.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int livroId = (int) tableModel.getValueAt(selectedRow, 0);
        JOptionPane.showMessageDialog(this, "Funcionalidade de edição ainda não implementada para o livro ID: " + livroId);
    }

    // Exclui o livro selecionado
    private void excluirLivroSelecionado() {
        int selectedRow = tabelaLivros.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um livro para excluir.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int livroId = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este livro?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            LivroDAO livroDAO = new LivroDAO();
            boolean sucesso = livroDAO.delete(livroId);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Livro excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarLivros(); // Recarrega os livros após a exclusão
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir o livro. Tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    // Exibe apenas os livros favoritos
    private void exibirFavoritos() {
        LivroDAO livroDAO = new LivroDAO();
        List<Livro> favoritos = livroDAO.findFavoritos();
        atualizarTabela(favoritos);
    }

    // Método principal para testar a tela
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GerenciarLivrosView().setVisible(true));
    }
}
