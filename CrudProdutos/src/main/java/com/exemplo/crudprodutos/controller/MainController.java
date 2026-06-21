package com.exemplo.crudprodutos.controller;

import com.exemplo.crudprodutos.dao.ProdutoDAO;
import com.exemplo.crudprodutos.model.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

public class MainController {

    @FXML private TextField tfNome;
    @FXML private TextField tfPreco;
    @FXML private TextField tfQuantidade; // NOVO
    @FXML private TextField tfBusca;
    @FXML private TableView<Produto> tabela;
    @FXML private TableColumn<Produto, Double> colunaPreco;

    private ProdutoDAO dao = new ProdutoDAO();
    private Produto produtoSelecionado = null;

    private static final NumberFormat MOEDA =
            NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    @FXML
    public void initialize() {
        configurarColunaPreco();
        carregarTabelaComBusca();

        tabela.getSelectionModel().selectedItemProperty().addListener(
                (obs, antigo, novo) -> {
                    if (novo != null) {
                        produtoSelecionado = novo;
                        tfNome.setText(novo.getNome());
                        tfPreco.setText(String.valueOf(novo.getPreco()));
                        tfQuantidade.setText(String.valueOf(novo.getQuantidade())); // NOVO
                        tfNome.setStyle(estiloNormal());
                        tfPreco.setStyle(estiloNormal());
                        tfQuantidade.setStyle(estiloNormal()); // NOVO
                    }
                }
        );
    }

    private void configurarColunaPreco() {
        colunaPreco.setCellFactory(col -> new TableCell<Produto, Double>() {
            @Override
            protected void updateItem(Double valor, boolean vazio) {
                super.updateItem(valor, vazio);
                if (vazio || valor == null) setText(null);
                else setText(MOEDA.format(valor));
            }
        });
    }

    private void carregarTabelaComBusca() {
        ObservableList<Produto> listaBruta =
                FXCollections.observableArrayList(dao.listarTodos());
        FilteredList<Produto> listaFiltrada = new FilteredList<>(listaBruta, p -> true);
        tfBusca.textProperty().addListener((obs, antigo, novo) -> {
            listaFiltrada.setPredicate(produto -> {
                if (novo == null || novo.trim().isEmpty()) return true;
                return produto.getNome().toLowerCase()
                        .contains(novo.toLowerCase().trim());
            });
        });
        tabela.setItems(listaFiltrada);
    }

    @FXML
    private void salvar() {
        String nome     = tfNome.getText().trim();
        String precoStr = tfPreco.getText().trim();
        String qtdStr   = tfQuantidade.getText().trim(); // NOVO

        boolean valido = true;

        if (nome.isEmpty()) {
            tfNome.setStyle(estiloErro()); valido = false;
        } else {
            tfNome.setStyle(estiloNormal());
        }

        double preco = 0;
        if (precoStr.isEmpty()) {
            tfPreco.setStyle(estiloErro()); valido = false;
        } else {
            try {
                preco = Double.parseDouble(precoStr.replace(",", "."));
                tfPreco.setStyle(estiloNormal());
            } catch (NumberFormatException e) {
                tfPreco.setStyle(estiloErro()); valido = false;
            }
        }

        // NOVO — valida quantidade
        int quantidade = 0;
        if (qtdStr.isEmpty()) {
            tfQuantidade.setStyle(estiloErro()); valido = false;
        } else {
            try {
                quantidade = Integer.parseInt(qtdStr);
                tfQuantidade.setStyle(estiloNormal());
            } catch (NumberFormatException e) {
                tfQuantidade.setStyle(estiloErro()); valido = false;
            }
        }

        if (!valido) return;

        if (produtoSelecionado == null) {
            Produto novo = new Produto();
            novo.setNome(nome);
            novo.setPreco(preco);
            novo.setQuantidade(quantidade); // NOVO
            dao.inserir(novo);
        } else {
            produtoSelecionado.setNome(nome);
            produtoSelecionado.setPreco(preco);
            produtoSelecionado.setQuantidade(quantidade); // NOVO
            dao.atualizar(produtoSelecionado);
            produtoSelecionado = null;
        }

        limparFormulario();
        carregarTabelaComBusca();
    }

    @FXML
    private void excluir() {
        if (produtoSelecionado == null) {
            mostrarAlerta("Selecione um produto na tabela para excluir!");
            return;
        }
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText("Excluir produto");
        confirmacao.setContentText(
                "Tem certeza que deseja excluir \"" + produtoSelecionado.getNome() + "\"?"
        );
        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            dao.excluir(produtoSelecionado.getId());
            produtoSelecionado = null;
            limparFormulario();
            carregarTabelaComBusca();
        }
    }

    private void limparFormulario() {
        tfNome.clear();
        tfPreco.clear();
        tfQuantidade.clear(); // NOVO
        tfBusca.clear();
        tfNome.setStyle(estiloNormal());
        tfPreco.setStyle(estiloNormal());
        tfQuantidade.setStyle(estiloNormal()); // NOVO
        tabela.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private String estiloNormal() {
        return "-fx-background-color: #ffffff; -fx-border-color: #cccccc; " +
                "-fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 6;";
    }

    private String estiloErro() {
        return "-fx-background-color: #fff0f0; -fx-border-color: #f44336; " +
                "-fx-border-width: 2; -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 6;";
    }
}