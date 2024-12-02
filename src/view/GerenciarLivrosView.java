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
    private boolean exibirFavoritos = false; // Controle para o botão "Exibir Favoritos"

    public GerenciarLivrosView() {
        setTitle("Gerenciar Livros");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        inicializarComponentes();
        carregarLivros();
    }

    private void inicializarComponentes() {
        JPanel panelBotoes = new JPanel();
        panelBotoes.setLayout(new FlowLayout());

        JButton btnAdicionar = new JButton("Adicionar Livro");
        btnAdicionar.addActionListener(e -> abrirTelaCadastro(null)); // Adicionar novo livro
        panelBotoes.add(btnAdicionar);

        JButton btnEditar = new JButton("Editar Livro");
        btnEditar.addActionListener(e -> editarLivroSelecionado());
        panelBotoes.add(btnEditar);

        JButton btnExcluir = new JButton("Excluir Livro");
        btnExcluir.addActionListener(e -> excluirLivroSelecionado());
        panelBotoes.add(btnExcluir);

        JButton btnFavoritos = new JButton("Exibir Favoritos");
        btnFavoritos.addActionListener(e -> alternarFavoritos(btnFavoritos));
        panelBotoes.add(btnFavoritos);

        add(panelBotoes, BorderLayout.SOUTH);

        configurarTabela();
        JScrollPane scrollPane = new JScrollPane(tabelaLivros);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void configurarTabela() {
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Título", "Autor", "Gênero", "Classificação", "Favorito"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Apenas a coluna "Favorito" é editável
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 5 ? Boolean.class : String.class;
            }
        };

        tabelaLivros = new JTable(tableModel);
        tabelaLivros.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();

            if (column == 5) { // Atualizar status de favorito
                int livroId = (int) tableModel.getValueAt(row, 0);
                boolean favorito = (boolean) tableModel.getValueAt(row, 5);

                LivroDAO livroDAO = new LivroDAO();
                livroDAO.favoritar(livroId, favorito);
            }
        });
    }

    public void carregarLivros() {
        LivroDAO livroDAO = new LivroDAO();
        List<Livro> livros = livroDAO.findAll();
        atualizarTabela(livros);
    }

    private void atualizarTabela(List<Livro> livros) {
        tableModel.setRowCount(0);

        for (Livro livro : livros) {
            tableModel.addRow(new Object[]{
                livro.getId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getGeneroLiterario(),
                livro.getClassificacao(),
                livro.isFavorito()
            });
        }
    }

    private void abrirTelaCadastro(Livro livroParaEditar) {
        CadastroLivroView telaCadastro = new CadastroLivroView(this, livroParaEditar);
        telaCadastro.setVisible(true);
        carregarLivros(); // Atualizar a tabela após adicionar/editar um livro
    }

    private void editarLivroSelecionado() {
        int selectedRow = tabelaLivros.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um livro para editar.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int livroId = (int) tableModel.getValueAt(selectedRow, 0);
        LivroDAO livroDAO = new LivroDAO();
        Livro livro = livroDAO.findById(livroId);

        if (livro != null) {
            abrirTelaCadastro(livro);
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados do livro.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

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
            livroDAO.delete(livroId);
            carregarLivros();
        }
    }

    private void alternarFavoritos(JButton btnFavoritos) {
        LivroDAO livroDAO = new LivroDAO();

        if (!exibirFavoritos) {
            List<Livro> favoritos = livroDAO.findFavoritos();
            atualizarTabela(favoritos);
            btnFavoritos.setText("Exibir Todos");
        } else {
            carregarLivros();
            btnFavoritos.setText("Exibir Favoritos");
        }

        exibirFavoritos = !exibirFavoritos;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GerenciarLivrosView().setVisible(true));
    }
}
