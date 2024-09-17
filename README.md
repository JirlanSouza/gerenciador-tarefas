# Gerencidor de Tarefas

Projeto de um sistema web para gerenciar tarefas.

## Tecnologias utilizadas

- Java 17  
- JSP
- Servlet
- JDBC
- PostgreSQL

## Rodar o projeto

Requisitos ter o [Java 17](https://www.openlogic.com/openjdk-downloads) e [Maven 3.9](https://maven.apache.org/download.cgi)  
e ter uma instacia do Postgres rodando, adicionar a url e as credenciais para acesso ao banco de dados no arquivo [database.properties](src/main/resources/database.properties)  

Arir o diretório do projeto e rodar os comandos:

```bash
mvn wildfly:start
mvn wildfly:deploy
```

A aplicação estrá rodando no endereço htttp://localhost:8080
