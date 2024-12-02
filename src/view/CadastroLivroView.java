package view;

import dao.GeneroLiterarioDAO;
import dao.LivroDAO;
import models.Livro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.filechooser.FileNameExtensionFilter;
import models.GeneroLiterario;

public class CadastroLivroView extends JDialog {

    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JComboBox<GeneroLiterario> comboGenero;
    private JTextField txtClassificacao;
    private JButton btnSalvar;

    private Livro livroEditando; // Livro em edição (null se for novo)
    private GerenciarLivrosView janelaPrincipal;
    private JCheckBox chkFavorito;

    private JLabel lblImagem;
    private JButton btnSelecionarImagem;
    private String imagemSelecionada;
    
    public CadastroLivroView(GerenciarLivrosView parent, Livro livroParaEditar) {
        super(parent, "Cadastro de Livro", true);
        this.janelaPrincipal = parent;
        this.livroEditando = livroParaEditar;

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(5, 2));

        inicializarComponentes();
        if (livroParaEditar != null) {
            carregarDadosParaEdicao(livroParaEditar);
        }
    }

    private void inicializarComponentes() {
        JLabel lblTitulo = new JLabel("Título:");
        txtTitulo = new JTextField();
        add(lblTitulo);
        add(txtTitulo);

        JLabel lblAutor = new JLabel("Autor:");
        txtAutor = new JTextField();
        add(lblAutor);
        add(txtAutor);

        JLabel lblGenero = new JLabel("Gênero:");
        comboGenero = new JComboBox<>();
        popularComboGenero(); // Chama o método para preencher o combo
        add(lblGenero);
        add(comboGenero);

        JLabel lblClassificacao = new JLabel("Classificação:");
        txtClassificacao = new JTextField();
        add(lblClassificacao);
        add(txtClassificacao);

        JLabel lblFavorito = new JLabel("Favorito:");
        chkFavorito = new JCheckBox();
        add(lblFavorito);
        add(chkFavorito);

        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(this::salvarLivro);
        add(new JLabel()); // Espaço vazio
        add(btnSalvar);
        
        JLabel lblImagemTexto = new JLabel("Imagem:");
        
        lblImagem = new JLabel();
        lblImagem.setPreferredSize(new Dimension(100, 100));
        lblImagem.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(lblImagemTexto);
        add(lblImagem);

        btnSelecionarImagem = new JButton("Selecionar Imagem");
        btnSelecionarImagem.addActionListener(e -> selecionarImagem());
        add(new JLabel()); // Espaço vazio
        add(btnSelecionarImagem);
    }
    
    private void selecionarImagem() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imagens", "jpg", "png", "jpeg"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File imagem = fileChooser.getSelectedFile();
            try {
                imagemSelecionada = Files.readString(imagem.toPath());
                ImageIcon icon = new ImageIcon(new ImageIcon(imagemSelecionada).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                lblImagem.setIcon(icon);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar imagem: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarDadosParaEdicao(Livro livro) {
        txtTitulo.setText(livro.getTitulo());
        txtAutor.setText(livro.getAutor());
        comboGenero.setSelectedItem(livro.getGeneroLiterario()); // Seleciona o gênero pelo nome
        txtClassificacao.setText(String.valueOf(livro.getClassificacao())); // Converte para String
        chkFavorito.setSelected(livro.isFavorito()); 
    }

    private void salvarLivro(ActionEvent e) {
        try {
            Livro livro = livroEditando != null ? livroEditando : new Livro();
            livro.setTitulo(txtTitulo.getText());
            livro.setAutor(txtAutor.getText());
            livro.setGeneroLiterario(((GeneroLiterario) comboGenero.getSelectedItem()).getId()); // Pega o ID do gênero
            livro.setClassificacao(Integer.parseInt(txtClassificacao.getText()));
            livro.setFavorito(chkFavorito.isSelected());
            livro.setImagem(imagemSelecionada);
            
            LivroDAO livroDAO = new LivroDAO();
            livroDAO.save(livro);

            JOptionPane.showMessageDialog(this, "Livro salvo com sucesso!");
            if (janelaPrincipal != null) {
                janelaPrincipal.carregarLivros();
            }
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar livro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void popularComboGenero() {
        GeneroLiterarioDAO generoDAO = new GeneroLiterarioDAO();
        for (GeneroLiterario genero : generoDAO.findAll()) {
            comboGenero.addItem(genero); // Adiciona o objeto GeneroLiterario
        }
}



}
