--liquibase formatted sql
--changeset eduardo-villasboas:2020-09-19_01 author:eduardo-villasboas
CREATE TABLE customer (
	id uuid NOT NULL,
	name varchar(255) NOT NULL,
	cpf varchar(11) NOT NULL,
	birth_date date, 
	CONSTRAINT customer_pkey PRIMARY KEY (id),
	CONSTRAINT customer_cpf_unique UNIQUE (cpf)
);
--rollback DROP TABLE customer;
