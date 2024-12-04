package view;

import dao.GeneroLiterarioDAO;
import dao.LivroDAO;
import models.GeneroLiterario;
import models.Livro;
import dao.ConnectionFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.util.List;

public class ListarLivrosPorGeneroView extends JFrame {

    private JComboBox<GeneroLiterario> comboGeneros;
    private JTable tabelaLivros;
    private DefaultTableModel tableModel;

    public ListarLivrosPorGeneroView() {
        setTitle("Listar Livros por Gênero");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panelFiltro = new JPanel();
        panelFiltro.setLayout(new FlowLayout());

        JLabel lblGenero = new JLabel("Selecione o Gênero:");
        panelFiltro.add(lblGenero);

        comboGeneros = new JComboBox<>();
        carregarGeneros();
        comboGeneros.addActionListener(e -> carregarLivrosPorGenero());
        panelFiltro.add(comboGeneros);

        add(panelFiltro, BorderLayout.NORTH);

        configurarTabela();
        JScrollPane scrollPane = new JScrollPane(tabelaLivros);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void carregarGeneros() {
        GeneroLiterarioDAO generoDAO = new GeneroLiterarioDAO();
        LivroDAO livroDAO = new LivroDAO(ConnectionFactory.getConnection());
        List<GeneroLiterario> generos = generoDAO.findAll();

        comboGeneros.removeAllItems(); // Limpar a lista de gêneros
        for (GeneroLiterario genero : generos) {
            if (livroDAO.countByGenero(genero.getId()) > 0) { // Verifica se há livros no gênero
                comboGeneros.addItem(genero);
            }
        }
    }



    private void configurarTabela() {
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Imagem", "Título", "Autor", "Gênero", "Classificação"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaLivros = new JTable(tableModel);
        tabelaLivros.setRowHeight(100);
        tabelaLivros.getColumnModel().getColumn(1).setCellRenderer(new ImageRenderer());
        tabelaLivros.getColumnModel().getColumn(1).setPreferredWidth(100); // Tamanho da coluna de imagem
    }

    private void carregarLivrosPorGenero() {
        GeneroLiterario generoSelecionado = (GeneroLiterario) comboGeneros.getSelectedItem();
        if (generoSelecionado != null) {
            try (Connection connection = ConnectionFactory.getConnection()) {
                LivroDAO livroDAO = new LivroDAO(connection);
                List<Livro> livros = livroDAO.findByGenero(generoSelecionado.getId());
                atualizarTabela(livros);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void atualizarTabela(List<Livro> livros) {
        tableModel.setRowCount(0);

        for (Livro livro : livros) {
            ImageIcon imagemIcon = carregarImagemIcon(livro.getImagem());
            tableModel.addRow(new Object[] {
                livro.getId(),
                imagemIcon,
                livro.getTitulo(),
                livro.getAutor(),
                livro.getGeneroLiterario(),
                livro.getClassificacao()
            });
        }
    }

    private ImageIcon carregarImagemIcon(String urlImagem) {
        if (urlImagem != null && !urlImagem.isEmpty()) {
            try {
                // Verifica se é um link HTTP/HTTPS
                if (urlImagem.startsWith("http://") || urlImagem.startsWith("https://")) {
                    URL url = new URL(urlImagem);
                    ImageIcon icon = new ImageIcon(url);
                    Image imagemRedimensionada = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    return new ImageIcon(imagemRedimensionada);
                } else {
                    // Carregar de arquivo local (se necessário)
                    ImageIcon icon = new ImageIcon(urlImagem);
                    Image imagemRedimensionada = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    return new ImageIcon(imagemRedimensionada);
                }
            } catch (Exception e) {
                System.err.println("Erro ao carregar imagem: " + e.getMessage());
            }
        }
        return null;
    }

    private static class ImageRenderer extends JLabel implements TableCellRenderer {
        public ImageRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof ImageIcon) {
                setIcon((ImageIcon) value);
            } else {
                setIcon(null);
            }
            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ListarLivrosPorGeneroView().setVisible(true));
    }
}
