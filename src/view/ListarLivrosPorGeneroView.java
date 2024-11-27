package view;

import dao.GeneroLiterarioDAO;
import dao.LivroDAO;
import models.GeneroLiterario;
import models.Livro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
        // Painel para seleção de gênero
        JPanel panelFiltro = new JPanel();
        panelFiltro.setLayout(new FlowLayout());

        JLabel lblGenero = new JLabel("Selecione o Gênero:");
        panelFiltro.add(lblGenero);

        comboGeneros = new JComboBox<>();
        carregarGeneros();
        comboGeneros.addActionListener(e -> carregarLivrosPorGenero());
        panelFiltro.add(comboGeneros);

        add(panelFiltro, BorderLayout.NORTH);

        // Configuração da tabela
        configurarTabela();
        JScrollPane scrollPane = new JScrollPane(tabelaLivros);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void carregarGeneros() {
        GeneroLiterarioDAO generoDAO = new GeneroLiterarioDAO();
        List<GeneroLiterario> generos = generoDAO.findAll();

        // Adiciona os gêneros na JComboBox
        for (GeneroLiterario genero : generos) {
            comboGeneros.addItem(genero);
        }
    }

    private void configurarTabela() {
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Título", "Autor", "Gênero", "Classificação"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede edição das células
            }
        };

        tabelaLivros = new JTable(tableModel);
    }

    private void carregarLivrosPorGenero() {
        GeneroLiterario generoSelecionado = (GeneroLiterario) comboGeneros.getSelectedItem();

        if (generoSelecionado != null) {
            LivroDAO livroDAO = new LivroDAO();
            List<Livro> livros = livroDAO.findByGenero(generoSelecionado.getId());
            atualizarTabela(livros);
        }
    }

    private void atualizarTabela(List<Livro> livros) {
        tableModel.setRowCount(0); // Limpa os dados da tabela

        for (Livro livro : livros) {
            tableModel.addRow(new Object[]{
                livro.getId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getGeneroLiterario(),
                livro.getClassificacao()
            });
        }
    }

    // Método principal para teste
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ListarLivrosPorGeneroView().setVisible(true));
    }
}
