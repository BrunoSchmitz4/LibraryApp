package view;

import dao.UsuarioDAO;
import models.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import com.formdev.flatlaf.FlatLightLaf;

public class CadastroUsuarioView extends JFrame {

    private JTextField txtNome;
    private JTextField txtEmail;
    private JPasswordField txtSenha;

    public CadastroUsuarioView() {
        setTitle("Cadastro de Usuário");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel lblNome = new JLabel("Nome:");
        txtNome = new JTextField();
        JLabel lblEmail = new JLabel("E-mail:");
        txtEmail = new JTextField();
        JLabel lblSenha = new JLabel("Senha:");
        txtSenha = new JPasswordField();

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(this::cadastrarUsuario);

        panel.add(lblNome);
        panel.add(txtNome);
        panel.add(lblEmail);
        panel.add(txtEmail);
        panel.add(lblSenha);
        panel.add(txtSenha);

        add(panel, BorderLayout.CENTER);
        add(btnCadastrar, BorderLayout.SOUTH);
    }

    private void cadastrarUsuario(ActionEvent e) {
        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String senha = new String(txtSenha.getPassword());

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        if (usuarioDAO.findByEmail(email) != null) {
            JOptionPane.showMessageDialog(this, "O e-mail já está em uso. Tente outro.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);

        usuarioDAO.create(usuario);

        JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");

        abrirLoginView();
    }

    private void abrirLoginView() {
        dispose();
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            CadastroUsuarioView frame = new CadastroUsuarioView();
            frame.setVisible(true);
        });
    }
}
