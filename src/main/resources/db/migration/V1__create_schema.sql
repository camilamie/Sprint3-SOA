CREATE TABLE veiculo (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         marca VARCHAR(100) NOT NULL,
                         modelo VARCHAR(100) NOT NULL,
                         versao VARCHAR(100) NOT NULL,
                         ano_modelo VARCHAR(20),
                         CONSTRAINT uk_veiculo UNIQUE (marca, modelo, versao)
);

CREATE TABLE equipamento (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             nome VARCHAR(255) NOT NULL UNIQUE,
                             categoria VARCHAR(100)
);

CREATE TABLE veiculo_equipamento (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     veiculo_id BIGINT NOT NULL,
                                     equipamento_id BIGINT NOT NULL,
                                     equipamento_valor VARCHAR(255),
                                     disponivel BOOLEAN NOT NULL DEFAULT FALSE,
                                     CONSTRAINT fk_ve_veiculo FOREIGN KEY (veiculo_id) REFERENCES veiculo(id),
                                     CONSTRAINT fk_ve_equipamento FOREIGN KEY (equipamento_id) REFERENCES equipamento(id),
                                     CONSTRAINT uk_ve UNIQUE (veiculo_id, equipamento_id)
);

CREATE INDEX idx_veiculo_busca ON veiculo(marca, modelo, versao);
CREATE INDEX idx_equipamento_nome ON equipamento(nome);