CREATE TABLE ramo_atividade
(
  id bigint NOT NULL,
  descricao character varying(255) NOT NULL,
  nome character varying(60) NOT NULL,
  indice numeric(19,2),
  CONSTRAINT ramo_atividade_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ramo_atividade
  OWNER TO postgres;

CREATE SEQUENCE ramo_atividade_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 43
  CACHE 1;
ALTER TABLE ramo_atividade
  OWNER TO postgres;  

ALTER TABLE EMPRESA  
ADD ramo_atividade_id bigint;

ALTER TABLE EMPRESA  
ADD CONSTRAINT EMPRESA_RAMO_ATIVIDADE_FK FOREIGN KEY (ramo_atividade_id)
      REFERENCES ramo_atividade (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE EMPRESA  
ALTER COLUMN ramo_atividade_id SET NOT NULL;

 
  