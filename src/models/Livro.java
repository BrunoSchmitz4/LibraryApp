package models;

import java.util.Date;

public class Livro {
    private int id;
    private String titulo;
    private String autor;
    private int generoLiterario; // ID do gênero literário
    private int classificacao;
    private String imagem;
    private boolean favorito; // Indica se o livro é favoritado
    private Date dataLeitura; // Nova propriedade para data de leitura

    // Construtor padrão
    public Livro() {}

    // Construtor usado nos testes
    public Livro(String titulo, String autor, String imagem, int generoLiterario, int classificacao, String imagemLink, Date dataLeitura) {
        this.titulo = titulo;
        this.autor = autor;
        this.generoLiterario = generoLiterario;
        this.classificacao = classificacao;
        this.imagem = imagem;
        this.favorito = false; // Definido como padrão para falso
        this.dataLeitura = dataLeitura;
    }

    // Construtor completo (existe também caso você queira usar outros construtores)
    public Livro(int id, String titulo, String autor, int generoLiterario, int classificacao, String imagem, boolean favorito, Date dataLeitura) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.generoLiterario = generoLiterario;
        this.classificacao = classificacao;
        this.imagem = imagem;
        this.favorito = favorito;
        this.dataLeitura = dataLeitura;
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

   // Método para obter a data de leitura
    public Date getDataLeitura() {
        return dataLeitura;
    }

    // Método para definir a data de leitura
    public void setDataLeitura(Date dataLeitura) {
        this.dataLeitura = dataLeitura;
    }

    // Método para verificar se a data de leitura é válida (não maior que a data atual)
    public boolean isDataLeituraValida() {
        return dataLeitura != null && !dataLeitura.after(new Date());
    }
}
