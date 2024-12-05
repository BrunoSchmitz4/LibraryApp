package dao;

import models.GeneroLiterario;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import models.Livro;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeneroLiterarioDAOTest {

    private GeneroLiterarioDAO generoDAO;
    private Connection connection;

    @Before
    public void setUp() {
        try {
            generoDAO = new GeneroLiterarioDAO(TestConnection.getConnection());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Erro ao inicializar a conexão com o banco de dados de teste.");
        }
    }

    @Test
    public void tearDown() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
//    Autora: Tiffani
    @Test
    public void testAdicionarGeneroLiterario() {
        GeneroLiterario genero = new GeneroLiterario();
        genero.setNome("Livro Técnico");

        generoDAO.create(genero);

        List<GeneroLiterario> generos = generoDAO.findAll();
        assertEquals(1, generos.size());
        assertEquals("Livro Técnico", generos.get(0).getNome());
    }

    @Test
    public void testAdicionarLivroAoGenero() {
        GeneroLiterario genero = new GeneroLiterario();
        genero.setNome("Livro Técnico");
        generoDAO.create(genero);

        List<GeneroLiterario> generos = generoDAO.findAll();
        assertEquals(1, generos.size());
        assertEquals("Livro Técnico", generos.get(0).getNome());

        LivroDAO livroDAO = new LivroDAO(connection);
        Livro livro = new Livro();
        livro.setTitulo("HeartStopper");
        livro.setAutor("Alice Oseman");
        livro.setGeneroLiterario(generos.get(0).getId());
        livro.setClassificacao(5);
        livro.setImagem("https://m.media-amazon.com/images/I/8129HX+5JGL._SY425_.jpg");
        livro.setFavorito(true);
        livro.setDataLeitura(new java.util.Date());

        String resultado = livroDAO.adicionarLivro(livro);
        assertEquals("Livro adicionado com sucesso!", resultado);
    }

    @Test
    public void testVisualizarGeneroSemLivros() {
        GeneroLiterario genero = new GeneroLiterario();
        genero.setNome("Biografia do Autor");
        generoDAO.create(genero);

        List<GeneroLiterario> generos = generoDAO.findAll();
        assertEquals(1, generos.size());
        assertEquals("Biografia do Autor", generos.get(0).getNome());
    }

    @Test
    public void testEditarGeneroComNomeVazio() {
        GeneroLiterario genero = new GeneroLiterario();
        genero.setNome("Livro LGBTQIA+");
        generoDAO.create(genero);

        genero.setNome("");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            generoDAO.update(genero);
        });
        assertTrue(exception.getMessage().contains("O nome do gênero literário não pode estar vazio"));
    }

    @Test
    public void testEditarGeneroComNomeValido() {
        GeneroLiterario genero = new GeneroLiterario();
        genero.setNome("Livro LGBTQIA+");
        generoDAO.create(genero);

        genero.setNome("Romance Escolar");
        generoDAO.update(genero);

        GeneroLiterario atualizado = generoDAO.findAll().get(0);
        assertEquals("Romance Escolar", atualizado.getNome());
    }
}
