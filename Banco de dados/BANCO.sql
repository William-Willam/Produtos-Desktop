-- Cria o banco de dados
create database if not exists crud_produtos;

-- Entra no banco
use crud_produtos;

-- criar a tabela de produtos
create table if not exists produtos(
	id int auto_increment primary key,
    nome varchar(100) not null,
    preco decimal(10,2) not null
);

/*

	AUTO_INCREMENT — o MySQL gera o id sozinho, você não precisa informar
	VARCHAR(100) — texto de até 100 caracteres
	DECIMAL(10,2) — número com até 10 dígitos e 2 casas decimais (ex: 99.90)
	NOT NULL — campo obrigatório, não aceita vazio
    
*/

SELECT * FROM produtos;
USE crud_produtos;

INSERT INTO produtos (nome, preco) VALUES
('Mouse Gamer RGB', 89.90),
('Teclado Mecânico', 199.90),
('Monitor 24"', 899.00),
('Headset 7.1', 149.90),
('Webcam Full HD', 219.90),
('SSD 480GB', 269.90),
('Memória RAM 16GB', 329.90),
('Placa de Vídeo GTX 1650', 1299.00),
('Processador Ryzen 5', 899.90),
('Fonte 650W', 389.90),
('Gabinete Gamer', 299.90),
('Mousepad XL', 59.90),
('Hub USB 4 portas', 49.90),
('Cabo HDMI 2m', 29.90),
('Cooler para CPU', 119.90);



