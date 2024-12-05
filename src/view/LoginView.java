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

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel lblEmail = new JLabel("E-mail:");
        txtEmail = new JTextField();
        JLabel lblSenha = new JLabel("Senha:");
        txtSenha = new JPasswordField();

        JButton btnLogin = new JButton("Entrar");
        btnLogin.addActionListener(this::autenticarUsuario);

        JButton btnCriarConta = new JButton("Criar Conta");
        btnCriarConta.addActionListener(e -> abrirCadastroUsuario());

        panel.add(lblEmail);
        panel.add(txtEmail);
        panel.add(lblSenha);
        panel.add(txtSenha);

        add(panel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        bottomPanel.add(btnLogin);
        bottomPanel.add(btnCriarConta);

        add(bottomPanel, BorderLayout.SOUTH);
    }
    
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

    private void autenticarUsuario(ActionEvent e) {
        String email = txtEmail.getText();
        String senha = new String(txtSenha.getPassword());

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

    private void abrirCadastroUsuario() {
        dispose();
        SwingUtilities.invokeLater(() -> new CadastroUsuarioView().setVisible(true));
    }

    private void abrirMenuPrincipal() {
        dispose();
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
