package view;

import dao.LivroDAO;
import model.Livro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CadastroLivroView extends JFrame {

    // Componentes da interface
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtGeneroId;
    private JTextField txtClassificacao;
    private JTextField txtImagem;
    private JButton btnSalvar;
    private JButton btnCancelar;
    private int idLivro = 0; // Inicializa com 0, usado para identificar edições


    public CadastroLivroView() {
        setTitle("Cadastro de Livro");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha apenas esta janela
        setLocationRelativeTo(null); // Centraliza a janela

        // Painel principal
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        // Campos e rótulos
        panel.add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        panel.add(txtTitulo);

        panel.add(new JLabel("Autor:"));
        txtAutor = new JTextField();
        panel.add(txtAutor);

        panel.add(new JLabel("ID do Gênero Literário:"));
        txtGeneroId = new JTextField();
        panel.add(txtGeneroId);

        panel.add(new JLabel("Classificação:"));
        txtClassificacao = new JTextField();
        panel.add(txtClassificacao);

        panel.add(new JLabel("URL da Imagem:"));
        txtImagem = new JTextField();
        panel.add(txtImagem);

        // Botões
        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");

        panel.add(btnSalvar);
        panel.add(btnCancelar);

        add(panel);

        // Ações dos botões
        btnSalvar.addActionListener(this::salvarLivro);
        btnCancelar.addActionListener(e -> dispose()); // Fecha a janela
    }
    
    public void setDadosLivro(int id, String titulo, String autor, int generoId, int classificacao, String imagem) {
    // Preencher os campos com os dados recebidos
    txtTitulo.setText(titulo);
    txtAutor.setText(autor);
    txtGeneroId.setText(String.valueOf(generoId));
    txtClassificacao.setText(String.valueOf(classificacao));
    txtImagem.setText(imagem);

    // Desabilitar o campo de ID (opcional, pois ele não é exibido na tela)
    this.idLivro = id; // Variável que guardará o ID para edição no banco
}


    // Método para salvar o livro
    private void salvarLivro(ActionEvent e) {
    try {
        // Coletar os dados da interface
        String titulo = txtTitulo.getText();
        String autor = txtAutor.getText();
        int generoId = Integer.parseInt(txtGeneroId.getText());
        int classificacao = Integer.parseInt(txtClassificacao.getText());
        String imagem = txtImagem.getText();

        LivroDAO livroDAO = new LivroDAO();

        if (idLivro > 0) {
            // Atualizar livro existente
            Livro livro = new Livro(idLivro, titulo, autor, generoId, classificacao, imagem);
            livroDAO.update(livro);
            JOptionPane.showMessageDialog(this, "Livro atualizado com sucesso!");
        } else {
            // Inserir novo livro
            Livro livro = new Livro(0, titulo, autor, generoId, classificacao, imagem);
            livroDAO.create(livro);
            JOptionPane.showMessageDialog(this, "Livro cadastrado com sucesso!");
        }

        dispose(); // Fecha a janela após salvar
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Erro: Preencha os campos numéricos corretamente.", "Erro de validação", JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Erro ao salvar o livro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}


    // Método principal para testes isolados
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CadastroLivroView().setVisible(true);
        });
    }
}
