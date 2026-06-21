package com.exemplo.crudprodutos.model;

    // Model = representação de uma linha da tabela "produtos" do banco
    // Cada campo desta classe corresponde a uma coluna da tabela

public class Produto {

    private int id;         // coluna: id
    private String nome;    // coluna: nome
    private double preco;   // coluna: preco
    private int quantidade;

    // Construtor vazio — obrigatório para o TableView funcionar
    public Produto(){}

    // Construtor completo — usado ao ler os dados do banco
    public Produto(int id, String nome, double preco, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    // Getters e Setters
    // O TableView usa reflexão para chamar getNome(), getPreco(), etc.
    // Se o getter não existir, a coluna fica em branco

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
