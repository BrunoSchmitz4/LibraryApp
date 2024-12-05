package view;

import dao.LivroDAO;
import models.Livro;
import models.GeneroLiterario;
import dao.ConnectionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;

public class GerenciarLivrosView extends JFrame {

    public JTable tabelaLivros;
    private DefaultTableModel tableModel;
    private boolean exibirFavoritos = false;

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
        btnAdicionar.addActionListener(e -> abrirTelaCadastro(null));
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
            new Object[]{"ID", "Imagem", "Título", "Autor", "Gênero", "Classificação", "Favorito"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 6 ? Boolean.class : (columnIndex == 1 ? ImageIcon.class : String.class);
            }
        };

        tabelaLivros = new JTable(tableModel);
        tabelaLivros.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();

            if (column == 5) { // Atualizar status de favorito
                int livroId = (int) tableModel.getValueAt(row, 0);
                boolean favorito = (boolean) tableModel.getValueAt(row, 5);

                try (Connection connection = ConnectionFactory.getConnection()) {
                    LivroDAO livroDAO = new LivroDAO(connection);
                    livroDAO.favoritar(livroId, favorito);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar favorito: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        tabelaLivros.getColumnModel().getColumn(1).setCellRenderer(new ImageRenderer());
    }

    public void carregarLivros() {
        try (Connection connection = ConnectionFactory.getConnection()) {
            LivroDAO livroDAO = new LivroDAO(connection);
            List<Livro> livros = livroDAO.findAll();
            atualizarTabela(livros);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar livros: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarTabela(List<Livro> livros) {
        tableModel.setRowCount(0);

        for (Livro livro : livros) {
            ImageIcon imagemIcon = livro.getImagem() != null ? new ImageIcon(livro.getImagem()) : null;
            tableModel.addRow(new Object[]{
                livro.getId(),
                imagemIcon,
                livro.getTitulo(),
                livro.getAutor(),
                livro.getGeneroLiterario(),
                livro.getClassificacao(),
                livro.isFavorito()
            });
        }
    }

    private static class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel lbl = new JLabel();
            lbl.setHorizontalAlignment(SwingConstants.CENTER);

            if (value != null) {
                try {
                    URL url = new URL(value.toString());
                    ImageIcon icon = new ImageIcon(url);
                    // Redimensiona a imagem para caber na célula
                    Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    lbl.setIcon(new ImageIcon(scaledImage));
                } catch (Exception e) {
                    lbl.setText("Erro na imagem");
                }
            } else {
                lbl.setText("Sem imagem");
            }

            return lbl;
        }
    }

    private void abrirTelaCadastro(Livro livroParaEditar) {
        CadastroLivroView telaCadastro = new CadastroLivroView(this, livroParaEditar);
        telaCadastro.setVisible(true);
        carregarLivros();
    }

    private void editarLivroSelecionado() {
        int selectedRow = tabelaLivros.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um livro para editar.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int livroId = (int) tableModel.getValueAt(selectedRow, 0);

        try (Connection connection = ConnectionFactory.getConnection()) {
            LivroDAO livroDAO = new LivroDAO(connection);
            Livro livro = livroDAO.findById(livroId);

            if (livro != null) {
                abrirTelaCadastro(livro);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao carregar dados do livro.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar livro para edição: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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
            try (Connection connection = ConnectionFactory.getConnection()) {
                LivroDAO livroDAO = new LivroDAO(connection);
                livroDAO.delete(livroId);
                carregarLivros();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir livro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void alternarFavoritos(JButton btnFavoritos) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            LivroDAO livroDAO = new LivroDAO(connection);

            if (!exibirFavoritos) {
                List<Livro> favoritos = livroDAO.findFavoritos();
                atualizarTabela(favoritos);
                btnFavoritos.setText("Exibir Todos");
            } else {
                carregarLivros();
                btnFavoritos.setText("Exibir Favoritos");
            }

            exibirFavoritos = !exibirFavoritos;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao alternar exibição de favoritos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GerenciarLivrosView().setVisible(true));
    }
    
    public JTable getTabelaLivros() {
        return tabelaLivros;
    }

}
