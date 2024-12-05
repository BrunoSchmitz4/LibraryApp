package view;

import dao.UsuarioDAO;
import models.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import com.formdev.flatlaf.FlatLightLaf;

public class LoginView extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtSenha;

    public LoginView() {
        setTitle("Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Painel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        // Componentes do formulário
        JLabel lblEmail = new JLabel("E-mail:");
        txtEmail = new JTextField();
        JLabel lblSenha = new JLabel("Senha:");
        txtSenha = new JPasswordField();

        // Botão de login
        JButton btnLogin = new JButton("Entrar");
        btnLogin.addActionListener(this::autenticarUsuario);

        // Botão de criar conta
        JButton btnCriarConta = new JButton("Criar Conta");
        btnCriarConta.addActionListener(e -> abrirCadastroUsuario());

        // Adicionar componentes ao painel
        panel.add(lblEmail);
        panel.add(txtEmail);
        panel.add(lblSenha);
        panel.add(txtSenha);

        add(panel, BorderLayout.CENTER);

        // Painel inferior com os botões
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        bottomPanel.add(btnLogin);
        bottomPanel.add(btnCriarConta);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void autenticarUsuario(ActionEvent e) {
    String email = txtEmail.getText();
    String senha = new String(txtSenha.getPassword());

    // Verifica se algum campo está vazio
    if (email.isEmpty() || senha.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
        return;
    }

    UsuarioDAO usuarioDAO = new UsuarioDAO();
    Usuario usuario = usuarioDAO.autenticar(email, senha);

    if (usuario != null) {
        JOptionPane.showMessageDialog(this, "Bem-vindo, " + usuario.getNome() + "!");
        abrirMenuPrincipal();
    } else {
        JOptionPane.showMessageDialog(this, "Credenciais inválidas. Tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
    }
}


    // Método para abrir a tela de cadastro de usuário
    private void abrirCadastroUsuario() {
        dispose(); // Fecha a janela de login
        SwingUtilities.invokeLater(() -> new CadastroUsuarioView().setVisible(true));
    }

    // Método para abrir o menu principal (exemplo)
    private void abrirMenuPrincipal() {
        dispose(); // Fecha a janela de login
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }

    // Método principal para teste
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            LoginView frame = new LoginView();
            frame.setVisible(true);
        });
    }
}
