ALTER TABLE rodada ALTER COLUMN valor DROP NOT NULL;

ALTER TABLE resultado_rodada
ADD  indice_atratividade numeric(19,2);

CREATE UNIQUE INDEX uq_empresa_acronimo ON empresa (acronimo);
CREATE UNIQUE INDEX uq_usuario_login ON usuario (login);