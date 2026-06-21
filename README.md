# Produtos-Desktop

# 📦 CrudProdutos

Aplicação desktop desenvolvida em **Java 21 + JavaFX** com banco de dados **MySQL**, seguindo o padrão de arquitetura **MVC** (Model-View-Controller).

Permite cadastrar, visualizar, editar, excluir e buscar produtos em tempo real.

---

## 🖥️ Funcionalidades

- ✅ Cadastrar novo produto (nome, preço, quantidade)
- ✅ Listar todos os produtos em uma tabela
- ✅ Editar produto ao clicar na linha da tabela
- ✅ Excluir produto com confirmação
- ✅ Busca em tempo real por nome
- ✅ Validação visual com borda vermelha nos campos inválidos
- ✅ Preço formatado como moeda brasileira (R$ 23,90)

---

## 🗂️ Estrutura do Projeto

```
CrudProdutos/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/exemplo/crudprodutos/
│       │       ├── Launcher.java           # Inicia o app JavaFX
│       │       ├── controller/
│       │       │   └── MainController.java # Lógica da tela (eventos, validações)
│       │       ├── model/
│       │       │   └── Produto.java        # Representa um produto (Model)
│       │       └── dao/
│       │           └── ProdutoDAO.java     # Acesso ao banco de dados (SQL)
│       └── resources/
│           └── com/exemplo/crudprodutos/
│               └── MainView.fxml           # Layout da tela (Scene Builder)
├── module-info.java                        # Configuração do módulo Java
└── pom.xml                                 # Dependências Maven
```

---

## 🏗️ Arquitetura MVC

O projeto segue o padrão **MVC (Model-View-Controller)**:

| Camada | Arquivo | Responsabilidade |
|---|---|---|
| **Model** | `Produto.java` | Representa os dados. Cada campo é uma coluna da tabela no banco. |
| **View** | `MainView.fxml` | Define o layout visual da tela montado no Scene Builder. |
| **Controller** | `MainController.java` | Liga a View ao banco: recebe eventos dos botões e chama o DAO. |
| **DAO** | `ProdutoDAO.java` | Único arquivo que escreve SQL. Isola o banco do restante do código. |

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Para que serve |
|---|---|---|
| Java | 21 | Linguagem principal |
| JavaFX | 21.0.6 | Interface gráfica desktop |
| Scene Builder | — | Editor visual de arquivos FXML |
| MySQL | 8.x | Banco de dados relacional |
| MySQL Connector/J | 8.3.0 | Driver JDBC para conectar Java ao MySQL |
| Maven | — | Gerenciador de dependências e build |

---

## ⚙️ Como Configurar e Rodar

### Pré-requisitos

- JDK 21 instalado
- MySQL instalado e rodando
- IntelliJ IDEA (Community ou Ultimate)
- Scene Builder instalado

### 1. Criar o banco de dados

Execute no MySQL Workbench ou terminal:

```sql
CREATE DATABASE IF NOT EXISTS crud_produtos;

USE crud_produtos;

CREATE TABLE IF NOT EXISTS produtos (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    nome       VARCHAR(100) NOT NULL,
    preco      DECIMAL(10,2) NOT NULL,
    quantidade INT NOT NULL DEFAULT 0
);
```

### 2. Configurar a conexão

Abra `ProdutoDAO.java` e ajuste as credenciais:

```java
private static final String URL     = "jdbc:mysql://127.0.0.1:3306/crud_produtos";
private static final String USUARIO = "root";
private static final String SENHA   = "sua_senha_aqui";
```

### 3. Rodar o projeto

No IntelliJ, clique com o botão direito em `Launcher.java` → **Run 'Launcher'**.

---

## 📁 Explicação dos Arquivos

### `Produto.java` — Model

Representa uma linha da tabela `produtos` do banco. Cada campo corresponde a uma coluna.

O JavaFX usa **reflexão** para chamar os getters automaticamente ao preencher o `TableView`. Por isso os nomes dos getters (`getNome()`, `getPreco()`, etc.) precisam seguir o padrão Java Bean.

```java
// Exemplo: a TableColumn com property="nome" chama getNome() automaticamente
public String getNome() { return nome; }
```

### `ProdutoDAO.java` — Data Access Object

Toda a comunicação com o banco de dados fica **isolada aqui**. O Controller nunca escreve SQL.

Usa `PreparedStatement` em vez de concatenar strings SQL para evitar **SQL Injection**:

```java
// ✅ Seguro — usa placeholders "?"
String sql = "INSERT INTO produtos (nome, preco, quantidade) VALUES (?, ?, ?)";
PreparedStatement ps = c.prepareStatement(sql);
ps.setString(1, p.getNome());
```

O bloco `try-with-resources` fecha a conexão automaticamente ao terminar:

```java
try (Connection c = conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
    // conexão fechada automaticamente aqui
}
```

### `MainView.fxml` — View

Arquivo XML gerado pelo **Scene Builder** que define o layout visual. Principais componentes usados:

| Componente | O que faz |
|---|---|
| `BorderPane` | Layout raiz. Divide a tela em TOP, CENTER, BOTTOM, LEFT, RIGHT |
| `VBox` | Empilha filhos **verticalmente** (um embaixo do outro) |
| `HBox` | Organiza filhos **horizontalmente** (lado a lado) |
| `TextField` | Campo de texto para entrada do usuário |
| `Button` | Botão. O atributo `onAction` define o método do Controller a chamar |
| `TableView` | Tabela com suporte a seleção de linhas |
| `TableColumn` | Coluna da tabela. O `PropertyValueFactory` chama o getter do Model |

O atributo `fx:controller` conecta o FXML ao Controller:

```xml
<BorderPane fx:controller="com.exemplo.crudprodutos.controller.MainController">
```

O atributo `fx:id` conecta um componente ao campo `@FXML` no Controller:

```xml
<!-- No FXML -->
<TextField fx:id="tfNome"/>
```
```java
// No Controller
@FXML private TextField tfNome;
```

### `MainController.java` — Controller

Cérebro da aplicação. É chamado automaticamente pelo JavaFX após o FXML carregar.

**`initialize()`** — executado automaticamente ao abrir a tela:
- Carrega os produtos do banco na tabela
- Registra o listener de seleção de linha

**`salvar()`** — chamado pelo botão Salvar:
- Valida os campos (borda vermelha se inválido)
- Se nenhuma linha estiver selecionada → INSERT
- Se uma linha estiver selecionada → UPDATE

**`excluir()`** — chamado pelo botão Excluir:
- Exibe janela de confirmação
- Se confirmado → DELETE

**`FilteredList`** — permite filtrar a tabela em tempo real sem ir ao banco:

```java
FilteredList<Produto> listaFiltrada = new FilteredList<>(listaBruta, p -> true);
tfBusca.textProperty().addListener((obs, antigo, novo) -> {
    listaFiltrada.setPredicate(produto ->
        produto.getNome().toLowerCase().contains(novo.toLowerCase())
    );
});
```

### `module-info.java` — Módulo Java

Obrigatório no Java 9+. Define quais pacotes o módulo exporta e quais módulos externos ele usa.

O `opens` é necessário porque o `FXMLLoader` usa **reflexão** para injetar os campos `@FXML`:

```java
module com.exemplo.crudprodutos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens com.exemplo.crudprodutos to javafx.graphics, javafx.fxml;
    opens com.exemplo.crudprodutos.controller to javafx.fxml;
    opens com.exemplo.crudprodutos.model to javafx.base;

    exports com.exemplo.crudprodutos;
}
```

### `pom.xml` — Maven

Gerencia as dependências do projeto. O Maven baixa automaticamente as bibliotecas do repositório central.

```xml
<!-- Driver para conectar ao MySQL -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.3.0</version>
</dependency>
```

---

## 🔄 Fluxo das Operações CRUD

```
Usuário preenche campos → clica Salvar
    │
    ├── Campo inválido? → borda vermelha, para aqui
    │
    ├── Nenhuma linha selecionada → ProdutoDAO.inserir() → INSERT no banco
    │
    └── Linha selecionada → ProdutoDAO.atualizar() → UPDATE no banco

Usuário clica em linha da tabela
    └── Listener preenche os campos com os dados do produto

Usuário clica Excluir
    └── Janela de confirmação
        ├── Cancelou → nada acontece
        └── Confirmou → ProdutoDAO.excluir() → DELETE no banco

Usuário digita no campo Busca
    └── FilteredList filtra a tabela em tempo real (sem ir ao banco)
```

---

## 📌 Observações

- O campo **Preço** aceita tanto ponto quanto vírgula como separador decimal (ex: `29.90` ou `29,90`)
- O **ID** é gerado automaticamente pelo MySQL (`AUTO_INCREMENT`) — não é necessário informar
- Ao clicar em **Salvar** sem selecionar nenhuma linha, sempre cria um novo produto
- Para **editar**, clique primeiro na linha desejada na tabela, altere os campos e clique Salvar
