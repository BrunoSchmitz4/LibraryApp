package view;

import dao.LivroDAO;
import models.Livro;

import javax.swing.*;
import java.awt.*;

public class CadastroLivroView extends JFrame {

    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtGeneroLiterario;
    private JTextField txtClassificacao;
    private JTextField txtImagem;
    private JCheckBox chkFavorito;

    public CadastroLivroView() {
        setTitle("Cadastrar Livro");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 2, 10, 10)); // Layout com 7 linhas e 2 colunas

        inicializarComponentes();
    }

    // Inicializa os componentes da interface
    private void inicializarComponentes() {
        JLabel lblTitulo = new JLabel("Título:");
        txtTitulo = new JTextField();
        add(lblTitulo);
        add(txtTitulo);

        JLabel lblAutor = new JLabel("Autor:");
        txtAutor = new JTextField();
        add(lblAutor);
        add(txtAutor);

        JLabel lblGeneroLiterario = new JLabel("Gênero Literário (ID):");
        txtGeneroLiterario = new JTextField();
        add(lblGeneroLiterario);
        add(txtGeneroLiterario);

        JLabel lblClassificacao = new JLabel("Classificação:");
        txtClassificacao = new JTextField();
        add(lblClassificacao);
        add(txtClassificacao);

        JLabel lblImagem = new JLabel("URL da Imagem:");
        txtImagem = new JTextField();
        add(lblImagem);
        add(txtImagem);

        JLabel lblFavorito = new JLabel("Favorito:");
        chkFavorito = new JCheckBox();
        add(lblFavorito);
        add(chkFavorito);

        // Botão para salvar o livro
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvarLivro());
        add(btnSalvar);

        // Botão para cancelar o cadastro
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose()); // Fecha a janela
        add(btnCancelar);
    }

    // Salva o livro no banco de dados
    private void salvarLivro() {
        try {
            String titulo = txtTitulo.getText();
            String autor = txtAutor.getText();
            int generoLiterario = Integer.parseInt(txtGeneroLiterario.getText());
            int classificacao = Integer.parseInt(txtClassificacao.getText());
            String imagem = txtImagem.getText();
            boolean favorito = chkFavorito.isSelected();

            // Validações simples
            if (titulo.isEmpty() || autor.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios!", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Cria o objeto Livro
            Livro livro = new Livro();
            livro.setTitulo(titulo);
            livro.setAutor(autor);
            livro.setGeneroLiterario(generoLiterario);
            livro.setClassificacao(classificacao);
            livro.setImagem(imagem);
            livro.setFavorito(favorito);

            // Salva o livro no banco de dados
            LivroDAO livroDAO = new LivroDAO();
            livroDAO.insert(livro);

            JOptionPane.showMessageDialog(this, "Livro cadastrado com sucesso!");
            dispose(); // Fecha a janela após o cadastro
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Classificação e Gênero Literário devem ser números válidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocorreu um erro ao cadastrar o livro.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método principal para teste
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CadastroLivroView().setVisible(true));
    }
}
