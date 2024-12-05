package view;

import dao.ConnectionFactory;
import dao.GeneroLiterarioDAO;
import dao.LivroDAO;
import dao.TestConnection;
import models.Livro;
import models.GeneroLiterario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.fail;

public class CadastroLivroView extends JDialog {

    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JComboBox<GeneroLiterario> comboGenero;
    private JTextField txtClassificacao;
    private JTextField txtImagemLink;
    private JCheckBox chkFavorito;
    private JSpinner spinnerDataLeitura;
    private JButton btnSalvar;

    private Livro livroEditando;
    private GerenciarLivrosView janelaPrincipal;

    public CadastroLivroView(GerenciarLivrosView parent, Livro livroParaEditar) {
        super(parent, "Cadastro de Livro", true);
        this.janelaPrincipal = parent;
        this.livroEditando = livroParaEditar;

        setSize(500, 500);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());

        inicializarComponentes();
        if (livroParaEditar != null) {
            carregarDadosParaEdicao(livroParaEditar);
        }
    }

    private void inicializarComponentes() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Título:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(lblTitulo, gbc);

        txtTitulo = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(txtTitulo, gbc);

        JLabel lblAutor = new JLabel("Autor:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(lblAutor, gbc);

        txtAutor = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(txtAutor, gbc);

        JLabel lblGenero = new JLabel("Gênero:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(lblGenero, gbc);

        comboGenero = new JComboBox<>();
        popularComboGenero();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(comboGenero, gbc);

        JLabel lblClassificacao = new JLabel("Classificação:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        add(lblClassificacao, gbc);

        txtClassificacao = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(txtClassificacao, gbc);

        JLabel lblImagemLink = new JLabel("Link da Imagem:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        add(lblImagemLink, gbc);

        txtImagemLink = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(txtImagemLink, gbc);

        JLabel lblDataLeitura = new JLabel("Data de Leitura:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        add(lblDataLeitura, gbc);

        SpinnerDateModel dateModel = new SpinnerDateModel();
        spinnerDataLeitura = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDataLeitura, "dd/MM/yyyy");
        spinnerDataLeitura.setEditor(dateEditor);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(spinnerDataLeitura, gbc);

        // Favorito
        JLabel lblFavorito = new JLabel("Favorito:");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        add(lblFavorito, gbc);

        chkFavorito = new JCheckBox();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        add(chkFavorito, gbc);

        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(this::salvarLivro);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        add(btnSalvar, gbc);
    }

    private void salvarLivro(ActionEvent e) {
        try {
            Date dataLeitura = (Date) spinnerDataLeitura.getValue();
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            formato.setLenient(false);

            String dataStr = formato.format(dataLeitura);
            try {
                formato.parse(dataStr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Data de leitura está em um formato inválido. Use o formato dd/MM/yyyy.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Livro livro = livroEditando != null ? livroEditando : new Livro();
            livro.setTitulo(txtTitulo.getText());
            livro.setAutor(txtAutor.getText());
            livro.setGeneroLiterario(((GeneroLiterario) comboGenero.getSelectedItem()).getId());
            livro.setClassificacao(Integer.parseInt(txtClassificacao.getText()));
            livro.setFavorito(chkFavorito.isSelected());
            livro.setImagem(txtImagemLink.getText());
            livro.setDataLeitura(dataLeitura);

            Connection connection = ConnectionFactory.getConnection();
            LivroDAO livroDAO = new LivroDAO(connection);
            String resultado = livroDAO.adicionarLivro(livro);

            if (resultado.equals("Livro adicionado com sucesso!")) {
                JOptionPane.showMessageDialog(this, "Livro salvo com sucesso!");
                if (janelaPrincipal != null) {
                    janelaPrincipal.carregarLivros();
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, resultado, "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar livro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void popularComboGenero() {
        try {
            GeneroLiterarioDAO generoDAO = new GeneroLiterarioDAO(TestConnection.getConnection());
            List<GeneroLiterario> generos = generoDAO.findAll();
            for (GeneroLiterario genero : generos) {
                comboGenero.addItem(genero);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Erro ao inicializar a conexão com o banco de dados de teste.");
        }
        
    }

    private void carregarDadosParaEdicao(Livro livro) {
        txtTitulo.setText(livro.getTitulo());
        txtAutor.setText(livro.getAutor());
        comboGenero.setSelectedItem(livro.getGeneroLiterario());
        txtClassificacao.setText(String.valueOf(livro.getClassificacao()));
        txtImagemLink.setText(livro.getImagem());
        chkFavorito.setSelected(livro.isFavorito());

        if (livro.getDataLeitura() != null) {
            spinnerDataLeitura.setValue(livro.getDataLeitura());
        }
    }
}
