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
            // Inicializa a conexão com o banco de dados existente para testes
            connection = ConnectionFactory.getConnection(); // Método de fábrica de conexão já configurado em seu código
            // Cria a instância do DAO sem passar a conexão pelo construtor
            generoDAO = new GeneroLiterarioDAO();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Erro ao inicializar a conexão com o banco de dados de teste.");
        }
    }

    @Test
    public void tearDown() {
        try {
            // Fecha a conexão após cada teste
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAdicionarGeneroLiterario() {
        GeneroLiterario genero = new GeneroLiterario();
        genero.setNome("Livro Técnico");

        // Adiciona o gênero literário
        generoDAO.create(genero);

        // Verifica se o gênero foi adicionado com sucesso
        List<GeneroLiterario> generos = generoDAO.findAll();
        assertEquals(1, generos.size());
        assertEquals("Livro LGBTQIA+", generos.get(0).getNome());
    }

    @Test
    public void testAdicionarLivroAoGenero() {
        // Adiciona um gênero para associar ao livro
        GeneroLiterario genero = new GeneroLiterario();
        genero.setNome("Livro Técnico");
        generoDAO.create(genero);

        // Verifica se o gênero foi adicionado corretamente
        List<GeneroLiterario> generos = generoDAO.findAll();
        assertEquals(1, generos.size());
        assertEquals("Livro LGBTQIA+", generos.get(0).getNome());

        // Adiciona um livro associado ao gênero
        LivroDAO livroDAO = new LivroDAO(connection); // Instância do LivroDAO sem argumentos
        Livro livro = new Livro();
        livro.setTitulo("HeartStopper");
        livro.setAutor("Alise Oseman");
        livro.setGeneroLiterario(generos.get(0).getId()); // Define o ID do gênero
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

        // Tenta atualizar o nome para um valor vazio
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

        // Atualiza o nome do gênero
        genero.setNome("Romance Escolar");
        generoDAO.update(genero);

        GeneroLiterario atualizado = generoDAO.findAll().get(0);
        assertEquals("Romance Escolar", atualizado.getNome());
    }
}
