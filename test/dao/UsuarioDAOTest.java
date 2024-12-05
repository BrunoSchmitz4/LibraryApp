package dao;

import dao.UsuarioDAO;
import models.Usuario;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

// Autora: Tiffani Candido
public class UsuarioDAOTest {

    private UsuarioDAO usuarioDAO;

    @Before
    public void setUp() {
        usuarioDAO = new UsuarioDAO();
    }

//    Autora: Tiffani
    @Test
    public void testLoginComCredenciaisCorretas() {
        String email = "joao@gmail.com";
        String senha = "senha123";

        Usuario usuario = usuarioDAO.autenticar(email, senha);

        assertNotNull("O usuário deve ser autenticado com credenciais corretas", usuario);
        assertEquals("O nome do usuário deve ser João", "João Silva", usuario.getNome());
        assertEquals("O email deve ser joao@gmail.com", email, usuario.getEmail());
    }

    //    Autora: Tiffani
    @Test
    public void testLoginComEmailIncorreto() {
        String email = "joao@hotmail.com";
        String senha = "senha123";

        Usuario usuario = usuarioDAO.autenticar(email, senha);

        assertNull("Credenciais inválidas. Tente novamente.", usuario);
    }

    @Test
    public void testLoginComSenhaIncorreta() {
        String email = "joao@hotmail.com";
        String senha = "senha1234";

        Usuario usuario = usuarioDAO.autenticar(email, senha);

        assertNull("Email ou senha incorreta", usuario);
    }

    @Test
    public void testLoginComCamposVazios() {
        String email = "";
        String senha = "";

        Usuario usuario = usuarioDAO.autenticar(email, senha);

        assertNull("Preencha o campos", usuario);
    }
}
