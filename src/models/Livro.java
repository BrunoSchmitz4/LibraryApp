package model;

public class Livro {
    private int id;
    private String titulo;
    private String autor;
    private int generoLiterarioId;
    private int classificacao;
    private String imagem;

    // Construtor padrão
    public Livro() {}

    // Construtor completo
    public Livro(int id, String titulo, String autor, int generoLiterarioId, int classificacao, String imagem) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.generoLiterarioId = generoLiterarioId;
        this.classificacao = classificacao;
        this.imagem = imagem;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public int getGeneroLiterarioId() { return generoLiterarioId; }
    public void setGeneroLiterarioId(int generoLiterarioId) { this.generoLiterarioId = generoLiterarioId; }

    public int getClassificacao() { return classificacao; }
    public void setClassificacao(int classificacao) { this.classificacao = classificacao; }

    public String getImagem() { return imagem; }
    public void setImagem(String imagem) { this.imagem = imagem; }
}
