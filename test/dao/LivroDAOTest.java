package dao;

import com.formdev.flatlaf.json.ParseException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import models.Livro;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LivroDAOTest {
    private LivroDAO livroDAO;

    @Before
    public void setUp() throws SQLException {
        // Simulação de conexão para testes; substitua por uma conexão real para testes de integração
        livroDAO = new LivroDAO(TestConnection.getConnection());
    }
    

    //    Autor: Bruno
    @Test
    public void testGeneroNaoPodeSerVazio() {
        Livro livro = new Livro("Título Teste", "Autor Teste", "https://imagem-teste.jpeg", 0, 4, "https://imagem-teste.jpeg", new java.util.Date());
        String resultado = livroDAO.adicionarLivro(livro);
        System.out.println("Escolha um gênero literário existente ou disponível.");
        assertEquals("Escolha um gênero literário existente ou disponível.", resultado);
    }

    //    Autor: Bruno
    @Test
    public void testDataLeituraNaoPodeSerMaiorQueAtual() {
        Livro livro = new Livro("Título Teste", "Autor Teste", "https://imagem-teste.jpeg", 1, 4, "https://imagem-teste.jpeg", new java.util.Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)); // Data no futuro
        String resultado = livroDAO.adicionarLivro(livro);

        assertEquals("A data de leitura não pode ser maior que o dia atual.", resultado);
    }

    //    Autor: Bruno
    @Test
    public void testTituloMaximo200Caracteres() {
        String titulo = "T".repeat(201); // Cria um título com 201 caracteres
        Livro livro = new Livro(titulo, "Autor Teste", "https://imagem-teste.jpeg", 1, 4, "https://imagem-teste.jpeg", new java.util.Date());
        String resultado = livroDAO.adicionarLivro(livro);

        assertEquals("O título deve ter no máximo 200 caracteres.", resultado);
    }

    @Test
    public void testAutoriaMaximo150Caracteres() {
        String autor = "A".repeat(151); // Cria um autor com 151 caracteres
        Livro livro = new Livro("Título Teste", autor, "https://imagem-teste.jpeg", 1, 4, "https://imagem-teste.jpeg", new java.util.Date());
        String resultado = livroDAO.adicionarLivro(livro);

        assertEquals("A autoria deve ter no máximo 150 caracteres.", resultado);
    }
    
    //    Autor: Bruno
    @Test
    public void testImagemFormatoInvalido() {
        Livro livro = new Livro("Título Teste", "Autor Teste", "https://imagem-teste.bmp", 1, 4, "https://imagem-teste.bmp", new java.util.Date());
        String resultado = livroDAO.adicionarLivro(livro);

        assertEquals("O link da imagem deve começar com http ou https e terminar com .jpeg, .png, .jpg ou .svg.", resultado);
    }

    //    Autor: Bruno
    @Test
    public void testClassificacaoInvalida() {
        Livro livro = new Livro("Título Teste", "Autor Teste", "https://imagem-teste.jpeg", 1, 6, "https://imagem-teste.jpeg", new java.util.Date()); // Classificação inválida
        String resultado = livroDAO.adicionarLivro(livro);

        assertEquals("A classificação precisa ser uma quantidade válida.", resultado);
    }

    //    Autor: Bruno
    @Test
    public void testImagemLinkInvalido() {
        Livro livro = new Livro("Título Teste", "Autor Teste", "imagem-teste.jpeg", 1, 4, "https://imagem-teste.jpeg", new java.util.Date()); // Link não começa com http
        String resultado = livroDAO.adicionarLivro(livro);

        assertEquals("O link da imagem deve começar com http ou https e terminar com .jpeg, .png, .jpg ou .svg.", resultado);
    }

    //    Autor: Bruno
    @Test
    public void testGeneroInexistente() {
        Livro livro = new Livro("Título Teste", "Autor Teste", "https://imagem-teste.jpeg", 99, 4, "https://imagem-teste.jpeg", new java.util.Date()); // Gênero inexistente
        String resultado = livroDAO.adicionarLivro(livro);

        assertEquals("Escolha um gênero literário existente ou disponível.", resultado);
    }

    //    Autor: Bruno
    @Test
    public void testTituloDuplicado() {
        Livro livro1 = new Livro("Título Duplicado", "Autor Teste", "https://imagem-teste.jpeg", 1, 4, "https://imagem-teste.jpeg", new java.util.Date());
        Livro livro2 = new Livro("Título Duplicado", "Outro Autor", "https://outra-imagem.jpeg", 1, 3, "https://outra-imagem.jpeg", new java.util.Date());

        livroDAO.adicionarLivro(livro1);
        String resultado = livroDAO.adicionarLivro(livro2);

        assertEquals("Já existe um livro com o mesmo título.", resultado);
    }
    
    //    Autor: Bruno e Tiffani
    @Test
    public void testAdicionarLivroComSucesso() {
        Livro livro = new Livro("Título Válido 2", "Autor Válido", "https://imagem-teste.jpeg", 1, 5, "https://imagem-teste.jpeg", new java.util.Date());
        String resultado = livroDAO.adicionarLivro(livro);

        assertEquals("Livro adicionado com sucesso!", resultado);
    }
    
    // Autora: Tiffani Candido
    @Test
    public void testLivroAdicionado() {
        // Criação de um livro para teste
        Livro novoLivro = new Livro(
            "Chapeuzinho vermelho",
            "Irmãos Grimm",
            "https://exemplo.com/imagem.jpg",
            2, // ID de gênero literário válido
            5, // Classificação
            "https://exemplo.com/imagem.jpg",
            new Date(System.currentTimeMillis()) // Data de leitura válida
        );

        // Adiciona o livro ao banco de dados e verifica a mensagem de retorno
        String resultado = livroDAO.adicionarLivro(novoLivro);
        assertEquals("Livro adicionado com sucesso!", resultado);

        // Verifica se o livro foi adicionado na lista de livros
        List<Livro> livros = livroDAO.findAll();
        assertNotNull(livros);
        assertTrue(
            livros.stream().anyMatch(
                livro -> "Chapeuzinho vermelho".equals(livro.getTitulo()) && "Irmãos Grimm".equals(livro.getAutor())
            )
        );

        // Recupera os detalhes do livro adicionado
        Livro livroDetalhes = livroDAO.findAll().stream()
            .filter(livro -> "Chapeuzinho vermelho".equals(livro.getTitulo()))
            .findFirst()
            .orElse(null);

        assertNotNull(livroDetalhes);
        assertEquals("Irmãos Grimm", livroDetalhes.getAutor());
        assertEquals("https://exemplo.com/imagem.jpg", livroDetalhes.getImagem());
        assertEquals(2, livroDetalhes.getGeneroLiterario());
        assertEquals(5, livroDetalhes.getClassificacao());
        assertNotNull(livroDetalhes.getDataLeitura());
    }
    
//    Autora: Tiffani
    @Test
    public void testNaoPermitirLivroSemAutor() {
        // Cria um livro com o campo "autor" vazio
        Livro livro = new Livro(
            "Chapeuzinho vermelho 2",
            "", // Autor vazio
            "https://exemplo.com/imagem.jpg",
            2, // ID de gênero literário válido
            5, // Classificação
            "https://exemplo.com/imagem.jpg",
            new Date(System.currentTimeMillis()) // Data de leitura válida
        );

        // Tenta adicionar o livro e captura a mensagem de erro
        String resultado = livroDAO.adicionarLivro(livro);

        // Verifica se a mensagem de erro é a esperada
        assertEquals("O autor não pode ser vazio.", resultado);

        // Certifica-se de que o livro não foi adicionado
        List<Livro> livros = livroDAO.findAll();
        assertTrue(livros.stream().noneMatch(l -> "Chapeuzinho vermelho 2".equals(l.getTitulo())));
    }

    // Autora: Tiffani
    @Test
    public void testDataLeituraFormatoInvalido() {
        // Define um formato de data incorreto (yyyy/MM/dd)
        String dataInvalida = "2023/12/01";

        // Tenta parsear a data no formato desejado e verifica se está no formato correto
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        formato.setLenient(false); // Garante que apenas datas estritamente válidas sejam aceitas
        boolean formatoValido;
        try {
            formato.parse(dataInvalida);// Tenta parsear a data
            formatoValido = true; // Se não lançar exceção, formato é válido
        } catch (Exception e) {
            formatoValido = false; // Se lançar exceção, formato é inválido
        }

        // Valida que o formato é reconhecido como inválido
        assertFalse(formatoValido);

        // Garante que um livro com uma data de leitura inválida não será salvo no banco
        Livro livro = new Livro(
            "Chapeuzinho Vermelho",
            "Irmãos Grimm",
            "https://exemplo.com/imagem.jpg",
            2, // ID de gênero literário válido
            5, // Classificação válida
            "https://exemplo.com/imagem.jpg",
            null // Data será tratada posteriormente
        );

        // Adiciona o livro e captura o resultado
        String resultado = livroDAO.adicionarLivro(livro);

        // Verifica se o livro não foi adicionado com sucesso
        assertNotEquals("Livro adicionado com sucesso!", resultado);
    }
    
    // Autora: Tiffani
    @Test
    public void testAdicionarLivroSemAutor() {
        try {
            livroDAO = new LivroDAO(TestConnection.getConnection());
            Livro livro = new Livro();
            livro.setTitulo("Chapeuzinho Vermelho");
            livro.setAutor("");
            livro.setGeneroLiterario(2);
            livro.setClassificacao(5);
            livro.setImagem("https://exemplo.com/imagem.jpg");
            livro.setFavorito(false);
            livro.setDataLeitura(new Date());
            
            String resultado = livroDAO.adicionarLivro(livro);
            
            assertNotEquals("Livro adicionado com sucesso!", resultado);
            assertTrue(resultado.contains("O autor não pode ser vazio."));

        } catch (Exception e) {
        }
    }
}
