# Java Reflection + Teoria de Grafos = Injeção de Dependência

## Descrição

Projeto desenvolvido ao longo do treinamento Java Reflection + Teoria de Grafos = Injeção de Dependência.

Consiste de um framework que implementa algumas das funcionalidades mais fundamentais de frameworks modernos de desenvolvimento web em Java, como Spring e Quarkus. Sendo elas, injeção de dependência e provimento de endpoints para chamadas HTTP. Para validação desse framework, também foi desenvolvida uma aplicação web básica que o utiliza.

## Como usar

O repositório contém tanto o projeto do framework (WebFramework) quanto da aplicação web (MyWebTestFramework) que o utiliza, ambos sendo projetos Maven. A aplicação web importa o framework como uma dependência, de modo que primeiro se deve instalá-lo executando `mvn install` na pasta WebFramework. Após isso, já se pode compilar e rodar o projeto MyWebTestFramework da forma que se desejar.

## Funcionalidades

O projeto foi desenvolvido acompanhando a codificação do projeto presente [nesse repositório](https://github.com/eduardohen1/treinamentoMinsaitJavaReflections), durante o treinamento. Desse modo, ele contempla as mesmas funcionalidades daquele, com algumas pequenas alterações:

- A requisição `GET /produto` foi alterada para retornar uma lista de produtos em vez de apenas um, de modo a facilitar o teste das funcionalidades. Essa lista é inicializada com dados de teste quando o projeto sobe e armazenada em memória.

- A requisição `POST /produto` foi alterada para criar um novo produto na lista armazenada em memória. Após criado um novo produto com ela, esse produto pode ser manipulado com as demais funcionalidades do projeto.

Além dessas alterações, o projeto presente aqui adiciona algumas funcionalidades novas:

- Foi criada a funcionalidade de atualizar um produto, através da requisição `PUT /produto/{id}`. Para ela, é necessário informar o id do produto a ser atualizado como path parameter e o conteúdo da nova versão do produto como body da requisição. Após feita a alteração, é possível conferir o resultado consultando novamente a lista de produtos.

- Foi criada a funcionalidade de remover um produto, através da requisição `DELETE /produto/{id}`. Para ela, é necessário informar o id do produto a ser removido como path parameter. Após feita a alteração, é possível conferir o resultado consultando novamente a lista de produtos.

## Exemplos de requests

A pasta `requests` na raiz do repositório contém alguns exemplos de requests com a ferramenta curl que podem ser utilizadas para testar as funcionalidades.