-- Tabela Usuario (Superclasse)
CREATE TABLE usuario (
    id_usuario SERIAL PRIMARY KEY,
    nome VARCHAR(100),
    email VARCHAR(100),
    senha VARCHAR(100),
    telefone VARCHAR(20),
    tipo VARCHAR(50)
);

CREATE TABLE usuario_comum (
    id_usuario INT PRIMARY KEY,
    matricula VARCHAR(50),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE usuario_especial (
    id_usuario INT PRIMARY KEY,
    codigo VARCHAR(50),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE localizacao (
    id_localizacao SERIAL PRIMARY KEY,
    estante VARCHAR(20),
    pratileira VARCHAR(20),
    secao VARCHAR(50),
    UNIQUE (estante, pratileira, secao)
);

CREATE TABLE emprestimo (
    id_emprestimo SERIAL PRIMARY KEY,
    id_usuario INT,
    data_emprestimo DATE,
    data_devolucao DATE,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE emprestimo_item (
    id_emprestimo INT,
    id_item INT,
    PRIMARY KEY (id_emprestimo, id_item),
    FOREIGN KEY (id_emprestimo) REFERENCES emprestimo(id_emprestimo) ON DELETE CASCADE,
    FOREIGN KEY (id_item) REFERENCES item(id_item) ON DELETE CASCADE
);

CREATE TABLE item (
    id_item SERIAL PRIMARY KEY,
    titulo VARCHAR(200),
    data_publicacao VARCHAR(10),
    status VARCHAR(20),
    tipo VARCHAR(50),
    id_localizacao INT UNIQUE,
    FOREIGN KEY (id_localizacao) REFERENCES localizacao(id_localizacao)
);

CREATE TABLE livro (
    id_item INT PRIMARY KEY,
    isbn VARCHAR(20),
    editora VARCHAR(100),
    FOREIGN KEY (id_item) REFERENCES item(id_item)
);

CREATE TABLE periodico (
    id_item INT PRIMARY KEY,
    issn VARCHAR(20),
    editora VARCHAR(100),
    FOREIGN KEY (id_item) REFERENCES item(id_item)
);

CREATE TABLE midia (
    id_item INT PRIMARY KEY,
    doi VARCHAR(100),
    url TEXT,
    editora VARCHAR(100),
    FOREIGN KEY (id_item) REFERENCES item(id_item)
);

CREATE TABLE monografia (
    id_item INT PRIMARY KEY,
    identificador VARCHAR(50),
    FOREIGN KEY (id_item) REFERENCES item(id_item)
);

CREATE TABLE monografia_autores (
    id_item INT,
    autor VARCHAR(100),
    PRIMARY KEY (id_item, autor),
    FOREIGN KEY (id_item) REFERENCES monografia(id_item)
);
