package com.exemplo.crudprodutos.dao;

import com.exemplo.crudprodutos.model.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private static final String URL     = "";
    private static final String USUARIO = "";
    private static final String SENHA   = "";

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    // CREATE — agora inclui quantidade
    public void inserir(Produto p) {
        String sql = "INSERT INTO produtos (nome, preco, quantidade) VALUES (?, ?, ?)";
        try (Connection c = conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setDouble(2, p.getPreco());
            ps.setInt(3, p.getQuantidade()); // NOVO
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ — agora lê quantidade
    public List<Produto> listarTodos() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produtos";
        try (Connection c = conectar();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Produto p = new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getInt("quantidade") // NOVO
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // UPDATE — agora atualiza quantidade
    public void atualizar(Produto p) {
        String sql = "UPDATE produtos SET nome=?, preco=?, quantidade=? WHERE id=?";
        try (Connection c = conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setDouble(2, p.getPreco());
            ps.setInt(3, p.getQuantidade()); // NOVO
            ps.setInt(4, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE — sem mudança
    public void excluir(int id) {
        String sql = "DELETE FROM produtos WHERE id=?";
        try (Connection c = conectar();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}