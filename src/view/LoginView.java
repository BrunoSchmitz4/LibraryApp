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
        setSize(400, 200);
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

        // Adicionar componentes ao painel
        panel.add(lblEmail);
        panel.add(txtEmail);
        panel.add(lblSenha);
        panel.add(txtSenha);

        add(panel, BorderLayout.CENTER);
        add(btnLogin, BorderLayout.SOUTH);
    }

    // Método para autenticar o usuário
    private void autenticarUsuario(ActionEvent e) {
        String email = txtEmail.getText();
        String senha = new String(txtSenha.getPassword());

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.autenticar(email, senha);

        if (usuario != null) {
            JOptionPane.showMessageDialog(this, "Bem-vindo, " + usuario.getNome() + "!");
            abrirMenuPrincipal();
        } else {
            JOptionPane.showMessageDialog(this, "Credenciais inválidas. Tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para abrir o menu principal (exemplo)
    private void abrirMenuPrincipal() {
        // Aqui você pode redirecionar para a tela principal do sistema
        dispose(); // Fecha a janela de login
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }

    // Método principal para teste
    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
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
