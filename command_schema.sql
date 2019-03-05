-- Adminer 4.3.1 MySQL dump

DROP TABLE IF EXISTS commande;
CREATE TABLE commande (
  id varchar(128) NOT NULL,
  created_at timestamp NOT NULL,
  updated_at timestamp DEFAULT NULL,
  livraison timestamp NOT NULL,
  nom varchar(128) NOT NULL,
  mail varchar(256) NOT NULL,
  montant decimal(8,2) DEFAULT NULL,
  remise decimal(8,2) DEFAULT NULL,
  token varchar(128) DEFAULT NULL,
  client_id bigint DEFAULT NULL,
  ref_paiement varchar(128) DEFAULT NULL,
  date_paiement timestamp DEFAULT NULL,
  mode_paiement bigint DEFAULT NULL,
  status bigint NOT NULL DEFAULT 1);


DROP TABLE IF EXISTS item;
CREATE TABLE item (
  id bigint NOT NULL ,
  uri varchar(128) NOT NULL,
  libelle varchar(128) DEFAULT NULL,
  tarif decimal(8,2) DEFAULT NULL,
  quantite bigint DEFAULT 1,
  command_id varchar(128) NOT NULL,
  PRIMARY KEY (id));


-- 2018-10-29 10:10:29
