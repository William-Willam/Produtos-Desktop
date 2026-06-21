module com.exemplo.crudprodutos {
    // Importa os módulos do JavaFX que usamos
    requires javafx.controls;
    requires javafx.fxml;

    // Importa o driver do MySQL
    requires java.sql;
    requires mysql.connector.j;

    // Exporta e abre os pacotes para o JavaFX conseguir acessar
    // "opens" é necessário porque o FXMLLoader usa reflexão para
    // instanciar o Controller e injetar os campos @FXML
    opens com.exemplo.crudprodutos to javafx.graphics, javafx.fxml;
    opens com.exemplo.crudprodutos.controller to javafx.fxml;
    opens com.exemplo.crudprodutos.model to javafx.base;

    // Exporta o pacote principal
    exports com.exemplo.crudprodutos;
}