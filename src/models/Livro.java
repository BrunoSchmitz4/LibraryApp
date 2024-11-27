package models;

public class Livro {
    private int id;
    private String titulo;
    private String autor;
    private int generoLiterario; // ID do gênero literário
    private int classificacao;
    private String imagem;
    private boolean favorito; // Indica se o livro é favoritado

    // Construtor padrão
    public Livro() {}

    // Construtor completo
    public Livro(int id, String titulo, String autor, int generoLiterario, int classificacao, String imagem, boolean favorito) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.generoLiterario = generoLiterario;
        this.classificacao = classificacao;
        this.imagem = imagem;
        this.favorito = favorito;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getGeneroLiterario() {
        return generoLiterario;
    }

    public void setGeneroLiterario(int generoLiterario) {
        this.generoLiterario = generoLiterario;
    }

    public int getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(int classificacao) {
        this.classificacao = classificacao;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }
}
